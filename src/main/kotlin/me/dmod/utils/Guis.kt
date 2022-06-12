package me.dmod.utils

import me.dmod.DMod.Companion.config
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.client.gui.inventory.GuiChest
import net.minecraft.client.gui.inventory.GuiEditSign
import net.minecraft.tileentity.TileEntitySign
import net.minecraft.util.ChatComponentText
import org.apache.commons.lang3.RandomStringUtils

object Guis {
    @JvmStatic
    var savedScreen: GuiScreen? = null
    @JvmStatic
    var savedSign: GuiScreen? = null
    @JvmStatic
    private var savedBook: GuiScreen? = null
    @JvmStatic
    var latest: GuiScreen? = null
    var worldSwitched = false

    @JvmStatic
    fun saveAndCloseScreen() {
        val currentScreen = Minecraft.getMinecraft().currentScreen
        latest = currentScreen
        if (currentScreen == null) return
        worldSwitched = false
        if (currentScreen is GuiScreenBook) savedBook = currentScreen else if (currentScreen is GuiEditSign) savedSign = currentScreen else savedScreen = currentScreen
        if (config.enableGuiMessages) {
            if (currentScreen is GuiScreenBook) Chat.sendPrefixMessage(config.guiSaveBookMessage) else if (currentScreen is GuiEditSign) Chat.sendPrefixMessage(config.guiSaveSignMessage) else Chat.sendPrefixMessage(config.guiSaveMessage)
        }
        Minecraft.getMinecraft().thePlayer.closeScreen()
    }

    fun showScreen() {
        if (savedScreen == null && config.enableGuiMessages) Chat.sendPrefixMessage(config.guiNotFoundMessage) else {
            if (config.enableGuiMessages) Chat.sendPrefixMessage(config.guiDisplayMessage)
            if (savedScreen is GuiChest && worldSwitched) {
                Minecraft.getMinecraft().thePlayer.displayGUIChest((savedScreen as GuiChest?)!!.lowerChestInventory)
            } else Minecraft.getMinecraft().displayGuiScreen(savedScreen)
            savedScreen = null
        }
        worldSwitched = false
    }

    fun showSign() {
        if (config.autoPreviousSave) saveAndCloseScreen()
        if (savedSign == null && config.enableGuiMessages) Chat.sendPrefixMessage(config.signGuiNotFoundMessage) else {
            if (config.enableGuiMessages) Chat.sendPrefixMessage(config.guiDisplayMessage)
            Minecraft.getMinecraft().displayGuiScreen(savedSign)
        }
    }

    fun showBook() {
        if (savedBook == null && config.enableGuiMessages) Chat.sendPrefixMessage(config.bookGuiNotFoundMessage) else {
            if (config.enableGuiMessages) Chat.sendPrefixMessage(config.guiDisplayMessage)
            Minecraft.getMinecraft().displayGuiScreen(savedBook)
        }
    }

    fun toggleInventoryPackets() {
        config.enableInventory = !config.enableInventory
        if (config.enableInventoryMessages) {
            if (config.enableInventory) Chat.sendPrefixMessage(config.enableInventoryMessage) else Chat.sendPrefixMessage(config.disableInventoryMessage)
        }
    }

    fun crunch() {
        val reportPlayer = Utils.getRandomPlayer(excludeSelf = true)
        val name = reportPlayer?.gameProfile?.name ?: "TimeDeo"
        Chat.run("/report $name")
        Variables.shouldCrunch = true
    }
}