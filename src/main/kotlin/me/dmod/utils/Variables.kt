package me.dmod.utils

import net.minecraft.inventory.Slot
import net.minecraft.item.ItemStack
import java.time.Instant

object Variables {
    var nick: Boolean = false

    @JvmField
    var tabCommands = ArrayList<String?>()

    @JvmField
    var guiItems = ArrayList<List<String>>()

    @JvmField
    var tabComplete = false

    @JvmField
    var newTabComplete = false

    @JvmField
    var tps = -1f

    @JvmField
    var lastTimeEvent: Instant? = null

    @JvmField
    var isFlying = false

    @JvmField
    var shouldReEnableFlight = false

    @JvmField
    var shouldCrunch = false

    @JvmField
    var loadedItemStacks = HashMap<Slot, ItemStack>()
}