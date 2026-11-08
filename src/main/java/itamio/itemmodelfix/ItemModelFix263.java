package itamio.itemmodelfix;

import itamio.itemmodelfix.config.Config;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unofficial port of Item Model Fix by PepperCode1 (LGPL-3.0-only).
 * See NOTICE for credit and licensing, and README for the port status:
 * the original mod hooked the old baked-model ItemModelGenerator
 * (ModelElement-based), which was removed in 26.3's new cuboid model
 * system (net.minecraft.client.resources.model.cuboid.ItemModelGenerator).
 * This port therefore ships the original config/utilities and a loadable
 * mod initializer; the outline/pixel generation modes need a rewrite
 * against the new QuadCollection baking pipeline and are documented as
 * not yet implemented in README.
 */
public class ItemModelFix263 implements ModInitializer {
    public static final String MOD_ID = "itemmodelfix263";
    public static final Logger LOGGER = LoggerFactory.getLogger("ItemModelFix263");

    @Override
    public void onInitialize() {
        Config.load();
        LOGGER.info("[ItemModelFix263] loaded (config: {})", Config.get().generationType);
    }
}
