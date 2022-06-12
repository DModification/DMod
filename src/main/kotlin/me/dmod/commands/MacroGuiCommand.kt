package me.dmod.commands

import me.dmod.utils.Chat.sendPrefixMessage
import me.dmod.utils.Config
import me.dmod.utils.Macros
import me.dmod.utils.Macros.Clicks
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender

class MacroGuiCommand : CommandBase() {
    override fun getCommandName(): String {
        return "macrogui"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isEmpty()) {
            sendPrefixMessage("&a-----------------------------------------------")
            sendPrefixMessage("&3/macrogui <Name> <GUI Name> &8- &fStarts recording a macro. &3Name&f can be anything.")
            sendPrefixMessage("If not specified, &3<GUI Name>&f will be set to the next GUI you open.")
            sendPrefixMessage("&3/macrogui list &8- &fLists the registered macros, along with the title of the menu they are tracking.")
            sendPrefixMessage("&3/macrogui run <Name> &8- &fRuns a macro.")
            sendPrefixMessage("&3/macrogui delete <Name> &8- &fDeletes a macro.")
            sendPrefixMessage("&3/macrogui stop <Name> &8- &fStops a running macro.")
            sendPrefixMessage("&3/macrogui stop &8- &fCancels recording macros.")
            sendPrefixMessage("&a-----------------------------------------------")
        } else {
            when (args[0].lowercase()) {
                "list" -> {
                    if (Macros.macroClicks.isEmpty()) return sendPrefixMessage("&cNo macros have been registered.")
                    sendPrefixMessage("Use &b/macrogui run <Name> &fto enable one!")
                    for ((key, value) in Macros.macroClicks) {
                        sendPrefixMessage("&b${key}&f in GUI&b ${value.guiName}&8 - ${if (value.enabled) "&aACTIVE" else "&cINACTIVE"}")
                    }
                }
                "delete" -> {
                    if (args.size > 1) {
                        if (Macros.macroClicks.containsKey(args[1])) {
                            Macros.macroClicks.remove(args[1])
                            sendPrefixMessage("&fMacro &a${args[1]}&f has been removed!")
                            Config.writeConfig()
                        } else {
                            sendPrefixMessage("&cMacro &b${args[1]}&c does not exist!")
                        }
                    }
                }
                "stop" -> {
                    if (args.size > 1) {
                        Macros.macroClicks[args[1]]?.apply {
                            if (enabled) {
                                enabled = false
                                sendPrefixMessage("&fMacro &b${args[1]}&f has been&a disabled!")
                                Config.writeConfig()
                            } else {
                                sendPrefixMessage("&cMacro &b${args[1]}&c is not enabled!")
                            }
                        } ?: sendPrefixMessage("&cMacro &b${args[1]}&c does not exist!")
                    } else {
                        Macros.macroClicks.entries.removeIf { (_, value): Map.Entry<String?, Clicks> -> value.clickData.size == 0 }
                        sendPrefixMessage("&aCancelled recording.")
                        Macros.recordingMacro = null
                    }
                }
                "run" -> {
                    if (args.size > 1) {
                        Macros.macroClicks[args[1]]?.apply {
                            if (enabled) {
                                sendPrefixMessage("&cMacro &b${args[1]}&c is already running!")
                            } else {
                                enabled = true
                                sendPrefixMessage("&fMacro &b${args[1]}&f has been&a enabled&f!")
                                Config.writeConfig()
                            }
                        } ?: sendPrefixMessage("&cMacro &b${args[1]}&c does not exist!")
                    } else {
                        sendPrefixMessage("Usage: &b/macrogui run <Name>")
                    }
                }
                else -> {
                    Macros.recordingMacro
                    if (Macros.recordingMacro == null) {
                        Macros.recordingMacro = args[0]
                        val guiName: String
                        if (args.size > 1) {
                            guiName = args[1].replace("_".toRegex(), " ")
                            sendPrefixMessage("&aSuccess!&f Recording will start when the GUI &b$guiName &fis opened!")
                        } else {
                            return sendPrefixMessage("&cSpecify the name of the GUI.")
                        }
                        Macros.macroClicks[args[0]] = Clicks(guiName)
                    } else {
                        sendPrefixMessage("&cYou've already started recording. &b/macrogui stop&c to stop recording.")
                    }
                }
            }
        }
    }
}