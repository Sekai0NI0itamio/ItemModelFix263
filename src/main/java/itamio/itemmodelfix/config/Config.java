package itamio.itemmodelfix.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import itamio.itemmodelfix.model.ItemModelGenerationType;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Config {
    private static final Logger LOGGER = LoggerFactory.getLogger("ItemModelFix263");
    private static Config instance;

    public ItemModelGenerationType generationType = ItemModelGenerationType.VANILLA;

    public static Config get() {
        if (instance == null) {
            load();
        }
        return instance;
    }

    public static void load() {
        Path file = FabricLoader.getInstance().getConfigDir().resolve("itemmodelfix263.json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Config config = new Config();
        if (Files.exists(file)) {
            try (Reader reader = Files.newBufferedReader(file)) {
                Config loaded = gson.fromJson(reader, Config.class);
                if (loaded != null) {
                    config = loaded;
                }
            } catch (IOException | RuntimeException e) {
                LOGGER.warn("Failed to load config, using defaults", e);
            }
        }
        instance = config;
        try {
            Files.createDirectories(file.getParent());
            try (Writer writer = Files.newBufferedWriter(file)) {
                gson.toJson(config, writer);
            }
        } catch (IOException e) {
            LOGGER.warn("Failed to write config", e);
        }
    }
}
