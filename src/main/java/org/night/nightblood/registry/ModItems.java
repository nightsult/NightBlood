package org.night.nightblood.registry;

import net.minecraft.world.item.*;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.night.nightblood.Nightblood;
import org.night.nightblood.item.blood.BloodBottleFilledItem;
import org.night.nightblood.item.blood.BloodBottleItem;
import org.night.nightblood.item.blood.BloodPickaxeItem;
import org.night.nightblood.item.blood.BloodSwordItem;
import org.night.nightblood.item.night.NightPickaxeItem;
import org.night.nightblood.item.night.NightSwordItem;
import org.night.nightblood.item.night.NightTier;

public final class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Nightblood.MOD_ID);

    public static final DeferredItem<NightSwordItem> NIGHT_SWORD = ITEMS.register("night_sword",
            () -> new NightSwordItem(NightTier.NIGHT, 3, -2.4F, new Item.Properties()));

    public static final DeferredItem<NightPickaxeItem> NIGHT_PICKAXE = ITEMS.register("night_pickaxe",
            () -> new NightPickaxeItem(NightTier.NIGHT, 1, -2.8F, new Item.Properties()));

    public static final DeferredItem<BloodSwordItem> BLOOD_SWORD = ITEMS.register("blood_sword",
            () -> new BloodSwordItem(NightTier.NIGHT, 3, -2.4F, new Item.Properties()));

    public static final DeferredItem<BloodPickaxeItem> BLOOD_PICKAXE = ITEMS.register("blood_pickaxe",
            () -> new BloodPickaxeItem(NightTier.NIGHT, 1, -2.8F, new Item.Properties()));

    public static final DeferredItem<BloodBottleItem> BLOOD_BOTTLE = ITEMS.register("blood_bottle",
            () -> new BloodBottleItem(new Item.Properties().stacksTo(16)));

    public static final DeferredItem<BloodBottleFilledItem> BLOOD_BOTTLE_FILLED = ITEMS.register("blood_bottle_filled",
            () -> new BloodBottleFilledItem(new Item.Properties().stacksTo(16)));
}
