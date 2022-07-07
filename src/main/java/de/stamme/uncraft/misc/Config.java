package de.stamme.uncraft.misc;

import de.stamme.uncraft.main.Uncraft;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Config {

    static FileConfiguration config = Uncraft.plugin.getConfig();

    public static void update() {

        String configPath = Uncraft.plugin.getDataFolder() + File.separator + "config.yml";
        File configFile = new File(configPath);
        if (!configFile.exists()) {
            // No config file exists
            Uncraft.plugin.saveDefaultConfig();
            Uncraft.plugin.reloadConfig();
            Config.config = Uncraft.plugin.getConfig();
            return;
        }

        // Reading old config.yml
        String configString = "";
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(configPath));
            configString = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            Uncraft.log(Level.SEVERE, "Failed to verify config.yml version");
        }

        // Looking for version String in file
        String version = Uncraft.plugin.getDescription().getVersion();
        String versionString = "version " + version;
        Pattern verPat = Pattern.compile("version [0-9.]+\\b");
        Matcher m = verPat.matcher(configString);
        if (m.find()) {
            String s = m.group();
            if (s.equalsIgnoreCase(versionString)) {
                //	Config is up to date!
                return;
            }
        }

        // Config file needs to be updated
        Map<String, Object> entries = config.getValues(true);

        if (!configFile.delete()) {
            Uncraft.log(Level.SEVERE, "Failed to delete outdated config.yml");
            return;
        }

        Uncraft.plugin.saveDefaultConfig();

        // Reading new config.yml
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(configPath));
            configString = new String(encoded, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            Uncraft.log(Level.SEVERE, "Failed to read new config.yml file");
        }

        for (Map.Entry<String, Object> entry: entries.entrySet()) {
            Pattern keyPat = Pattern.compile(entry.getKey() + ":.+\\b");
            Object obj = entry.getValue();
            configString = keyPat.matcher(configString).replaceAll(entry.getKey() + ": " + obj.toString());
        }
        configString = verPat.matcher(configString).replaceFirst(versionString);

        File newConfig = new File(configPath);
        try {
            FileWriter fw = new FileWriter(newConfig, false);
            fw.write(configString);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
            Uncraft.log(Level.SEVERE, "Failed to write to new config.yml file");
            return;
        }
        Uncraft.plugin.reloadConfig();
        Config.config = Uncraft.plugin.getConfig();

        Uncraft.log("config.yml updated!");
    }

    public static int getUpdateCheck() {
        return config.getInt("update-check");
    }

    public static boolean getUseWhitelist() {
        return config.getBoolean("use-whitelist");
    }

    public static List<String> getWhitelist() {
        return config.getStringList("whitelist");
    }

    public static List<String> getBlacklist() {
        return config.getStringList("blacklist");
    }

}