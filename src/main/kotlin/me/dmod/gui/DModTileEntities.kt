package me.dmod.gui

import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.ScrollComponent
import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedMaxSizeConstraint
import gg.essential.elementa.constraints.RelativeConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.elementa.effects.OutlineEffect
import net.minecraft.client.Minecraft
import net.minecraft.tileentity.TileEntity
import net.minecraft.tileentity.TileEntitySign
import java.awt.Color

class DModTileEntities : WindowScreen(ElementaVersion.V1) {
    private val scrollComponent: ScrollComponent
    private val loadedTileEntities: List<TileEntity> = Minecraft.getMinecraft().theWorld.loadedTileEntityList

    init {
        UIText("Tile Entities", true).childOf(window).constrain {
            x = CenterConstraint()
            y = SiblingConstraint() + RelativeConstraint(0.05f)
            textScale = 2.pixels()
            color = Color(130, 209, 227).toConstraint()
        }
        scrollComponent = ScrollComponent(
            innerPadding = 4f,
        ).childOf(window).constrain {
            x = CenterConstraint()
            y = 15.percent()
            width = 90.percent()
            height = 70.percent() + 2.pixels()
        }
        for (tile in loadedTileEntities) {
            val isSign = tile is TileEntitySign
            val container = UIBlock().childOf(scrollComponent).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(5f)
                width = 90.percent()
                height = if (!isSign) ChildBasedMaxSizeConstraint() + 30.pixels() else ChildBasedMaxSizeConstraint() + 85.pixels()
                color = Color(136, 157, 191, 50).toConstraint()
            }.effect(OutlineEffect(Color(136, 157, 191), 1f))
            UIText("").childOf(container).constrain {
                x = CenterConstraint()
                y = SiblingConstraint()
            }
            UIText("\u00a7c\u00a7l" + tile.blockType.localizedName).childOf(container).constrain {
                x = CenterConstraint()
                    y = SiblingConstraint()
                }
            UIText(
                "\u00a7b\u00a7lPosition: \u00a7r" + (tile.pos.toString().replace("BlockPos{", "").replace("}", ""))
            ).childOf(container).constrain {
                x = CenterConstraint()
                y = SiblingConstraint(2F)
            }
            if (isSign) {
                val tileAsSign = tile as TileEntitySign
                UIText("\u00a7b\u00a7lSign Text: \u00a7r").childOf(container).constrain {
                    x = CenterConstraint()
                    y = SiblingConstraint(2F)
                }
                for (i in 0..3) {
                    UIText("\u00a77Line " + (i + 1) + ": \u00a7r" + tileAsSign.signText[i].formattedText).childOf(
                        container
                    ).constrain {
                        x = CenterConstraint()
                        y = SiblingConstraint(2F)
                    }
                }
            }
        }
    }
}