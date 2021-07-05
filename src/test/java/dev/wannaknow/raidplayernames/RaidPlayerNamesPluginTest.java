package dev.wannaknow.raidplayernames;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class RaidPlayerNamesPluginTest {
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(RaidPlayerNamesPlugin.class);
        RuneLite.main(args);
    }
}
