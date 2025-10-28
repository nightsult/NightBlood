package org.night.nightblood;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.night.nightblood.network.BloodSyncPacket;
import org.night.nightblood.network.BloodSyncPacketHandler;
import org.night.nightblood.registry.ModAttachments;
import org.night.nightblood.registry.ModCreativeTabs;
import org.night.nightblood.registry.ModItems;

@Mod(Nightblood.MOD_ID)
public final class Nightblood {
    public static final String MOD_ID = "nightblood";

    public Nightblood(IEventBus modEventBus) {
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachments.ATTACHMENT_TYPES.register(modEventBus);
        org.night.nightblood.registry.ModBlocks.BLOCKS.register(modEventBus);

        org.night.nightblood.registry.ModBlocks.registerBlockItems();

        modEventBus.addListener(this::registerPayloads);
    }

    private void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                BloodSyncPacket.TYPE,
                BloodSyncPacket.STREAM_CODEC,
                BloodSyncPacketHandler::handleClient
        );
        registrar.playToClient(
                org.night.nightblood.network.BloodChangePacket.TYPE,
                org.night.nightblood.network.BloodChangePacket.STREAM_CODEC,
                org.night.nightblood.network.BloodChangePacketHandler::handleClient
        );
    }
}
