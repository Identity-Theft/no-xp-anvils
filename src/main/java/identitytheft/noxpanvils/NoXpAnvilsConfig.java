package identitytheft.noxpanvils;

import eu.midnightdust.lib.config.MidnightConfig;

public class NoXpAnvilsConfig extends MidnightConfig {
    @Comment(centered = true) public static Comment comment;

    @Entry public static boolean rename = false;
    @Entry public static boolean repair = false;
	@Entry public static boolean enchant = false;
    @Entry public static boolean combineGear = false;
    @Entry public static boolean combineBooks = false;
}
