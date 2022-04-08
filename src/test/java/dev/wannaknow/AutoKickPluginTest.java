package dev.wannaknow;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class AutoKickPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(AutoKickPlugin.class);
		RuneLite.main(args);
	}
}