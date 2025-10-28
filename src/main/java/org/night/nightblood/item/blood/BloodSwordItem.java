package org.night.nightblood.item.blood;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;

import java.util.List;

public class BloodSwordItem extends SwordItem {

    public BloodSwordItem(Tier tier, int attackBonus, float attackSpeed, Properties props) {
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
        return super.isFoil(stack) || hasBloodGlint(stack);
    }

    private boolean hasBloodGlint(ItemStack stack) {
        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return customData.contains("blood_glint") && customData.copyTag().getBoolean("blood_glint");
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.empty();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(0, Component.translatable(this.getDescriptionId()));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.contains("blood_damage_bonus")) {
            float damageBonus = customData.copyTag().getFloat("blood_damage_bonus");
            int lootingLevel = customData.copyTag().getInt("blood_looting_level");

            if (damageBonus > 0) {
                tooltipComponents.add(Component.literal("§c+").append(String.format("%.0f", damageBonus)).append(" Blood Damage"));
            }
            if (lootingLevel > 0) {
                tooltipComponents.add(Component.literal("§6Looting ").append(String.valueOf(lootingLevel)).append(" (Blood)"));
            }
        }

        tooltipComponents.add(Component.literal(""));

        if (tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(Component.literal("§7Empowered by blood"));
            tooltipComponents.add(Component.literal("§7• Increased damage"));
            tooltipComponents.add(Component.literal("§7• Enhanced looting"));
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("§7Max: +10 Damage, Looting X"));
        } else {
            tooltipComponents.add(Component.literal("§8Press §7[SHIFT]§8 for more info"));
        }
    }
}
