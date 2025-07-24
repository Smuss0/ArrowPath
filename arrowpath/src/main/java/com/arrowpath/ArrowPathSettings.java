package com.arrowpath;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.opengl.GL11;

public class ArrowPathSettings extends GuiScreen {
   private ArrowPath mod;
   private GuiButton buttonShow;
   private GuiButton buttonColor;
   private GuiSlider sliderLength;
   private GuiSlider sliderWidth;

   public ArrowPathSettings(ArrowPath mod) {
      this.mod = mod;
   }

   public void func_73866_w_() {
      super.field_146292_n.add(this.buttonShow = new GuiButton(0, super.field_146294_l / 2 - 50, super.field_146295_m / 2 - 35, 100, 20, "Path: " + (this.mod.isShowPath() ? "On" : "Off")));
      super.field_146292_n.add(this.buttonColor = new GuiButton(1, super.field_146294_l / 2 - 50, super.field_146295_m / 2 - 10, 100, 20, "Color"));
      super.field_146292_n.add(this.sliderLength = new GuiSlider(2, super.field_146294_l / 2 - 50, super.field_146295_m / 2 + 15, 100, 20, "Length: ", "", 1.0D, 20.0D, (double)this.mod.getPathLength(), false, true));
      super.field_146292_n.add(this.sliderWidth = new GuiSlider(3, super.field_146294_l / 2 - 50, super.field_146295_m / 2 + 40, 100, 20, "Width: ", "", 1.0D, 20.0D, (double)this.mod.getPathWidth(), false, true));
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_146276_q_();
      GL11.glPushMatrix();
      GL11.glScalef(1.2F, 1.2F, 0.0F);
      super.func_73732_a(super.field_146289_q, "Arrow Path", Math.round((float)(super.field_146294_l / 2) / 1.2F), Math.round((float)(super.field_146295_m / 2) / 1.2F) - 50, -1);
      GL11.glPopMatrix();
      this.buttonShow.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.buttonColor.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.sliderLength.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.sliderWidth.func_146112_a(super.field_146297_k, mouseX, mouseY);
   }

   public void func_146284_a(GuiButton button) {
      switch(button.field_146127_k) {
      case 0:
         this.mod.setShowPath(!this.mod.isShowPath());
         this.buttonShow.field_146126_j = "Path: " + (this.mod.isShowPath() ? "On" : "Off");
         break;
      case 1:
         ArrowPath.mc.func_147108_a(new ArrowPathColor(this.mod));
         break;
      case 2:
         this.mod.setPathLength(this.sliderLength.getValueInt());
         break;
      case 3:
         this.mod.setPathWidth(this.sliderWidth.getValueInt());
      }

   }

   public void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      this.mod.setPathLength(this.sliderLength.getValueInt());
      this.mod.setPathWidth(this.sliderWidth.getValueInt());
   }

   public void func_146281_b() {
      this.mod.saveConfig();
   }
}
