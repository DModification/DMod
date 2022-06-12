package me.dmod.gui

import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Category
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import gg.essential.vigilance.data.SortingBehavior
import me.dmod.DMod
import me.dmod.utils.Chat
import java.io.File

object DModConfig : Vigilant(File("config/DMod.toml"), "DMod", sortingBehavior = CategorySorting) {

    @Property(type = PropertyType.SWITCH, name = "Blink Toggle", description = "Toggles blink.", category = "Packets", subcategory = "Blink", hidden = true)
    var enableBlink = true

    @Property(type = PropertyType.SWITCH, name = "&2Enable Hovering Text", description = "Enables text that reminds you to disable blink.", category = "Packets", subcategory = "Blink")
    var enableBlinkTitle = false

    @Property(type = PropertyType.TEXT, name = "&dBlink Text Message", description = "The hovering text's content.\n&e%s&r stands for how long blink has been enabled for.", category = "Packets", subcategory = "Blink")
    var blinkTextMessage = "&cBlink is enabled.&4 %s seconds."

    @Property(type = PropertyType.NUMBER, name = "Blink Text X Pos", description = "The position on the X axis. /dmod pos to edit.", category = "Packets", subcategory = "Blink", max = Int.MAX_VALUE, hidden = true)
    var blinkTextX = 10

    @Property(type = PropertyType.NUMBER, name = "Blink Text Y Pos", description = "The position on the Y axis. /dmod pos to edit.", category = "Packets", subcategory = "Blink", max = Int.MAX_VALUE, hidden = true)
    var blinkTextY = 10

    @Property(type = PropertyType.SWITCH, name = "&3Blink Messages", description = "Toggles blink messages.", category = "Packets", subcategory = "Blink")
    var enableBlinkMessages = true

    @Property(type = PropertyType.TEXT, name = "&aEnable Blink Message", description = "The message sent when blink is enabled.", category = "Packets", subcategory = "Blink")
    var blinkFirstMessage = "Packets are now paused."

    @Property(type = PropertyType.TEXT, name = "&8Packets Sending Message", description = "The message sent when packets are being sent.", category = "Packets", subcategory = "Blink")
    var blinkSecondMessage = "Sending packets..."

    @Property(type = PropertyType.TEXT, name = "&6Packets Sent Message", description = "The message sent when packets have finished sending.", category = "Packets", subcategory = "Blink")
    var blinkThirdMessage = "Finished sending packets."

    @Property(type = PropertyType.TEXT, name = "&eCancel Packets Message", description = "The message sent when&e SHIFT&r is being held down while disabling blink in order to cancel packets.", category = "Packets", subcategory = "Blink")
    var blinkCancelMessage = "Cancelled sending packets."

    @Property(type = PropertyType.SWITCH, name = "&cDisable Flight Packets", description = "Ignores server packets that turn off flight.", subcategory = "Misc", category = "Packets")
    var disableFlightPackets = false

    @Property(type = PropertyType.NUMBER, name = "&bFlight Speed", description = "The speed multiplier for /DMod fly.", category = "Packets", subcategory = "Misc", max = 1000, min = 1)
    var flightSpeed = 2

    @Property(type = PropertyType.SWITCH, name = "&2Inventory Packets Toggle", description = "Toggles inventory packets.", category = "Packets", subcategory = "Inventory")
    var enableInventory = true

    @Property(type = PropertyType.SWITCH, name = "&3Inventory Packets Messages", description = "Toggles inventory packets messages.", category = "Packets", subcategory = "Inventory")
    var enableInventoryMessages = true

    @Property(type = PropertyType.TEXT, name = "&aEnable Inventory Packets Message", description = "The message sent when inventory packets get&a enabled&r.", category = "Packets", subcategory = "Inventory")
    var enableInventoryMessage = "Inventory packets have been enabled."

    @Property(type = PropertyType.TEXT, name = "&cDisable Inventory Packets Message", description = "The message sent when inventory packets get&c disabled&r.", category = "Packets", subcategory = "Inventory")
    var disableInventoryMessage = "Inventory packets have been disabled."


    @Property(type = PropertyType.SWITCH, name = "&bWorld Border", description = "Disable world border load packets.", category = "Rendering")
    var disableWorldBorderPacket = false

