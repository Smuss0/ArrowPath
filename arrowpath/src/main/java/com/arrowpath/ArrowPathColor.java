package com.arrowpath;

import java.awt.Color;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.config.GuiSlider;
import org.lwjgl.opengl.GL11;

public class ArrowPathColor extends GuiScreen {
   private ArrowPath mod;
   private GuiSlider sliderRed;
   private GuiSlider sliderGreen;
   private GuiSlider sliderBlue;
   private GuiSlider sliderAlpha;
   private GuiButton buttonSettings;

   public ArrowPathColor(ArrowPath mod) {
      this.mod = mod;
   }

   public void func_73866_w_() {
      super.field_146292_n.add(this.sliderRed = new GuiSlider(0, super.field_146294_l / 2 - 50, super.field_146295_m / 2 - 35, 100, 20, "Red: ", "", 0.0D, 255.0D, (double)this.mod.getPathRed(), false, true));
      super.field_146292_n.add(this.sliderGreen = new GuiSlider(1, super.field_146294_l / 2 - 50, super.field_146295_m / 2 - 10, 100, 20, "Green: ", "", 0.0D, 255.0D, (double)this.mod.getPathGreen(), false, true));
      super.field_146292_n.add(this.sliderBlue = new GuiSlider(2, super.field_146294_l / 2 - 50, super.field_146295_m / 2 + 15, 100, 20, "Blue: ", "", 0.0D, 255.0D, (double)this.mod.getPathBlue(), false, true));
      super.field_146292_n.add(this.sliderAlpha = new GuiSlider(3, super.field_146294_l / 2 - 50, super.field_146295_m / 2 + 40, 100, 20, "Alpha: ", "", 30.0D, 255.0D, (double)this.mod.getPathAlpha(), false, true));
      super.field_146292_n.add(this.buttonSettings = new GuiButton(4, super.field_146294_l / 2 - 50, super.field_146295_m / 2 + 65, 100, 20, "Back"));
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_146276_q_();
      GL11.glPushMatrix();
      GL11.glScalef(1.2F, 1.2F, 0.0F);
      super.func_73732_a(super.field_146289_q, "Arrow Path", Math.round((float)(super.field_146294_l / 2) / 1.2F), Math.round((float)(super.field_146295_m / 2) / 1.2F) - 50, (new Color(this.mod.getPathRed(), this.mod.getPathGreen(), this.mod.getPathBlue())).getRGB());
      GL11.glPopMatrix();
      this.sliderRed.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.sliderGreen.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.sliderBlue.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.sliderAlpha.func_146112_a(super.field_146297_k, mouseX, mouseY);
      this.buttonSettings.func_146112_a(super.field_146297_k, mouseX, mouseY);
   }

   public void func_146284_a(GuiButton button) {
      switch(button.field_146127_k) {
      case 0:
         this.mod.setPathRed(this.sliderRed.getValueInt());
         break;
      case 1:
         this.mod.setPathGreen(this.sliderGreen.getValueInt());
         break;
      case 2:
         this.mod.setPathBlue(this.sliderBlue.getValueInt());
         break;
      case 3:
         this.mod.setPathAlpha(this.sliderAlpha.getValueInt());
         break;
      case 4:
         ArrowPath.mc.func_147108_a(new ArrowPathSettings(this.mod));
      }

   }

   public void func_146273_a(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
      this.mod.setPathRed(this.sliderRed.getValueInt());
      this.mod.setPathGreen(this.sliderGreen.getValueInt());
      this.mod.setPathBlue(this.sliderBlue.getValueInt());
      this.mod.setPathAlpha(this.sliderAlpha.getValueInt());
   }

   public void func_146281_b() {
      this.mod.saveConfig();
   }
}
