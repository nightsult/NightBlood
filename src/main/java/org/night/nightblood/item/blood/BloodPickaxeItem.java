package org.night.nightblood.item.blood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.registry.ModAttachments;

import java.util.List;

public class BloodPickaxeItem extends PickaxeItem {

    public BloodPickaxeItem(Tier tier, int attackBonus, float attackSpeed, Properties props) {
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
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, net.minecraft.world.entity.LivingEntity miner) {
        if (level instanceof ServerLevel serverLevel && miner instanceof Player player) {
            if (!player.isCrouching()) {
                BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
                int bloodLevel = bloodData.getBloodLevel();

                int miningSize = getMiningSize(bloodLevel);

                if (miningSize > 1) {
                    mineArea(stack, serverLevel, state, pos, player, miningSize);
                }
            }
        }

        return super.mineBlock(stack, level, state, pos, miner);
    }

    private int getMiningSize(int bloodLevel) {
        if (bloodLevel < 10) return 1;
        if (bloodLevel < 20) return 2;
        if (bloodLevel < 40) return 3;
        if (bloodLevel < 80) return 4;
        return 5;
    }

    private void mineArea(ItemStack stack, ServerLevel level, BlockState centerState, BlockPos centerPos, Player player, int size) {
        int radius = size / 2;

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (x == 0 && y == 0 && z == 0) continue;

                    BlockPos pos = centerPos.offset(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (isOre(state)) continue;

                    if (!state.isAir() && state.getDestroySpeed(level, pos) >= 0 &&
                        stack.isCorrectToolForDrops(state)) {

                        level.destroyBlock(pos, true, player);

                        stack.hurtAndBreak(1, player, net.minecraft.world.entity.EquipmentSlot.MAINHAND);
                    }
                }
            }
        }
    }

    private boolean isOre(BlockState state) {
        String name = state.getBlock().getDescriptionId().toLowerCase();
        return name.contains("ore") || name.contains("mineral");
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(0, Component.translatable(this.getDescriptionId()));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (customData.contains("blood_speed_mult")) {
            float speedMult = customData.copyTag().getFloat("blood_speed_mult");

            if (speedMult > 1.0F) {
                int percentage = (int)((speedMult - 1.0F) * 100);
                tooltipComponents.add(Component.literal("§e+").append(String.valueOf(percentage)).append("% Mining Speed"));
            }
        }

        if (customData.contains("blood_mining_size")) {
            int miningSize = customData.copyTag().getInt("blood_mining_size");
            if (miningSize > 1) {
                tooltipComponents.add(Component.literal("§b").append(String.valueOf(miningSize)).append("x").append(String.valueOf(miningSize)).append(" Area Mining"));
            }
        }

        tooltipComponents.add(Component.literal(""));

        if (tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(Component.literal("§7Empowered by blood"));
            tooltipComponents.add(Component.literal("§7• Area mining (ignores ores)"));
            tooltipComponents.add(Component.literal("§7• Increased speed"));
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("§7Blood Levels:"));
            tooltipComponents.add(Component.literal("§7• 10-19 blood: 2x2"));
            tooltipComponents.add(Component.literal("§7• 20-39 blood: 3x3"));
            tooltipComponents.add(Component.literal("§7• 40-79 blood: 4x4"));
            tooltipComponents.add(Component.literal("§7• 80-100 blood: 5x5"));
            tooltipComponents.add(Component.literal(""));
            tooltipComponents.add(Component.literal("§8Sneak to disable area mining"));
        } else {
            tooltipComponents.add(Component.literal("§8Press §7[SHIFT]§8 for more info"));
        }
    }
}
