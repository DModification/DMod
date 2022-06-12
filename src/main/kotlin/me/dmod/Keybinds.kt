package me.dmod

import me.dmod.utils.Config
import net.minecraft.client.settings.KeyBinding
import org.lwjgl.input.Keyboard

enum class Actions {
    NONE,
    CLOSE_SILENTLY,
    BLINK,
    INVENTORY_PACKETS,
    CREATE_GHOST_BLOCKS,
    REVERT_GHOST_BLOCKS,
    SAVE_MACRO,
    OPEN_GUI,
    OPEN_BOOK_GUI,
    OPEN_SIGN_GUI,
    COMMAND,
    CRUNCH_GUI
}

val keybinds = mutableListOf<Triple<Int, Actions, String>>()



object Keybinds {
    @JvmStatic
    fun addKeybind(key: Int = 0, action: Actions, command: String = "") {
        keybinds += Triple(key, action, command)
        Config.writeKeybindConfig()
    }
    @JvmStatic
    fun addDefaultKeybinds() {
        addKeybind(Keyboard.KEY_F6, Actions.CLOSE_SILENTLY)
        addKeybind(Keyboard.KEY_O, Actions.BLINK)
        addKeybind(Keyboard.KEY_BACKSLASH, Actions.INVENTORY_PACKETS)
        addKeybind(Keyboard.KEY_N, Actions.CREATE_GHOST_BLOCKS)
        addKeybind(Keyboard.KEY_Y, Actions.REVERT_GHOST_BLOCKS)
        addKeybind(Keyboard.KEY_EQUALS, Actions.SAVE_MACRO)
        addKeybind(Keyboard.KEY_V, Actions.OPEN_GUI)
        addKeybind(Keyboard.KEY_B, Actions.OPEN_SIGN_GUI)
        addKeybind(Keyboard.KEY_G, Actions.OPEN_BOOK_GUI)
        addKeybind(Keyboard.KEY_F4, Actions.CRUNCH_GUI)
    }

    @JvmStatic
    fun searchByAction(action: Actions): MutableList<Triple<Int, Actions, String>> {
        val found = mutableListOf<Triple<Int, Actions, String>>()
        for (keybind in keybinds) {
            if (keybind.second == action) found.add(keybind)
        }
        return found
    }

    @JvmStatic
    fun isDown(action: Actions) : Boolean {
        for (keybind in keybinds) {
            if (keybind.second == action && isDown(keybind.first)) return true
        }
        return false
    }

    @JvmStatic
    fun isDown(keybind: KeyBinding): Boolean {
        return isDown(keybind.keyCode)
    }

    @JvmStatic
    fun isDown(key: Int): Boolean {
        return Keyboard.isKeyDown(key)
    }

    @JvmStatic
    fun isPressed(action: Actions) : Boolean {
        for (keybind in keybinds) {
            if (keybind.second == action && isPressed(keybind.first)) return true
        }
        return false
    }

    @JvmStatic
    fun isPressed(keybind: KeyBinding): Boolean {
        return isPressed(keybind.keyCode)
    }

    @JvmStatic
    fun isPressed(key: Int): Boolean {
        return Keyboard.getEventKey() == key && Keyboard.isKeyDown(key)
    }
}