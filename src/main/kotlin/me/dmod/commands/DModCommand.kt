package me.dmod.commands

import gg.essential.api.EssentialAPI
import me.dmod.DMod.Companion.config
import me.dmod.gui.*
import me.dmod.utils.Chat
import me.dmod.utils.Chat.formatNBTTag
import me.dmod.utils.Chat.run
import me.dmod.utils.Chat.sendMessage
import me.dmod.utils.Chat.sendPrefixMessage
import me.dmod.utils.DModRunnable
import me.dmod.utils.Packets.timedToggle
import me.dmod.utils.Utils.crashClearInventory
import me.dmod.utils.Utils.lookingAtBlockPos
import me.dmod.utils.Utils.lookingAtEntity
import me.dmod.utils.Utils.teleportTo
import me.dmod.utils.Variables
import net.minecraft.client.Minecraft
import net.minecraft.client.entity.EntityOtherPlayerMP
import net.minecraft.client.gui.inventory.GuiEditSign
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.nbt.NBTTagList
import net.minecraft.nbt.NBTTagString
import net.minecraft.network.play.client.C02PacketUseEntity
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement
import net.minecraft.tileentity.TileEntitySign

class DModCommand : CommandBase() {
    override fun getCommandName(): String {
        return "dmod"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 0
    }

    override fun getCommandUsage(sender: ICommandSender): String? {
        return null
    }

