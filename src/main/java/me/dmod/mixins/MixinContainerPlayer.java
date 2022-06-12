package me.dmod.mixins;

import me.dmod.DMod;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ContainerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ContainerPlayer.class)
public abstract class MixinContainerPlayer {
    @Inject(at = @At(value = "INVOKE", shift = At.Shift.AFTER, target = "Lnet/minecraft/inventory/Container;onContainerClosed(Lnet/minecraft/entity/player/EntityPlayer;)V"), method = "onContainerClosed", cancellable = true)
    private void onClose(EntityPlayer playerIn, CallbackInfo ci) {
        if (DMod.getConfig().getDisableDropGridItems()) ci.cancel();
    }
}
