package me.dmod.gui

import gg.essential.api.EssentialAPI
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.*
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.gui.buttons.KeyButton
import me.dmod.gui.buttons.MainButton
import me.dmod.gui.buttons.ShowAllDataButton
import me.dmod.utils.Chat
import me.dmod.utils.Chat.formatNBTTag
import net.minecraft.entity.Entity
import net.minecraft.nbt.NBTTagCompound
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList

class DModAllEntities (viewingEntityType: String, entitiesToLoad: ArrayList<Entity>) : WindowScreen(ElementaVersion.V1) {
    private var scrollComponent: ScrollComponent = ScrollComponent()
    private var clickedButton: KeyButton? = null
    private var currentlySearching: String = ""
    private var allComponents = CopyOnWriteArrayList<UIComponent>()

    init {
        UIText(viewingEntityType + "s", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        val searchContainer = UIBlock(color = Color.BLACK).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + 5.pixels()
            width = 75.percent()
            height = 10.pixels()
        }.effect(OutlineEffect(Color(136, 157, 191), 1f))
        val searchInput = UITextInput("", false)
        searchInput.setColor(Color.WHITE)
        (searchInput.childOf(searchContainer).constrain {
            width = 75.percent()
            x = 1.pixels()
            y = 0.5.pixels()
        }.onLeftClick {
            if (clickedButton == null) grabWindowFocus()
        } as UITextInput).also {
            it.onKeyType { typedChar, keyCode ->
                currentlySearching = it.getText()
                println(scrollComponent.allChildren.size)
                for(container in allComponents) {
                    scrollComponent.removeChild(container)
                    for(component in container.childrenOfType<UIText>()) {
                        val text: UIText = component
                        if (text.getText().lowercase().contains(currentlySearching.lowercase())) {
                            container.setY(SiblingConstraint() + 5.pixels())
                            scrollComponent.addChild(container)
                            break
                        }
                    }
                }
            }
        }
        scrollComponent = ScrollComponent(
            innerPadding = 4f,
        ).childOf(window).constrain {
            x = CenterConstraint()
            y = 18.percent()
            width = 90.percent()
            height = 70.percent() + 2.pixels()
        }
        entitiesToLoad.forEach {
            val entity = it
            val message = StringBuilder("\u00a73Name\u00a77: \u00a7b")
            message.append(entity.name).append("\n")
            message.append("\u00a73Display Name\u00a77: \u00a7b")
            message.append(entity.displayName.formattedText).append("\n")
            message.append("\u00a73NBT Tag\u00a77: \u00a7b")
            Chat.nested = 1
            val nbt = NBTTagCompound()
            entity.writeToNBT(nbt)
            message.append(formatNBTTag(nbt)).append("\n")
            val eachDataValue = message.toString().split("\n")

            val container = UIBlock().childOf(scrollComponent).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(5f)
                width = 90.percent()
                height = ChildBasedMaxSizeConstraint()
                color = Color(136, 157, 191, 50).toConstraint()
            }.effect(OutlineEffect(Color(136, 157, 191), 1f))
            for (i in 0 until eachDataValue.size) {
                if (eachDataValue[i].contains("NBT Tag") || eachDataValue[i].contains("Attributes")) {
                    break
                }
                UIText(eachDataValue[i]).childOf(container).constrain {
                    x = CenterConstraint()
                    y = if (i == 0) SiblingConstraint() + 5.pixels() else SiblingConstraint() + 1.pixels()
                }
            }
            container.setHeight(ChildBasedSizeConstraint() + (15).pixels())
            ShowAllDataButton("Show all data").childOf(container).constrain {
                x = CenterConstraint()
                width = 50.percent()
                y = SiblingConstraint() + 5.pixels()
            }.onLeftClick {
                container.clearChildren()
                for (i in eachDataValue.indices) {
                    UIText(eachDataValue[i]).childOf(container).constrain {
                        x = CenterConstraint()
                        y = if (i == 0) SiblingConstraint() + 5.pixels() else SiblingConstraint() + 1.pixels()
                    }
                }
                container.setHeight(ChildBasedSizeConstraint() + (eachDataValue.size).pixels())
            }
        }
        allComponents = CopyOnWriteArrayList(scrollComponent.allChildren)
        MainButton("Back").childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + 5.pixels()
            width = 150.pixels()
            height = 20.pixels()
        }.onMouseClick {
            EssentialAPI.getGuiUtil().openScreen(DModEntities())
        }
    }
}
