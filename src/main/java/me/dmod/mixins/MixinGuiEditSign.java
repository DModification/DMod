package me.dmod.mixins;

import me.dmod.Actions;
import me.dmod.DMod;
import me.dmod.Keybinds;
import me.dmod.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C12PacketUpdateSign;
import net.minecraft.tileentity.TileEntitySign;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

@Mixin(GuiEditSign.class)
public abstract class MixinGuiEditSign extends GuiScreen {
    @Shadow
    private TileEntitySign tileSign;

    private char typedChar;

    @Redirect(method = "onGuiClosed", at=@At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V"))
    public void addToSendQueue(NetHandlerPlayClient netHandlerPlayClient, Packet<?> p_147297_1_) {
        if (!Keybinds.isDown(Actions.CLOSE_SILENTLY)) Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C12PacketUpdateSign(this.tileSign.getPos(), this.tileSign.signText));
    }

    @Inject(method = "keyTyped", at=@At("HEAD"))
    public void headKeyTyped(char typedChar, int keyCode, CallbackInfo ci) {
        this.typedChar = typedChar;
    }

    @ModifyVariable(method = "keyTyped", at=@At("STORE"))
    private String modifyString(String s) {
        if (typedChar == '\u0016') {
            try {
                s = s + Utils.readClipboard();
            } catch (UnsupportedFlavorException | IOException e) {
                DMod.getLogger().error("Failed to read clipboard.");
                e.printStackTrace();
            }
        }
        s = s.replaceAll("CL([0-9a-fA-Fk-oK-O])", "\u00a7$1").replaceAll("NL", "\n");
        return s;
    }

    @ModifyConstant(method = "keyTyped", constant = @Constant(intValue = 90))
    private int modifyMax(int max) {
        if (DMod.getConfig().getDisableSignLimit()) return Integer.MAX_VALUE;
        return max;
    }
}
