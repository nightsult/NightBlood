package org.night.nightblood.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.night.nightblood.Nightblood;

public record BloodChangePacket(int amount, boolean isPositive) implements CustomPacketPayload {

    public static final Type<BloodChangePacket> TYPE = new Type<>(
        ResourceLocation.fromNamespaceAndPath(Nightblood.MOD_ID, "blood_change")
    );

    public static final StreamCodec<FriendlyByteBuf, BloodChangePacket> STREAM_CODEC = StreamCodec.composite(
        StreamCodec.of(
            (buf, amount) -> buf.writeInt(amount),
            buf -> buf.readInt()
        ),
        BloodChangePacket::amount,
        StreamCodec.of(
            (buf, isPositive) -> buf.writeBoolean(isPositive),
            buf -> buf.readBoolean()
        ),
        BloodChangePacket::isPositive,
        BloodChangePacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}

