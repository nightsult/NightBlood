package org.night.nightblood.registry;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.night.nightblood.Nightblood;
import org.night.nightblood.blood.BloodData;

import java.util.function.Supplier;

public final class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, Nightblood.MOD_ID);

    public static final Supplier<AttachmentType<BloodData>> BLOOD_DATA = ATTACHMENT_TYPES.register(
            "blood_data",
            () -> AttachmentType.builder(() -> new BloodData())
                    .serialize(new BloodData.Serializer())
                    .build()
    );
}
