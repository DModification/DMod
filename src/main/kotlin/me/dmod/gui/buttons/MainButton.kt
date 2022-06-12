package me.dmod.gui.buttons

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import java.awt.Color

// https://github.com/Skytils/SkytilsMod/blob/1.x/src/main/kotlin/skytils/skytilsmod/gui/components/SimpleButton.kt
class MainButton @JvmOverloads constructor(t: String, private val h: Boolean = false, private val w: Boolean = false) :
    UIBlock(Color(130, 223, 227, 80)) {

    val text = UIText(t).constrain {
        x = CenterConstraint()
        y = CenterConstraint()
        color = Color(14737632).toConstraint()
    } childOf this

    init {
        this
            .constrain {
                width = if (w) {
                    RelativeConstraint()
                } else {
                    (text.getWidth() + 40).pixels()
                }
                height = if (h) {
                    RelativeConstraint()
                } else {
                    (text.getHeight() + 10).pixels()
                }
            }.onMouseEnter {
                animate {
                    setColorAnimation(
                        Animations.OUT_EXP,
                        0.5f,
                        Color(137, 130, 227, 80).toConstraint(),
                        0f
                    )
                }
                text.constrain {
                    color = Color(130, 209, 227).toConstraint()
                }
            }.onMouseLeave {
                animate {
                    setColorAnimation(
                        Animations.OUT_EXP,
                        0.5f,
                        Color(130, 223, 227, 80).toConstraint()
                    )
                }
                text.constrain {
                    color = Color(14737632).toConstraint()
                }
            }
    }
}