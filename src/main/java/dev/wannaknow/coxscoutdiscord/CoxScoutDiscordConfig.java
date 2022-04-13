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

	@ConfigItem(
			keyName = "requiredItems",
			name = "Required items",
			description = "List of required items in following format: room=item1,item2 (each room should be on a new row). Like: Muttadiles=ice barrage,zamorak godsword"
	)
	default String requiredItems()
	{
		return "";
	}

	@ConfigItem(
			keyName = "optionalItems",
			name = "Optional items",
			description = "List of optional items in following format: room=item1,item2 (each room should be on a new row). Like: Ice Demon=ice barrage,zamorak godsword"
	)
	default String optionalItems()
	{
		return "";
	}
}
