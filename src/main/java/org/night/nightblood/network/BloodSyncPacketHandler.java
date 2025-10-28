package org.night.nightblood.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.night.nightblood.blood.BloodData;
import org.night.nightblood.registry.ModAttachments;

public class BloodSyncPacketHandler {

    public static void handleClient(BloodSyncPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = Minecraft.getInstance().player;
            if (player != null) {
                BloodData bloodData = player.getData(ModAttachments.BLOOD_DATA);
                bloodData.setBloodLevel(packet.bloodLevel());
            }
        });
    }
}

