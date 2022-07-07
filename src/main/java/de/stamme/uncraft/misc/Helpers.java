package de.stamme.uncraft.misc;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Helpers {

    /**
     * Forms a vector array from an ItemStack list
     *
     * @param items list of ItemStacks
     * @return vector array of ItemStacks
     */
    public static ItemStack[] getVector(List<ItemStack> items) {

        ItemStack[] vector = new ItemStack[items.size()];
        for (int i = 0; i < items.size(); i++) {
            vector[i] = items.get(i);
        }

        return vector;
    }
}
