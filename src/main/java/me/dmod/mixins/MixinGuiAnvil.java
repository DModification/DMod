package me.dmod.mixins;

import net.minecraft.client.gui.GuiRepair;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(GuiRepair.class)
public abstract class MixinGuiAnvil extends GuiContainer implements ICrafting {

    public MixinGuiAnvil(Container inventorySlotsIn) {
        super(inventorySlotsIn);
    }

    @ModifyConstant(method="initGui()V", constant = @Constant(intValue = 30))
    private int modifyLength(int length) {
        return Integer.MAX_VALUE;
    }

    @ModifyVariable(method="renameItem()V", at=@At("STORE"))
    private String modifyName(String name) {
        return name.replaceAll("(NL|\\\\n)", "\n").replaceAll("CL", "\u00a7");
    }
}
