package me.dmod.mixins;

import me.dmod.DMod;
import net.minecraft.network.Packet;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(C17PacketCustomPayload.class)
public abstract class MixinC17PacketCustomPayload implements Packet<INetHandlerPlayServer> {
    @ModifyConstant(method = "<init>(Ljava/lang/String;Lnet/minecraft/network/PacketBuffer;)V", constant = @Constant(intValue = 32767))
    private int modifyMax(int max) {
        if (DMod.getConfig().getFixLargeItems()) return Integer.MAX_VALUE;
        return max;
    }
}
