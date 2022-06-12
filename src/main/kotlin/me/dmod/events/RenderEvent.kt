package me.dmod.events

import me.dmod.DMod.Companion.config
import me.dmod.events.custom.ServerTimeEvent
import me.dmod.utils.*
import net.minecraftforge.client.event.RenderGameOverlayEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class RenderEvent {
    @SubscribeEvent
    fun renderOverlay(event: RenderGameOverlayEvent.Text?) {
        if (config.enableBlink && config.enableBlinkTitle) {
            Packets.displayBlinkTimer()
        }
        if (config.enableServerTpsTitle) {
            if (Variables.tps < 0) ServerTimeEvent.resetArray()
            Render.renderString(config.serverTpsTextX, config.serverTpsTextY, config.serverTpsMessage.replace("%t", Utils.getTPS()))
        }
        if (config.enableMacroTitle) {
            val message = StringBuilder(config.macroHoveringMessageTitle)
            for ((key, value) in Macros.macroClicks) {
                if (!value.enabled) continue
                message.append("\n").append(config.macroHoveringMessageContent.replace("%n", key).replace("%g", value.guiName))
            }
            Render.renderMultiLineString(config.activeMacrosX, config.activeMacrosY, message.toString())
        }
    }
}