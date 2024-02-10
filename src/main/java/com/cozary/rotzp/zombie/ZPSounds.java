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

import com.cozary.rotzp.Rotzp;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ZPSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Rotzp.MOD_ID);

    public static final RegistryObject<SoundEvent> ENTITY_ZOMBIE_PIGMAN_AMBIENT = SOUNDS.register("entity.zombie_pigman.ambient", () -> new SoundEvent(new ResourceLocation(Rotzp.MOD_ID, "entity.zombie_pigman.ambient")));
    public static final RegistryObject<SoundEvent> ENTITY_ZOMBIE_PIGMAN_ANGRY = SOUNDS.register("entity.zombie_pigman.angry", () -> new SoundEvent(new ResourceLocation(Rotzp.MOD_ID, "entity.zombie_pigman.angry")));
    public static final RegistryObject<SoundEvent> ENTITY_ZOMBIE_PIGMAN_DEATH = SOUNDS.register("entity.zombie_pigman.death", () -> new SoundEvent(new ResourceLocation(Rotzp.MOD_ID, "entity.zombie_pigman.death")));
    public static final RegistryObject<SoundEvent> ENTITY_ZOMBIE_PIGMAN_HURT = SOUNDS.register("entity.zombie_pigman.hurt", () -> new SoundEvent(new ResourceLocation(Rotzp.MOD_ID, "entity.zombie_pigman.hurt")));

}
