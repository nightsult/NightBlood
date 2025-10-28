package org.night.nightblood.event;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.night.nightblood.Nightblood;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.network.BloodSyncPacket;
import org.night.nightblood.registry.ModAttachments;
import org.night.nightblood.registry.ModItems;

@EventBusSubscriber(modid = Nightblood.MOD_ID)
public final class BloodEvents {

    private static final int BLOOD_DRAIN_INTERVAL = 1200;

    @SubscribeEvent
    public static void onBloodSwordDamage(LivingIncomingDamageEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        var src = event.getSource().getEntity();
        if (!(src instanceof ServerPlayer player)) return;

        ItemStack main = player.getMainHandItem();
        if (main.is(ModItems.BLOOD_SWORD.get())) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            float damageBonus = bloodData.getDamageBonus();

            if (damageBonus > 0) {
                event.setAmount(event.getAmount() + damageBonus);
            }
        }
    }

    @SubscribeEvent
    public static void onBloodPickaxeSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();

        ItemStack held = player.getMainHandItem();
        if (held.is(ModItems.BLOOD_PICKAXE.get())) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            float speedMult = bloodData.getMiningSpeedMultiplier();

            if (speedMult > 1.0F) {
                event.setNewSpeed(event.getNewSpeed() * speedMult);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (!(player.level() instanceof ServerLevel level)) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
        int bloodLevel = bloodData.getBloodLevel();

        if (level.getGameTime() % BLOOD_DRAIN_INTERVAL == 0 && bloodLevel > 0) {
            bloodData.removeBlood(1);
            syncBloodToClient(serverPlayer, bloodData);
        }

        ItemStack main = player.getMainHandItem();

        if (main.is(ModItems.BLOOD_SWORD.get())) {
            updateBloodSword(main, bloodData);
        }

        if (main.is(ModItems.BLOOD_PICKAXE.get())) {
            updateBloodPickaxe(main, bloodData, level);
        }
    }

    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        if (!(event.getEntity().level() instanceof ServerLevel level)) return;
        var source = event.getSource().getEntity();
        if (!(source instanceof ServerPlayer player)) return;

        ItemStack weapon = player.getMainHandItem();
        if (!weapon.is(ModItems.BLOOD_SWORD.get())) return;

        CustomData customData = weapon.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        if (!customData.contains("blood_looting_level")) return;

        int lootingLevel = customData.copyTag().getInt("blood_looting_level");
        if (lootingLevel <= 0) return;

        event.getDrops().forEach(entityItem -> {
            ItemStack stack = entityItem.getItem();
            int currentCount = stack.getCount();

            for (int i = 0; i < lootingLevel; i++) {
                if (level.random.nextFloat() < 0.5F) {
                    currentCount++;
                }
            }

            stack.setCount(currentCount);
        });
    }

    @SubscribeEvent
    public static void onPlayerHurt(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            int currentBlood = bloodData.getBloodLevel();

            if (currentBlood > 0) {
                int oldBlood = currentBlood;
                bloodData.removeBlood(10);
                int newBlood = bloodData.getBloodLevel();

                syncBloodToClient(player, bloodData);

                if (newBlood != oldBlood && isHoldingBloodRelatedItem(player)) {
                    int difference = oldBlood - newBlood;
                    showBloodChange(player, difference, false);
                }
            }
        }
    }

    private static boolean isHoldingBloodRelatedItem(ServerPlayer player) {
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        return isBloodItem(mainHand) || isBloodItem(offHand);
    }

    private static boolean isBloodItem(ItemStack stack) {
        if (stack.isEmpty()) return false;

        if (stack.is(ModItems.BLOOD_SWORD.get()) || stack.is(ModItems.BLOOD_PICKAXE.get())) {
            return true;
        }

        if (stack.is(ModItems.BLOOD_BOTTLE.get()) || stack.is(ModItems.BLOOD_BOTTLE_FILLED.get())) {
            return true;
        }

        return false;
    }

    private static void showBloodChange(ServerPlayer player, int amount, boolean isPositive) {
        PacketDistributor.sendToPlayer(
            player,
            new org.night.nightblood.network.BloodChangePacket(amount, isPositive)
        );
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            syncBloodToClient(player, bloodData);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
            syncBloodToClient(player, bloodData);
        }
    }

    private static void syncBloodToClient(ServerPlayer player, BloodData bloodData) {
        PacketDistributor.sendToPlayer(player, new BloodSyncPacket(bloodData.getBloodLevel()));
    }

    private static void updateBloodSword(ItemStack sword, BloodData bloodData) {
        int bloodLevel = bloodData.getBloodLevel();
        float damageBonus = bloodData.getDamageBonus();
        int lootingLevel = bloodData.getLootingLevel();

        CustomData customData = sword.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        sword.set(DataComponents.CUSTOM_DATA, customData.update(tag -> {
            tag.putBoolean("blood_glint", bloodLevel > 0);
            tag.putFloat("blood_damage_bonus", damageBonus);
            tag.putInt("blood_looting_level", lootingLevel);
        }));

        if (lootingLevel > 0) {
            ItemEnchantments ench = sword.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
            ItemEnchantments.Mutable mut = new ItemEnchantments.Mutable(ench);

        }
    }

    private static void updateBloodPickaxe(ItemStack pickaxe, BloodData bloodData, ServerLevel level) {
        int bloodLevel = bloodData.getBloodLevel();
        float speedMult = bloodData.getMiningSpeedMultiplier();
        int miningSize = getMiningSize(bloodLevel);

        CustomData customData = pickaxe.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        pickaxe.set(DataComponents.CUSTOM_DATA, customData.update(tag -> {
            tag.putBoolean("blood_glint", bloodLevel > 0);
            tag.putFloat("blood_speed_mult", speedMult);
            tag.putInt("blood_mining_size", miningSize);
        }));
    }

    private static int getMiningSize(int bloodLevel) {
        if (bloodLevel < 10) return 1;
        if (bloodLevel < 20) return 2;
        if (bloodLevel < 40) return 3;
        if (bloodLevel < 80) return 4;
        return 5;
    }
}
