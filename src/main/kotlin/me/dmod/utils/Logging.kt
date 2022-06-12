package me.dmod.utils

import me.dmod.DMod.Companion.config
import net.minecraft.client.Minecraft
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C0DPacketCloseWindow
import net.minecraft.network.play.client.C0EPacketClickWindow
import net.minecraft.network.play.client.C12PacketUpdateSign
import net.minecraft.network.play.server.*
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent

object Logging {
    @JvmStatic
    fun logPacket(packet: Packet<*>?) {
        if (config.logSignPackets) {
            if (packet is C12PacketUpdateSign && config.cPacketUpdateSign) logCPacketUpdateSign(packet)
            else if (packet is S33PacketUpdateSign && config.sPacketUpdateSign) logSPacketUpdateSign(packet)
            else if (packet is S36PacketSignEditorOpen && config.sPacketSignEditorOpen) logSPacketSignEditorOpen(packet)
        } else if (config.logWindowPackets) {
            if (packet is C0EPacketClickWindow && config.cPacketClickWindow) logCPacketClickWindow(packet)
            else if (packet is C0DPacketCloseWindow && config.cPacketCloseWindow) logCPacketCloseWindow()
            else if (packet is S2EPacketCloseWindow && config.sPacketCloseWindow) logSPacketCloseWindow()
            else if (packet is S30PacketWindowItems && config.sPacketWindowItems) logSPacketWindowItems(packet)
            else if (packet is S2DPacketOpenWindow && config.sPacketOpenWindow) logSPacketOpenWindow(packet)
        }
    }

    private fun logCPacketUpdateSign(packet: C12PacketUpdateSign) {
        val lines = StringBuilder()
        for (i in packet.lines.indices) {
            lines.append("\u00a73").append(i).append("\u00a77: \u00a7r").append(packet.lines[i].formattedText).append("\u00a78 | ")
        }
        Chat.sendPrefixMessage("""
    §aSent §fUpdate Sign Packet§7:
    §bLines§7: ${lines.substring(0, lines.length - 2)}
    §bPos§7: §3${packet.position.x}§7, §3${packet.position.y}§7, §3${packet.position.z}
    """.trimIndent())
    }

    private fun logSPacketUpdateSign(packet: S33PacketUpdateSign) {
        val lines = StringBuilder()
        for (i in packet.lines.indices) {
            lines.append("\u00a73").append(i).append("\u00a77: \u00a7r").append(packet.lines[i].formattedText).append("\u00a78 | ")
        }
        Chat.sendPrefixMessage("""
    §cReceived §fUpdate Sign Packet§7:
    §bLines§7: ${lines.substring(0, lines.length - 2)}
    §bPos§7: §3${packet.pos.x}§7, §3${packet.pos.y}§7, §3${packet.pos.z}
    """.trimIndent())
    }

    private fun logSPacketSignEditorOpen(packet: S36PacketSignEditorOpen) {
        Chat.sendPrefixMessage("""
    §cReceived §fSign Editor Open Packet§7:
    §bPos§7: §3${packet.signPosition.x}§7, §3${packet.signPosition.y}§7, §3${packet.signPosition.z}
    """.trimIndent())
    }

    private fun logSPacketWindowItems(packet: S30PacketWindowItems) {
        val itemStacks = packet.itemStacks
        val component: IChatComponent = ChatComponentText("\u00a7cReceived \u00a7fWindow Items Packet\u00a77:\n\u00a7bItems\u00a77: ")
        for (i in itemStacks.indices) {
            if (itemStacks[i] == null) continue
            component.appendText("\u00a73$i\u00a77: \u00a7f").appendSibling(itemStacks[i]!!.chatComponent).appendSibling(ChatComponentText("\u00a77, \u00a7f"))
        }
        if (Minecraft.getMinecraft().thePlayer != null) Chat.sendPrefixComponent(component)
    }

    private fun logSPacketOpenWindow(packet: S2DPacketOpenWindow) {
        Chat.sendPrefixMessage("""
    §cReceived §fPacket Open Window§7:
    §bTitle§7: §3${packet.windowTitle.formattedText}§7, §bHas Slots§7: §3${packet.hasSlots()}${if (packet.hasSlots()) "\u00a77, \u00a7bSlot Count\u00a77: \u00a73" + packet.slotCount else ""}
    """.trimIndent())
    }

    private fun logCPacketClickWindow(packet: C0EPacketClickWindow) {
        val component: IChatComponent = ChatComponentText("\u00a7aSent \u00a7fClick Window Packet\u00a77:\n\u00a73Item\u00a77: \u00a7r")
        if (packet.clickedItem != null) component.appendSibling(packet.clickedItem.chatComponent) else component.appendText("\u00a7cnone")
        component.appendText("\u00a77, \u00a73Button\u00a77: \u00a7b" + packet.usedButton + "\u00a77, ")
        component.appendText("\u00a73Mode\u00a77: \u00a7b" + packet.mode + "\u00a77, ")
        Chat.sendPrefixComponent(component)
    }

    private fun logCPacketCloseWindow() {
        Chat.sendPrefixMessage("\u00a7aSent \u00a7fPacket Close Window\u00a77.")
    }

    private fun logSPacketCloseWindow() {
        Chat.sendPrefixMessage("\u00a7cReceived \u00a7fPacket Close Window\u00a77.")
    }
}