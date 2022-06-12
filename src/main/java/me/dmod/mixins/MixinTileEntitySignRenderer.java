package me.dmod.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySignRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TileEntitySignRenderer.class)
public abstract class MixinTileEntitySignRenderer extends TileEntitySpecialRenderer<TileEntitySign> {

    private TileEntitySign tileEntitySign;

    @Inject(at = @At("HEAD"), method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V")
    public void renderTileEntityAt(TileEntitySign k, double f2, double ichatcomponent, double list, float s, int j, CallbackInfo ci) {
        this.tileEntitySign = k;
    }

    @Redirect(at=@At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiUtilRenderComponents;splitText(Lnet/minecraft/util/IChatComponent;ILnet/minecraft/client/gui/FontRenderer;ZZ)Ljava/util/List;"), method = "renderTileEntityAt")
    public List<IChatComponent> splitText(IChatComponent p_178908_0_, int p_178908_1_, FontRenderer p_178908_2_, boolean p_178908_3_, boolean p_178908_4_) {
        List<IChatComponent> componentList = new ArrayList<>();
        componentList.add(new ChatComponentText(p_178908_0_.getFormattedText().replaceAll("\n", "NL")));
        return componentList;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/ModelSign;renderSign()V"), method = "renderTileEntityAt(Lnet/minecraft/tileentity/TileEntitySign;DDDFI)V")
    public void renderSign(ModelSign instance) {
        int maxLength = 0;
        for (int i = 0; i < 4; i++) {
            if (Minecraft.getMinecraft().fontRendererObj.getStringWidth(tileEntitySign.signText[i].getFormattedText()) > maxLength) {
                maxLength = Minecraft.getMinecraft().fontRendererObj.getStringWidth(tileEntitySign.signText[i].getFormattedText());
            }
        }
        float scaleFactor = maxLength <= 90 ? 1 : (float)maxLength / 90;
        instance.signStick.render(0.0625F);
        GlStateManager.scale(scaleFactor, 1f, 1f);
        instance.signBoard.render(0.0625F);
    }
}
