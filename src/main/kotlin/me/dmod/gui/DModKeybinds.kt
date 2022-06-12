package me.dmod.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.universal.UKeyboard
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.Actions
import me.dmod.Keybinds
import me.dmod.gui.buttons.ActionsDropDown
import me.dmod.gui.buttons.KeyButton
import me.dmod.gui.buttons.MainButton
import me.dmod.gui.buttons.RemoveButton
import me.dmod.keybinds
import net.minecraft.client.Minecraft
import net.minecraft.util.ChatAllowedCharacters
import org.lwjgl.input.Keyboard
import java.awt.Color

class DModKeybinds : WindowScreen(ElementaVersion.V1) {
    private val scrollComponent: ScrollComponent
    private val keys = mutableListOf<KeyButton>()
    private var clickedButton: KeyButton? = null

    init {
        UIText("Keybindings", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        scrollComponent = ScrollComponent(
            innerPadding = 4f,
        ).childOf(window).constrain {
            x = CenterConstraint()
            y = 15.percent()
            width = 90.percent()
            height = 70.percent() + 2.pixels()
        }

        val bottomButtons = UIContainer().childOf(window).constrain {
            x = CenterConstraint()
            y = 90.percent()
            width = ChildBasedSizeConstraint()
            height = ChildBasedSizeConstraint()
        }

        MainButton("Create Keybind").childOf(bottomButtons).constrain {
            x = SiblingConstraint(5f)
            y = 0.pixels()
        }.onLeftClick {
            createKeybind()
        }
        MainButton("Save and Close").childOf(bottomButtons).constrain {
            x = SiblingConstraint(5f)
            y = 0.pixels()
        }.onLeftClick {
            saveKeybinds()
        }
        for (key in keybinds) {
            createKeybind(key.first, key.second, key.third)
        }
    }

    private fun createKeybind(key: Int = 0, action: Actions = Actions.NONE, command: String = "") {
        val container = UIBlock().childOf(scrollComponent).constrain {
            x = CenterConstraint()
            y = SiblingConstraint(5f)
            width = 90.percent()
            height = ChildBasedMaxSizeConstraint() + 15.pixels()
            color = Color(136, 157, 191, 50).toConstraint()
        }.effect(OutlineEffect(Color(136, 157, 191), 1f))
        UIText("Action: ").childOf(container).constrain {
            x = SiblingConstraint() + 4.pixels()
            y = CenterConstraint()
        }
        val containerCommandAction = UIContainer().childOf(container).constrain {
            width = ChildBasedSizeConstraint() + 7.pixels()
            height = ChildBasedMaxSizeConstraint()
            x = SiblingConstraint(1f)
            y = CenterConstraint()
        }
        val containerAction = ActionsDropDown(action.ordinal, Actions.values().map {
            it.name.replace("_", " ").lowercase().split(" ").joinToString(" ") { string -> string.replaceFirstChar(Char::titlecase) }
        }
        ).childOf(containerCommandAction).constrain {
            x = SiblingConstraint()
            y = CenterConstraint()
        }
        val keyText = UIText("Key: ").childOf(container).constrain {
            x = SiblingConstraint(1f) + 8.pixels()
            y = CenterConstraint()
        }
        val containerKey = KeyButton(key).childOf(container).constrain {
            x = SiblingConstraint(1f) + 2.pixels()
            y = CenterConstraint()
        }
        keys.add(containerKey)
        val commandInput = UITextInput("Enter Command")
        (commandInput.constrain {
            x = SiblingConstraint() + 4.pixels()
            y = CenterConstraint()
            width = 75.pixels()
        }.onLeftClick {
            if (clickedButton == null) grabWindowFocus()
        } as UITextInput).also {
            it.setText(command)
            it.onKeyType { _, _ ->
                it.setText(it.getText().filter(ChatAllowedCharacters::isAllowedCharacter).take(255))
                containerKey.command = it.getText()
            }
        }
        if (containerAction.getValue() == Actions.COMMAND.ordinal) {
            if (containerCommandAction.children.size < 2) {
                containerCommandAction.addChild(commandInput)
                containerCommandAction.enableEffect(OutlineEffect(Color(74, 76, 237), 1f))
            }
        }
        RemoveButton("x").childOf(container).constrain {
            x = 90.percent()
            width = 20.pixels()
            y = CenterConstraint()
        }.onLeftClick {
            container.parent.removeChild(container)
            keys.remove(containerKey)
        }
        containerKey.constrain {
            x = SiblingConstraint(3f)
            y = CenterConstraint()
            containerKey.action = action.ordinal
            containerKey.command = command
        }.onMouseClick {
            if (containerKey != clickedButton) {
                clickedButton?.text?.setText(Keyboard.getKeyName(clickedButton!!.key))
                clickedButton = containerKey
            }
        }.effect(OutlineEffect(Color(74, 76, 237), 1f))
        containerAction.onMouseClick {
            if (containerAction.active) {
                containerKey.hide()
                keyText.hide()
            } else {
                containerKey.unhide()
                keyText.unhide()
                if (containerAction.getValue() == Actions.COMMAND.ordinal) {
                    if (containerCommandAction.children.size < 2) {
                        containerCommandAction.addChild(commandInput)
                        containerCommandAction.enableEffect(OutlineEffect(Color(74, 76, 237), 1f))
                    }
                } else {
                    containerCommandAction.removeChild(commandInput)
                    containerCommandAction.removeEffect<OutlineEffect>()
                }
            }
        }
        containerAction.onValueChange {
            containerKey.action = containerAction.getValue()
        }
    }

    override fun onKeyPressed(keyCode: Int, typedChar: Char, modifiers: UKeyboard.Modifiers?) {
        if (clickedButton != null) {
            when {
                keyCode == 1 -> clickedButton!!.key = 0
                keyCode != 0 -> clickedButton!!.key = keyCode
                typedChar.code > 0 -> clickedButton!!.key = typedChar.code + 256
            }
            clickedButton!!.text.setText(Keyboard.getKeyName(clickedButton!!.key))
            clickedButton = null
        }
        else super.onKeyPressed(keyCode, typedChar, modifiers)
    }

    private fun saveKeybinds() {
        keybinds.clear()
        for (key in keys) {
            println("key: ${key.key}, command: ${key.command}")
            Keybinds.addKeybind(key.key, Actions.values()[key.action], key.command)
        }
        Minecraft.getMinecraft().thePlayer.closeScreen()
    }
}