package me.dmod.mixins;

import me.dmod.DMod;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiScreenBook.class)
public abstract class MixinGuiScreenBook extends GuiScreen {
    @Shadow private String bookTitle;

    @Shadow protected abstract String pageGetCurrent();

    @Shadow protected abstract void pageSetCurrent(String p_146457_1_);

    @ModifyConstant(method = "keyTypedInTitle", constant = @Constant(intValue = 16))
    private int modifyMax(int max) {
        if (DMod.getConfig().getDisableBookLimit()) return Integer.MAX_VALUE;
        return max;
    }

    @Inject(method = "keyTypedInTitle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiScreenBook;updateButtons()V", ordinal = 1))
    public void keyTypedInTitle(char p_146460_1_, int p_146460_2_, CallbackInfo ci) {
        this.bookTitle = this.bookTitle.replaceAll("CL", "\u00a7").replaceAll("NL", "\n").replaceAll("\\\\n", "\n");
    }

    @Inject(method = "pageInsertIntoCurrent", at=@At("HEAD"), cancellable = true)
    private void pageInsertIntoCurrent(String p_146459_1_, CallbackInfo ci) {
        if (DMod.getConfig().getDisableBookLimit()) {
            String s = this.pageGetCurrent();
            String s1 = s + p_146459_1_;
            this.pageSetCurrent(s1);
            ci.cancel();
        }
    }
}
