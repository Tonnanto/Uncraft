package de.stamme.uncraft.misc;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {

    // Strings to identify types of materials
    private final String[] TYPE_METAL = {"INGOT"};
    private final String[] TYPE_STONE = {"STONE", "BASALT", "ANDESITE", "GRANITE", "DIORITE"};
    private final String[] TYPE_WOOD = {"WOOD", "PLANKS", "STICK", "ACACIA", "BIRCH", "DARK_OAK", "JUNGLE",
            "MANGROVE", "OAK", "SPRUCE"};
    private final String[] TYPE_WOOL = {"WOOL"};

    /**
     * Plays a sound that matches the material and action
     *
     * @param player The player who performs the action
     * @param material material to play the sound for
     */
    public void playSound(Player player, Material material) {

        if (material == null) return;

        // Default Sound
        Sound sound = Sound.ENTITY_ITEM_PICKUP;

        // Selection of sounds for specific materials
        if (checkMaterials(material, TYPE_METAL)) {
            sound = Sound.BLOCK_ANVIL_USE;

        } else if (checkMaterials(material, TYPE_STONE)) {
            sound = Sound.BLOCK_STONE_BREAK;

        } else if (checkMaterials(material, TYPE_WOOD)) {
            sound = Sound.BLOCK_WOOD_BREAK;

        } else if (checkMaterials(material, TYPE_WOOL)) {
            sound = Sound.BLOCK_WOOL_BREAK;

        }

        // paying the sound
        player.playSound(player.getLocation(), sound, 1, 1);
    }

    /**
     * Helper for checking sound category type of recipe
     *
     * @param material that is checked for matching sound
     * @return true if matches sound category
     */
    private boolean checkMaterials(Material material, String[] itemTypes) {
        boolean matching = false;
        for (String itemType : itemTypes) {
            if (material.name().contains(itemType)) {
                matching = true;
                break;
            }
        }

        return matching;
    }


}
