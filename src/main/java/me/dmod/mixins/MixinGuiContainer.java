package me.dmod.mixins;

import me.dmod.DMod;
import me.dmod.utils.Chat;
import me.dmod.utils.Macros;
import me.dmod.utils.Utils;
import me.dmod.utils.Variables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Mixin(value = GuiContainer.class, priority = 1)
public abstract class MixinGuiContainer extends GuiScreen {


    @Inject(at = @At("HEAD"), method = "handleMouseClick(Lnet/minecraft/inventory/Slot;III)V")
    public void handleMouseClick(Slot slotIn, int slotId, int clickedButton, int clickType, CallbackInfo ci) {
        if (Macros.getRecordingMacro() != null) {
            if (((GuiContainer) (Object) this) instanceof GuiChest) {
               GuiChest guiChest = (GuiChest) Minecraft.getMinecraft().currentScreen;
               String guiName = guiChest.lowerChestInventory.getName();
               try {
                   if (guiName.toLowerCase().contains(Macros.macroClicks.get(Macros.getRecordingMacro()).getGuiName().toLowerCase())) {
                       Macros.macroClicks.get(Macros.getRecordingMacro()).clickData.add(new Macros.ClickData(slotIn, slotId, clickedButton, clickType));
                   }
               } catch (Exception e) {
                    e.printStackTrace();
                    Chat.sendPrefixMessage("Error occurred.");
               }
           }
        }
    }


    @ModifyVariable(method = "drawSlot(Lnet/minecraft/inventory/Slot;)V", at = @At("STORE"))
    public ItemStack modifyItemStack(ItemStack itemstack) {
        if (DMod.getConfig().getFixBannerCrash() && itemstack != null && itemstack.getItem() == null) itemstack.setItem(Items.banner);
        return itemstack;
    }


    @Inject(method = "drawSlot", at = @At("TAIL"))
    public void drawSlot(Slot slotIn, CallbackInfo ci) {
        ItemStack itemstack = slotIn.getStack();
        itemstack = this.modifyItemStack(itemstack);
        if (DMod.getConfig().getUuidHighlight()) {
            if (slotIn.getSlotIndex() == 0) Variables.loadedItemStacks = new HashMap<>();
            if (itemstack != null) {
                String hexColor = null;
                if (Utils.getItemStackUUID(itemstack) != null)
                    hexColor = String.format("4D" + "%06X", (0xFFFFFF & Objects.requireNonNull(Utils.getItemStackUUID(itemstack)).hashCode()));
                for (Map.Entry<Slot, ItemStack> set : Variables.loadedItemStacks.entrySet()) {
                    if (Objects.equals(Utils.getItemStackUUID(itemstack), Utils.getItemStackUUID(set.getValue())) && Utils.getItemStackUUID(itemstack) != null) {
                        GlStateManager.disableDepth();
                        assert hexColor != null;
                        Gui.drawRect(slotIn.xDisplayPosition, slotIn.yDisplayPosition, slotIn.xDisplayPosition + 16, slotIn.yDisplayPosition + 16, (int) Long.parseLong(hexColor, 16));
                        Gui.drawRect(set.getKey().xDisplayPosition, set.getKey().yDisplayPosition, set.getKey().xDisplayPosition + 16, set.getKey().yDisplayPosition + 16, (int) Long.parseLong(hexColor, 16));
                        GlStateManager.enableDepth();
                    }
                }
                if (Utils.getItemStackUUID(itemstack) != null && itemstack.stackSize > 1) {
                    GlStateManager.disableDepth();
                    assert hexColor != null;
                    Gui.drawRect(slotIn.xDisplayPosition, slotIn.yDisplayPosition, slotIn.xDisplayPosition + 16, slotIn.yDisplayPosition + 16, (int) Long.parseLong(hexColor, 16));
                    GlStateManager.enableDepth();
                }
                Variables.loadedItemStacks.put(slotIn, itemstack);
            }
        }
    }
}
