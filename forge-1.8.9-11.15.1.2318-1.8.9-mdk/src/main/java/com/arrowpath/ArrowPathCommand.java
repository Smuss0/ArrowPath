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
        return "/arrowpath <toggle|player|party|all>";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            send("Usage: /arrowpath <toggle|player|party|all>");
            return;
        }

        switch (args[0].toLowerCase()) {
            case "toggle":
                ArrowPathMain.enabled = !ArrowPathMain.enabled;
                send("Arrow path rendering " + (ArrowPathMain.enabled ? "enabled" : "disabled"));
                break;

            case "player":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.PLAYER;
                send("Now showing arrows shot by you only.");
                break;

            case "party":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.PARTY;
                send("Now showing arrows shot by all players.");
                break;

            case "all":
                ArrowPathMain.currentMode = ArrowPathMain.Mode.ALL;
                send("Now showing arrows shot by all entities.");
                break;

            default:
                send("Unknown mode. Use: toggle, player, party, all");
        }
    }

    private void send(String message) {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("[ArrowPath] " + message));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}