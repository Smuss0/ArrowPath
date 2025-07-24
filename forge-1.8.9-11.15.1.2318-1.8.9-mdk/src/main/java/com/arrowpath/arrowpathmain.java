package com.arrowpath; // cambia questo package a piacere

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

@Mod(modid = "arrowpath", name = "Arrow Path", version = "1.0")
public class arrowpathmain {

    private Map<EntityArrow, List<double[]>> arrowPaths = new HashMap<>();
    private Minecraft mc = Minecraft.getMinecraft();

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null) return;

        // Ottieni tutte le frecce attualmente nel mondo
        List<EntityArrow> currentArrows = mc.theWorld.getEntities(EntityArrow.class, e -> e != null && !e.isDead);

        // Aggiungi o aggiorna le posizioni delle frecce ancora presenti
        for (EntityArrow arrow : currentArrows) {
            List<double[]> path = arrowPaths.get(arrow);
            if (path == null) {
                path = new ArrayList<>();
                arrowPaths.put(arrow, path);
            }
            path.add(new double[]{arrow.posX, arrow.posY, arrow.posZ});
            if (path.size() > 100) {
                path.remove(0);
            }
        }

        // Rimuovi dalla mappa tutte le frecce che NON sono più nel mondo
        Iterator<EntityArrow> iterator = arrowPaths.keySet().iterator();
        while (iterator.hasNext()) {
            EntityArrow arrow = iterator.next();
            if (!currentArrows.contains(arrow) || arrow.isDead) {
                iterator.remove();
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null) return;

        GlStateManager.pushMatrix();
        double camX = mc.getRenderManager().viewerPosX;
        double camY = mc.getRenderManager().viewerPosY;
        double camZ = mc.getRenderManager().viewerPosZ;

        GlStateManager.translate(-camX, -camY, -camZ);

        GlStateManager.disableTexture2D();
        GL11.glLineWidth(2.0F); // usa GL11 e non GlStateManager per la line width
        GlStateManager.color(1.0F, 0F, 0F, 1.0F);

        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (List<double[]> points : arrowPaths.values()) {
            for (double[] pos : points) {
                GL11.glVertex3d(pos[0], pos[1], pos[2]);
            }
        }
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}
