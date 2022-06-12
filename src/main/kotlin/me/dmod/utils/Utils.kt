package me.dmod.utils

import net.minecraft.client.Minecraft
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.network.play.client.C03PacketPlayer.C04PacketPlayerPosition
import net.minecraft.util.BlockPos
import java.awt.Toolkit
import java.awt.datatransfer.DataFlavor
import java.awt.datatransfer.StringSelection
import java.awt.datatransfer.UnsupportedFlavorException
import java.io.IOException

object Utils {
    @JvmStatic
    val lookingAtBlockPos: BlockPos?
        get() {
            val movingObjectPosition = Minecraft.getMinecraft().thePlayer.rayTrace(100.0, 0f)
            return movingObjectPosition.blockPos
        }

    @JvmStatic
    val lookingAtEntity: Entity?
        get() {
            val movingObjectPosition = Minecraft.getMinecraft().objectMouseOver
            return movingObjectPosition.entityHit
        }

    fun isNickValid(nickname: String): Boolean {
        if (nickname.length < 5) return true
        if (nickname.length > 10) return false
        val blacklist = arrayOf("elite", "Elite", "nate", "Nate", "cyber", "Cyber", "the", "The", "sonic", "Sonic", "maxi", "Maxi")
        var underscores = 0
        var uppercases = 0
        var digits = 0
        for (char in nickname) {
            when {
                char.isUpperCase() -> uppercases++
                char.isDigit() -> digits++
                char == '_' -> underscores++
            }
        }
        for (word in blacklist) if (nickname.contains(word)) return false
        when {
            digits > 0 -> return false
            underscores > 1 -> return false
            uppercases > 2 -> return false
        }
        return true
    }



    @JvmStatic
    fun crashClearInventory() {
        for (i in Minecraft.getMinecraft().thePlayer.inventory.mainInventory.indices) {
            if (i != 8) Minecraft.getMinecraft().thePlayer.inventory.mainInventory[i] = null
        }
    }


    @JvmStatic
    fun getItemStackUUID(itemStack: ItemStack): String? {
        var tag = itemStack.tagCompound
        try {
            if (tag != null) {
                if (tag.hasKey("ExtraAttributes")) {
                    tag = tag.getCompoundTag("ExtraAttributes")
                    if (tag.hasKey("uuid")) {
                        return tag.getString("uuid")
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }


    @JvmStatic
    fun teleportTo(x: Double, y: Double, z: Double) {
        Minecraft.getMinecraft().thePlayer.setPosition(x, y, z)
        Minecraft.getMinecraft().netHandler.addToSendQueue(C04PacketPlayerPosition(x, y, z, true))
    }


    @JvmStatic
    @Throws(IOException::class, UnsupportedFlavorException::class)
    fun readClipboard(): String {
        return Toolkit.getDefaultToolkit().systemClipboard.getData(DataFlavor.stringFlavor) as String
    }


    fun writeClipboard(content: String?) {
        val selection = StringSelection(content)
        val clipboard = Toolkit.getDefaultToolkit().systemClipboard
        clipboard.setContents(selection, selection)
    }


    @JvmStatic
    fun getTPS(): String {
        val tps = Variables.tps
        if (tps <= -2f) return "&cFROZEN"
        if (tps == -1f || tps.isNaN()) return "&7Loading..."
        return String.format("%.1f", tps)
    }

    fun getRandomPlayer(excludeSelf: Boolean = false): NetworkPlayerInfo? {
        val player = Minecraft.getMinecraft().netHandler.playerInfoMap
            .filter { p -> p.gameProfile.id.version() == 4 }
            .random()
        return if(excludeSelf && player.gameProfile.id.equals(Minecraft.getMinecraft().thePlayer.uniqueID)) null
        else player
    }
}