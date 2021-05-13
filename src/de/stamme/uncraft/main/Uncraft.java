package de.stamme.uncraft.main;


import de.stamme.uncraft.commands.TestCommand;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class Uncraft extends JavaPlugin {

    public static void main(String[] args) {
        Material material = Material.IRON_AXE;

        Recipe recipe = Bukkit.getRecipe(NamespacedKey.randomKey());

        System.out.println(recipe.getResult().getType().name());
    }

    public void onEnable() {
        loadCommands();
    }

    private void loadCommands() {
        Objects.requireNonNull(getCommand("test")).setExecutor(new TestCommand());
    }

}
