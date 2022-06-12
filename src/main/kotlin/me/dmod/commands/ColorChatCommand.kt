package me.dmod.commands

import me.dmod.utils.Chat.run
import net.minecraft.command.CommandBase
import net.minecraft.command.CommandException
import net.minecraft.command.ICommandSender

class ColorChatCommand : CommandBase() {
    override fun getCommandName(): String {
        return "colorchat"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    @Throws(CommandException::class)
    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        val message = java.lang.String.join(" ", *args).replace("&".toRegex(), "\u00a7").replace("NL".toRegex(), "\n").replace("\\\\n".toRegex(), "\n")
        run(message)
    }
}