    @Property(type = PropertyType.SWITCH, name = "&5Paintings", description = "Modifies painting summon packets.", category = "Rendering")
    var paintingsPacket = false

    @Property(type = PropertyType.SLIDER, name = "&dPaintings Rate", description = "The occurrence of paintings. Set to 0 for none.", category = "Rendering", max = 100)
    var paintingsRate = 0

    @Property(type = PropertyType.SWITCH, name = "&6Armour Stands", description = "Modifies armour stand summon packets.", category = "Rendering")
    var armourStandsPacket = false

    @Property(type = PropertyType.SLIDER, name = "&eArmour Stands Rate", description = "The occurrence of armour stands. Set to 0 for none.", category = "Rendering", max = 100)
    var armourStandsRate = 0


    @Property(type = PropertyType.SWITCH, name = "&eSign Previous Auto-Save", description = "Enables automatically saving the previous GUI after opening a sign.", category = "GUIs")
    var autoPreviousSave = false

    @Property(type = PropertyType.SWITCH, name = "&eAnvil Auto-Save", description = "Enables automatically saving Anvil GUIs.", category = "GUIs")
    var autoAnvilSave = false

    @Property(type = PropertyType.SWITCH, name = "&9Drop Crafting Items", description = "Prevents items in the 2x2 crafting grid from dropping.", category = "GUIs")
    var disableDropGridItems = false

    @Property(type = PropertyType.SWITCH, name = "&5UUID Highlight", description = "Enables highlighting items that share the same UUID.", category = "GUIs")
    var uuidHighlight = false

    @Property(type = PropertyType.SWITCH, name = "&6Print NBT", description = "Pretty prints the NBT of the item in chat.\n&eRCTRL&r to use.", category = "GUIs")
    var enablePrintNBT = true

    @Property(type = PropertyType.SWITCH, name = "&9Instant Close", description = "Instantly closes guis.", category = "GUIs")
    var instantCloseGui = false

    @Property(type = PropertyType.SWITCH, name = "&3GUI Messages", description = "Toggles GUI messages.", category = "GUIs")
    var enableGuiMessages = true

    @Property(type = PropertyType.TEXT, name = "&aSaved GUI Message", description = "The message sent when a GUI is saved and exited.", category = "GUIs")
    var guiSaveMessage = "Saved GUI."

    @Property(type = PropertyType.TEXT, name = "&aSaved Sign GUI Message", description = "The message sent when a Sign GUI is saved and exited.", category = "GUIs")
    var guiSaveSignMessage = "Saved Sign GUI."

    @Property(type = PropertyType.TEXT, name = "&aSaved Book GUI Message", description = "The message sent when a Book GUI is saved and exited.", category = "GUIs")
    var guiSaveBookMessage = "Saved Book GUI."

    @Property(type = PropertyType.TEXT, name = "&6Display GUI Message", description = "The message sent when a saved GUI is displayed.", category = "GUIs")
    var guiDisplayMessage = "Displaying GUI."

    @Property(type = PropertyType.TEXT, name = "&cNot found GUI Message", description = "The message sent when there are no recent GUIs.", category = "GUIs")
    var guiNotFoundMessage = "No recent GUIs found."

    @Property(type = PropertyType.TEXT, name = "&cSign Not found GUI Message", description = "The message sent when there are no recent Sign GUIs.", category = "GUIs")
    var signGuiNotFoundMessage = "No recent Sign GUIs found."

    @Property(type = PropertyType.TEXT, name = "&cBook Not found GUI Message", description = "The message sent when there are no recent Book GUIs.", category = "GUIs")
    var bookGuiNotFoundMessage = "No recent Book GUIs found."


    @Property(type = PropertyType.TEXT, name = "&aMacro Start Delay", description = "The delay right after the GUI is opened.", category = "Macros", max = 100)
    var macroOpenDelay = "100"

    @Property(type = PropertyType.TEXT, name = "&aMacro Click Delay", description = "The delay between each inventory click in milliseconds.", category = "Macros", max = 100)
    var macroClickDelay = "50"

    @Property(type = PropertyType.SWITCH, name = "&cLoop Macros", description = "Runs the macro after finishing it.", category = "Macros", max = 500)
    var loopMacros = false

