package de.stamme.uncraft.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.*;

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

                    List<ItemStack> ingredients = new ArrayList<>();
                    for (ItemStack ingredient : ((ShapedRecipe) recipe).getIngredientMap().values()) {
                        if (ingredient != null) {
                            ingredients.add(ingredient);
                        }
                    }

                    player.getInventory().setItemInMainHand(null);
                    HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(getVector(ingredients));

                    playSound(player, ingredients);

                    remainingItems.forEach((key, value) -> player.sendMessage(value.getType().name()));

                    for (Map.Entry<Integer, ItemStack> remainingItem : remainingItems.entrySet()) {
                        player.getLocation().getWorld().dropItem(player.getLocation(), remainingItem.getValue());
                    }

                }
                // TODO: Handle other types of recipes
            }
        }

        return true;
    }
}
