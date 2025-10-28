package org.night.nightblood.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import org.night.nightblood.Nightblood;
import org.night.nightblood.registry.ModBlocks;

@EventBusSubscriber(modid = Nightblood.MOD_ID)
public class BloodAltarEvents {

    @SubscribeEvent
    public static void onPotionApplicable(MobEffectEvent.Applicable event) {
        if (!(event.getEntity() instanceof LivingEntity entity)) return;
        if (!(entity.level() instanceof ServerLevel)) return;

        if (!isOnBloodAltar(entity)) return;

        MobEffectInstance effectInstance = event.getEffectInstance();

        if (effectInstance != null && effectInstance.getEffect() == MobEffects.POISON) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    private static boolean isOnBloodAltar(LivingEntity entity) {
        BlockPos pos = entity.blockPosition();
        BlockState state = entity.level().getBlockState(pos);

        BlockState below = entity.level().getBlockState(pos.below());

        return state.is(ModBlocks.BLOOD_ALTAR.get()) || below.is(ModBlocks.BLOOD_ALTAR.get());
    }
}
