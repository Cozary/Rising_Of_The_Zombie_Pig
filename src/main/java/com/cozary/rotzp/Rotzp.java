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

package com.cozary.rotzp;

import com.cozary.rotzp.events.ModEvents;
import com.cozary.rotzp.zombie.ClientHandler;
import com.cozary.rotzp.zombie.ZPEntityTypes;
import com.cozary.rotzp.zombie.ZPSounds;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("rotzp")
@Mod.EventBusSubscriber(modid = Rotzp.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Rotzp {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "rotzp";

    public Rotzp() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

        eventBus.addListener(this::doClientStuff);

        ZPSounds.SOUNDS.register(eventBus);
        ZPEntityTypes.ENTITY_TYPES.register(eventBus);

        MinecraftForge.EVENT_BUS.register(new ModEvents());
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        ClientHandler.init();

    }

}
