package org.night.nightblood.item.blood;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BloodBottleItem extends Item {

    public BloodBottleItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) {
        if (target instanceof Animal && !target.level().isClientSide) {
            boolean isOnAltar = isOnBloodAltar(target);

            if (!isOnAltar) {
                target.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1));
                target.hurt(target.damageSources().playerAttack(player), 4.0F);
            }

            stack.shrink(1);

            ItemStack filledBottle = new ItemStack(org.night.nightblood.registry.ModItems.BLOOD_BOTTLE_FILLED.get());
            if (!player.getInventory().add(filledBottle)) {
                player.drop(filledBottle, false);
            }

            player.level().playSound(null, player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.PLAYERS, 1.0F, 1.0F);

            if (!isOnAltar) {
                player.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_HURT, SoundSource.HOSTILE, 1.0F, 0.8F);
            }

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private boolean isOnBloodAltar(LivingEntity entity) {
        BlockPos pos = entity.blockPosition();
        BlockState state = entity.level().getBlockState(pos);

        BlockState below = entity.level().getBlockState(pos.below());

        return state.is(org.night.nightblood.registry.ModBlocks.BLOOD_ALTAR.get()) ||
               below.is(org.night.nightblood.registry.ModBlocks.BLOOD_ALTAR.get());
    }
}
