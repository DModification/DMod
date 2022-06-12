package me.dmod.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.gui.buttons.MainButton
import me.dmod.gui.buttons.NBTInputComponent
import me.dmod.utils.Chat
import net.minecraft.client.Minecraft
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import java.awt.Color

class DModNBT(private val stack: ItemStack) : WindowScreen(ElementaVersion.V1, newGuiScale = 4) {
    init {
        UIText("NBT Editor", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }

        Chat.nested = 1
        val nbt = NBTInputComponent(Chat.formatNBTTag(stack.serializeNBT(), true).replace("\u00a7", "&")).constrain {
            x = CenterConstraint()
            y = CenterConstraint()
        }.onKeyType { typedChar, keyCode ->  println("$typedChar $keyCode") }.onMouseClick {
            grabWindowFocus()
        }.childOf(window)

        MainButton("Save and Close").constrain {
            x = CenterConstraint()
            y = 90.percent()
        }.onLeftClick {
            saveNBT(nbt as NBTInputComponent)
        }.childOf(window)
    }

    private fun saveNBT(nbt: NBTInputComponent) {
        Minecraft.getMinecraft().thePlayer.closeScreen()
        val nbtString = nbt.textInput.getText()
        try {
            val jsonToNbt = JsonToNBT.getTagFromJson(nbtString.replace("\n", "").replace("&", "\u00a7"))
            stack.readFromNBT(jsonToNbt)
        } catch (e: Exception) {
            Chat.sendPrefixMessage("&c${e.message}")
        }
    }
}