    override fun processCommand(sender: ICommandSender, args: Array<String>) {
        if (args.isNotEmpty()) {
            when (args[0]) {
                "click" -> {
                    var times = 800
                    if (args.size > 1) {
                        try {
                            times = args[1].toInt()
                        } catch (e: Exception) {
                            return sendPrefixMessage("&cFailed to parse number.")
                        }
                    }
                    for (i in 1..times) {
                        if (lookingAtEntity === null) Minecraft.getMinecraft().netHandler.addToSendQueue(C08PacketPlayerBlockPlacement(Minecraft.getMinecraft().thePlayer.heldItem))
                        else Minecraft.getMinecraft().netHandler.addToSendQueue(C02PacketUseEntity(lookingAtEntity, C02PacketUseEntity.Action.INTERACT))
                    }
                }
                "config" -> {
                    EssentialAPI.getGuiUtil().openScreen(config.gui())
                }
                "spam" -> {
                    val command = args[1].substring(args[1].indexOf("\"") + 1, args[1].lastIndexOf("\""))
                    val iterations = args[2].toLongOrNull() ?: return sendPrefixMessage("Invalid iterations")
                    val interval = args[3].toLongOrNull() ?: return sendPrefixMessage("Invalid interval")
                    sendPrefixMessage(command)
                    object: DModRunnable() {
                        override fun run() {
                            run(Chat.processSpamCommand(command))
                        }
                    }.scheduleRepeatingTask(0, interval, iterations = iterations)
                }
                "lorespam" -> {
                    if (args.size < 2) return sendPrefixMessage("&3/dmod lorespam [amount]")
                    val stack = ItemStack(Items.stone_shovel)
                    val display = NBTTagCompound()
                    val list = NBTTagList()
                    for (i in 1..args[1].toInt()) list.appendTag(NBTTagString())
                    display.setTag("Lore", list)
                    stack.setTagInfo("display", display)
                    Minecraft.getMinecraft().thePlayer.inventory.addItemStackToInventory(stack)
                }
                "amount" -> {
                    val heldItem = Minecraft.getMinecraft().thePlayer.heldItem ?: return sendPrefixMessage("You must be holding an item.")
                    if (args.size < 2) return sendPrefixMessage("&3/dmod amount [amount]")
                    try {
                        val amount = args[1].toInt()
                        heldItem.stackSize = amount
                    } catch (e: Exception) {
                        sendPrefixMessage("&cFailed to parse the amount.")
                    }
                }
                "damage" -> {
                    val heldItem = Minecraft.getMinecraft().thePlayer.heldItem ?: return sendPrefixMessage("You must be holding an item.")
                    if (args.size < 2) return sendPrefixMessage("&3/dmod damage [amount]")
                    try {
                        heldItem.itemDamage = args[1].toInt()
                    } catch (e: Exception) {
                        sendPrefixMessage("&cFailed to parse the amount.")
                    }
                    heldItem.itemDamage = args[1].toInt()
                }
                "head" -> {
                    val item = Minecraft.getMinecraft().thePlayer.heldItem ?: return sendPrefixMessage("You must be holding an item.")
                    try {
                        Minecraft.getMinecraft().thePlayer.replaceItemInInventory(103, item)
                        sendPrefixMessage("Put item on head.")
                    } catch (e: Exception) {
                        sendPrefixMessage("&cAn error occurred. Check your logs.")
                        e.printStackTrace()
                    }
                }
                "chestthis" -> {
                    try {
                        val item = ItemStack(Blocks.chest)
                        if (args.size == 2)
                            item.item = Item.getByNameOrId(args[1])
                        val chestData = NBTTagCompound()
                        val items = NBTTagList()
                        val item1 = Minecraft.getMinecraft().thePlayer.heldItem.serializeNBT()
                        items.appendTag(item1)
                        chestData.setTag("Items", items)
                        item.setTagInfo("BlockEntityTag", chestData)

                        Minecraft.getMinecraft().thePlayer.inventory.addItemStackToInventory(item)
                        sendPrefixMessage("Gave chest with item")
                    } catch (e: Exception) {
                        sendPrefixMessage("No item in hand.")
                    }
                }
                "item" -> {
                    if (args.size < 3) return sendPrefixMessage("&3/dmod item [id] [amount]")
                    val input = args[1]
                    val amount = args[2].toInt()
                    val item = ItemStack(Item.getByNameOrId(input), amount)
                    Minecraft.getMinecraft().thePlayer.inventory.addItemStackToInventory(item)
                }
                "name" -> {
                    try {
                        val item = Minecraft.getMinecraft().thePlayer.heldItem
                        item.setStackDisplayName(args[1].replace("_", " ").replace("&", "\u00a7"))
                    } catch (e: Exception) {
                        sendPrefixMessage("Not enough arguments.")
                    }
                }
                "nick" -> {
                    Variables.nick = !Variables.nick
                    if (Variables.nick) run("/nick help setrandom")
                }
                "pos" -> EssentialAPI.getGuiUtil().openScreen(DModLocations())
                "nbt" -> {
                    val heldItem = Minecraft.getMinecraft().thePlayer.heldItem
                    if (heldItem != null) EssentialAPI.getGuiUtil().openScreen(DModNBT(heldItem))
                    else sendPrefixMessage("No item in hand.")
                }
                "self" -> Minecraft.getMinecraft().netHandler.addToSendQueue(C02PacketUseEntity(Minecraft.getMinecraft().thePlayer, C02PacketUseEntity.Action.INTERACT))
                "hide" -> crashClearInventory()
                "fly" -> {
                    Variables.isFlying = !Variables.isFlying
                    if (Variables.isFlying) {
                        sendPrefixMessage("\u00a7aEnabled flying.")
                        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = true
                        Minecraft.getMinecraft().thePlayer.capabilities.flySpeed = config.flightSpeed.toFloat() / 20
                    } else {
                        sendPrefixMessage("\u00a7cDisabled flying.")
                        Minecraft.getMinecraft().thePlayer.capabilities.allowFlying = Variables.shouldReEnableFlight
                        Minecraft.getMinecraft().thePlayer.capabilities.isFlying = false
                        Minecraft.getMinecraft().thePlayer.capabilities.flySpeed = 0.05f
                    }
                }
                "tp" -> teleportTo(args[1].toFloat().toDouble(), args[2].toFloat().toDouble(), args[3].toFloat().toDouble())
                "blink" -> if (args.size < 2 || args[1].split(":").toTypedArray().size < 3) sendPrefixMessage("Format\u00a77: \u00a73/DMod blink 15:29:10") else {
                    val time = args[1].split(":").toTypedArray()
                    try {
                        val hour = time[0].toInt()
                        val minute = time[1].toInt()
                        val second = time[2].toInt()
                        if (hour > 23 || minute > 59 || second > 59) {
                            throw Exception("Invalid data.")
                        } else if (hour < 0 || minute < 0 || second < 0) {
                            throw Exception("Invalid data.")
                        }
                        sendPrefixMessage("Blink will be toggled at \u00a73" + args[1])
                        timedToggle = intArrayOf(hour, minute, second)
                    } catch (e: Exception) {
                        sendPrefixMessage("\u00a7cFailed to parse number.")
                    }
                }
                "autosign" -> {
                    EssentialAPI.getGuiUtil().openScreen(DModAutoSign())
                }

                "customchat" -> {
                    EssentialAPI.getGuiUtil().openScreen(DModCustomChat())
                }

                "help" -> {
                    sendPrefixMessage("&bCommands:")
                    sendMessage("\u00a73/DMod help \u00a77- \u00a7fShows this message.")
                    sendMessage("\u00a73/DMod pos \u00a77- \u00a7fOpens the position GUI.")
                    sendMessage("\u00a73/DMod nbt \u00a77- \u00a7fOpens the NBT GUI.")
                    sendMessage("\u00a73/DMod self \u00a77- \u00a7fInteracts with yourself.")
                    sendMessage("\u00a73/DMod click [amount] \u00a77- \u00a7fRight clicks [amount] times.")
                    sendMessage("\u00a73/DMod hide \u00a77- \u00a7fHides the inventory.")
                    sendMessage("\u00a73/DMod fly \u00a77- \u00a7fToggles flying.")
                    sendMessage("\u00a73/DMod tp [x] [y] [z] \u00a77- \u00a7fTeleports to the specified coordinates.")
                    sendMessage("\u00a73/DMod blink [hh:mm:ss] \u00a77- \u00a7fToggles blinking at the specified time.")
                    sendMessage("\u00a73/DMod item [id] [amount] \u00a77- \u00a7fGives you the specified item.")
                    sendMessage("\u00a73/DMod damage [amount] \u00a77- \u00a7fChanges the damage value of the held item.")
                    sendMessage("\u00a73/DMod name [name] \u00a77- \u00a7fChanges the name of the held item.")
                    sendMessage("\u00a73/DMod head \u00a77- \u00a7fPuts the held item on your head.")
                    sendMessage("\u00a73/DMod lorespam [amount] \u00a77- \u00a7fGives an item with the specified amount of lore lines.")
                    sendMessage("\u00a73/DMod spam [message, ..., message] [amount] [delay] \u00a77- \u00a7fSends the specified message(s) in chat.")
                    sendMessage("\u00a73/DMod autosign \u00a77- \u00a7fOpens the autosign menu.")
                    sendMessage("\u00a73/DMod customchat \u00a77- \u00a7fOpens the customchat menu.")
                }
                config.blockDataCommand -> {
                    val blockPos = lookingAtBlockPos
                    if (blockPos != null) {
                        val tileEntity = Minecraft.getMinecraft().theWorld.getTileEntity(blockPos)
                        if (tileEntity is TileEntitySign) {
                            sendPrefixMessage("\u00a73Editable\u00a77: \u00a7b" + tileEntity.isEditable)
                            if (args.size > 1 && args[1] == "edit") {
                                EssentialAPI.getGuiUtil().openScreen(GuiEditSign(tileEntity))
                            }
                        }
                        if (tileEntity != null) {
                            Chat.nested = 1
                            sendMessage(formatNBTTag(tileEntity.serializeNBT()))
                        } else sendPrefixMessage("\u00a7cNot a tile entity.")
                    } else sendPrefixMessage("\u00a7cNot a block.")
                }
                config.entityDataCommand -> {
                    val entity = lookingAtEntity
                    if (entity == null) {
                        sendPrefixMessage("\u00a7cNo entity found.")
                    } else {
                        val message = StringBuilder("\u00a73Name\u00a77: \u00a7b")
                        message.append(entity.name).append("\n")
                        message.append("\u00a73Display Name\u00a77: \u00a7b")
                        message.append(entity.displayName.formattedText).append("\n")
                        message.append("\u00a73NBT Tag\u00a77: \u00a7b")
                        Chat.nested = 1
                        var nbt: NBTTagCompound? = NBTTagCompound()
                        if (entity !is EntityOtherPlayerMP) nbt = entity.serializeNBT() else entity.writeToNBT(nbt)
                        message.append(formatNBTTag(nbt!!)).append("\n")
                        sendMessage(message.toString())
                    }
                }
            }
            return
        }
        EssentialAPI.getGuiUtil().openScreen(DModMainMenu())
    }
}
