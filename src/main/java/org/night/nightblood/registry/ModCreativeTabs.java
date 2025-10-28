package org.night.nightblood.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.nightblood.Nightblood;

import java.util.function.Supplier;

public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Nightblood.MOD_ID);

    public static final Supplier<CreativeModeTab> NIGHTBLOOD_TAB = CREATIVE_MODE_TABS.register("nightblood_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.nightblood"))
                    .icon(() -> new ItemStack(ModItems.NIGHT_SWORD.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.NIGHT_SWORD.get());
                        output.accept(ModItems.NIGHT_PICKAXE.get());

                        output.accept(ModItems.BLOOD_SWORD.get());
                        output.accept(ModItems.BLOOD_PICKAXE.get());
                        output.accept(ModItems.BLOOD_BOTTLE.get());
                        output.accept(ModItems.BLOOD_BOTTLE_FILLED.get());

                        output.accept(ModBlocks.BLOOD_ALTAR.get());
                    })
                    .build()
    );
}
