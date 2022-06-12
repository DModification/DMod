package me.dmod.utils

import net.minecraft.inventory.Slot
import java.io.IOException

object Macros {
    @JvmStatic
    var recordingMacro: String? = null
    fun saveMacro() {
        if (recordingMacro != null) {
            Chat.sendPrefixMessage("Saved macro \u00a7a$recordingMacro")
            try {
                Config.writeConfig()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            recordingMacro = null
        } else Chat.sendPrefixMessage("\u00a7cYou haven't started recording a macro!")
    }

    @JvmField
    var macroClicks = HashMap<String, Clicks>()

    class Clicks(var guiName: String) {
        @JvmField
        var enabled = false

        @JvmField
        var clickData = ArrayList<ClickData?>()
    }

    class ClickData(var slotIn: Slot?, var slotId: Int, var clickedButton: Int, var clickType: Int)
}