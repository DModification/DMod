package me.dmod.utils

import me.dmod.DMod
import me.dmod.DMod.Companion.config
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreenBook
import net.minecraft.client.gui.inventory.GuiContainer
import net.minecraft.nbt.*
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import net.minecraftforge.client.ClientCommandHandler
import org.apache.commons.lang3.RandomStringUtils
import org.lwjgl.input.Mouse
import java.awt.Toolkit
import java.awt.datatransfer.StringSelection
import java.util.*
import java.util.function.Supplier

object Chat {
    @JvmField
    var nested = 0

    @JvmStatic
    fun sendMessage(message: String?) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(message))
    }

    @JvmStatic
    fun sendPrefixMessage(message: String) {
        sendPrefixMessage(message, true)
    }

    @JvmStatic
    fun sendPrefixMessage(message: String, colours: Boolean) {
        var message = message
        if (colours) message = message.replace("&".toRegex(), "\u00a7")
        if (message == "") return
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(DMod.PREFIX + message))
    }

    fun sendPrefixComponent(component: IChatComponent?) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(ChatComponentText(DMod.PREFIX).appendSibling(component))
    }

    fun copyItemNBT() {
        val container = Minecraft.getMinecraft().currentScreen as GuiContainer
        if (container.slotUnderMouse != null) {
            val itemStack = container.slotUnderMouse.stack
            if (itemStack != null) {
                val tag = itemStack.serializeNBT()
                val clipboard = Toolkit.getDefaultToolkit().systemClipboard
                nested = 1
                val formatted = formatNBTTag(tag)
                if (config.enablePrintNBT) {
                    sendMessage(formatted)
                } else sendPrefixMessage("\u00a7aCopied NBT.")
                val selection = StringSelection(formatted.replace("\u00a7.".toRegex(), ""))
                clipboard.setContents(selection, selection)
            }
        }
    }

    fun copyChatComponentAction() {
        val chatComponent = Minecraft.getMinecraft().ingameGUI.chatGUI.getChatComponent(Mouse.getX(), Mouse.getY())
        if (chatComponent != null && chatComponent.chatStyle != null && chatComponent.chatStyle.chatClickEvent != null) {
            sendPrefixMessage("Copied &3" + chatComponent.chatStyle.chatClickEvent.value + "&r to clipboard.")
            Utils.writeClipboard(chatComponent.chatStyle.chatClickEvent.value)
        }
    }

    fun copyBookComponentAction() {
        val guiScreenBook = Minecraft.getMinecraft().currentScreen as GuiScreenBook
        val x = Mouse.getEventX() * guiScreenBook.width / guiScreenBook.mc.displayWidth
        val y = guiScreenBook.height - Mouse.getEventY() * guiScreenBook.height / guiScreenBook.mc.displayHeight - 1
        val chatComponent = guiScreenBook.func_175385_b(x, y)
        if (chatComponent != null && chatComponent.chatStyle != null && chatComponent.chatStyle.chatClickEvent != null) {
            sendPrefixMessage("Copied &3" + chatComponent.chatStyle.chatClickEvent.value + "&r to clipboard.")
            Utils.writeClipboard(chatComponent.chatStyle.chatClickEvent.value)
        }
    }

    @JvmStatic
    fun formatNBTTag(tag: NBTTagCompound, color: Boolean = false): String {
        val stringBuilder = if (color) StringBuilder("{\n") else StringBuilder("\u00a7b{\n")
        for ((key, value) in tag.tagMap) {
            if (stringBuilder.length > 4) stringBuilder.append(",\n")
            for (i in 1..nested * 4) {
                stringBuilder.append(" ")
            }
            if (value is NBTTagList) {
                stringBuilder.append(if (color) "$key: [\n" else "\u00a73$key\u00a7a: \u00a7b[\n")
                stringBuilder.append(formatNBTTagList(value, color))
                stringBuilder.append("\n")
                for (i in 1..nested * 4) {
                    stringBuilder.append(" ")
                }
                stringBuilder.append(if (color) "]" else "\u00a7b]")
            } else if (value is NBTTagCompound) {
                nested++
                stringBuilder.append(if (color) "$key: ${formatNBTTag(value, true)}" else "\u00a73$key\u00a77: ${formatNBTTag(value, false)}")
            } else stringBuilder.append(if (color) "$key: $value" else "\u00a73$key\u00a77: \u00a7a$value")
        }
        nested--
        stringBuilder.append("\n")
        for (i in 1..nested * 4) {
            stringBuilder.append(" ")
        }
        stringBuilder.append(if (color) "}" else "\u00a7b}")
        return stringBuilder.toString().replace("\n\n", "\n")
    }

    private fun formatNBTTagList(tag: NBTTagList, color: Boolean = false): String {
        nested++
        val stringBuilder = StringBuilder()
        for (i in 0 until tag.tagCount()) {
            val base = tag[i]
            val id = base.id.toInt()
            for (j in 1..nested * 4) {
                stringBuilder.append(" ")
            }
            stringBuilder.append(if (color) "$i: " else "\u00a73$i\u00a77: \u00a7a")
            when (id) {
                1 -> {
                    val nbtTagByte = base as NBTTagByte
                    stringBuilder.append(nbtTagByte.byte.toInt())
                }
                2 -> {
                    val nbtTagShort = base as NBTTagShort
                    stringBuilder.append(nbtTagShort.short.toInt())
                }
                3 -> {
                    val nbtTagInt = base as NBTTagInt
                    stringBuilder.append(nbtTagInt.toString())
                }
                4 -> {
                    val nbtTagLong = base as NBTTagLong
                    stringBuilder.append(nbtTagLong.toString())
                }
                5 -> {
                    val nbtTagFloat = base as NBTTagFloat
                    stringBuilder.append(nbtTagFloat.toString())
                }
                6 -> {
                    val nbtTagDouble = base as NBTTagDouble
                    stringBuilder.append(nbtTagDouble.toString())
                }
                7 -> {
                    val nbtTagByteArray = base as NBTTagByteArray
                    stringBuilder.append(Arrays.toString(nbtTagByteArray.byteArray))
                }
                8 -> {
                    val nbtTagString = base as NBTTagString
                    stringBuilder.append('"').append(nbtTagString.string).append('"')
                }
                9 -> {
                    nested++
                    val nbtTagList = base as NBTTagList
                    stringBuilder.append(formatNBTTagList(nbtTagList, color))
                }
                10 -> {
                    nested++
                    val nbtTagCompound = base as NBTTagCompound
                    stringBuilder.append(formatNBTTag(nbtTagCompound, color))
                }
                11 -> {
                    val nbtTagIntArray = base as NBTTagIntArray
                    stringBuilder.append(Arrays.toString(nbtTagIntArray.intArray))
                }
            }
            if (i != tag.tagCount() - 1) stringBuilder.append(",")
            stringBuilder.append("\n")
        }
        nested--
        return stringBuilder.toString()
    }

    @JvmStatic
    fun run(message: String?) {
        if (ClientCommandHandler.instance.executeCommand(Minecraft.getMinecraft().thePlayer, message) != 0) return
        Minecraft.getMinecraft().thePlayer.sendChatMessage(message)
    }

    fun processSpamCommand(spamCommand: String): String {
        return spamCommand
            .replaceIfContains("<by>") {RandomStringUtils.randomAlphanumeric(5)}
            .replaceIfContains("<player>") {Utils.getRandomPlayer()!!.gameProfile.name}
    }

    private fun String.replaceIfContains(oldValue: String, newValue: Supplier<String>): String{
        return if(this.contains(oldValue)) this.replace(oldValue, newValue.get()) else this
    }
}