package me.dmod.gui

import gg.essential.api.EssentialAPI
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.UIComponent
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.components.input.UITextInput
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import gg.essential.vigilance.utils.onLeftClick
import me.dmod.gui.buttons.KeyButton
import me.dmod.gui.buttons.ViewEntitiesButton
import net.minecraft.client.Minecraft
import net.minecraft.entity.Entity
import java.awt.Color
import java.util.concurrent.CopyOnWriteArrayList

class DModEntities : WindowScreen(ElementaVersion.V1) {
    private var scrollComponent: ScrollComponent = ScrollComponent()
    private val loadedEntities: List<Entity> = Minecraft.getMinecraft().theWorld.loadedEntityList
    private var clickedButton: KeyButton? = null
    private var currentlySearching = ""
    private var allComponents = CopyOnWriteArrayList<UIComponent>()

    init {
        val entityAmountMap: HashMap<String, Int> = HashMap()
        val entitiesInTypeMap: HashMap<String, ArrayList<Entity>> = HashMap()
        for(entity in loadedEntities) {
            val entityType: String = entity.javaClass.toString().split(".")[(entity.javaClass.toString().split(".").size) - 1].replace("Entity", "")
            if (!entityAmountMap.containsKey(entityType)) {
                entityAmountMap[entityType] = 1
                val list: ArrayList<Entity> = ArrayList()
                list.add(entity)
                entitiesInTypeMap[entityType] = list
            } else {
                entityAmountMap[entityType] = entityAmountMap[entityType]!! + 1
                val list: ArrayList<Entity> = entitiesInTypeMap[entityType] as ArrayList<Entity>
                list.add(entity)
                entitiesInTypeMap[entityType] = list
            }
        }
        UIText("Entities", true).childOf(window).constrain {
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
            it.onKeyType { _, _ ->
                currentlySearching = it.getText()
                for(container in allComponents) {
                    scrollComponent.removeChild(container)
                    for(component in container.childrenOfType<UIText>()) {
                        val text: UIText = component
                        if (text.getText().lowercase().contains(currentlySearching.lowercase())) {
                            container.setY(SiblingConstraint() + 5.pixels())
                            scrollComponent.addChild(container)
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
        entityAmountMap.forEach { entry ->
            val container = UIBlock().childOf(scrollComponent).constrain {
                x = CenterConstraint()
                y = SiblingConstraint() + 5.pixels()
                width = 90.percent()
                height = ChildBasedMaxSizeConstraint() + 30.pixels()
                color = Color(136, 157, 191, 50).toConstraint()
            }.effect(OutlineEffect(Color(136, 157, 191), 1f))
            UIText("\u00a7c\u00a7l" + entry.key + "\u00a7r: " + entry.value.toString()).childOf(container).constrain {
                x = SiblingConstraint() + 10.pixels()
                y = CenterConstraint()
            }
            ViewEntitiesButton("View all " + entry.key + "s").childOf(container).constrain {
                x = (75 - (entry.key.length)).percent()
                width = (80 + (entry.key.length * 4)).pixels()
                y = CenterConstraint()
            }.onLeftClick {
                EssentialAPI.getGuiUtil().openScreen(DModAllEntities(entry.key, entitiesInTypeMap[entry.key] as ArrayList<Entity>))
            }
        }
        allComponents = CopyOnWriteArrayList(scrollComponent.allChildren)
    }
}