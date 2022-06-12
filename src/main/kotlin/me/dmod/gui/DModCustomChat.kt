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
import java.awt.Color

class DModCustomChat : WindowScreen(ElementaVersion.V1, newGuiScale = 4) {

    init {
        UIText("Custom Chat", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        val textContainer = UIBlock(color = Color.BLACK).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + 5.pixels()
            width = 75.percent()
            height = 10.pixels()
        }.effect(OutlineEffect(Color(136, 157, 191), 1f))
        val textInput = UITextInput("", false)
        textInput.setColor(Color.WHITE)
        textInput.childOf(textContainer).constrain {
            width = 100.percent() - 2.pixels()
            x = 1.pixels()
            y = 0.5.pixels()
        }.onLeftClick {grabWindowFocus()}
        MainButton("Send").constrain {
            x = CenterConstraint()
            y = 90.percent()
        }.onLeftClick {
            Chat.run(textInput.getText())
        }.childOf(window)
    }
}