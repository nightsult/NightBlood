package org.night.nightblood.event;

import com.mojang.logging.LogUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import org.night.nightblood.Nightblood;
import org.night.nightblood.command.BloodCommand;
import org.night.nightblood.registry.ModItems;
import org.slf4j.Logger;

@EventBusSubscriber(modid = Nightblood.MOD_ID)
public final class NightEvents {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final float NIGHT_SWORD_EXTRA_DAMAGE = 10.0F;
    private static final float NIGHT_PICKAXE_SPEED_MULT   = 2.0F;
    private static final String FORTUNE_TAG = "nightblood_applied_fortune";
    private static final String NIGHT_GLINT_TAG = "nightblood_night_glint";

    private static boolean isNight(ServerLevel level) {
        long dayTime = level.getDayTime() % 24000L;
        return dayTime >= 13000L && dayTime <= 23000L;
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        var src = event.getSource().getEntity();
        if (!(src instanceof Player player)) return;

        ItemStack main = player.getMainHandItem();
        if (main.is(ModItems.NIGHT_SWORD.get()) && isNight(level)) {
            event.setAmount(event.getAmount() + NIGHT_SWORD_EXTRA_DAMAGE);
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack held = player.getMainHandItem();
        if (held.is(ModItems.NIGHT_PICKAXE.get()) && isNight(level)) {
            event.setNewSpeed(event.getNewSpeed() * NIGHT_PICKAXE_SPEED_MULT);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) return;

        ItemStack main = player.getMainHandItem();
        boolean holdingNightSword = main.is(ModItems.NIGHT_SWORD.get());
        boolean holdingNightPick = main.is(ModItems.NIGHT_PICKAXE.get());
        boolean night = isNight(level);

        if (holdingNightSword) {
            CustomData customData = main.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            boolean hasGlint = customData.contains(NIGHT_GLINT_TAG) && customData.copyTag().getBoolean(NIGHT_GLINT_TAG);

            if (night && !hasGlint) {
                main.set(DataComponents.CUSTOM_DATA, customData.update(tag -> tag.putBoolean(NIGHT_GLINT_TAG, true)));
            } else if (!night && hasGlint) {
                main.set(DataComponents.CUSTOM_DATA, customData.update(tag -> tag.remove(NIGHT_GLINT_TAG)));
            }
        }

        if (night && (holdingNightSword || holdingNightPick)) {
            if (level.getGameTime() % 2 == 0) {
                double x = player.getX();
                double y = player.getY() + player.getEyeHeight() - 0.4;
                double z = player.getZ();

                double yaw = Math.toRadians(player.getYRot() + 90);
                double pitch = Math.toRadians(player.getXRot());

                double handOffsetX = Math.cos(yaw) * 0.4;
                double handOffsetZ = Math.sin(yaw) * 0.4;
                double handOffsetY = -Math.sin(pitch) * 0.3;

                for (int i = 0; i < 2; i++) {
                    double angle = Math.random() * Math.PI * 2;
                    double radius = 0.2 + Math.random() * 0.15;
                    double offsetX = Math.cos(angle) * radius;
                    double offsetZ = Math.sin(angle) * radius;
                    double offsetY = (Math.random() - 0.5) * 0.25;

                    level.sendParticles(
                            ParticleTypes.ENCHANT,
                            x + handOffsetX + offsetX,
                            y + handOffsetY + offsetY,
                            z + handOffsetZ + offsetZ,
                            1,
                            0, 0.05, 0,
                            0.03
                    );
                }
            }
        }

        if (holdingNightPick && night) {
            var fortuneHolder = level.registryAccess()
                    .registryOrThrow(Registries.ENCHANTMENT)
                    .getHolderOrThrow(Enchantments.FORTUNE);

            ItemEnchantments ench = main.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(ench);
            int cur = mut.getLevel(fortuneHolder);

            CustomData customData = main.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            boolean wasAppliedByUs = customData.contains(FORTUNE_TAG);

            if (cur < 3 && !wasAppliedByUs) {
                mut.set(fortuneHolder, 3);
                main.set(DataComponents.ENCHANTMENTS, mut.toImmutable());
                main.set(DataComponents.CUSTOM_DATA, customData.update(tag -> {
                    tag.putBoolean(FORTUNE_TAG, true);
                    tag.putInt(FORTUNE_TAG + "_original", cur);
                }));
            }
        } else if (holdingNightPick) {
            CustomData customData = main.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            if (!main.isEmpty() && customData.contains(FORTUNE_TAG)) {
                var fortuneHolder = level.registryAccess()
                        .registryOrThrow(Registries.ENCHANTMENT)
                        .getHolderOrThrow(Enchantments.FORTUNE);

                ItemEnchantments ench = main.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
                ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(ench);

                int originalLevel = customData.copyTag().getInt(FORTUNE_TAG + "_original");
                mut.set(fortuneHolder, originalLevel);
                main.set(DataComponents.ENCHANTMENTS, mut.toImmutable());

                main.set(DataComponents.CUSTOM_DATA, customData.update(tag -> {
                    tag.remove(FORTUNE_TAG);
                    tag.remove(FORTUNE_TAG + "_original");
                }));
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        BloodCommand.register(event.getDispatcher());
    }
}
