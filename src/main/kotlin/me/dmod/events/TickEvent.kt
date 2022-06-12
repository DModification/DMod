package me.dmod.events

import me.dmod.Actions
import me.dmod.DMod.Companion.config
import me.dmod.Keybinds
import me.dmod.commands.NewCommandsCommand
import me.dmod.mixins.IMixinGuiScreenBook
import me.dmod.utils.*
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.potion.Potion
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent
import java.time.Instant
import java.time.ZonedDateTime
import java.util.*

class TickEvent {
    private var ticks = 0
    private var ghostTicks = 0
    private var lastNick: String = ""
    @SubscribeEvent
    fun onTick(event: ClientTickEvent) {
        val screen = Minecraft.getMinecraft().currentScreen
        if (Variables.nick) {
            if (screen is GuiScreenBook) {
                val regex = Regex("\\[\"We've generated a random username for you:\",\"\\\\n\",\"\u00a7l(.*)\u00a7r.*")
                val book: IMixinGuiScreenBook = screen as IMixinGuiScreenBook
                val page = book.bookPages.getStringTagAt(0)
                val result = regex.find(page)
                if (result != null && result.groupValues[1] != lastNick) {
                    lastNick = result.groupValues[1]
                    if (!Utils.isNickValid(lastNick)) {
                        Thread {
                            Thread.sleep(600)
                            Chat.run("/nick help setrandom")
                        }.start()
                    }
                }
            }
        }

       
        if (Packets.timedToggle != null) {
            val time = ZonedDateTime.now()
            if (time.hour >= Packets.timedToggle!![0] && time.minute >= Packets.timedToggle!![1] && time.second >= Packets.timedToggle!![2]) {
                Packets.toggleBlink()
                Packets.timedToggle = null
            }
        }

       
        if (Minecraft.getMinecraft().thePlayer != null) {
            if (config.disableMiningFatigue && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.digSlowdown.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.digSlowdown.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Mining Fatigue"))
                }
            }
            if (config.disableJumpBoost && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.jump.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.jump.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Jump Boost"))
                }
            }
            if (config.disableBlindness && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.blindness.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.blindness.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Blindness"))
                }
            }
            if (config.disableHaste && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.digSpeed.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.digSpeed.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Haste"))
                }
            }
            if (config.disableSlowness && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSlowdown.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.moveSlowdown.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Slowness"))
                }
            }
            if (config.disableNausea && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.confusion.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.confusion.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Nausea"))
                }
            }
            if (config.disableSpeed && Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed.getId())) {
                Minecraft.getMinecraft().thePlayer.removePotionEffect(Potion.moveSpeed.getId())
                if (config.effectsMessage != "") {
                    Chat.sendPrefixMessage(config.effectsMessage.replace("%e", "Speed"))
                }
            }
        }

       
        if (Variables.lastTimeEvent != null && Instant.now().epochSecond - Variables.lastTimeEvent!!.epochSecond > 4) {
            Variables.tps = -2.0f
        }

       
        if (Keybinds.isDown(Actions.CREATE_GHOST_BLOCKS) && Minecraft.getMinecraft().currentScreen == null && event.phase == TickEvent.Phase.START) {
            if (ghostTicks == config.ghostHoldingDelay / 50 && config.enableGhostHolding) {
                Ghosts.deleteBlock()
                ghostTicks = -1
            }
            ghostTicks++
        } else if (ghostTicks != 0 && !Keybinds.isDown(Actions.CREATE_GHOST_BLOCKS)) ghostTicks = 0

       
        if (!Variables.tabComplete) ticks = 0
        if (Variables.tabComplete) {
            ticks++
            if (ticks == 1) {
                NewCommandsCommand.openSlashChat()
            }
            if (ticks == 60) {
                Variables.tabComplete = false
                Variables.newTabComplete = false
                Chat.sendPrefixMessage("\u00a7cCouldn't get commands.")
            }
        }

       
        if (event.phase == TickEvent.Phase.START) {
            if (Minecraft.getMinecraft().thePlayer == null && config.enableBlink) config.enableBlink = false
        }

       
        if (Minecraft.getMinecraft().currentScreen is GuiChest) {
            val guiChest = Minecraft.getMinecraft().currentScreen as GuiChest
            val guiTitle = guiChest.lowerChestInventory.name
            val matches = HashMap<String, Int>()
            for (i in 0 until guiChest.lowerChestInventory.sizeInventory) {
                val itemStack = guiChest.lowerChestInventory.getStackInSlot(i) ?: continue
                var guiItem: List<String>? = null
                for (item in Variables.guiItems) {
                    if (guiTitle.lowercase(Locale.getDefault()).contains(item[0].lowercase(Locale.getDefault())) && itemStack.displayName.lowercase(
                            Locale.getDefault()
                        )
                            .contains(item[1].lowercase(Locale.getDefault()))) {
                        matches.merge(item[1], 1) { _: Int?, b: Int -> matches[item[b]]!! + b }
                        if (item[2].toInt() == matches[item[1]]) {
                            guiItem = item
                            break
                        }
                    }
                }
                if (guiItem != null) {
                    Variables.guiItems.remove(guiItem)
                    Chat.sendPrefixMessage("Clicked Item \u00a7f" + itemStack.displayName + " \u00a7fin GUI \u00a7a" + guiTitle)
                    Minecraft.getMinecraft().playerController.windowClick(guiChest.inventorySlots.windowId, i, 0, 0, Minecraft.getMinecraft().thePlayer)
                }
            }
        }
    }
}