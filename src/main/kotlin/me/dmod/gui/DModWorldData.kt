package me.dmod.gui

import gg.essential.api.EssentialAPI
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import me.dmod.gui.buttons.MainButton
import java.awt.Color

class DModWorldData : WindowScreen(ElementaVersion.V1) {
    init {
        UIText("World Data", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.27f)
            textScale = 3.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        MainButton("Entities").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.075f)
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DModEntities())
        }
        MainButton("Tile Entities").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.009f)
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DModTileEntities())
        }
    }
}