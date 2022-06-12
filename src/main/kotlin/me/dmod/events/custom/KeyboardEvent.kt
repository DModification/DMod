package me.dmod.events.custom

import gg.essential.elementa.WindowScreen
import gg.essential.vigilance.gui.SettingsGui
import me.dmod.Actions
import me.dmod.Keybinds
import me.dmod.gui.DModLocations
import me.dmod.keybinds
import me.dmod.utils.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.*
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.client.gui.inventory.GuiContainerCreative
import net.minecraft.client.gui.inventory.GuiEditSign
import org.lwjgl.input.Keyboard

object KeyboardEvent {
    private val blacklist = arrayOf<Class<*>>(
        GuiChat::class.java,
        GuiEditSign::class.java,
        GuiScreenBook::class.java,
        GuiNewChat::class.java,
        GuiContainerCreative::class.java,
        SettingsGui::class.java,
        DModLocations::class.java,
        WindowScreen::class.java
    )

    @JvmOverloads
    fun onKeyboard(origin: GuiScreen? = null) {
        if (Minecraft.getMinecraft().thePlayer == null) return
        val eventKey = Keyboard.getEventKey()
        if (!Keybinds.isDown(eventKey)) return
        if (eventKey == Keyboard.KEY_NONE) return
        var blacklisted = false
        if (origin is GuiRepair) {
            if (origin.nameField.isFocused) {
                blacklisted = true
            }
        } else if (origin != null) {
            for (clazz in blacklist) {
                if (clazz.isAssignableFrom(origin.javaClass)) {
                    blacklisted = true
                    break
                }
            }
        }

        if (!blacklisted) {
            for (keybind in keybinds) {
                if (!Keybinds.isPressed(keybind.first)) continue
                when (keybind.second) {
                        Actions.BLINK -> Packets.toggleBlink()
                        Actions.OPEN_SIGN_GUI -> Guis.showSign()
                        Actions.OPEN_BOOK_GUI -> Guis.showBook()
                        Actions.OPEN_GUI -> Guis.showScreen()
                        Actions.INVENTORY_PACKETS -> Guis.toggleInventoryPackets()
                        Actions.SAVE_MACRO -> Macros.saveMacro()
                        Actions.COMMAND -> Chat.run(keybind.third)
                        Actions.CRUNCH_GUI -> Guis.crunch()
                        else -> {
                            if (origin === null) {
                                when (keybind.second) {
                                    Actions.CREATE_GHOST_BLOCKS -> Ghosts.deleteBlock()
                                    Actions.REVERT_GHOST_BLOCKS -> Ghosts.refreshBlocks()
                                    else -> {}
                                }
                            }
                        }
                    }
            }
        }
        for (keybind in keybinds) {
            if (!Keybinds.isPressed(keybind.first)) continue
            when (keybind.second) {
                Actions.CLOSE_SILENTLY -> Guis.saveAndCloseScreen()
                else -> {}
            }
        }
        if (Keybinds.isPressed(Keyboard.KEY_RCONTROL)) {
            when (origin) {
                is GuiChat -> Chat.copyChatComponentAction()
                is GuiScreenBook -> Chat.copyBookComponentAction()
                is GuiContainer -> Chat.copyItemNBT()
            }
        }
    }
}