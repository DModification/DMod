package me.dmod.mixins;

import gg.essential.elementa.components.input.AbstractTextInput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(AbstractTextInput.class)
public interface IMixinAbstractTextInput {
    @Invoker(value = "commitTextAddition", remap = false)
    void invokeCommitTextAddition(String newText);
}
