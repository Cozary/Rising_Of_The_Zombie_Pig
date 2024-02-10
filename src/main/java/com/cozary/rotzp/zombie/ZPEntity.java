/*
 *
 *  * Copyright (c) 2024 Cozary
 *  *
 *  * This file is part of Rising Of The Zombie Pig, a mod made for Minecraft.
 *  *
 *  * Rising Of The Zombie Pig is free software: you can redistribute it and/or modify it
 *  * under the terms of the GNU General Public License as published
 *  * by the Free Software Foundation, either version 3 of the License, or
 *  * (at your option) any later version.
 *  *
 *  * Rising Of The Zombie Pig is distributed in the hope that it will be useful, but
 *  * WITHOUT ANY WARRANTY; without even the implied warranty of
 *  * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 *  * GNU General Public License for more details.
 *  *
 *  * You should have received a copy of the GNU General Public License
 *  * License along with Rising Of The Zombie Pig.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package com.cozary.rotzp.zombie;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;

import javax.annotation.Nullable;
import java.util.UUID;

public class ZPEntity extends Monster {

    private static final UUID ATTACK_SPEED_BOOST_MODIFIER_UUID = UUID.fromString("49455A49-7EC5-45BA-B886-3B90B23A1718");
    private static final AttributeModifier ATTACK_SPEED_BOOST_MODIFIER = (new AttributeModifier(ATTACK_SPEED_BOOST_MODIFIER_UUID, "Attacking speed boost", 0.05D, AttributeModifier.Operation.ADDITION));
    private int angerLevel;
    private int randomSoundDelay;
    private UUID angerTargetUUID;

    public ZPEntity(EntityType<? extends ZPEntity> zpEntity, Level level) {
        super(zpEntity, level);
        this.setPathfindingMalus(BlockPathTypes.LAVA, 8.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE, 0.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.23F)
                .add(Attributes.ATTACK_DAMAGE, 5.0D);
    }

    @Override
    public void setLastHurtByMob(@Nullable LivingEntity livingBase) {
        super.setLastHurtByMob(livingBase);
        if (livingBase != null) {
            this.angerTargetUUID = livingBase.getUUID();
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new ZPAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.targetSelector.addGoal(1, new HurtByAggressorGoal(this));
        this.targetSelector.addGoal(2, new TargetAggressorGoal(this));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0D));
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void customServerAiStep() {
        AttributeInstance iattributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        LivingEntity livingentity = this.getLastHurtByMob();
        if (this.isAngry()) {
            if (!this.isBaby() && !iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
                iattributeinstance.addTransientModifier(ATTACK_SPEED_BOOST_MODIFIER);
            }

            --this.angerLevel;
            LivingEntity livingentity1 = livingentity != null ? livingentity : this.getTarget();
            if (!this.isAngry() && livingentity1 != null) {
                if (!this.hasLineOfSight(livingentity1)) {
                    this.setLastHurtByMob(null);
                    this.setTarget(null);
                } else {
                    this.angerLevel = this.func_223336_ef();
                }
            }
        } else if (iattributeinstance.hasModifier(ATTACK_SPEED_BOOST_MODIFIER)) {
            iattributeinstance.removeModifier(ATTACK_SPEED_BOOST_MODIFIER);
        }

        if (this.randomSoundDelay > 0 && --this.randomSoundDelay == 0) {
            this.playSound(ZPSounds.ENTITY_ZOMBIE_PIGMAN_ANGRY.get(), this.getSoundVolume() * 2.0F, ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 1.8F);
        }

        if (this.isAngry() && this.angerTargetUUID != null && livingentity == null) {
            Player playerentity = this.level.getPlayerByUUID(this.angerTargetUUID);
            this.setLastHurtByMob(playerentity);
            this.lastHurtByPlayer = playerentity;
            this.lastHurtByPlayerTime = this.getLastHurtByMobTimestamp();
        }

        super.customServerAiStep();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader worldIn) {
        return worldIn.isUnobstructed(this) && !worldIn.containsAnyLiquid(this.getBoundingBox());
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putShort("Anger", (short) this.angerLevel);
        if (this.angerTargetUUID != null) {
            compound.putString("HurtBy", this.angerTargetUUID.toString());
        } else {
            compound.putString("HurtBy", "");
        }

    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.angerLevel = compound.getShort("Anger");
        String s = compound.getString("HurtBy");
        if (!s.isEmpty()) {
            this.angerTargetUUID = UUID.fromString(s);
            Player playerentity = this.level.getPlayerByUUID(this.angerTargetUUID);
            this.setLastHurtByMob(playerentity);
            if (playerentity != null) {
                this.lastHurtByPlayer = playerentity;
                this.lastHurtByPlayerTime = this.getLastHurtByMobTimestamp();
            }
        }

    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else {
            Entity entity = source.getEntity();
            if (entity instanceof Player && !((Player) entity).isCreative() && this.hasLineOfSight(entity)) {
                this.func_226547_i_((LivingEntity) entity);
            }

            return super.hurt(source, amount);
        }
    }

    private boolean func_226547_i_(LivingEntity p_226547_1_) {
        this.angerLevel = this.func_223336_ef();
        this.randomSoundDelay = this.random.nextInt(40);
        this.setLastHurtByMob(p_226547_1_);
        return true;
    }

    private int func_223336_ef() {
        return 400 + this.random.nextInt(400);
    }

    private boolean isAngry() {
        return this.angerLevel > 0;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ZPSounds.ENTITY_ZOMBIE_PIGMAN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ZPSounds.ENTITY_ZOMBIE_PIGMAN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ZPSounds.ENTITY_ZOMBIE_PIGMAN_DEATH.get();
    }

    static class HurtByAggressorGoal extends HurtByTargetGoal {
        public HurtByAggressorGoal(ZPEntity p_i45828_1_) {
            super(p_i45828_1_);
            this.setAlertOthers(Zombie.class);
        }

        protected void alertOther(Mob mobIn, LivingEntity targetIn) {
            if (mobIn instanceof ZPEntity && this.mob.hasLineOfSight(targetIn) && ((ZPEntity) mobIn).func_226547_i_(targetIn)) {
                mobIn.setTarget(targetIn);
            }

        }
    }

    static class TargetAggressorGoal extends NearestAttackableTargetGoal<Player> {
        public TargetAggressorGoal(ZPEntity p_i45829_1_) {
            super(p_i45829_1_, Player.class, true);
        }

        public boolean canUse() {
            return ((ZPEntity) this.mob).isAngry() && super.canUse();
        }
    }

    static class ZPAttackGoal extends MeleeAttackGoal {

        private final ZPEntity zombie;
        private int raiseArmTicks;

        public ZPAttackGoal(ZPEntity zombieIn, double speedIn, boolean longMemoryIn) {
            super(zombieIn, speedIn, longMemoryIn);
            this.zombie = zombieIn;
        }

        public void start() {
            super.start();
            this.raiseArmTicks = 0;
        }

        public void stop() {
            super.stop();
            this.zombie.setAggressive(false);
        }

        public void tick() {
            super.tick();
            ++this.raiseArmTicks;
            this.zombie.setAggressive(this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2);

        }
    }
}
