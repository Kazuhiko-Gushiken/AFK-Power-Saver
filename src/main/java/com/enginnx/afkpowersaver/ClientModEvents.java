package com.enginnx.afkpowersaver;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.telemetry.events.WorldUnloadEvent;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.settings.KeyConflictContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.GameShuttingDownEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = AFKPowerSaver.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void registerBindings(RegisterKeyMappingsEvent event) {
        AFKPowerSaver.disableRender = new KeyMapping("key.afkpowersaver.toggle",
                KeyConflictContext.IN_GAME,
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_P,
                "category.afkpowersaver.toggle");
        event.register(AFKPowerSaver.disableRender );
    }

    public static boolean renderDisabled = false;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(ClientModEvents::onClientTick);
    }

    public static int previousFramerateLimit = -1;
    public static int previousChunkLimit = -1;

    /*
    public static void resetFramerate() {
        Minecraft mc = Minecraft.getInstance();
        if (previousFramerateLimit > 0) {
            mc.options.framerateLimit().set(previousFramerateLimit);
            previousFramerateLimit = -1;
            renderDisabled = false;
            System.out.println("Framerate limit reset on world close/disconnect/shutdown.");
        }
    }
    */

    public static void onClientTick(ClientTickEvent.Post event) {
        if (AFKPowerSaver.disableRender.consumeClick()) {
            renderDisabled = !renderDisabled;

            Minecraft mc = Minecraft.getInstance();
            assert mc.player != null;
            if (renderDisabled) {
                previousFramerateLimit = mc.options.framerateLimit().get();
                previousChunkLimit = mc.options.renderDistance().get();
                mc.options.framerateLimit().set(10);
                mc.options.renderDistance().set(2);

                mc.player.sendSystemMessage(Component.literal("Rendering disabled. FPS and Render Distance capped."));
            } else {
                if (previousFramerateLimit > 0 && previousChunkLimit > 0) {
                    mc.options.framerateLimit().set(previousFramerateLimit);
                    mc.options.renderDistance().set(previousChunkLimit);
                }
                mc.player.sendSystemMessage(Component.literal("Rendering enabled. FPS and Render Distance restored."));
            }


        }
    }
}
