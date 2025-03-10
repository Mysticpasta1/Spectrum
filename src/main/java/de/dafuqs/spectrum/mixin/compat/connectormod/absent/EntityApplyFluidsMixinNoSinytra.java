package de.dafuqs.spectrum.mixin.compat.connectormod.absent;

import de.dafuqs.spectrum.blocks.fluid.SpectrumFluid;
import de.dafuqs.spectrum.interfaces.TouchingWaterAware;
import de.dafuqs.spectrum.registries.SpectrumFluidTags;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Entity.class)
public class EntityApplyFluidsMixinNoSinytra {

    // 25.12.2023: Lithium's mixin cancel makes this code not run, making fluids not swimmable
    // https://github.com/CaffeineMC/lithium-fabric/blob/300f430d7b8618ac3b0862892b36696dcfab5a85/src/main/java/me/jellysquid/mods/lithium/mixin/entity/collisions/fluid/EntityMixin.java#L46
    // we therefore disable that mixin in our fabric.mod.json like documented in
    // https://github.com/CaffeineMC/lithium-fabric/wiki/Disabling-Lithium's-Mixins-using-your-mod's-fabric-mod.json
    @Redirect(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    public boolean spectrum$updateMovementInFluid(FluidState fluidState, TagKey<Fluid> tag) {
        if (tag == FluidTags.WATER) {
            return (fluidState.isIn(FluidTags.WATER) || fluidState.isIn(SpectrumFluidTags.SWIMMABLE_FLUID));
        } else {
            return fluidState.isIn(tag);
        }
    }

    @ModifyArg(method = "onSwimmingStart()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"), index = 0)
    private ParticleEffect spectrum$modifySwimmingStartParticles(ParticleEffect particleEffect) {
        Fluid fluid = ((Entity) (Object) this).getWorld().getFluidState(((Entity) (Object) this).getBlockPos()).getFluid();
        if (fluid instanceof SpectrumFluid spectrumFluid) {
            return spectrumFluid.getSplashParticle();
        }
        return particleEffect;
    }

    @Inject(method = "updateMovementInFluid", at = @At(value = "INVOKE", target = "Ljava/lang/Math;max(DD)D"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void spectrum$updateMovementInFluid(TagKey<Fluid> tag, double speed, CallbackInfoReturnable<Boolean> info, Box box, int i, int j, int k, int l, int m, int n, double d, boolean bl, boolean bl2, Vec3d vec3d, int o, BlockPos.Mutable mutable, int p, int q, int r, FluidState fluidState) {
        ((TouchingWaterAware) this).spectrum$setActuallyTouchingWater(fluidState.isIn(FluidTags.WATER));
    }
}
