package me.dmod.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import org.apache.commons.lang3.tuple.Pair
import org.lwjgl.opengl.GL11
import kotlin.math.pow
import kotlin.math.roundToInt

object Render {

    private fun getNewCoords(x: Int, y: Int): Pair<Float, Float> {
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        val width = x % resolution.scaledWidth
        val height = y % resolution.scaledHeight
        return Pair.of(width.toFloat(), height.toFloat())
    }


    fun renderString(x: Int, y: Int, text: String) {
        val (newX, newY) = getNewCoords(x, y)
        val formattedText = text.replace("&", "\u00a7")
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(formattedText, newX, newY, 1)
    }


    fun renderMultiLineString(x: Int, y: Int, text: String, scale: Double = 1.0) {
        var (newX, newY) = getNewCoords(x, y)
        val formattedText = text.replace("&", "\u00a7")
        val scaleReset = scale.pow(-1.0)
        GL11.glScaled(scale, scale, scale)
        newY -= Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT
        for (line in formattedText.split("\n")) {
            newY += (Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * scale).toInt()
            renderString(newX.roundToInt(), newY.roundToInt(), line)
        }
        GL11.glScaled(scaleReset, scaleReset, scaleReset)
        GlStateManager.color(1f, 1f, 1f, 1f)
    }
}