package dev.wannaknow.coxscoutdiscord;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.FriendsChatMemberJoined;
import net.runelite.api.events.VarbitChanged;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import net.runelite.client.plugins.raids.Raid;
import net.runelite.client.plugins.raids.RaidRoom;
import net.runelite.client.plugins.raids.RoomType;
import net.runelite.client.plugins.raids.events.RaidScouted;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.util.Text;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static net.runelite.http.api.RuneLiteAPI.GSON;

@Slf4j
@PluginDescriptor(
	name = "CoX Scout Discord",
	description = "Sends the Chambers of Xeric layout to discord",
	tags = {"chambers", "xeric", "scout", "discord","webhook"}
)
public class CoxScoutDiscordPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private CoxScoutDiscordConfig config;

	@Inject
	private ClientThread clientThread;

	@Inject
	private OkHttpClient okHttpClient;

	private static final String RAID_START_MESSAGE = "The raid has begun!";

	private boolean inRaidChambers;

	private Raid raid;

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
		inRaidChambers = false;
	}

	@Provides
	CoxScoutDiscordConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoxScoutDiscordConfig.class);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		if (client.getGameState() != GameState.LOGGED_IN) {
			return;
		}

		boolean setting = client.getVar(Varbits.IN_RAID) == 1;

		if (inRaidChambers != setting) {
			inRaidChambers = setting;
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		String m = Text.removeTags(event.getMessage());
		if (inRaidChambers && event.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION) {

			if (m.startsWith(RAID_START_MESSAGE)) {
			}
		}
	}

	@Subscribe
	public void onRaidScouted(RaidScouted raidScouted)
	{
		this.raid = raidScouted.getRaid();
		if(raidScouted.isFirstScout() == false) {
			sendWebhook();
		}
	}

	private List<RaidRoom> getOrderedRooms(Raid raid)
	{
		List<RaidRoom> orderedRooms = new ArrayList<>();
		for (Room r : raid.getLayout().getRooms())
		{
			final int position = r.getPosition();
			final RaidRoom room = raid.getRoom(position);

			if (room == null)
			{
				continue;
			}
			if(room.getType() != RoomType.COMBAT && room.getType() != RoomType.PUZZLE) {
				continue;
			}

			orderedRooms.add(room);
		}

		return orderedRooms;
	}

	private Map<String, List<String>> mapConfig(String configString) {
		Map<String,List<String>> items = new HashMap<>();
		if(configString.length() > 0) {
			String[] configSplit = configString.split("\n");
			for (String config : configSplit) {
				if(config.length() > 0) {
					String[] listItems = config.split("=");
					if(listItems[0] != null && listItems[1] != null && listItems[0].length() > 0 && listItems[1].length() > 0) {
						items.put(listItems[0].toLowerCase(), Arrays.asList(listItems[1].split(",")));
					}
				}
			}
		}

		return items;
	}

	private void sendWebhook() {
		StringBuilder sb = new StringBuilder();
		Map<String, List<String>> configRequiredItems = mapConfig(config.requiredItems());
		Map<String, List<String>> configOptionalItems = mapConfig(config.optionalItems());

		sb.append("\n\n**Cox Raid - Scout Information**\n\n");

		List<RaidRoom> rooms = getOrderedRooms(raid);
		List<String> roomNames = new ArrayList<>();
		List<String> requiredItems = new ArrayList<>();
		List<String> optionalItems = new ArrayList<>();
 		for(RaidRoom room : rooms) {
			roomNames.add(room.getName());
			if(configRequiredItems.containsKey(room.getName().toLowerCase())) {
				requiredItems.addAll(configRequiredItems.get(room.getName().toLowerCase()));
			}
			if(configOptionalItems.containsKey(room.getName().toLowerCase())) {
				optionalItems.addAll(configOptionalItems.get(room.getName().toLowerCase()));
			}
		}

		sb.append("**Rotation:**\n").append(String.join(", ", roomNames)).append("\n\n");
		if(requiredItems.size() > 0) {
			sb.append("**Required items:**\n").append(String.join(", ", requiredItems)).append("\n\n");
		}
		if(optionalItems.size() > 0) {
			sb.append("**Optional items:**\n").append(String.join(", ", optionalItems)).append("\n\n");
		}

		WebhookBody webhookBody = new WebhookBody();
		webhookBody.setContent(sb.toString());
		sendWebhook(webhookBody);
	}

	private void sendWebhook(WebhookBody body) {
		RequestBody requestBody = new MultipartBody.Builder()
				.setType(MultipartBody.FORM)
				.addFormDataPart("payload_json",  GSON.toJson(body))
//				.addFormDataPart("image", URLEncoder.encode(fileName + ".png", "UTF-8"),
//						RequestBody.create(MediaType.parse("image/*png"),
//								screenshotOutput.toByteArray()))
				.build();

		Request request = new Request.Builder()
//				.url("https://discord.com/api/webhooks/963858839193649222/2VBT2P1YZ4LKRhkLnccLc9rrdZpVTSSCcvRxmSZKdRMNskRHYp78m6oCPA72laoQmmWi")
				.url(config.url())
				.post(requestBody)
				.build();

		try {
			okHttpClient.newCall(request).execute().close();


		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
