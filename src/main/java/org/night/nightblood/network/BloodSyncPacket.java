package org.night.nightblood.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.night.nightblood.Nightblood;

public record BloodSyncPacket(int bloodLevel) implements CustomPacketPayload {

    public static final Type<BloodSyncPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "blood_sync")
    );

    public static final StreamCodec<FriendlyByteBuf, BloodSyncPacket> STREAM_CODEC = StreamCodec.composite(
            StreamCodec.of((buf, value) -> buf.writeInt(value), FriendlyByteBuf::readInt),
            BloodSyncPacket::bloodLevel,
            BloodSyncPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

