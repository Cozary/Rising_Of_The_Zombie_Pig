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
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class ZPRenderer extends MobRenderer<ZPEntity, ZPModel<ZPEntity>> {

    protected static final ResourceLocation TEXTURE = new ResourceLocation(Rotzp.MOD_ID, "textures/entity/zombie_pigman.png");

    public ZPRenderer(EntityRendererProvider.Context context) {
        super(context, new ZPModel<>(context.bakeLayer(ClientHandler.ZP_LAYER)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(ZPEntity entity) {
        return TEXTURE;
    }


}