    @Property(type = PropertyType.SWITCH, name = "&bAnti Kick", description = "Disable clicks while the server is frozen.", category = "Macros")
    var noFreezeClicks = false

    @Property(type = PropertyType.SWITCH, name = "&2Enable Hovering Text", description = "Displays text on your screen related to macros.", category = "Macros")
    var enableMacroTitle = false

    @Property(type = PropertyType.TEXT, name = "&3Hovering Text Title", description = "The title of the hovering text.", category = "Macros")
    var macroHoveringMessageTitle = "&aActive Macros"

    @Property(type = PropertyType.TEXT, name = "&bHovering Text Content", description = "The line for each active macro. %n is the macro's name, %g is the GUI's name.", category = "Macros")
    var macroHoveringMessageContent = "&3%n &7in GUI &b%g"

    @Property(type = PropertyType.NUMBER, name = "Active Macros X Pos", description = "The position on the X axis. /dmod pos to edit.", category = "Macros", max = Int.MAX_VALUE, hidden = true)
    var activeMacrosX = 10

    @Property(type = PropertyType.NUMBER, name = "Active Macros Y Pos", description = "The position on the Y axis. /dmod pos to edit.", category = "Macros", max = Int.MAX_VALUE, hidden = true)
    var activeMacrosY = 50


    @Property(type = PropertyType.SWITCH, name = "&5Long Press", description = "Enables creating ghost blocks while holding down the key. If disabled, only creates one per key press.", category = "Ghost Blocks")
    var enableGhostHolding = true

    @Property(type = PropertyType.NUMBER, name = "&6Holding Delay", description = "The delay between ghost blocks being created in milliseconds.", category = "Ghost Blocks", max = 1000, increment = 50)
    var ghostHoldingDelay = 50

    @Property(type = PropertyType.PARAGRAPH, name = "&0Blacklist", description = "Blocks that are in this list won't be broken. Each line counts as a block. Uses exact name if preceded by a \"!\".", category = "Ghost Blocks")
    var ghostBlacklist = ""

    @Property(type = PropertyType.SWITCH, name = "&3Ghost Blocks Messages", description = "Toggles ghost blocks messages.", category = "Ghost Blocks")
    var enableGhostMessages = true

    @Property(type = PropertyType.TEXT, name = "&aRevert Message", description = "The message sent when ghost blocks get reverted.", category = "Ghost Blocks")
    var revertGhostMessage = "Reverted ghost blocks."


    @Property(type = PropertyType.TEXT, name = "&dTPS Message", description = "The message for hovering text and /dtps.", category = "Server TPS")
    var serverTpsMessage = "&dServer TPS: &5%t"

    @Property(type = PropertyType.SWITCH, name = "&2Enable Hovering Text", description = "Displays text on your screen with the server TPS.", category = "Server TPS")
    var enableServerTpsTitle = false

    @Property(type = PropertyType.NUMBER, name = "Server Tps X Pos", description = "The position on the X axis. /dmod pos to edit.", category = "Server TPS", max = Int.MAX_VALUE, hidden = true)
    var serverTpsTextX = 10

    @Property(type = PropertyType.NUMBER, name = "Server Tps Text Y Pos", description = "The position on the Y axis. /dmod pos to edit.", category = "Server TPS", max = Int.MAX_VALUE, hidden = true)
    var serverTpsTextY = 30


    @Property(type = PropertyType.SWITCH, name = "&cAuto-Sign", description = "Auto fills signs with a random 256 character string.", category = "Misc")
    var autoSign = false

//    @Property(type = PropertyType.SWITCH, name = "&9Include Unicode", description = "Unicode characters included in auto-fill.", category = "Misc")
//    var doUnicode = false

    @Property(type = PropertyType.SWITCH, name = "&dColor Chat", description = "Replaces & symbols with the section symbol.", category = "Misc")
    var colorChat = false

    @Property(type = PropertyType.TEXT, name = "&bBlock Data", description = "Prints the data of the block you're looking at.", category = "Misc")
    var blockDataCommand = "blockdata"

    @Property(type = PropertyType.TEXT, name = "&bEntity Data", description = "Prints the data of the entity you're looking at.", category = "Misc")
    var entityDataCommand = "entitydata"


