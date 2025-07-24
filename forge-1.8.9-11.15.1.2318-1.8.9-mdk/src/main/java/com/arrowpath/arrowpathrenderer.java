package com.arrowpath;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.*;

public class arrowpathrenderer {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<EntityArrow, List<Vec3>> arrowTrails = new HashMap<>();
    private final arrowpathsettings settings;

    public arrowpathrenderer(arrowpathsettings settings) {
        this.settings = settings;
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (!settings.isShowPathEnabled()) return;

        for (Entity entity : mc.theWorld.loadedEntityList) {
            if (entity instanceof EntityArrow) {
                EntityArrow arrow = (EntityArrow) entity;
                Entity shooter = arrow.shootingEntity;

                boolean shouldTrack = false;
                switch (settings.getMode()) {
                    case SOLO:
                        shouldTrack = (shooter == mc.thePlayer);
                        break;
                    case MULTI:
                        shouldTrack = (shooter instanceof EntityPlayer);
                        break;
                    case ALL:
                        shouldTrack = true;
                        break;
                }

                if (!shouldTrack) continue;

                if (!arrowTrails.containsKey(arrow)) {
                    arrowTrails.put(arrow, new ArrayList<>());
                }

                List<Vec3> trail = arrowTrails.get(arrow);
                trail.add(new Vec3(arrow.posX, arrow.posY, arrow.posZ));
            }
        }

        renderTrails(event.partialTicks);
    }

    private void renderTrails(float partialTicks) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glLineWidth(2.0F);
        GL11.glBegin(GL11.GL_LINE_STRIP);

        double dx = mc.getRenderManager().viewerPosX;
        double dy = mc.getRenderManager().viewerPosY;
        double dz = mc.getRenderManager().viewerPosZ;

        for (List<Vec3> trail : arrowTrails.values()) {
            for (Vec3 vec : trail) {
                GL11.glColor3f(arrowpathcolor.red, arrowpathcolor.green, arrowpathcolor.blue);
                GL11.glVertex3d(vec.xCoord - dx, vec.yCoord - dy, vec.zCoord - dz);
            }
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
    }
}
