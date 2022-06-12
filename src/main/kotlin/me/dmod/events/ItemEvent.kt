package me.dmod.events

import me.dmod.utils.Utils
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import org.lwjgl.input.Keyboard

class ItemEvent {
    @SubscribeEvent
    fun onLoreUpdate(event: ItemTooltipEvent) {
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            val uuid = Utils.getItemStackUUID(event.itemStack)
            if (uuid != null) {
                event.toolTip.add("\u00a78UUID: $uuid")
            }
        }
    }
}