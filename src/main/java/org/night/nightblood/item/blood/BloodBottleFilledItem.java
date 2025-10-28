package org.night.nightblood.item.blood;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.registry.ModAttachments;

public class BloodBottleFilledItem extends Item {

    public BloodBottleFilledItem(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (entity instanceof ServerPlayer player) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            int oldBlood = bloodData.getBloodLevel();
            bloodData.addBlood(BloodData.BLOOD_PER_BOTTLE);
            int newBlood = bloodData.getBloodLevel();

            net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
                    player,
                    new org.night.nightblood.network.BloodSyncPacket(newBlood)
            );

            int difference = newBlood - oldBlood;
            if (difference > 0) {
                showBloodChange(player, difference, true);
            }

            level.playSound(null, player.blockPosition(), SoundEvents.GENERIC_DRINK, SoundSource.PLAYERS, 1.0F, 1.0F);

            stack.shrink(1);
            if (stack.isEmpty()) {
                return new ItemStack(org.night.nightblood.registry.ModItems.BLOOD_BOTTLE.get());
            } else {
                if (!player.getInventory().add(new ItemStack(org.night.nightblood.registry.ModItems.BLOOD_BOTTLE.get()))) {
                    player.drop(new ItemStack(org.night.nightblood.registry.ModItems.BLOOD_BOTTLE.get()), false);
                }
                return stack;
            }
        }
        return stack;
    }

    private void showBloodChange(ServerPlayer player, int amount, boolean isPositive) {
        net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(
            player,
            new org.night.nightblood.network.BloodChangePacket(amount, isPositive)
        );
    }

    @Override
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
        return 32;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.startUsingItem(hand);
        return InteractionResultHolder.consume(itemStack);
    }
}
