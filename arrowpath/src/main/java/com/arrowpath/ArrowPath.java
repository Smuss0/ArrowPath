package com.arrowpath;

import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

@Mod(
   modid = "arrowpath",
   name = "Arrow Path",
   version = "1.0",
   clientSideOnly = true,
   acceptedMinecraftVersions = "[1.8.9]"
)
public class ArrowPath {
   public static Minecraft mc = Minecraft.func_71410_x();
   public static Configuration config;
   private int pathRed;
   private int pathGreen;
   private int pathBlue;
   private int pathAlpha;
   private int pathLength;
   private int pathWidth;
   private boolean showPath;

   @EventHandler
   public void onInit(FMLInitializationEvent event) {
      MinecraftForge.EVENT_BUS.register(new ArrowPathRender(this));
      ClientCommandHandler.instance.func_71560_a(new ArrowPathCommand(this));
      this.loadConfig();
   }

   public int getPathRed() {
      return this.pathRed;
   }

   public void setPathRed(int pathRed) {
      this.pathRed = pathRed;
   }

   public int getPathGreen() {
      return this.pathGreen;
   }

   public void setPathGreen(int pathGreen) {
      this.pathGreen = pathGreen;
   }

   public int getPathBlue() {
      return this.pathBlue;
   }

   public void setPathBlue(int pathBlue) {
      this.pathBlue = pathBlue;
   }

   public int getPathAlpha() {
      return this.pathAlpha;
   }

   public void setPathAlpha(int pathAlpha) {
      this.pathAlpha = pathAlpha;
   }

   public int getPathLength() {
      return this.pathLength;
   }

   public void setPathLength(int pathLength) {
      this.pathLength = pathLength;
   }

   public int getPathWidth() {
      return this.pathWidth;
   }

   public void setPathWidth(int pathWidth) {
      this.pathWidth = pathWidth;
   }

   public boolean isShowPath() {
      return this.showPath;
   }

   public void setShowPath(boolean showPath) {
      this.showPath = showPath;
   }

   public void loadConfig() {
      config.load();
      this.pathRed = config.get("general", "pathRed", 255).getInt();
      this.pathGreen = config.get("general", "pathGreen", 255).getInt();
      this.pathBlue = config.get("general", "pathBlue", 255).getInt();
      this.pathAlpha = config.get("general", "pathAlpha", 255).getInt();
      this.pathLength = config.get("general", "pathLength", 10).getInt();
      this.pathWidth = config.get("general", "pathWidth", 10).getInt();
      this.showPath = config.get("general", "showPath", true).getBoolean();
      if (config.hasChanged()) {
         config.save();
      }

   }

   public void saveConfig() {
      config.get("general", "pathRed", 255).set(this.pathRed);
      config.get("general", "pathGreen", 255).set(this.pathGreen);
      config.get("general", "pathBlue", 255).set(this.pathBlue);
      config.get("general", "pathAlpha", 255).set(this.pathAlpha);
      config.get("general", "pathLength", 10).set(this.pathLength);
      config.get("general", "pathWidth", 10).set(this.pathWidth);
      config.get("general", "showPath", true).set(this.showPath);
      if (config.hasChanged()) {
         config.save();
      }

   }

   static {
      config = new Configuration(new File(mc.field_71412_D + File.separator + "config" + File.separator + "arrowPath.cfg"));
   }
}
