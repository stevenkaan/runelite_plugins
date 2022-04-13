package dev.wannaknow.coxscoutdiscord;

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

	@Override
	protected void startUp() throws Exception
	{
	}

	@Override
	protected void shutDown() throws Exception
	{
	}

	@Provides
	CoxScoutDiscordConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(CoxScoutDiscordConfig.class);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{

	}
}
