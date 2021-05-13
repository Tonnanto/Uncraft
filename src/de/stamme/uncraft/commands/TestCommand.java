package de.stamme.uncraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.List;

public class TestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        commandSender.sendMessage("Test");

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            ItemStack item = player.getInventory().getItemInMainHand();

            List<Recipe> recipes = Bukkit.getRecipesFor(item);

            for (Recipe recipe: recipes) {
                if (recipe instanceof ShapedRecipe) {
                    ((ShapedRecipe) recipe).getIngredientMap().forEach((k, v) -> player.sendMessage(k + ": " + v.getType().name()));
                }
            }
        }

        return true;
    }
}
