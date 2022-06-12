package me.dmod.commands

import me.dmod.utils.Chat.sendPrefixMessage
import me.dmod.utils.Variables
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiChat
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender
import java.util.*

class NewCommandsCommand : CommandBase() {
    override fun getCommandName(): String {
        return "newcommands"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        Variables.tabComplete = true
        sendPrefixMessage("Scanning commands, don't move...")
        if (java.lang.String.join(" ", *args).lowercase(Locale.getDefault()).contains("new") || java.lang.String.join(" ", *args)
                .lowercase(Locale.getDefault())
                .contains("add")) {
            Variables.newTabComplete = true
        }
    }

    companion object {
        fun openSlashChat() {
            val chat = GuiChat("/")
            Minecraft.getMinecraft().displayGuiScreen(chat)
            chat.sendAutocompleteRequest("/", "/")
        }
    }
}