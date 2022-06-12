package me.dmod.mixins;

import com.google.common.base.Charsets;
import io.netty.buffer.ByteBuf;
import me.dmod.DMod;
import net.minecraft.network.PacketBuffer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PacketBuffer.class)
public abstract class MixinPacketBuffer extends ByteBuf {

    @Shadow public abstract int readVarIntFromBuffer();

    @Shadow @Final private ByteBuf buf;

    @ModifyConstant(method = "readNBTTagCompoundFromBuffer", constant = @Constant(longValue = 2097152L))
    private long modifyMaxLong(long maxLong) {
        return Long.MAX_VALUE;
    }

    @Inject(method="writeString", at=@At("HEAD"), cancellable = true)
    public void writeString(String string, CallbackInfoReturnable<PacketBuffer> cir) {
        if (DMod.getConfig().getFixLargeItems()) {
            byte[] abyte = string.getBytes(Charsets.UTF_8);
            this.writeVarIntToBuffer(abyte.length);
            this.writeBytes(abyte);
            cir.setReturnValue(new PacketBuffer(this.buf));
        }
    }

    @Inject(method="readStringFromBuffer", at=@At("HEAD"), cancellable = true)
    public void readStringFromBuffer(int maxLength, CallbackInfoReturnable<String> cir) {
        if (DMod.getConfig().getFixLargeItems()) {
            int i = this.readVarIntFromBuffer();
            cir.setReturnValue(new String(this.readBytes(i).array(), Charsets.UTF_8));
        }
    }

    @ModifyConstant(method="readChatComponent", constant=@Constant(intValue = 32767))
    private int modifyMaxInt(int maxInt) {
        if (DMod.getConfig().getFixLargeItems()) return Integer.MAX_VALUE;
        return maxInt;
    }

    @Shadow
    public abstract void writeVarIntToBuffer(int length);
}
