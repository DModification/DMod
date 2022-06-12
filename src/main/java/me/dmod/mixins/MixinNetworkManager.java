package me.dmod.mixins;

import io.netty.channel.ChannelHandlerContext;
import me.dmod.DMod;
import me.dmod.events.custom.ServerTimeEvent;
import me.dmod.utils.Chat;
import me.dmod.utils.Logging;
import me.dmod.utils.Packets;
import me.dmod.utils.Variables;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.network.play.client.C0DPacketCloseWindow;
import net.minecraft.network.play.server.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {
    @Inject(method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At("HEAD"), cancellable = true)
    public void sendPacket(Packet<?> packetIn, CallbackInfo callbackInfo) {
        if (packetIn instanceof C02PacketUseEntity) {
            System.out.println(((C02PacketUseEntity) packetIn).getAction());
        }
        if (DMod.getConfig().getEnableBlink()) {
            Packets.addBlinkPacket(packetIn);
            callbackInfo.cancel();
        }
        if (!callbackInfo.isCancelled()) {
            Logging.logPacket(packetIn);
        }
    }

    @Inject(method = "channelRead0", at = @At("HEAD"), cancellable = true)
    private void onChannelRead(ChannelHandlerContext context, Packet<?> packet, CallbackInfo callbackInfo) {
        if (packet instanceof S03PacketTimeUpdate) ServerTimeEvent.timeUpdate();
        if (DMod.getConfig().getInstantCloseGui() && packet instanceof S2DPacketOpenWindow) {
            Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C0DPacketCloseWindow());
        }
        if (!callbackInfo.isCancelled()) {
            Logging.logPacket(packet);
        }
        if (packet instanceof S44PacketWorldBorder && DMod.getConfig().getDisableWorldBorderPacket()) callbackInfo.cancel();
        if (packet instanceof S39PacketPlayerAbilities) {
            Variables.shouldReEnableFlight = ((S39PacketPlayerAbilities) packet).isAllowFlying();
            if (DMod.getConfig().getDisableFlightPackets()) {
                if (!((S39PacketPlayerAbilities) packet).isFlying()) callbackInfo.cancel();
            }
        }
        if (!DMod.getConfig().getEnableInventory() && packet instanceof S2DPacketOpenWindow) callbackInfo.cancel();
        if(packet instanceof S2FPacketSetSlot){
            S2FPacketSetSlot setSlotPacket = (S2FPacketSetSlot) packet;
            ItemStack item = setSlotPacket.func_149174_e();
            if(item != null && item.getDisplayName().equalsIgnoreCase("-") && Variables.shouldCrunch){
                callbackInfo.cancel();
                Minecraft.getMinecraft().currentScreen = null;
                Minecraft.getMinecraft().setIngameFocus();
                Variables.shouldCrunch = false;
                Chat.sendPrefixMessage("\u00a7aCrunched GUI.");
                return;
            }
        }
        if (packet instanceof S10PacketSpawnPainting && DMod.getConfig().getPaintingsPacket()) {
            if (DMod.getConfig().getPaintingsRate() == 0) callbackInfo.cancel();
            else {
                Random rand = new Random();
                int random = rand.nextInt(DMod.getConfig().getPaintingsRate());
                if (random != 1) callbackInfo.cancel();
            }
        } else if (packet instanceof S0EPacketSpawnObject && DMod.getConfig().getArmourStandsPacket() && ((S0EPacketSpawnObject) packet).getType() == 78) {
            if (DMod.getConfig().getArmourStandsRate() == 0) callbackInfo.cancel();
            else {
                Random rand = new Random();
                int random = rand.nextInt(DMod.getConfig().getArmourStandsRate());
                if (random != 1) callbackInfo.cancel();
            }
        }
    }
}
