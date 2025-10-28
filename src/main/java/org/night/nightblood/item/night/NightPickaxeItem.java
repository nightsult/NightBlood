package org.night.nightblood.item.night;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Item;

public class NightPickaxeItem extends PickaxeItem {
    public NightPickaxeItem(Tier tier, int attackBonus, float attackSpeed, Item.Properties props) {
        super(tier, props.attributes(PickaxeItem.createAttributes(tier, attackBonus, attackSpeed)));
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return true;
    }
}
