package de.stamme.uncraft.commands;


import de.stamme.uncraft.misc.Helpers;
import de.stamme.uncraft.misc.Sounds;
import de.stamme.uncraft.misc.StringFormatter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class UncraftCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {

        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;

            // get item in players main hand
            ItemStack playersHand = player.getInventory().getItemInMainHand();

            // get recipe of item
            List<Recipe> recipes = Bukkit.getRecipesFor(playersHand);

            if (!recipes.isEmpty()) {
                Recipe recipe = recipes.get(0);

                int newHandItemCount = playersHand.getAmount() - recipe.getResult().getAmount();
                boolean hasRequiredItemsInHand = playersHand.getAmount() >= recipe.getResult().getAmount();

                // get ingredients of recipe
                List<ItemStack> ingredients = getIngredients(recipe);

                // No ingredients found
                if (ingredients.isEmpty()) {
                    player.sendMessage("Could not uncraft this item");
                    return true;
                }

                // Prevents uncrafting if not all outputs of this recipe are in players hand
                if (!hasRequiredItemsInHand) {
                    player.sendMessage(getMissingIngredientsMessage(ingredients, recipe));
                    return true;
                }

                // remove item(s) from hand
                if (newHandItemCount > 0) {
                    playersHand.setAmount(newHandItemCount);
                } else {
                    playersHand = null;
                }
                player.getInventory().setItemInMainHand(playersHand);

                // add ingredients to players inventory
                // get remaining items if inventory full
                HashMap<Integer, ItemStack> remainingItems = player.getInventory().
                        addItem(Helpers.getVector(ingredients));

                // TODO: Debug purpose
                remainingItems.forEach((key, value) -> player.sendMessage(value.getType().name()));

                // drop remaining items in the floor
                for (Map.Entry<Integer, ItemStack> remainingItem : remainingItems.entrySet()) {
                    Objects.requireNonNull(player.getLocation().getWorld()).dropItem(player.getLocation(),
                            remainingItem.getValue());
                }


                // Type of first material in uncrafted recipe
                Material material = ingredients.get(0).getType();

                // play appropriate sound
                Sounds sound = new Sounds();
                sound.playSound(player, material);
            } else {
                player.sendMessage("Could not uncraft this item");
            }
        }

        return true;
    }


    /**
     * Helper to receive ingredient list from Recipes
     *
     * @param recipe The recipe to get the ingredients from
     * @return list of ingredients
     */
    private List<ItemStack> getIngredients(Recipe recipe) {
        List<ItemStack> ingredients = new ArrayList<>();

        // Recipes exactly specified in the crafting matrix (e.g. tools)
        if (recipe instanceof ShapedRecipe) {
            for (ItemStack ingredient : ((ShapedRecipe) recipe).getIngredientMap().values()) {
                if (ingredient != null) {
                    ingredients.add(ingredient);
                }
            }

            // Recipes not specified in the crafting matrix (e.g. wooden planks)
        } else if (recipe instanceof ShapelessRecipe) {
            for (ItemStack ingredient : ((ShapelessRecipe) recipe).getIngredientList()) {
                if (ingredient != null) {
                    ingredients.add(ingredient);
                }
            }

            // Recipes crafted in cooking stations (includes FurnaceRecipe, SmokingRecipe, CampfireRecipe,
            // BlastingRecipe which extend CookingRecipe)
        } else if (recipe instanceof CookingRecipe) {
            ingredients.add(((CookingRecipe<? extends CookingRecipe<?>>) recipe).getInput());

            // Recipes crafted in the smithing station consisting of a base item and an addition to improve the base item
        }
//        else if (recipe instanceof SmithingRecipe) {
//            SmithingRecipe smithingRecipe = ((SmithingRecipe) recipe);
//            if (smithingRecipe.getBase() instanceof RecipeChoice.ExactChoice) {
//                RecipeChoice.ExactChoice recipeChoice = (RecipeChoice.ExactChoice) smithingRecipe.getBase();
//                for (ItemStack ingredient : recipeChoice.getChoices()) {
//                    if (ingredient != null) {
//                        ingredients.add(ingredient);
//                    }
//                }
//            } else if (smithingRecipe.getBase() instanceof RecipeChoice.MaterialChoice) {
//                RecipeChoice.MaterialChoice recipeChoice = (RecipeChoice.MaterialChoice) smithingRecipe.getBase();
//
//                // TODO: Support smithing recipes
//                // Netherite Sword + Command -> [DIAMOND_SWORD]
//                System.out.println("SmithingRecipe RecipeChoice.MaterialChoice.getChoices: "
//                        + recipeChoice.getChoices());
//                // Netherite Sword + Command -> ItemStack{DIAMOND_SWORD x 1}
//                System.out.println("SmithingRecipe RecipeChoice.MaterialChoice.getItemStack: "
//                        + recipeChoice.getItemStack());
//            }
//        }

        // Reformatting output list to not contain duplicate ingredients
        Map<Material, Integer> materialAmount = new HashMap<>();
        for (ItemStack ingredient : ingredients) {
            if (materialAmount.containsKey(ingredient.getType())) {
                materialAmount.put(ingredient.getType(), materialAmount.get(ingredient.getType())
                        + ingredient.getAmount());
            } else {
                materialAmount.put(ingredient.getType(), ingredient.getAmount());
            }
        }

        return materialAmount.entrySet().stream().map(
                ingredient -> new ItemStack(ingredient.getKey(), ingredient.getValue())
        ).collect(Collectors.toList());
    }

    private String getMissingIngredientsMessage(List<ItemStack> ingredients, Recipe recipe) {
        // String of all Items in the List
        StringBuilder ingredientsListString = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            ItemStack item = ingredients.get(i);
            ingredientsListString.append(StringFormatter.formatItemStack(item));

            if (i < ingredients.size() - 2) {
                ingredientsListString.append(", ");
            } else if (i == ingredients.size() - 2) {
                ingredientsListString.append(" and ");
            } else {
                ingredientsListString.append(".");
            }
        }

        return String.format("You need at least %s to receive %s",
                StringFormatter.formatItemStack(recipe.getResult()),
                ingredientsListString);
    }

}
