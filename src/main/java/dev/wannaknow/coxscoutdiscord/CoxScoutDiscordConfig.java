package dev.wannaknow.coxscoutdiscord;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("autokick")
public interface CoxScoutDiscordConfig extends Config
{
	@ConfigItem(
		keyName = "url",
		name = "Discord webhook URL",
		description = "The discord webhook url"
	)
	default String url()
	{
		return "";
	}
}
