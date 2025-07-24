package com.arrowpath;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class arrowpathcommand extends CommandBase {

    private final arrowpathsettings settings;

    public arrowpathcommand(arrowpathsettings settings) {
        this.settings = settings;
    }

    @Override
    public String getCommandName() {
        return "arrowpath";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/arrowpath toggle|solo|multi|all";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        if (args.length == 0) {
            sender.addChatMessage(new ChatComponentText("Uso: /arrowpath toggle|solo|multi|all"));
            return;
        }

        String sub = args[0].toLowerCase();
        switch (sub) {
            case "toggle":
                settings.toggleShowPath();
                sender.addChatMessage(new ChatComponentText("ArrowPath: " +
                        (settings.isShowPathEnabled() ? "attivato" : "disattivato")));
                break;
            case "solo":
                settings.setMode(arrowpathsettings.Mode.SOLO);
                sender.addChatMessage(new ChatComponentText("ArrowPath: modalità SOLO"));
                break;
            case "multi":
                settings.setMode(arrowpathsettings.Mode.MULTI);
                sender.addChatMessage(new ChatComponentText("ArrowPath: modalità MULTI"));
                break;
            case "all":
                settings.setMode(arrowpathsettings.Mode.ALL);
                sender.addChatMessage(new ChatComponentText("ArrowPath: modalità ALL"));
                break;
            default:
                sender.addChatMessage(new ChatComponentText("Uso: /arrowpath toggle|solo|multi|all"));
                break;
        }
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }
}
