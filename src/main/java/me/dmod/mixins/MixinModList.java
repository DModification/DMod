package me.dmod.mixins;

import me.dmod.DMod;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.network.handshake.FMLHandshakeMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Mixin(FMLHandshakeMessage.ModList.class)
public abstract class MixinModList {
    @Shadow private Map<String, String> modTags;
    @Inject(at=@At("RETURN"), method = "<init>(Ljava/util/List;)V")
    public void ModList(List<ModContainer> modList, CallbackInfo ci) {
        if (DMod.getConfig().getHideModsList()) {
            if (Minecraft.getMinecraft().getIntegratedServer() == null) {
                List<String> forgeMods = Arrays.asList("FML", "Forge", "mcp");
                modTags.entrySet().removeIf(entry -> !forgeMods.contains(entry.getKey()));
            }
        }
    }
}
