package me.dmod.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.gui.buttons.MainButton
import me.dmod.utils.Chat
import net.minecraft.client.Minecraft
import java.awt.Color

class DModAutoSign : WindowScreen(ElementaVersion.V1, newGuiScale = 4) {

    companion object {
        @JvmStatic
        val lines: Array<String> = Array(4) { "" }
    }

    private fun update() {
        val input = window.childrenOfType<UIBlock>().filter { it.childrenOfType<UITextInput>().isNotEmpty() }
            .map { it.childrenOfType<UITextInput>()[0] }
        for (i in 0..3) {
            lines[i] = input[i].getText()
        }
        Chat.sendPrefixMessage("\u00a7aUpdated auto-sign text.")
    }

    init {
        UIText("Auto Sign", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        for (i in 0..3) {
            UIBlock(color = Color.BLACK).childOf(window).constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 5.pixels()
                width = 75.percent()
                height = 15.pixels()
            }.effect(OutlineEffect(Color(136, 157, 191), 1f)).also { textContainer ->
                UITextInput("", false).constrain {
                    y = CenterConstraint()
                    x = 3.pixels()
                    width = 100.percent() - 3.pixels()
                }.childOf(textContainer).apply {
                    this.onLeftClick {
                        grabWindowFocus()
                    }
                    this.setColor(Color.WHITE)
                    if (lines[i].isEmpty()) this.placeholder = "Line ${i+1}"
                    else this.setText(lines[i])
                }
            }
        }
        MainButton("Save and Close").constrain {
            x = CenterConstraint()
            y = 90.percent()
        }.onLeftClick {
            update()
            Minecraft.getMinecraft().displayGuiScreen(null)
        }.childOf(window)
    }
}