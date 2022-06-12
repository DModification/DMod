package me.dmod.utils

import me.dmod.DMod.Companion.config
import me.dmod.gui.DModAutoSign
import net.minecraft.client.Minecraft
import net.minecraft.network.Packet
import net.minecraft.network.play.client.C12PacketUpdateSign
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntitySign
import net.minecraft.util.ChatComponentText
import net.minecraft.util.IChatComponent
import org.apache.commons.lang3.RandomStringUtils
import org.lwjgl.input.Keyboard
import java.util.*

object Packets {
    @JvmStatic
    var timedToggle: IntArray? = null
    private var blinkedAt: Date? = null
    private val packets = ArrayList<Packet<*>>()
    fun toggleBlink() {
        config.enableBlink = !config.enableBlink
        blinkedAt = Date()
        if (config.enableBlink) {
            if (config.enableBlinkMessages) Chat.sendPrefixMessage(config.blinkFirstMessage)
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
                if (config.enableBlinkMessages) Chat.sendPrefixMessage(config.blinkCancelMessage)
                packets.clear()
            } else {
                if (config.enableBlinkMessages) Chat.sendPrefixMessage(config.blinkSecondMessage)
                sendBlinkPackets()
                Minecraft.getMinecraft().theWorld.loadedTileEntityList
            }
        }
    }

    fun displayBlinkTimer() {
        blinkedAt?.let {
            val currentMillis = Date().toInstant().toEpochMilli()
            Render.renderString(
                config.blinkTextX,
                config.blinkTextY,
                config.blinkTextMessage.replace("%s", ((currentMillis - it.time) / 1000).toString())
            )
        }
    }

    @JvmStatic
    fun addBlinkPacket(packet: Packet<*>) {
        packets.add(packet)
    }

    private fun sendBlinkPackets() {
        for (packet in packets) Minecraft.getMinecraft().netHandler.networkManager.sendPacket(packet)
        packets.clear()
        if (config.enableBlinkMessages) Chat.sendPrefixMessage(config.blinkThirdMessage)
    }

    @JvmStatic
    fun fillSignPacket(tileEntitySign: TileEntitySign) {
        val lines = arrayListOf<IChatComponent>()
        for (i in 0..3) {
            lines.add(ChatComponentText(DModAutoSign.lines[i]))
//            lines.add(if (config.doUnicode) ChatComponentText(RandomStringUtils.random(256).replace("\u00a7", "").replace("\u001c", "")) else ChatComponentText(RandomStringUtils.randomAlphanumeric(256)))
        }
        Minecraft.getMinecraft().netHandler.addToSendQueue(C12PacketUpdateSign(tileEntitySign.pos, lines.toTypedArray()))
    }

}