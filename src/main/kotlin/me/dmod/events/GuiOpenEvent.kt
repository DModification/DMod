package me.dmod.events

import me.dmod.DMod.Companion.config
import me.dmod.gui.buttons.WorldDataButton
import me.dmod.utils.Chat
import me.dmod.utils.Guis
import me.dmod.utils.Macros
import me.dmod.utils.Variables
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiRepair
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraftforge.client.event.GuiOpenEvent
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import java.util.*

class GuiOpenEvent {

    @SubscribeEvent
    fun guiInit(event: InitGuiEvent) {
        if (event.gui is GuiContainer) {
            val tileEntityButton: GuiButton = WorldDataButton(15, 0, event.gui.height - 48, "World Data")
            tileEntityButton.height = 20
            tileEntityButton.width = 80
            event.buttonList.add(tileEntityButton)
        }
    }

    @SubscribeEvent
    fun guiOpen(event: GuiOpenEvent) {
        if (event.gui is GuiRepair && config.autoAnvilSave) {
            Chat.sendPrefixMessage("Automatically saved Anvil GUI.")
            Guis.saveAndCloseScreen()
        }
        if (event.gui !is GuiChest) return
        val guiChest = event.gui as GuiChest
        val guiName = (event.gui as GuiChest).lowerChestInventory.name
        for ((key, value) in Macros.macroClicks) {
            if (!value.enabled) continue
            if (guiName.lowercase(Locale.getDefault()).contains(value.guiName.lowercase(Locale.getDefault()))) {
                Thread {
                    var first = true
                    gl@ do {
                        if (Minecraft.getMinecraft().currentScreen == null || Minecraft.getMinecraft().currentScreen !is GuiChest) {
                            Thread.currentThread().interrupt()
                            break
                        }
                        val newGui = Minecraft.getMinecraft().currentScreen as GuiChest
                        try {
                            try {
                                config.macroClickDelay.toInt()
                                config.macroOpenDelay.toInt()
                            } catch (e: NumberFormatException) {
                                config.macroClickDelay = "10"
                                config.macroOpenDelay = "10"
                            }
                            Thread.sleep(config.macroOpenDelay.toLong())
                            for (clickData in value.clickData) {
                                Thread.sleep(config.macroClickDelay.toLong())
                                if (guiChest !== newGui) {
                                    Thread.currentThread().interrupt()
                                    break@gl
                                } else {
                                    if (!(Variables.tps == -2f && config.noFreezeClicks)) {
                                        if (clickData != null) {
                                            Minecraft.getMinecraft().playerController.windowClick((event.gui as GuiChest).inventorySlots.windowId, clickData.slotId, clickData.clickedButton, clickData.clickType, Minecraft.getMinecraft().thePlayer)
                                        }
                                    }
                                }
                            }
                            if (first) Chat.sendPrefixMessage("Finished running macro\u00a7a $key\u00a7f.")
                        } catch (e: InterruptedException) {
                            Chat.sendPrefixMessage("&cAn error occurred.")
                            e.printStackTrace()
                        }
                        first = false
                    } while (config.loopMacros)
                }.start()
            }
        }
    }
}
