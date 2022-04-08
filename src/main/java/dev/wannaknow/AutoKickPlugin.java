package dev.wannaknow;

import com.google.inject.Provides;
import javax.inject.Inject;

import net.runelite.api.*;
import net.runelite.api.events.FriendsChatMemberJoined;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PluginDescriptor(
	name = "Auto Kick",
	description = "Auto kick players from friends chat",
	tags = {"clan", "friends", "kick", "names", "auto","discord","webhook"}
)
public class AutoKickPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private AutoKickConfig config;

	@Inject
	private ClientThread clientThread;

	List<String> kickUsers = new ArrayList<>();

	@Override
	protected void startUp() throws Exception
	{
		kickUsers.addAll(Arrays.asList(config.names().split(System.lineSeparator())));
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Provides
	AutoKickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoKickConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		kickUsers.clear();
		kickUsers.addAll(Arrays.asList(config.names().toLowerCase().split("\\n")));
		FriendsChatManager friendsChatManager = client.getFriendsChatManager();
		if(friendsChatManager != null) {
			for (FriendsChatMember member : friendsChatManager.getMembers()) {
				checkIfPlayerNeedsToBeKicked(member);
			}
		}
	}

	@Subscribe
	public void onFriendsChatMemberJoined(FriendsChatMemberJoined event)
	{
		checkIfPlayerNeedsToBeKicked(event.getMember());
	}

	private void checkIfPlayerNeedsToBeKicked(FriendsChatMember checkMember) {
		FriendsChatManager friendsChatManager = client.getFriendsChatManager();

		if(kickUsers.contains(checkMember.getName().toLowerCase()) || friendsChatManager == null) {
			FriendsChatRank rank = friendsChatManager.getMyRank();
			if (friendsChatManager.getKickRank().getValue() >= rank.getValue()) {
				if (rank.getValue() > checkMember.getRank().getValue()) {
					client.runScript(ScriptID.FRIENDS_CHAT_SEND_KICK, checkMember.getName());
				}
			}
		}
	}
}
