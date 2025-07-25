package com.arrowpath;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;

public class ArrowPathCommand extends CommandBase {

    public ArrowPathCommand() {
        ClientCommandHandler.instance.registerCommand(this);
    }

    @Override
    public String getCommandName() {
        return "arrowpath";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/arrowpath <toggle|player|party|all|rgb|<color>>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            send("Usage: /arrowpath <toggle|player|party|all|rgb|<color>>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                ArrowPathMain.enabled = !ArrowPathMain.enabled;
                send("Mod " + (ArrowPathMain.enabled ? "enabled" : "disabled"));
                ArrowPathMain.saveConfig();
                break;

            case "player":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.PLAYER;
                send("Now showing arrows from you only.");
                ArrowPathMain.saveConfig();
                break;

            case "party":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.PARTY;
                send("Now showing arrows from players.");
                ArrowPathMain.saveConfig();
                break;

            case "all":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.ALL;
                send("Now showing all arrows.");
                ArrowPathMain.saveConfig();
                break;

            case "rgb":
                ArrowPathMain.rgbMode = true;
                send("Trail color set to RGB rainbow.");
                ArrowPathMain.saveConfig();
                break;

            default:
                if (ArrowPathColors.NAMED_COLORS.containsKey(args[0].toLowerCase())) {
                    ArrowPathMain.colorName = args[0].toLowerCase();
                    ArrowPathMain.currentColor = ArrowPathColors.NAMED_COLORS.get(ArrowPathMain.colorName);
                    ArrowPathMain.rgbMode = false;
                    send("Trail color set to: " + ArrowPathMain.colorName);
                    ArrowPathMain.saveConfig();
                } else {
                    send("Unknown color or option.");
                }
                break;
        }
    }

    private void send(String msg) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[ArrowPath] " + msg));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}