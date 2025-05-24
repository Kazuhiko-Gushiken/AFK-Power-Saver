package com.enginnx.afkpowersaver.mixin;

import static com.enginnx.afkpowersaver.ClientModEvents.renderDisabled;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleRenderMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void afkpowersaver$cancelParticles(float partialTick, CallbackInfo ci) {
        if (renderDisabled) {
            ci.cancel(); // Cancel particle rendering
        }
    }
}
