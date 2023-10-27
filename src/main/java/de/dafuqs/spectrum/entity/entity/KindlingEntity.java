package de.dafuqs.spectrum.entity.entity;

import de.dafuqs.additionalentityattributes.*;
import de.dafuqs.spectrum.entity.*;
import de.dafuqs.spectrum.registries.*;
import net.fabricmc.fabric.api.tag.convention.v1.*;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.damage.*;
import net.minecraft.entity.data.*;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.nbt.*;
import net.minecraft.recipe.*;
import net.minecraft.server.world.*;
import net.minecraft.sound.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.math.intprovider.*;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;

import java.util.*;

public class KindlingEntity extends HorseEntity implements RangedAttackMob, Angerable {
	
	protected static final Ingredient FOOD = Ingredient.fromTag(SpectrumItemTags.KINDLING_FOOD);
	
	private static final UniformIntProvider ANGER_TIME_RANGE = TimeHelper.betweenSeconds(30, 59);
	private static final TrackedData<Integer> ANGER = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> CLIPPED = DataTracker.registerData(KindlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	
	protected @Nullable UUID angryAt;
	
	// flying animation
	public float flapProgress;
	public float maxWingDeviation;
	public float prevMaxWingDeviation;
	public float prevFlapProgress;
	public float flapSpeed = 1.0F;
	private float field_28639 = 1.0F;
	
	public KindlingEntity(EntityType<? extends KindlingEntity> entityType, World world) {
		super(entityType, world);
		
		this.setPathfindingPenalty(PathNodeType.WATER, -0.75F);
		
		this.experiencePoints = 8;
	}
	
	public static DefaultAttributeContainer.Builder createKindlingAttributes() {
		return MobEntity.createMobAttributes()
				.add(EntityAttributes.GENERIC_MAX_HEALTH, 100.0D)
				.add(EntityAttributes.GENERIC_ARMOR, 25.0D)
				.add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 12.0D)
				.add(AdditionalEntityAttributes.MAGIC_PROTECTION, 6.0D)
				.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.6D)
				.add(EntityAttributes.HORSE_JUMP_STRENGTH, 24.0D);
	}

	@Override
	protected void initAttributes(Random random) {
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new HorseBondWithPlayerGoal(this, 1.4));
		this.goalSelector.add(2, new ProjectileAttackGoal(this, 1.25, 40, 20.0F));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, new TemptGoal(this, 1.25, FOOD, false));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(8, new LookAroundGoal(this));
		
		this.targetSelector.add(1, new CoughRevengeGoal(this));
		this.targetSelector.add(2, new UniversalAngerGoal<>(this, false));
	}
	
	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(ANGER, 0);
		this.dataTracker.startTracking(CLIPPED, 0);
	}
	
	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		this.writeAngerToNbt(nbt);
	}
	
	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		this.readAngerFromNbt(this.world, nbt);
	}
	
	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return FOOD.test(stack);
	}
	
	@Nullable
	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
		return SpectrumEntityTypes.KINDLING.create(world);
	}
	
	@Override
	protected SoundEvent getAmbientSound() {
		return SpectrumSoundEvents.ENTITY_KINDLING_AMBIENT;
	}
	
	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return SpectrumSoundEvents.ENTITY_KINDLING_HURT;
	}
	
	@Override
	protected SoundEvent getDeathSound() {
		return SpectrumSoundEvents.ENTITY_KINDLING_DEATH;
	}
	
	@Override
	public boolean isInAir() {
		return !this.onGround;
	}
	
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource damageSource) {
		return false;
	}
	
	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return 0.6F * dimensions.height;
	}
	
	@Override
	protected void mobTick() {
		super.mobTick();
		
		if (!this.world.isClient) {
			this.tickAngerLogic((ServerWorld) this.world, false);
			this.setClipped(this.getClipTime() - 1);
		}
		if (this.age % 600 == 0) {
			this.heal(1.0F);
		}
	}
	
	@Override
	public void tickMovement() {
		super.tickMovement();
		
		this.prevFlapProgress = this.flapProgress;
		this.prevMaxWingDeviation = this.maxWingDeviation;
		this.maxWingDeviation += (this.onGround ? -1.0F : 4.0F) * 0.3F;
		this.maxWingDeviation = MathHelper.clamp(this.maxWingDeviation, 0.0F, 1.0F);
		if (!this.onGround && this.flapSpeed < 1.0F) {
			this.flapSpeed = 1.0F;
		}
		
		this.flapSpeed *= 0.9F;
		Vec3d vec3d = this.getVelocity();
		if (!this.onGround && vec3d.y < 0.0) {
			this.setVelocity(vec3d.multiply(1.0, 0.6, 1.0));
		}
		
		this.flapProgress += this.flapSpeed * 2.0F;
	}
	
	@Override
	protected boolean hasWings() {
		return this.speed > this.field_28639;
	}
	
	@Override
	protected void addFlapEffects() {
		this.field_28639 = this.speed + this.maxWingDeviation / 2.0F;
	}
	
	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		if (this.getAngerTime() > 0) {
			return ActionResult.success(this.world.isClient);
		}
		
		ItemStack handStack = player.getMainHandStack();
		if (handStack.isIn(ConventionalItemTags.SHEARS)) {
			handStack.damage(1, player, (p) -> p.sendToolBreakStatus(hand));
			
			if (!this.world.isClient) {
				player.getInventory().offerOrDrop(SpectrumItems.EFFULGENT_FEATHER.getDefaultStack());
				
				setClipped(4800); // 4 minutes
				
				setTarget(player);
				setAngryAt(player.getUuid());
				chooseRandomAngerTime();
			}
			
			return ActionResult.success(world.isClient);
		}
		
		return super.interactMob(player, hand);
	}
	
	protected void coughAt(LivingEntity target) {
		KindlingCoughEntity kindlingCoughEntity = new KindlingCoughEntity(this.world, this);
		double d = target.getX() - this.getX();
		double e = target.getBodyY(0.33F) - kindlingCoughEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = Math.sqrt(d * d + f * f) * 0.2;
		kindlingCoughEntity.setVelocity(d, e + g, f, 1.5F, 10.0F);
		if (!this.isSilent()) {
			this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SpectrumSoundEvents.ENTITY_KINDLING_SHOOT, this.getSoundCategory(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
		}
		
		this.world.spawnEntity(kindlingCoughEntity);
	}
	
	public boolean isClipped() {
		return this.dataTracker.get(CLIPPED) > 0;
	}
	
	public int getClipTime() {
		return this.dataTracker.get(CLIPPED);
	}
	
	public void setClipped(int clipTime) {
		this.dataTracker.set(CLIPPED, clipTime);
	}
	
	@Override
	public boolean isAngry() {
		return this.getHorseFlag(32);
	}
	
	@Override
	public int getAngerTime() {
		return this.dataTracker.get(ANGER);
	}
	
	@Override
	public void setAngerTime(int angerTime) {
		this.dataTracker.set(ANGER, angerTime);
	}
	
	@Override
	public @Nullable UUID getAngryAt() {
		return this.angryAt;
	}
	
	@Override
	public void setAngryAt(@Nullable UUID angryAt) {
		this.angryAt = angryAt;
	}
	
	public
	@Override void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.get(this.random));
	}
	
	@Override
	public void attack(LivingEntity target, float pullProgress) {
		this.coughAt(target);
	}
	
	@Override
	public boolean eatsGrass() {
		return false;
	}
	
	@Override
	public boolean canBreedWith(AnimalEntity other) {
		return other != this && other instanceof KindlingEntity otherKindling && this.canBreed() && otherKindling.canBreed();
	}
	
	protected class CoughRevengeGoal extends RevengeGoal {
		
		public CoughRevengeGoal(KindlingEntity kindling) {
			super(kindling);
		}
		
		@Override
		public boolean shouldContinue() {
			return KindlingEntity.this.hasAngerTime() && super.shouldContinue();
		}
		
		@Override
		protected void setMobEntityTarget(MobEntity mob, LivingEntity target) {
			if (mob instanceof BeeEntity && this.mob.canSee(target)) {
				mob.setTarget(target);
			}
		}
		
	}
	
}
