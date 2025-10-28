package org.night.nightblood.item.night;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.CustomData;

public class NightSwordItem extends SwordItem {
    private static final String NIGHT_GLINT_TAG = "nightblood_night_glint";

    public NightSwordItem(Tier tier, int attackBonus, float attackSpeed, Item.Properties props) {
        super(tier, props.attributes(SwordItem.createAttributes(tier, attackBonus, attackSpeed)));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        if (super.isFoil(stack)) {
            return true;
        }

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains(NIGHT_GLINT_TAG) && customData.copyTag().getBoolean(NIGHT_GLINT_TAG);
    }
}
