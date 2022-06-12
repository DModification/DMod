package me.dmod.gui.buttons

import gg.essential.api.EssentialAPI
import me.dmod.gui.DModWorldData
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton

class WorldDataButton(buttonId: Int, x: Int, y: Int, buttonText: String?) : GuiButton(buttonId, x, y, buttonText) {
    override fun mousePressed(mc: Minecraft, mouseX: Int, mouseY: Int): Boolean {
        if (!hovered) return super.mousePressed(mc, mouseX, mouseY)
        EssentialAPI.getGuiUtil().openScreen(DModWorldData())
        return super.mousePressed(mc, mouseX, mouseY)
    }
}