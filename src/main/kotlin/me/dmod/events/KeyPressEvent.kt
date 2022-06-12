package me.dmod.events

import me.dmod.events.custom.KeyboardEvent
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.GuiScreenEvent.KeyboardInputEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent
import org.lwjgl.input.Keyboard

class KeyPressEvent {
    @SubscribeEvent
    fun onKeyPress(event: KeyInputEvent?) {
        KeyboardEvent.onKeyboard()
    }

    @SubscribeEvent
    fun onGuiKeyboard(event: KeyboardInputEvent.Pre) {
        KeyboardEvent.onKeyboard(event.gui)
        if (Keyboard.getEventKey() == Minecraft.getMinecraft().gameSettings.keyBindStreamStartStop.keyCode) event.isCanceled = true
    }
}