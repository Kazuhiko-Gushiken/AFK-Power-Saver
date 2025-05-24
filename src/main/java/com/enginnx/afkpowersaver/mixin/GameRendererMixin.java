package com.enginnx.afkpowersaver.mixin;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static com.enginnx.afkpowersaver.ClientModEvents.renderDisabled;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(method = "renderLevel", at = @At("HEAD"), cancellable = true)
    private void render(DeltaTracker deltaTracker, CallbackInfo ci) {
        if (renderDisabled) {
            ci.cancel();
        }
    }
}