    @Property(type = PropertyType.SWITCH, name = "&7Mining Fatigue", description = "Ignores the mining fatigue effect when enabled.", category = "Effects")
    var disableMiningFatigue = false

    @Property(type = PropertyType.SWITCH, name = "&bSpeed", description = "Ignores the speed effect when enabled.", category = "Effects")
    var disableSpeed = false

    @Property(type = PropertyType.SWITCH, name = "&8Slowness", description = "Ignores the slowness effect when enabled.", category = "Effects")
    var disableSlowness = false

    @Property(type = PropertyType.SWITCH, name = "&6Haste", description = "Ignores the haste effect when enabled.", category = "Effects")
    var disableHaste = false

    @Property(type = PropertyType.SWITCH, name = "&aJump Boost", description = "Ignores the jump boost effect when enabled.", category = "Effects")
    var disableJumpBoost = false

    @Property(type = PropertyType.SWITCH, name = "&0Blindness", description = "Ignores the blindness effect when enabled.", category = "Effects")
    var disableBlindness = false

    @Property(type = PropertyType.SWITCH, name = "&2Nausea", description = "Ignores the nausea effect when enabled.", category = "Effects")
    var disableNausea = false

    @Property(type = PropertyType.TEXT, name = "&3Removed Message", description = "The message sent when a potion effect gets removed. %e is the name of the potion effect. Leave empty for no message", category = "Effects")
    var effectsMessage = "Removed &3%e&f."


    @Property(type = PropertyType.SWITCH, name = "&bSigns", subcategory = "Packets", description = "Enables sign packets.", category = "Logging")
    var logSignPackets = false

    @Property(type = PropertyType.SWITCH, name = "&aOutgoing Update", subcategory = "Packets", description = "Triggers when the the client finishes writing a sign.", category = "Logging")
    var cPacketUpdateSign = true

    @Property(type = PropertyType.SWITCH, name = "&cIncoming Update", subcategory = "Packets", description = "Triggers when the client renders a sign.\n\u00a7eMay spam chat.", category = "Logging")
    var sPacketUpdateSign = false

    @Property(type = PropertyType.SWITCH, name = "&cOpen Sign", subcategory = "Packets", description = "Triggers when the server makes the client open a sign menu.", category = "Logging")
    var sPacketSignEditorOpen = true

    @Property(type = PropertyType.SWITCH, name = "&bGUIs", subcategory = "Packets", description = "Enables gui packets.", category = "Logging")
    var logWindowPackets = false

    @Property(type = PropertyType.SWITCH, name = "&aClick", subcategory = "Packets", description = "Triggers when the the client clicks in a gui.", category = "Logging")
    var cPacketClickWindow = false

    @Property(type = PropertyType.SWITCH, name = "&aOutgoing Close", subcategory = "Packets", description = "Triggers when the the client closes a gui.", category = "Logging")
    var cPacketCloseWindow = false

    @Property(type = PropertyType.SWITCH, name = "&cIncoming Close", subcategory = "Packets", description = "Triggers when the the server closes the client's gui.", category = "Logging")
    var sPacketCloseWindow = false

    @Property(type = PropertyType.SWITCH, name = "&cItems", subcategory = "Packets", description = "Triggers when the server sends the client a gui's items.", category = "Logging")
    var sPacketWindowItems = false

    @Property(type = PropertyType.SWITCH, name = "&cOpen GUI", subcategory = "Packets", description = "Triggers when the server makes the client open a gui.", category = "Logging")
    var sPacketOpenWindow = false


    @Property(type = PropertyType.SWITCH, name = "&0Banner Crash", description = "Prevents banner related crashes.", category = "Patches")
    var fixBannerCrash = true

    @Property(type = PropertyType.SWITCH, name = "&cLore Overflow", description = "Prevents large items from crashing the game.", category = "Patches")
    var fixLargeItems = true

    @Property(type = PropertyType.SWITCH, name = "&dMods", description = "Hides your forge mods when joining servers.\n&cMay require a restart.", category = "Patches")
    var hideModsList = true


