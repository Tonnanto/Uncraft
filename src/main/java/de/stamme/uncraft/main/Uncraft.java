package de.stamme.uncraft.main;


import de.stamme.uncraft.commands.UncraftCommand;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class Uncraft extends JavaPlugin {

    public static Uncraft plugin;
    public static final int spigotMCID = 0; // TODO

    public void onEnable() {
        loadCommands();
        setUpBStats();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("uncraft")).setExecutor(new UncraftCommand());
    }

    private void setUpBStats() {
        int pluginId = 15696;
        Metrics metrics = new Metrics(this, pluginId);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void log(Level level, String message) {
        Uncraft.plugin.getLogger().log(level, message);
    }
}
