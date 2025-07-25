package com.arrowpath;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GL11;

import java.awt.Color;
import java.io.File;
import java.util.*;

@Mod(modid = "arrowpath", name = "Arrow Path", version = "2.1")
public class ArrowPathMain {

    private final Minecraft mc = Minecraft.getMinecraft();
    private final Map<EntityArrow, List<TrailPoint>> arrowTrails = new HashMap<>();

    // Configurable fields
    public static boolean enabled = true;
    public static Mode currentMode = Mode.PLAYER;
    public static String colorName = "red";
    public static boolean rgbMode = false;
    public static Color currentColor = Color.RED;

    private static Configuration config;

    private static final int MAX_POINTS = 80;
    private static final long TRAIL_LIFETIME_MS = 700;

    public enum Mode {
        PLAYER, PARTY, ALL
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        new ArrowPathCommand();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        File configFile = event.getSuggestedConfigurationFile();
        config = new Configuration(configFile);
        loadConfig();
    }

    private static void loadConfig() {
        if (config == null) return;

        config.load();  // Carica una sola volta all'inizio

        enabled = config.getBoolean("enabled", Configuration.CATEGORY_GENERAL, true, "Enable or disable the mod");
        String modeStr = config.getString("mode", Configuration.CATEGORY_GENERAL, "player", "Display mode: player, party, all");
        colorName = config.getString("colorName", Configuration.CATEGORY_GENERAL, "red", "Color name for arrow trails");
        rgbMode = config.getBoolean("rgbMode", Configuration.CATEGORY_GENERAL, false, "Enable RGB color cycling");

        try {
            currentMode = Mode.valueOf(modeStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            currentMode = Mode.PLAYER;
        }

        currentColor = ArrowPathColors.getColor(colorName);

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void saveConfig() {
        if (config == null) return;

        // NON ricaricare qui: config.load();

        config.get(Configuration.CATEGORY_GENERAL, "enabled", true).set(enabled);
        config.get(Configuration.CATEGORY_GENERAL, "mode", "player").set(currentMode.name().toLowerCase());
        config.get(Configuration.CATEGORY_GENERAL, "colorName", "red").set(colorName);
        config.get(Configuration.CATEGORY_GENERAL, "rgbMode", false).set(rgbMode);

        config.save();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!enabled || mc.theWorld == null) return;

        List<EntityArrow> arrows = mc.theWorld.getEntities(EntityArrow.class, e -> {
            if (e == null || e.isDead) return false;
            Entity shooter = e.shootingEntity;
            if (currentMode == Mode.PLAYER) return shooter == mc.thePlayer;
            if (currentMode == Mode.PARTY) return shooter instanceof EntityPlayer;
            return true;
        });

        long now = System.currentTimeMillis();
        for (EntityArrow arrow : arrows) {
            List<TrailPoint> trail = arrowTrails.computeIfAbsent(arrow, k -> new ArrayList<>());
            trail.add(new TrailPoint(arrow.posX, arrow.posY, arrow.posZ, now));
            if (trail.size() > MAX_POINTS) trail.remove(0);
        }

        arrowTrails.entrySet().removeIf(e -> !arrows.contains(e.getKey()) || e.getKey().isDead);
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!enabled || mc.theWorld == null) return;

        GlStateManager.pushMatrix();
        double camX = mc.getRenderManager().viewerPosX;
        double camY = mc.getRenderManager().viewerPosY;
        double camZ = mc.getRenderManager().viewerPosZ;
        GlStateManager.translate(-camX, -camY, -camZ);
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(2.0F);

        long now = System.currentTimeMillis();

        for (List<TrailPoint> points : arrowTrails.values()) {
            GL11.glBegin(GL11.GL_LINE_STRIP);
            for (TrailPoint p : points) {
                float alpha = 1.0f - (now - p.timestamp) / (float) TRAIL_LIFETIME_MS;
                if (alpha <= 0) continue;
                Color color = rgbMode ? Color.getHSBColor(((now - p.timestamp) % 1000) / 1000f, 1f, 1f) : currentColor;
                GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, alpha);
                GL11.glVertex3d(p.x, p.y, p.z);
            }
            GL11.glEnd();
        }

        GlStateManager.enableTexture2D();
        GlStateManager.popMatrix();
    }

    private static class TrailPoint {
        final double x, y, z;
        final long timestamp;

        TrailPoint(double x, double y, double z, long timestamp) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.timestamp = timestamp;
        }
    }
}