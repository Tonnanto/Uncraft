package de.stamme.uncraft.main;


import de.stamme.uncraft.commands.UncraftCommand;
import de.stamme.uncraft.misc.Config;
import org.bstats.bukkit.Metrics;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.logging.Level;

public class Uncraft extends JavaPlugin {

    public static Uncraft plugin;
    public static final int spigotMCID = 103187;

    public void onEnable() {
        plugin = this;

        loadCommands();
        setUpBStats();

        // save default config if not existing - overwrite if config from older version
        Config.update();

        // run after reload is complete
        getServer().getScheduler().runTask(this, this::startUpdateChecker);
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("uncraft")).setExecutor(new UncraftCommand());
    }

    private void startUpdateChecker() {

        // Programmatically set the default permission value cause Bukkit doesn't handle plugin.yml properly for Load order STARTUP plugins
        org.bukkit.permissions.Permission perm = getServer().getPluginManager().getPermission("uncraft.update");
        if (perm == null) {
            perm = new org.bukkit.permissions.Permission("uncraft.update");
            perm.setDefault(PermissionDefault.OP);
            plugin.getServer().getPluginManager().addPermission(perm);
        }
        perm.setDescription("Allows a user or the console to check for Uncraft updates");

        getServer().getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            if (getServer().getConsoleSender().hasPermission("uncraft.update") && getConfig().getBoolean("update-check", true)) {
                log("Checking for Updates ... ");
                new UpdateChecker(this, spigotMCID).getVersion(version -> {
                    String oldVersion = this.getDescription().getVersion();
                    if (oldVersion.equalsIgnoreCase(version)) {
                        log("No Update available.");
                    } else {
                        log(String.format("New version (%s) is available! You are using an old version (%s).", version, oldVersion));
                    }
                });
            }
        }, 0, 432000);
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
