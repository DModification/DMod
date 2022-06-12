package me.dmod.mixins;

import me.dmod.utils.Chat;
import me.dmod.utils.Config;
import me.dmod.utils.Variables;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mixin(GuiChat.class)
public abstract class MixinGuiChat extends GuiScreen {

    @Shadow
    private boolean waitingOnAutocomplete;

    @Inject(at = @At("HEAD"), method = "onAutocompleteResponse([Ljava/lang/String;)V")
    public void headOnAutocompleteResponse(String[] response, CallbackInfo callbackInfo) {
        this.waitingOnAutocomplete = this.waitingOnAutocomplete || Variables.tabComplete;
    }

    @Inject(at = @At(value = "INVOKE", shift = At.Shift.BEFORE, target = "Ljava/lang/String;length()I"), method = "onAutocompleteResponse([Ljava/lang/String;)V", cancellable = true)
    public void lengthOnAutocompleteResponse(String[] response, CallbackInfo callbackInfo) throws IOException {
        List<String> commands = Arrays.asList(response);
        if (Variables.tabComplete && commands.contains("/gamemanager:tpa")) {
            Variables.tabComplete = false;
            if (Variables.tabCommands.isEmpty()) {
                Chat.sendPrefixMessage("First time running, saving commands...");
                Variables.tabCommands.addAll(commands);
                Config.writeConfig();
            } else if (Variables.newTabComplete) {
                Chat.sendPrefixMessage("Saving commands...");
                Variables.tabCommands.addAll(commands);
                Config.writeConfig();
                Minecraft.getMinecraft().thePlayer.closeScreen();
                callbackInfo.cancel();
                return;
            } else {
                ArrayList<String> newCommands = new ArrayList<>();
                for (String command : commands) {
                    if (!Variables.tabCommands.contains(command)) {
                        newCommands.add(command);
                    }
                }
                if (newCommands.isEmpty()) {
                    Chat.sendPrefixMessage("No new commands.");
                } else {
                    Chat.sendPrefixMessage("Found \u00a7b" + newCommands.size() + "\u00a7r new commands: \u00a7a" + String.join("\u00a7f, \u00a7a", newCommands));
                    Variables.tabCommands.addAll(newCommands);
                    Config.writeConfig();
                    Minecraft.getMinecraft().thePlayer.closeScreen();
                    callbackInfo.cancel();
                    return;
                }
            }
            Minecraft.getMinecraft().thePlayer.closeScreen();
            Chat.sendPrefixMessage("\u00a7aCommands have been scanned!");
            callbackInfo.cancel();
        }
    }
}
