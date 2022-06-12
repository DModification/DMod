package me.dmod.events

import me.dmod.events.custom.ServerTimeEvent
import me.dmod.utils.Ghosts
import me.dmod.utils.Guis
import me.dmod.utils.Variables
import net.minecraft.client.Minecraft
import net.minecraftforge.event.entity.EntityJoinWorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class WorldEvent {
    @SubscribeEvent
    fun onWorldJoin(event: EntityJoinWorldEvent) {
        if (event.entity === Minecraft.getMinecraft().thePlayer) {
            if (Guis.savedScreen != null) Guis.worldSwitched = true
            ServerTimeEvent.resetArray()
            Variables.run {
                tps = -1f
                lastTimeEvent = null
                Ghosts.resetGhosts()
            }
        }
    }
}