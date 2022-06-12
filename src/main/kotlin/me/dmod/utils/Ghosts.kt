package me.dmod.utils

import me.dmod.DMod.Companion.config
import net.minecraft.block.state.IBlockState
import net.minecraft.client.Minecraft
import net.minecraft.util.BlockPos
import org.apache.commons.lang3.tuple.Pair

object Ghosts {
    private var ghostBlocks = ArrayList<Pair<BlockPos, IBlockState>>()

    fun deleteBlock() {
        val pos = Utils.lookingAtBlockPos
        val state = Minecraft.getMinecraft().theWorld.getBlockState(pos)
        val blockId = state.block.registryName.substring(state.block.registryName.indexOf(":") + 1)
        val blacklist = config.ghostBlacklist.split("\n").toTypedArray()
        if (blacklist[0] != "") {
            blacklist.forEach { entry ->
                if (entry.startsWith("!")) {
                    if (entry.substring(1).equals(blockId, true))
                        return
                } else if (blockId.contains(entry, true))
                        return
            }
        }
        ghostBlocks.add(Pair.of(pos, state))
        setGhostBlock(pos)
    }

    fun resetGhosts() {
        ghostBlocks.clear()
    }

    fun refreshBlocks() {
        for ((pos, state) in ghostBlocks) {
            Minecraft.getMinecraft().theWorld.setBlockState(pos, state)
        }
        ghostBlocks.clear()
        if (config.enableGhostMessages) Chat.sendPrefixMessage(config.revertGhostMessage)
    }

    private fun setGhostBlock(pos: BlockPos?) {
        Minecraft.getMinecraft().theWorld.setBlockToAir(pos)
    }
}