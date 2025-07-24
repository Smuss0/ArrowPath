package com.arrowpath;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class ArrowPathCommand extends CommandBase {
   private ArrowPath mod;

   public ArrowPathCommand(ArrowPath mod) {
      this.mod = mod;
   }

   public String func_71517_b() {
      return "arrowpath";
   }

   public String func_71518_a(ICommandSender sender) {
      return EnumChatFormatting.RED + "/" + this.func_71517_b();
   }

   public void func_71515_b(ICommandSender sender, String[] args) {
      MinecraftForge.EVENT_BUS.register(this);
   }

   public boolean func_71519_b(ICommandSender sender) {
      return true;
   }

   @SubscribeEvent
   public void onClientTick(ClientTickEvent event) {
      ArrowPath.mc.func_147108_a(new ArrowPathSettings(this.mod));
      MinecraftForge.EVENT_BUS.unregister(this);
   }
}
