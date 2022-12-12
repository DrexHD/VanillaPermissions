package me.drex.vanillapermissions.util;

import net.minecraft.SharedConstants;
import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;

/**
 * In 22w45a (1.19.3 snapshot) Mojang made some changes to registries.
 * This class exists to provide a way to access registries
 */
public class RegistryProvider {

    public static ResourceLocation itemKey(Item item) {
        DefaultedRegistry<Item> registry;
        // >=22w45a (1.19.3 snapshot)
        if (SharedConstants.WORLD_VERSION >= 3208) {
            registry = BuiltInRegistries.ITEM;
        } else {
            registry = (DefaultedRegistry<Item>) getRegistry("field_11142");
        }
        return registry.getKey(item);
    }

    public static ResourceLocation blockKey(Block block) {
        DefaultedRegistry<Block> registry;
        // >=22w45a (1.19.3 snapshot)
        if (SharedConstants.WORLD_VERSION >= 3208) {
            registry = BuiltInRegistries.BLOCK;
        } else {
            registry = (DefaultedRegistry<Block>) getRegistry("field_11146");
        }
        return registry.getKey(block);
    }

    public static ResourceLocation entityKey(EntityType<?> entityType) {
        DefaultedRegistry<EntityType<?>> registry;
        // >=22w45a (1.19.3 snapshot)
        if (SharedConstants.WORLD_VERSION >= 3208) {
            registry = BuiltInRegistries.ENTITY_TYPE;
        } else {
            registry = (DefaultedRegistry<EntityType<?>>) getRegistry("field_11145");
        }
        return registry.getKey(entityType);
    }

    private static DefaultedRegistry<?> getRegistry(String fieldName) {
        try {
            Class<?> registryClass = Class.forName("net.minecraft.class_2378");
            Field field = registryClass.getField(fieldName);
            return (DefaultedRegistry<?>) field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
