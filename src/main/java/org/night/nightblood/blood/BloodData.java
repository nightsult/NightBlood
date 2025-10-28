package org.night.nightblood.blood;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.Nullable;

public class BloodData {
    public static final int MAX_BLOOD = 100;
    public static final int BLOOD_PER_BOTTLE = 5;

    private int bloodLevel;

    public BloodData() {
        this.bloodLevel = 0;
    }

    public BloodData(int bloodLevel) {
        this.bloodLevel = Math.max(0, Math.min(MAX_BLOOD, bloodLevel));
    }

    public int getBloodLevel() {
        return bloodLevel;
    }

    public void setBloodLevel(int bloodLevel) {
        this.bloodLevel = Math.max(0, Math.min(MAX_BLOOD, bloodLevel));
    }

    public void addBlood(int amount) {
        setBloodLevel(this.bloodLevel + amount);
    }

    public void removeBlood(int amount) {
        setBloodLevel(this.bloodLevel - amount);
    }

    public boolean hasBlood(int amount) {
        return this.bloodLevel >= amount;
    }

    public float getDamageBonus() {
        return (bloodLevel / 10) * 1.5F;
    }

    public int getLootingLevel() {
        return bloodLevel / 10;
    }

    public float getMiningSpeedMultiplier() {
        return 1.0F + (bloodLevel / 100.0F);
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, BloodData> {
        @Override
        public BloodData read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider provider) {
            return new BloodData(tag.getInt("blood_level"));
        }

        @Override
        public @Nullable CompoundTag write(BloodData attachment, HolderLookup.Provider provider) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("blood_level", attachment.bloodLevel);
            return tag;
        }
    }
}
