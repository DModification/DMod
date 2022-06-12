package me.dmod.gui

import gg.essential.api.EssentialAPI
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import me.dmod.DMod
import me.dmod.gui.buttons.MainButton
import java.awt.Color

class DModMainMenu : WindowScreen(ElementaVersion.V1) {
    init {
        UIText("DMod", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.27f)
            textScale = 3.pixels()
            color = Color(130, 209, 227).toConstraint()
        }

        UIText("DMod is a Minecraft utility mod that aids", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.035f)
            textScale = 1.pixels()
            color = Color(8421504).toConstraint()
        }
        UIText("the Hypixel Team in discovering vulnerabilities.", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint()
            textScale = 1.pixels()
            color = Color(8421504).toConstraint()
        }
        MainButton("Config").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.075f)
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DMod.config.gui())
        }
        MainButton("Keybinds").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.009f)
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DModKeybinds())
        }
        MainButton("Locations").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.009f)
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DModLocations())
        }
    }
}