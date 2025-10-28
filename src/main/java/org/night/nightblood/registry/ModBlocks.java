package org.night.nightblood.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.nightblood.Nightblood;
import org.night.nightblood.block.BloodAltarBlock;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Nightblood.MOD_ID);

    public static final DeferredBlock<Block> BLOOD_ALTAR = BLOCKS.registerBlock(
            "blood_altar",
            BloodAltarBlock::new,
            BlockBehaviour.Properties.of()
                    .strength(2.0f, 6.0f)
                    .sound(SoundType.STONE)
                    .noOcclusion()
    );

    public static void registerBlockItems() {
        ModItems.ITEMS.register("blood_altar", () -> new BlockItem(BLOOD_ALTAR.get(), new Item.Properties()));
    }
}

