package itamio.itemmodelfix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itamio.itemmodelfix.ItemModelFix;
import itamio.itemmodelfix.model.ItemModelGenerationType;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;

/**
 * Simple JSON config (no Cloth Config / ModMenu dependency).
 * File: config/itemmodelfix263.json, field "generationType".
 */
public class Config {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private static Config instance;

	public ItemModelGenerationType generationType = ItemModelGenerationType.OUTLINE;

	public static Config get() {
		if (instance == null) {
			load();
		}
		return instance;
	}

	public static void load() {
		Path configPath = FabricLoader.getInstance().getConfigDir();
		File file = new File(configPath.toFile(), "itemmodelfix263.json");
		Config config = new Config();
		if (file.exists()) {
			try (FileReader reader = new FileReader(file)) {
				Config loaded = GSON.fromJson(reader, Config.class);
				if (loaded != null) {
					config = loaded;
				}
			} catch (IOException | RuntimeException e) {
				ItemModelFix.LOGGER.warn("Failed to load config, using defaults", e);
			}
		}
		if (config.generationType == null) {
			config.generationType = ItemModelGenerationType.OUTLINE;
		}
		instance = config;
		try (FileWriter writer = new FileWriter(file)) {
			writer.write(GSON.toJson(config));
		} catch (IOException e) {
			ItemModelFix.LOGGER.warn("Failed to write config", e);
		}
	}
}
