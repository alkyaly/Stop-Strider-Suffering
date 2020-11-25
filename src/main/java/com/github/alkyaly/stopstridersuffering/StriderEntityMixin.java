package com.github.alkyaly.stopstridersuffering;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity {

    protected StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract boolean isSaddled();

    @Shadow public abstract void readCustomDataFromTag(CompoundTag tag);

    @Inject(at = @At("HEAD"), method = "interactMob")
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        World world = player.getEntityWorld();
        CompoundTag tag = new CompoundTag();
        if(!world.isClient && player.shouldCancelInteraction() && player.getStackInHand(hand).isEmpty() && isSaddled()) {
            dropItem(Items.SADDLE, 1);
            readCustomDataFromTag(tag);
        }
    }
}
