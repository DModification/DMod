package me.dmod.commands

import me.dmod.DMod
import me.dmod.events.custom.ServerTimeEvent
import me.dmod.utils.Chat.sendPrefixMessage
import me.dmod.utils.Utils
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class DTpsCommand : CommandBase() {
    override fun getCommandName(): String {
        return "dtps"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()) {
            if (args[0].equals("reset", true)) {
                ServerTimeEvent.resetArray()
                sendPrefixMessage("\u00a7aReset.")
            }
        }
        sendPrefixMessage(DMod.config.serverTpsMessage.replace("%t", Utils.getTPS()))
    }
}