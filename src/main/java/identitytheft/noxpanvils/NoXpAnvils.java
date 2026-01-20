package identitytheft.noxpanvils;

import identitytheft.noxpanvils.config.Config;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NoXpAnvils implements ModInitializer {
	public static final String MOD_ID = "no-xp-anvils";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Starting No XP Anvils!");
		Config.HANDLER.load();
	}
}