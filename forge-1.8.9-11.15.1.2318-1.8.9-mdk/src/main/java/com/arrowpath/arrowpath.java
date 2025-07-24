package com.arrowpath;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.client.ClientCommandHandler;

@Mod(modid = arrowpath.MODID, version = arrowpath.VERSION)
public class arrowpath {
    public static final String MODID = "arrowpath";
    public static final String VERSION = "1.0";

    @Mod.Instance
    public static arrowpath instance;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        arrowpathsettings settings = new arrowpathsettings();
        MinecraftForge.EVENT_BUS.register(new arrowpathrenderer(settings));
        ClientCommandHandler.instance.registerCommand(new arrowpathcommand(settings));
    }
}
