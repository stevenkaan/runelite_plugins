package dev.wannaknow;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("autokick")
public interface AutoKickConfig extends Config
{
	@ConfigItem(
		keyName = "names",
		name = "Names",
		description = "A list of names to be automatically kicked (Each name should be on a new row)"
	)
	default String names()
	{
		return "";
	}
}
