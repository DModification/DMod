package me.dmod.gui.buttons

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.input.AbstractTextInput
import gg.essential.elementa.components.input.UIMultilineTextInput
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.mixins.IMixinAbstractTextInput
import java.awt.Color

class NBTInputComponent(private val initial: String) : UIContainer() {
    private val textHolder = UIBlock().constrain {
        width = ChildBasedSizeConstraint() + 6.pixels()
        height = ChildBasedSizeConstraint() + 6.pixels()
        color = Color(0, 0, 0, 50).toConstraint()
    } childOf this

    val textInput: AbstractTextInput = UIMultilineTextInput().constrain {
            x = 3.pixels()
            y = 3.pixels()
            width = basicWidthConstraint { super.parent.getWidth() * 0.8f }
        }.setMaxLines(15)

    private var hasSetInitialText = false

    init {
        textInput childOf textHolder
            .onLeftClick { event ->
            event.stopPropagation()
            textInput.grabWindowFocus()
        }.onFocus {
            textInput.setActive(true)
        }.onFocusLost {
            textInput.setActive(false)
        }
        textInput.onKeyType { _, keyCode ->
            when (keyCode) {
                15 -> (textInput as IMixinAbstractTextInput).invokeCommitTextAddition("    ")
                28 -> {
                    if (!UKeyboard.isShiftKeyDown()) (textInput as IMixinAbstractTextInput).invokeCommitTextAddition("\n")
                }
            }
        }



        constrain {
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }
    }

    override fun animationFrame() {
        super.animationFrame()

        if (!hasSetInitialText) {
            textInput.setText(initial)
            hasSetInitialText = true
        }
    }
}