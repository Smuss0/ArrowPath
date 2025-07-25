package com.arrowpath;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.common.MinecraftForge;
import java.util.*;
import org.lwjgl.opengl.GL11;

@Mod(modid = "arrowpath", name = "Arrow Path", version = "1.0")
public class ArrowPathMain {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<EntityArrow, List<double[]>> arrowPaths = new HashMap<>();

    public static boolean enabled = true;
    public static Mode currentMode = Mode.PLAYER;

    public enum Mode {
        PLAYER,
        PARTY,
        ALL
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        new ArrowPathCommand();
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (mc.theWorld == null) return;

        List<EntityArrow> currentArrows = mc.theWorld.getEntities(EntityArrow.class, e -> {
            if (e == null || e.isDead) return false;

            Entity shooter = e.shootingEntity;
            if (currentMode == Mode.PLAYER) {
                return shooter == mc.thePlayer;
            } else if (currentMode == Mode.PARTY) {
                return shooter instanceof EntityPlayer;
            } else {
                return true;
            }
        });

        for (EntityArrow arrow : currentArrows) {
            arrowPaths.computeIfAbsent(arrow, k -> new ArrayList<>())
                    .add(new double[]{arrow.posX, arrow.posY, arrow.posZ});
            if (arrowPaths.get(arrow).size() > 100) {
                arrowPaths.get(arrow).remove(0);
            }
        }

        arrowPaths.entrySet().removeIf(entry ->
                !currentArrows.contains(entry.getKey()) || entry.getKey().isDead
        );
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        if (mc.theWorld == null || !enabled) return;

        GlStateManager.pushMatrix();
        double camX = mc.getRenderManager().viewerPosX;
        double camY = mc.getRenderManager().viewerPosY;
        double camZ = mc.getRenderManager().viewerPosZ;
        GlStateManager.translate(-camX, -camY, -camZ);

        GlStateManager.disableTexture2D();
        GL11.glLineWidth(2.0F);
        GlStateManager.color(1.0F, 0F, 0F, 1.0F);

        for (List<double[]> points : arrowPaths.values()) {
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (double[] pos : points) {
                GL11.glVertex3d(pos[0], pos[1], pos[2]);
            }
            GL11.glEnd();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }
}