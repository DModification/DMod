package me.dmod.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.stream.JsonWriter
import me.dmod.Actions
import me.dmod.Keybinds
import me.dmod.keybinds
import me.dmod.utils.Macros.ClickData
import me.dmod.utils.Macros.Clicks
import net.minecraft.inventory.Slot
import java.io.*
import java.nio.charset.StandardCharsets

object Config {
    @get:Throws(IOException::class)
    val config: Unit
        get() {
            val keybindsConfig = File("config/dmod_keys.json")
            if (keybindsConfig.exists()) {
                val parser = JsonParser()
                val jsonObject = parser.parse(InputStreamReader(FileInputStream("config/dmod_keys.json"), StandardCharsets.UTF_8)) as JsonObject
                for (element in jsonObject.getAsJsonArray("keybinds")) {
                    val key = element.asJsonObject.get("key").asInt
                    val action = element.asJsonObject.get("action").asInt
                    val command = element.asJsonObject.get("command").asString
                    Keybinds.addKeybind(key, Actions.values()[action], command)
                }
            } else {
                writeKeybindConfig()
            }

            val config = File("config/dmod.json")
            if (config.exists()) {
                val parser = JsonParser()
                val jsonObject = parser.parse(InputStreamReader(FileInputStream("config/dmod.json"), StandardCharsets.UTF_8)) as JsonObject
                for (element in jsonObject.getAsJsonArray("tabCommands")) {
                    Variables.tabCommands.add(element.asString)
                }
                for (element in jsonObject.getAsJsonArray("macros")) {
                    val macro = element.asJsonObject
                    val name = macro["name"].asString
                    val enabled = macro["enabled"].asBoolean
                    val guiName = macro["guiName"].asString
                    Macros.macroClicks[name] = Clicks(guiName)
                    Macros.macroClicks[name]!!.enabled = enabled
                    for (clicks in macro.getAsJsonArray("clicks")) {
                        val click = clicks.asJsonObject
                        val slotIn: Slot? = try {
                            val slotInObject = click["slotIn"].asJsonObject
                            Slot(null, slotInObject["index"].asInt, slotInObject["xPosition"].asInt, slotInObject["yPosition"].asInt)
                        } catch (exception: IllegalStateException) {
                            click["slotIn"].asJsonNull
                            null
                        }
                        val slotId = click["slotId"].asInt
                        val clickedButton = click["clickedButton"].asInt
                        val clickType = click["clickType"].asInt
                        Macros.macroClicks[name]!!.clickData.add(ClickData(slotIn, slotId, clickedButton, clickType))
                    }
                }
            } else {
                writeConfig()
            }
        }

    @JvmStatic
    fun writeKeybindConfig() {
        val keybindsWriter = JsonWriter(OutputStreamWriter(FileOutputStream("config/dmod_keys.json"), StandardCharsets.UTF_8))
        keybindsWriter.beginObject()
        keybindsWriter.name("keybinds")
        keybindsWriter.beginArray()
        if (keybinds.isEmpty()) Keybinds.addDefaultKeybinds()
        for (keybind in keybinds) {
            keybindsWriter.beginObject()
            keybindsWriter.name("key").value(keybind.first)
            keybindsWriter.name("action").value(keybind.second.ordinal)
            keybindsWriter.name("command").value(keybind.third)
            keybindsWriter.endObject()
        }
        keybindsWriter.endArray()
        keybindsWriter.endObject()
        keybindsWriter.close()
    }

    @JvmStatic
    @Throws(IOException::class)
    fun writeConfig() {

        val writer = JsonWriter(OutputStreamWriter(FileOutputStream("config/dmod.json"), StandardCharsets.UTF_8))
        writer.beginObject()
        writer.name("tabCommands")
        writer.beginArray()
        for (command in Variables.tabCommands) {
            writer.value(command)
        }
        writer.endArray()
        writer.name("macros")
        writer.beginArray()
        for ((key, value) in Macros.macroClicks) {
            writer.beginObject()
            writer.name("name").value(key)
            writer.name("enabled").value(value.enabled)
            writer.name("guiName").value(value.guiName)
            writer.name("clicks")
            writer.beginArray()
            for (clickData in value.clickData) {
                writer.beginObject()
                writer.name("slotIn")
                if (clickData!!.slotIn != null) {
                    writer.beginObject()
                    writer.name("index").value(clickData.slotIn!!.slotIndex.toLong())
                    writer.name("xPosition").value(clickData.slotIn!!.xDisplayPosition.toLong())
                    writer.name("yPosition").value(clickData.slotIn!!.yDisplayPosition.toLong())
                    writer.endObject()
                } else writer.nullValue()
                writer.name("slotId").value(clickData.slotId.toLong())
                writer.name("clickedButton").value(clickData.clickedButton.toLong())
                writer.name("clickType").value(clickData.clickType.toLong())
                writer.endObject()
            }
            writer.endArray()
            writer.endObject()
        }
        writer.endArray()
        writer.endObject()
        writer.close()
    }
}