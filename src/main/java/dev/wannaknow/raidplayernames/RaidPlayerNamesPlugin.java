package dev.wannaknow.raidplayernames;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.VarbitChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.raids.RaidRoom;
import net.runelite.client.plugins.raids.solver.Room;
import net.runelite.client.ui.ClientToolbar;
import net.runelite.client.ui.NavigationButton;
import net.runelite.client.util.ImageUtil;
import net.runelite.client.util.Text;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PluginDescriptor(
		name = "Raid Player Names",
		description = "A plugin which automaticlly saves all the names with the people you raid with",
		tags = {"combat", "raid", "pve", "pvm", "bosses", "cox", "tob", "names", "log"}
)
@Slf4j
public class RaidPlayerNamesPlugin extends Plugin {

	private static final String PLUGIN_NAME = "Raid name logger";
	private static final String ICON_FILE = "cox.png";

	@Inject
	private ClientThread clientThread;

	@Inject
	private Client client;

	@Inject
	private ClientToolbar clientToolbar;

	private static final String RAID_START_MESSAGE = "The raid has begun!";

	private static final String TOB_START = "You enter the Theatre of Blood";

	private boolean inRaidChambers;

	private RaidPlayerNamesPanel panel;
	private NavigationButton navigationButton;
	private static final int PARTY_LIST_ID_TOB = 1835020;

	@Override
	protected void startUp() throws Exception {
		panel = new RaidPlayerNamesPanel(this);
		final BufferedImage icon = ImageUtil.getResourceStreamFromClass(getClass(), ICON_FILE);
		navigationButton = NavigationButton.builder()
				.tooltip(PLUGIN_NAME)
				.icon(icon)
				.priority(5)
				.panel(panel)
				.build();

		clientToolbar.addNavigation(navigationButton);

		clientThread.invokeLater(() -> checkRaidPresence());
	}

	@Override
	protected void shutDown() throws Exception {
		inRaidChambers = false;

		clientToolbar.removeNavigation(navigationButton);
	}

	@Subscribe
	public void onVarbitChanged(VarbitChanged event)
	{
		checkRaidPresence();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		String m = Text.removeTags(event.getMessage());
		if (inRaidChambers && event.getType() == ChatMessageType.FRIENDSCHATNOTIFICATION) {

			if (m.startsWith(RAID_START_MESSAGE)) {
				List<Player> players = client.getPlayers();
				List<String> people = new ArrayList<>();
				for (Player player : players) {
					people.add(player.getName());
				}
				SwingUtilities.invokeLater(() -> panel.addRaid("CoX", people));
			}
		} else if (event.getType() == ChatMessageType.GAMEMESSAGE && m.startsWith(TOB_START)) {
			log.info("tob started");

			Widget raidingPartyWidget = client.getWidget(PARTY_LIST_ID_TOB);
			if (raidingPartyWidget == null) {
				log.info("widget = null");
				return;
			}

			String[] playerNames = raidingPartyWidget.getText().split("<br>");
			List<String> people = new ArrayList<>();
			for (int i = 0; i < playerNames.length; i++) {
				String name = playerNames[i];
				if(!StringUtils.isEmpty(name) && !name.equals("-")) {
					people.add(name);
				}
			}
			SwingUtilities.invokeLater(() -> panel.addRaid("ToB", people));
		}
	}

	private void checkRaidPresence() {
		if (client.getGameState() != GameState.LOGGED_IN) {
			return;
		}

		boolean setting = client.getVar(Varbits.IN_RAID) == 1;

		if (inRaidChambers != setting) {
			inRaidChambers = setting;
		}
	}
}