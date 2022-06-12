package me.dmod.gui.buttons

import me.dmod.utils.Render
import net.minecraft.client.Minecraft
import net.minecraft.client.audio.SoundHandler
import net.minecraft.client.gui.GuiButton

class PosButton(buttonId: Int, private val x: Int, private val y: Int, width: Double, height: Double, scale: Double, text: String, text2: String?, text2Offset: Int) : GuiButton(buttonId, x, y, text) {
    private val scale: Double
    private val text: String
    private val text2: String?
    private val text2Offset: Int
    override fun drawButton(mc: Minecraft, mouseX: Int, mouseY: Int) {
        val splitText: Array<String> = text2?.split("\n")?.toTypedArray() ?: text.split("\n").toTypedArray()
        var longestText = -1
        for (s in splitText) {
            val stringLength = mc.fontRendererObj.getStringWidth(s)
            if (stringLength > longestText) {
                longestText = stringLength
            }
        }
        if (text2 != null) {
            Render.renderMultiLineString((x + text2Offset * scale).toInt(), y, text2, scale)
        }
        Render.renderMultiLineString(x, y, text, scale)
    }

    override fun playPressSound(soundHandler: SoundHandler) {}

    init {
        this.width = width.toInt()
        this.height = height.toInt()
        this.scale = scale
        this.text = text
        this.text2 = text2
        this.text2Offset = text2Offset
    }
}