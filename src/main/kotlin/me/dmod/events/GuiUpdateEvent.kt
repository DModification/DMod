package me.dmod.events

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.util.ChatComponentText
import net.minecraft.util.MathHelper
import net.minecraftforge.client.event.GuiScreenEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard
import org.lwjgl.input.Mouse
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection

class GuiUpdateEvent {

    @SubscribeEvent
    fun onGuiClick(event: GuiScreenEvent.MouseInputEvent.Pre?) {
        if (Minecraft.getMinecraft().currentScreen is GuiChat) {
            if (Mouse.getEventButton() == 2 && Mouse.isButtonDown(2)) {
                val scaledresolution = ScaledResolution(Minecraft.getMinecraft())
                val i = scaledresolution.scaleFactor
                val f = Minecraft.getMinecraft().ingameGUI.chatGUI.chatScale
                var j = Mouse.getX() / i - 3
                var k = Mouse.getY() / i - 27
                j = MathHelper.floor_float(j.toFloat() / f)
                k = MathHelper.floor_float(k.toFloat() / f)
                if (j >= 0 && k >= 0) {
                    val l = Minecraft.getMinecraft().ingameGUI.chatGUI.lineCount.coerceAtMost(Minecraft.getMinecraft().ingameGUI.chatGUI.drawnChatLines.size)
                    if (j <= MathHelper.floor_float(Minecraft.getMinecraft().ingameGUI.chatGUI.chatWidth.toFloat() / Minecraft.getMinecraft().ingameGUI.chatGUI.chatScale) && k < Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT * l + l) {
                        val i1 = k / Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT + Minecraft.getMinecraft().ingameGUI.chatGUI.scrollPos
                        if (i1 >= 0 && i1 < Minecraft.getMinecraft().ingameGUI.chatGUI.drawnChatLines.size) {
                            val chatline = Minecraft.getMinecraft().ingameGUI.chatGUI.drawnChatLines[i1]
                            var content: String
                            content = if (!Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
                                chatline.chatComponent.unformattedText.replace("\u00a7.".toRegex(), "")
                            } else chatline.chatComponent.formattedText.replace("\u00a7b ✓".toRegex(), "").replace("\u00a7".toRegex(), "&")
                            if (content.endsWith("✓")) content = content.substring(0, content.length - 1)
                            if (content.endsWith("✓\u00a7r")) content = content.substring(0, content.length - 3)
                            val selection = StringSelection(content)
                            val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                            clipboard.setContents(selection, selection)
                            if (!chatline.chatComponent.unformattedText.endsWith("✓")) chatline.chatComponent.appendSibling(ChatComponentText("\u00a7b ✓"))
                            Minecraft.getMinecraft().ingameGUI.chatGUI.drawnChatLines[i1] = chatline
                        }
                    }
                }
            }
        }
    }
}