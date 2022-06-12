package me.dmod.commands

import me.dmod.DMod
import me.dmod.utils.Chat.sendPrefixMessage
import me.dmod.utils.Variables
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class ClickGuiCommand : CommandBase() {
    override fun getCommandName(): String {
        return "clickgui"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            sendPrefixMessage("Usage: \u00a7b/clickgui <GUI title> <Item name>")
        } else if (args[0].equals("delete", ignoreCase = true) || args[0].equals("reset", ignoreCase = true) || args[0].equals("clear", ignoreCase = true)) {
            sendPrefixMessage("\u00a7fCleared \u00a7b" + Variables.guiItems.size + "\u00a7f items.")
            Variables.guiItems.clear()
        } else if (args[0].equals("list", ignoreCase = true)) {
            val messageBuilder = StringBuilder("\u00a7b" + Variables.guiItems.size + "\u00a7f items waiting to be clicked.")
            for (item in Variables.guiItems) {
                messageBuilder.append("\n").append(DMod.PREFIX).append("\u00a7fItem \u00a7a").append(item[1]).append("\u00a7f in GUI \u00a7a").append(item[0]).append("\u00a7f, \u00a7a").append(ordinal(item[2].toInt())).append(" Match").append("\u00a7f,")
            }
            val message: String = if (Variables.guiItems.size > 0) messageBuilder.substring(0, messageBuilder.toString().length - 1) else messageBuilder.toString()
            sendPrefixMessage(message)
        } else if (args.size < 2) {
            sendPrefixMessage("Usage: \u00a7b/clickgui <GUI title> <Item name>")
        } else {
            val list: List<String> = if (args[1].contains(":")) {
                listOf(
                    args[0].replace("_", " "),
                    args[1].split(":").toTypedArray()[0].replace("_", " "),
                    args[1].split(":").toTypedArray()[1]
                )
            } else {
                listOf(args[0].replace("_", " "), args[1].replace("_", " "), "1")
            }
            Variables.guiItems.add(list)
            sendPrefixMessage("\u00a7aAdded item!")
        }
    }

    companion object {
        fun ordinal(num: Int): String {
            val suffix = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
            val m = num % 100
            return num.toString() + suffix[if (m in 4..20) 0 else m % 10]
        }
    }
}