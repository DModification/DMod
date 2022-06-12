package me.dmod.mixins;

import me.dmod.DMod;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.RegistryDelegate;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
public abstract class MixinItemStack {
    @Shadow
    private RegistryDelegate<Item> delegate;

    @Shadow private Item item;

    protected MixinItemStack() {
    }


    @Inject(method = "getItem", at = @At("HEAD"), cancellable = true)
    public void getItem(CallbackInfoReturnable<Item> cir) {
        if (DMod.getInitialized() && DMod.getConfig().getFixBannerCrash()) cir.setReturnValue(this.delegate != null ? this.delegate.get() : Items.banner);
    }

    @Inject(method = "getHasSubtypes", at = @At("HEAD"), cancellable = true)
    public void getHasSubtypes(CallbackInfoReturnable<Boolean> cir) {
        if (DMod.getInitialized() && DMod.getConfig().getFixBannerCrash()) if (this.item == null) cir.setReturnValue(false);
    }

    @ModifyVariable(method = "setItem", at = @At("HEAD"), argsOnly = true)
    public Item modifyNewItem(Item newItem) {
        if (DMod.getInitialized() && DMod.getConfig().getFixBannerCrash() && newItem == null) return Items.banner;
        else return newItem;
    }

}