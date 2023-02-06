package me.drex.vanillapermissions.util;

import net.minecraft.SharedConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * In 22w45a (1.19.3 snapshot) Mojang made some changes to registries.
 * This class exists to provide a way to access registries
 */
public class RegistryProvider {

    public static final boolean AFTER22W45A = SharedConstants.WORLD_VERSION >= 3208;
    public static final String REGISTRY = "net.minecraft.class_2378";
    public static final String BUILTIN_REGISTRIES = "net.minecraft.class_7923";
    public static final String GET_KEY_INTERMEDIARY = "method_10221";

    // Registry instances
    public static final Object REGISTRY_ITEM_INSTANCE = registryObject("field_11142", "field_41178");
    public static final Object REGISTRY_BLOCK_INSTANCE = registryObject("field_11146", "field_41175");
    public static final Object REGISTRY_ENTITY_TYPE_INSTANCE = registryObject("field_11145", "field_41177");

    // Appropriate getKey method (method_10221)
    public static Method ITEM_GET_KEY_METHOD = getKeyMethod(REGISTRY_ITEM_INSTANCE);
    public static Method BLOCK_GET_KEY_METHOD = getKeyMethod(REGISTRY_BLOCK_INSTANCE);
    public static Method ENTITY_TYPE_GET_KEY_METHOD = getKeyMethod(REGISTRY_ENTITY_TYPE_INSTANCE);

    public static ResourceLocation itemKey(Item item) {
        return invokeGetKey(REGISTRY_ITEM_INSTANCE, ITEM_GET_KEY_METHOD, item);
    }

    public static ResourceLocation blockKey(Block block) {
        return invokeGetKey(REGISTRY_BLOCK_INSTANCE, BLOCK_GET_KEY_METHOD, block);
    }

    public static ResourceLocation entityKey(EntityType<?> entityType) {
        return invokeGetKey(REGISTRY_ENTITY_TYPE_INSTANCE, ENTITY_TYPE_GET_KEY_METHOD, entityType);
    }

    private static Object registryObject(String oldFieldName, String newFieldName) {
        try {
            Class<?> clazz = Class.forName(AFTER22W45A ? BUILTIN_REGISTRIES : REGISTRY);
            Field field = clazz.getField(AFTER22W45A ? newFieldName : oldFieldName);
            return field.get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static Method getKeyMethod(Object registryInstance) {
        Class<?> clazz = registryInstance.getClass();
        try {
            // (Ljava/lang/Object;)Lnet/minecraft/resources/ResourceLocation;
            return clazz.getMethod(GET_KEY_INTERMEDIARY, Object.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> ResourceLocation invokeGetKey(Object registryInstance, Method getKeyMethod, T object) {
        try {
            return (ResourceLocation) getKeyMethod.invoke(registryInstance, object);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
