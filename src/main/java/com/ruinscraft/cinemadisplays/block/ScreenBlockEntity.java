/*
 * Cinema Displays Mod for Minecraft License
 *
 * Copyright (c) 2021 Ruinscraft, LLC
 *
 * This software is intellectual property of Ruinscraft, LLC and may not
 * be modified, distributed, or used for commercial purposes without
 * explicit written permission from the author.
 *
 * You may use this software for personal or testing purposes as long as
 * you do not modify it, distribute it, or claim to be the original
 * author.
 *
 * If you would like to license this software for commercial use, please
 * email: andersond@ruinscraft.com
 */

package com.ruinscraft.cinemadisplays.block;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ScreenBlockEntity extends BlockEntity {

    public static Identifier IDENT;
    public static BlockEntityType<ScreenBlockEntity> SCREEN_BLOCK_ENTITY;

    public ScreenBlockEntity() {
        super(SCREEN_BLOCK_ENTITY);
    }

    public static void register() {
        IDENT = new Identifier("cinemadisplays", "screen_block_entity");
        SCREEN_BLOCK_ENTITY = Registry.register(Registry.BLOCK_ENTITY_TYPE, IDENT,
                BlockEntityType.Builder.create(ScreenBlockEntity::new, ScreenBlock.SCREEN_BLOCK).build(null));
    }

}
