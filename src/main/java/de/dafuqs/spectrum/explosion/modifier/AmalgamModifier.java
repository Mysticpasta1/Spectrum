package de.dafuqs.spectrum.explosion.modifier;

import de.dafuqs.spectrum.explosion.Archetype;
import de.dafuqs.spectrum.explosion.ExplosionEffectFamily;
import de.dafuqs.spectrum.explosion.ItemBoundModifier;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AmalgamModifier extends ItemBoundModifier {

    public AmalgamModifier(Identifier id, ExplosionEffectFamily family, int color, Item... mappings) {
        super(id, family, color, mappings);
    }

    @Override
    public void applyToEntities(Archetype archetype, @NotNull List<Entity> entity) {}

    @Override
    public void applyToBlocks(Archetype archetype, @NotNull World world, @NotNull List<BlockPos> blocks) {}

    @Override
    public void applyToWorld(Archetype archetype, @NotNull World world, @NotNull Vec3d center) {}

    /**
     * This number exists exclusively so that the total amonut of damage dealt by a maxed out conflux is 6k flat<p>
     * 2500 * (1.3389 ** 3) = 6000.45
     * <p> <p>
     * Autism gaming
     */
    @Override
    public float getDamageModifer(Archetype archetype, BlockEntity blockEntity) {
        return 1.6778F;
    }

    @Override
    public float getBlastRadiusModifer(Archetype archetype, BlockEntity blockEntity) {
        return 1.5F;
    }
}
