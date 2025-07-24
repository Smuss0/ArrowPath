package com.arrowpath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

public class ArrowPathRender {
   private ArrowPath mod;
   private HashMap<EntityArrow, ArrayList<Vec3>> arrowMap;

   public ArrowPathRender(ArrowPath mod) {
      this.mod = mod;
      this.arrowMap = new HashMap();
   }

   @SubscribeEvent
   public void onRenderWorldLast(RenderWorldLastEvent event) {
      if (this.mod.isShowPath()) {
         Iterator iterator = ArrowPath.mc.field_71441_e.field_72996_f.iterator();

         while(iterator.hasNext()) {
            Entity entity = (Entity)iterator.next();
            if (entity instanceof EntityArrow && !this.arrowMap.containsKey(entity) && !entity.field_70128_L && this.isMoving(entity)) {
               this.arrowMap.put((EntityArrow)entity, new ArrayList(Arrays.asList(new Vec3(entity.field_70165_t, entity.field_70163_u, entity.field_70161_v))));
            }
         }

         iterator = this.arrowMap.entrySet().iterator();

         while(true) {
            while(iterator.hasNext()) {
               EntityArrow arrow = (EntityArrow)((Entry)iterator.next()).getKey();
               ArrayList<Vec3> arrowPath = (ArrayList)this.arrowMap.get(arrow);
               if (!arrow.field_70128_L && this.isMoving(arrow)) {
                  if (arrowPath.size() > this.mod.getPathLength()) {
                     arrowPath.remove(0);
                  }
               } else {
                  if (arrowPath.size() > 0) {
                     arrowPath.remove(0);
                  }

                  if (arrowPath.size() <= 0) {
                     iterator.remove();
                     continue;
                  }
               }

               Vec3 currentPosition = new Vec3(arrow.field_70165_t, arrow.field_70163_u, arrow.field_70161_v);
               if (((Vec3)arrowPath.get(arrowPath.size() - 1)).func_72436_e(currentPosition) >= 0.05000000074505806D) {
                  ((ArrayList)this.arrowMap.get(arrow)).add(currentPosition);
               }

               GL11.glPushMatrix();
               GL11.glEnable(3042);
               GL11.glBlendFunc(770, 771);
               GL11.glDisable(3553);
               GL11.glEnable(2848);
               GL11.glHint(3154, 4354);
               GL11.glLineWidth((float)this.mod.getPathWidth() / 10.0F + 0.5F);
               GL11.glColor4f((float)this.mod.getPathRed() / 255.0F, (float)this.mod.getPathGreen() / 255.0F, (float)this.mod.getPathBlue() / 255.0F, (float)this.mod.getPathAlpha() / 255.0F);
               Tessellator tessellator = Tessellator.func_178181_a();
               WorldRenderer worldRenderer = tessellator.func_178180_c();
               worldRenderer.func_181668_a(3, DefaultVertexFormats.field_181705_e);
               Iterator var8 = arrowPath.iterator();

               while(var8.hasNext()) {
                  Vec3 position = (Vec3)var8.next();
                  double xPos = position.field_72450_a - ArrowPath.mc.func_175598_ae().field_78730_l;
                  double yPos = position.field_72448_b - ArrowPath.mc.func_175598_ae().field_78731_m;
                  double zPos = position.field_72449_c - ArrowPath.mc.func_175598_ae().field_78728_n;
                  worldRenderer.func_181662_b(xPos, yPos, zPos).func_181675_d();
               }

               tessellator.func_78381_a();
               GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
               GL11.glDisable(2848);
               GL11.glEnable(3553);
               GL11.glDisable(3042);
               GL11.glPopMatrix();
            }

            return;
         }
      }
   }

   private boolean isMoving(Entity entity) {
      return entity.field_70169_q != entity.field_70165_t && entity.field_70167_r != entity.field_70163_u && entity.field_70166_s != entity.field_70161_v;
   }
}
