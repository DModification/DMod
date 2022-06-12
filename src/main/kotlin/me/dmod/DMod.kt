package me.dmod

import me.dmod.commands.*
import me.dmod.events.*
import me.dmod.gui.DModConfig
import me.dmod.utils.Config
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.io.IOException


@SideOnly(Side.CLIENT)
@Mod(modid = DMod.MOD_ID)
class DMod {
    @Mod.EventHandler
    @Throws(IOException::class)
    fun onInit(event: FMLInitializationEvent?) {
        config.initialize()
        config.markDirty()
        MinecraftForge.EVENT_BUS.register(TickEvent())
        MinecraftForge.EVENT_BUS.register(ItemEvent())
        MinecraftForge.EVENT_BUS.register(WorldEvent())
        MinecraftForge.EVENT_BUS.register(RenderEvent())
        MinecraftForge.EVENT_BUS.register(GuiOpenEvent())
        MinecraftForge.EVENT_BUS.register(KeyPressEvent())
        MinecraftForge.EVENT_BUS.register(GuiUpdateEvent())
        ClientCommandHandler.instance.registerCommand(DModCommand())
        ClientCommandHandler.instance.registerCommand(DTpsCommand())
        ClientCommandHandler.instance.registerCommand(ClickGuiCommand())
        ClientCommandHandler.instance.registerCommand(MacroGuiCommand())
        ClientCommandHandler.instance.registerCommand(ColorChatCommand())
        ClientCommandHandler.instance.registerCommand(NewCommandsCommand())
        Config.config
        initialized = true
    }

    companion object {
        const val MOD_ID: String = "DMod"
        const val PREFIX: String = "\u00a7b[DMod] \u00a7f"
        @get:JvmName("getLogger")
        @JvmStatic
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
        @JvmStatic
        val config = DModConfig
        @JvmStatic
        var initialized: Boolean = false
    }

}