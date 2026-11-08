package itamio.itemmodelfix;

import itamio.itemmodelfix.config.Config;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unofficial full port of Item Model Fix by PepperCode1 (LGPL-3.0-only)
 * to Minecraft 1.20.1 (Fabric). See NOTICE for credit and licensing.
 * On this version the baked-model ItemModelGenerator
 * (net.minecraft.client.render.model.json.ItemModelGenerator) still
 * exists, so the full original generation logic
 * (VANILLA / UNLERP / OUTLINE / PIXEL) works here.
 */
public class ItemModelFix implements ModInitializer {
	public static final String ID = "itemmodelfix263";
	public static final Logger LOGGER = LoggerFactory.getLogger("ItemModelFix");

	@Override
	public void onInitialize() {
		Config.load();
		LOGGER.info("[ItemModelFix] loaded (generationType: {})", Config.get().generationType);
	}
}
