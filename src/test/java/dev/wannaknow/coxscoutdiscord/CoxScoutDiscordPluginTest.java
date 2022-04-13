package dev.wannaknow.coxscoutdiscord;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CoxScoutDiscordPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(CoxScoutDiscordPlugin.class);
		RuneLite.main(args);
	}
}