    @Property(type = PropertyType.SWITCH, name = "&eSign Limit", description = "Disables the character limit on signs.", category = "Limits")
    var disableSignLimit = true

    @Property(type = PropertyType.SWITCH, name = "&6Book Limits", description = "Disables the limit in a book, including the author field.", category = "Limits")
    var disableBookLimit = true

    init {
        addDependency("blinkFirstMessage", "enableBlinkMessages")
        addDependency("blinkSecondMessage", "enableBlinkMessages")
        addDependency("blinkThirdMessage", "enableBlinkMessages")
        addDependency("blinkCancelMessage", "enableBlinkMessages")
        addDependency("blinkTextMessage", "enableBlinkTitle")
        addDependency("blinkTextX", "enableBlinkTitle")
        addDependency("blinkTextY", "enableBlinkTitle")
        addDependency("enableInventoryMessage", "enableInventoryMessages")
        addDependency("disableInventoryMessage", "enableInventoryMessages")
        addDependency("ghostHoldingDelay", "enableGhostHolding")
        addDependency("revertGhostMessage", "enableGhostMessages")
        addDependency("macroHoveringMessageTitle", "enableMacroTitle")
        addDependency("macroHoveringMessageContent", "enableMacroTitle")
        addDependency("guiSaveMessage", "enableGuiMessages")
        addDependency("guiSaveSignMessage", "enableGuiMessages")
        addDependency("guiSaveBookMessage", "enableGuiMessages")
        addDependency("guiDisplayMessage", "enableGuiMessages")
        addDependency("guiNotFoundMessage", "enableGuiMessages")
        addDependency("bookGuiNotFoundMessage", "enableGuiMessages")
        addDependency("paintingsRate", "paintingsPacket")
        addDependency("armourStandsRate", "armourStandsPacket")
        addDependency("cPacketUpdateSign", "logSignPackets")
        addDependency("sPacketUpdateSign", "logSignPackets")
        addDependency("sPacketSignEditorOpen", "logSignPackets")
        addDependency("sPacketWindowItems", "logWindowPackets")
        addDependency("sPacketOpenWindow", "logWindowPackets")
        addDependency("cPacketClickWindow", "logWindowPackets")
        addDependency("cPacketCloseWindow", "logWindowPackets")
        addDependency("sPacketCloseWindow", "logWindowPackets")
        registerListener<Any>(javaClass.getDeclaredField("enableInventory")) {
            if (DMod.initialized) {
                if (!this.enableInventory) {
                    if (DMod.config.enableInventoryMessages) Chat.sendPrefixMessage(DMod.config.enableInventoryMessage)
                } else {
                    if (DMod.config.enableInventoryMessages) Chat.sendPrefixMessage(DMod.config.disableInventoryMessage)
                }
            }
        }
    }

    private object CategorySorting : SortingBehavior() {
        override fun getCategoryComparator(): Comparator<in Category> = Comparator { o1, o2 ->
            if (o1.name == "Packets") return@Comparator -1
            if (o2.name == "Packets") return@Comparator 1
            if (o1.name == "GUIs") return@Comparator -2
            if (o2.name == "GUIs") return@Comparator 2
            if (o1.name == "Macros") return@Comparator -3
            if (o2.name == "Macros") return@Comparator 3
            if (o1.name == "Rendering") return@Comparator -4
            if (o2.name == "Rendering") return@Comparator 4
            if (o1.name == "Ghost Blocks") return@Comparator -5
            if (o2.name == "Ghost Blocks") return@Comparator 5
            if (o1.name == "Server TPS") return@Comparator -6
            if (o2.name == "Server TPS") return@Comparator 6
            if (o1.name == "Patches") return@Comparator -7
            if (o2.name == "Patches") return@Comparator 7
            if (o1.name == "Logging") return@Comparator -8
            if (o2.name == "Logging") return@Comparator 8
            if (o1.name == "Effects") return@Comparator -9
            if (o2.name == "Effects") return@Comparator 9
            if (o1.name == "Limits") return@Comparator -10
            if (o2.name == "Limits") return@Comparator 10
            if (o1.name == "Misc") return@Comparator -11
            if (o2.name == "Misc") return@Comparator 11
            else compareValuesBy(o1, o2) {
                it.name
            }
        }
    }

}