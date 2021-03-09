package io.github.alkyaly.stopstridersuffering;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
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

    @Shadow @Final private static TrackedData<Boolean> SADDLED;

    @Inject(at = @At("HEAD"), method = "interactMob")
    private void interactMob(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        World world = player.getEntityWorld();
        if(!world.isClient && player.shouldCancelInteraction() && player.getStackInHand(hand).isEmpty() && isSaddled()  && !hasPlayerRider()) {
            dropItem(Items.SADDLE, 1);
            dataTracker.set(SADDLED, false);
        }
    }
}
