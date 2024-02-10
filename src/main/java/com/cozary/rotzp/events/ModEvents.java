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

package com.cozary.rotzp.events;

import com.cozary.rotzp.zombie.ZPEntity;
import com.cozary.rotzp.zombie.ZPEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Pig;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

public class ModEvents {

    @SubscribeEvent
    public void ZombiPigMeat(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity().getMainHandItem().getItem() == Items.ROTTEN_FLESH && !event.getEntity().level.isClientSide) {
            if (event.getTarget().isAlive()) {
                LivingEntity target = (LivingEntity) event.getTarget();
                Random rand = new Random();
                if (target instanceof Pig) {
                    if (!event.getEntity().getAbilities().instabuild) {
                        event.getEntity().getMainHandItem().shrink(1);
                    }
                    int chance = event.getEntity().getCommandSenderWorld().random.nextInt(100);
                    if (chance < 40) {
                        if (event.getEntity().getCommandSenderWorld().getDifficulty() != Difficulty.PEACEFUL && net.minecraftforge.event.ForgeEventFactory.canLivingConvert(target, ZPEntityTypes.ZOMBIE_PIGMAN.get(), (timer) -> {
                        })) {
                            ZPEntity zombifiedpiglinentity = ZPEntityTypes.ZOMBIE_PIGMAN.get().create(event.getEntity().getCommandSenderWorld());
                            zombifiedpiglinentity.moveTo(target.getX(), target.getY(), target.getZ(), target.yRotO, target.xRotO);
                            zombifiedpiglinentity.setNoAi(((Pig) target).isNoAi());
                            if (target.hasCustomName()) {
                                zombifiedpiglinentity.setCustomName(target.getCustomName());
                                zombifiedpiglinentity.setCustomNameVisible(target.isCustomNameVisible());
                            }

                            zombifiedpiglinentity.setPersistenceRequired();
                            net.minecraftforge.event.ForgeEventFactory.onLivingConvert(target, zombifiedpiglinentity);
                            event.getEntity().getCommandSenderWorld().addFreshEntity(zombifiedpiglinentity);
                            target.remove(Entity.RemovalReason.CHANGED_DIMENSION);
                            for (int i = 0; i < 20; ++i) {
                                double d0 = rand.nextGaussian() * 0.02D;
                                double d1 = rand.nextGaussian() * 0.02D;
                                double d2 = rand.nextGaussian() * 0.02D;
                                ((ServerLevel) target.getCommandSenderWorld()).sendParticles(ParticleTypes.FLAME, target.getRandomX(1.0D) - d0 * 10.0D, target.getRandomY() - d1 * 10.0D, target.getRandomZ(1.0D) - d2 * 10.0D, (int) d0, d1, d2, 1D, 0.1);
                                ((ServerLevel) target.getCommandSenderWorld()).sendParticles(ParticleTypes.PORTAL, target.getRandomX(1.0D) - d0 * 10.0D, target.getRandomY() - d1 * 10.0D, target.getRandomZ(1.0D) - d2 * 10.0D, (int) d0, d1, d2, 1D, 0.1);
                                ((ServerLevel) target.getCommandSenderWorld()).sendParticles(ParticleTypes.REVERSE_PORTAL, target.getRandomX(1.0D) - d0 * 10.0D, target.getRandomY() - d1 * 10.0D, target.getRandomZ(1.0D) - d2 * 10.0D, (int) d0, d1, d2, 1D, 0.1);
                            }
                            if (!event.getEntity().getCommandSenderWorld().isClientSide) {
                                String msg = ChatFormatting.DARK_RED + "The Zombie Pig Rises";
                                event.getEntity().sendSystemMessage(Component.translatable(msg));
                            }
                        }

                    }
                }
            }
        }
    }


}
