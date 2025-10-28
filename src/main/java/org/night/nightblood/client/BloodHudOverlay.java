package org.night.nightblood.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import org.night.nightblood.Nightblood;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.registry.ModAttachments;
import org.night.nightblood.registry.ModItems;

@EventBusSubscriber(modid = Nightblood.MOD_ID, value = Dist.CLIENT)
public class BloodHudOverlay implements LayeredDraw.Layer {

    private static final ResourceLocation BLOOD_0 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_0.png");
    private static final ResourceLocation BLOOD_1 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_1.png");
    private static final ResourceLocation BLOOD_25 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_25.png");
    private static final ResourceLocation BLOOD_50 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_50.png");
    private static final ResourceLocation BLOOD_75 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_75.png");
    private static final ResourceLocation BLOOD_100 = ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "textures/gui/blood_100.png");

    private static int bloodChangeAmount = 0;
    private static int bloodChangeTimer = 0;
    private static final int BLOOD_CHANGE_DISPLAY_TIME = 40;

    @SubscribeEvent
    public static void onRegisterGuiLayers(RegisterGuiLayersEvent event) {
        event.registerBelow(VanillaGuiLayers.HOTBAR,
                ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "blood_hud"),
                new BloodHudOverlay());
    }

    public static void showBloodChange(int amount) {
        bloodChangeAmount = amount;
        bloodChangeTimer = BLOOD_CHANGE_DISPLAY_TIME;
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.options.hideGui) {
            return;
        }

        Player player = mc.player;
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();

        if (!isBloodItem(mainHand) && !isBloodItem(offHand)) {
            return;
        }

        BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);

        if (bloodData == null) {
            bloodData = new BloodData();
        }

        int bloodLevel = bloodData.getBloodLevel();

        int screenWidth = mc.getWindow().getGuiScaledWidth();
        int screenHeight = mc.getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 16;
        int y = screenHeight - 71;

        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        ResourceLocation texture = getBloodTexture(bloodLevel);

        guiGraphics.blit(texture, x, y, 0, 0, 32, 32, 32, 32);

        if (bloodChangeTimer > 0) {
            bloodChangeTimer--;

            float opacity = 1.0f;
            if (bloodChangeTimer < 10) {
                opacity = bloodChangeTimer / 10.0f;
            }

            int color;
            String text;
            if (bloodChangeAmount > 0) {
                color = 0xFF5555;
                text = "+" + bloodChangeAmount;
            } else {
                color = 0xAAAAAA;
                text = String.valueOf(bloodChangeAmount);
            }

            int alpha = (int) (opacity * 255);
            int finalColor = (alpha << 24) | (color & 0xFFFFFF);

            int textX = screenWidth / 2 - mc.font.width(text) / 2;
            int textY = y - 12;

            guiGraphics.drawString(mc.font, text, textX + 1, textY + 1, 0x000000 | (alpha << 24), false);
            guiGraphics.drawString(mc.font, text, textX, textY, finalColor, false);
        }

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.disableBlend();
    }

    private ResourceLocation getBloodTexture(int bloodLevel) {
        if (bloodLevel == 0) {
            return BLOOD_0;
        } else if (bloodLevel >= 100) {
            return BLOOD_100;
        } else if (bloodLevel >= 75) {
            return BLOOD_75;
        } else if (bloodLevel >= 50) {
            return BLOOD_50;
        } else if (bloodLevel >= 25) {
            return BLOOD_25;
        } else {
            return BLOOD_1;
        }
    }

    private boolean isBloodItem(ItemStack stack) {
        if (stack.isEmpty()) return false;

        return stack.is(ModItems.BLOOD_SWORD.get()) ||
               stack.is(ModItems.BLOOD_PICKAXE.get()) ||
               stack.is(ModItems.BLOOD_BOTTLE.get()) ||
               stack.is(ModItems.BLOOD_BOTTLE_FILLED.get());
    }
}
