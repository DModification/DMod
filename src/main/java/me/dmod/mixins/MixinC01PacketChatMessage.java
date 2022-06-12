package me.dmod.mixins;

import net.minecraft.network.play.client.C01PacketChatMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(C01PacketChatMessage.class)
public abstract class MixinC01PacketChatMessage {
    @ModifyConstant(method = "<init>(Ljava/lang/String;)V", constant = @Constant(intValue = 100))
    public int modifyLength(int length) {
        return Integer.MAX_VALUE;
    }
}
