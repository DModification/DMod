package me.dmod.gui

import me.dmod.DMod.Companion.config
import me.dmod.gui.buttons.PosButton
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiButton
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution

class DModLocations : GuiScreen() {
    private var moving: String? = null
    private var lastMouseX = -1
    private var lastMouseY = -1
    private var display: PosButton? = null
    private var tps: PosButton? = null
    private var activeMacros: PosButton? = null
    override fun doesGuiPauseGame(): Boolean {
        return false
    }

    override fun initGui() {
        super.initGui()
        val blinkText = config.blinkTextMessage
                .replace("%s", "1.483")
                .replace("&".toRegex(), "\u00a7")
        val tpsMessage = config.serverTpsMessage
                .replace("%t", "19.3914")
                .replace("&".toRegex(), "\u00a7")
        val activeMacrosMessage = """${config.macroHoveringMessageTitle}
${config.macroHoveringMessageContent.replace("%n", "Anvil").replace("%g", "Reforge")}
${config.macroHoveringMessageContent}"""
                .replace("%n", "Crafting")
                .replace("%g", "Craft Item")
                .replace("&".toRegex(), "\u00a7")
        val resolution = ScaledResolution(Minecraft.getMinecraft())
        display = PosButton(0, config.blinkTextX % resolution.scaledWidth, config.blinkTextY % resolution.scaledHeight, 145.0, 102.0, 1.0, blinkText, null, 0)
        tps = PosButton(1, config.serverTpsTextX % resolution.scaledWidth, config.serverTpsTextY % resolution.scaledHeight, 145.0, 102.0, 1.0, tpsMessage, null, 0)
        activeMacros = PosButton(2, config.activeMacrosX % resolution.scaledWidth, config.activeMacrosY % resolution.scaledHeight, 145.0, 102.0, 1.0, activeMacrosMessage, null, 0)
        buttonList.add(display)
        buttonList.add(tps)
        buttonList.add(activeMacros)
    }

    override fun drawScreen(mouseX: Int, mouseY: Int, partialTicks: Float) {
        drawDefaultBackground()
        mouseMoved(mouseX, mouseY)
        super.drawScreen(mouseX, mouseY, partialTicks)
    }

    private fun mouseMoved(mouseX: Int, mouseY: Int) {
        val xMoved = mouseX - lastMouseX
        val yMoved = mouseY - lastMouseY
        if (moving != null) {
            when (moving) {
                "display" -> {
                    config.blinkTextX = config.blinkTextX + xMoved
                    config.blinkTextY = config.blinkTextY + yMoved
                    display!!.xPosition = config.blinkTextX
                    display!!.yPosition = config.blinkTextY
                }
                "tps" -> {
                    config.serverTpsTextX = config.serverTpsTextX + xMoved
                    config.serverTpsTextY = config.serverTpsTextY + yMoved
                    tps!!.xPosition = config.serverTpsTextX
                    tps!!.yPosition = config.serverTpsTextY
                }
                "activeMacros" -> {
                    config.activeMacrosX = config.activeMacrosX + xMoved
                    config.activeMacrosY = config.activeMacrosY + yMoved
                    tps!!.xPosition = config.activeMacrosX
                    tps!!.yPosition = config.activeMacrosY
                }
            }
            buttonList.clear()
            initGui()
        }
        lastMouseX = mouseX
        lastMouseY = mouseY
    }

    public override fun actionPerformed(button: GuiButton) {
        if (button is PosButton) {
            when {
                button === display -> moving = "display"
                button === tps -> moving = "tps"
                button === activeMacros -> moving = "activeMacros"
            }
        }
    }

    public override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        super.mouseReleased(mouseX, mouseY, state)
        moving = null
    }
}