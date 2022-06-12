package me.dmod.mixins;

import com.mojang.authlib.GameProfile;
import me.dmod.DMod;
import me.dmod.utils.Guis;
import me.dmod.utils.Packets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C01PacketChatMessage;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerSP.class)
public abstract class MixinEntityPlayerSP extends AbstractClientPlayer {

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Redirect(method = "closeScreen()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/NetHandlerPlayClient;addToSendQueue(Lnet/minecraft/network/Packet;)V"))
    public void addToSendQueue(NetHandlerPlayClient netHandlerPlayClient, Packet<?> p_147297_1_) {
        if (Minecraft.getMinecraft().currentScreen != Guis.getLatest() && !(DMod.getConfig().getDisableDropGridItems() && Minecraft.getMinecraft().currentScreen instanceof GuiInventory)) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C0DPacketCloseWindow(this.openContainer.windowId));
        }
    }

    @Inject(method = "openEditSign", at=@At("HEAD"), cancellable = true)
    public void openEditSign(TileEntitySign signTile, CallbackInfo ci) {
        if (DMod.getConfig().getAutoSign()) {
            Packets.fillSignPacket(signTile);
            ci.cancel();
        } else if (DMod.getConfig().getAutoPreviousSave()) {
            Guis.saveAndCloseScreen();
        }
    }

    @Inject(method = "sendChatMessage", at=@At("HEAD"), cancellable = true)
    public void sendChatMessage(String message, CallbackInfo ci) {
        if (DMod.getConfig().getColorChat()) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C01PacketChatMessage((message.replaceAll("&", "\u00a7")).replaceAll("\\\\n", "\n")));
            ci.cancel();
        }
    }
}
