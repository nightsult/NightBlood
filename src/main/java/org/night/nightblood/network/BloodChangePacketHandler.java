package org.night.nightblood.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class BloodChangePacketHandler {

    public static void handleClient(BloodChangePacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Minecraft mc = Minecraft.getInstance();
            Player player = mc.player;

            if (player != null) {
                int amount = packet.isPositive() ? packet.amount() : -packet.amount();
                org.night.nightblood.client.BloodHudOverlay.showBloodChange(amount);
            }
        });
    }
}
