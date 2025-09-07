package identitytheft.noxpanvils;

import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoXpAnvils implements ModInitializer {
	public static final String MOD_ID = "no_xp_anvils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting No XP Anvils!");
		MidnightConfig.init(MOD_ID, NoXpAnvilsConfig.class);
	}
}