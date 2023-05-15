package me.drex.vanillapermissions.util;

import net.minecraft.resources.ResourceLocation;

public class Permission {

    public static final String ADMIN_BROADCAST_RECEIVE = permission("adminbroadcast.receive");
    public static final String BYPASS_SPAWN_PROTECTION = permission("bypass.spawn-protection");
    public static final String BYPASS_FORCE_GAMEMODE = permission("bypass.force-gamemode");
    public static final String BYPASS_MOVE_SPEED_PLAYER = permission("bypass.move-speed.player");
    public static final String BYPASS_MOVE_SPEED_VEHICLE = permission("bypass.move-speed.vehicle.%s.%s");
    public static final String BYPASS_CHAT_SPEED = permission("bypass.chat-speed");
    public static final String BYPASS_WHITELIST = permission("bypass.whitelist");
    public static final String BYPASS_PLAYER_LIMIT = permission("bypass.player-limit");
    public static final String COMMAND = permission("command.%s");
    public static final String DEBUG_STICK_USE = permission("%s.use.%s.%s");
    public static final String NBT_QUERY_ENTITY = permission("nbt.query.entity");
    public static final String NBT_QUERY_BLOCK = permission("nbt.query.block");
    public static final String NBT_LOAD_ENTITY = permission("nbt.load.entity");
    public static final String NBT_LOAD_BLOCK = permission("nbt.load.block");
    public static final String OPERATOR_BLOCK_PLACE = permission("operator_block.%s.place");
    public static final String OPERATOR_BLOCK_VIEW = permission("operator_block.%s.view");
    public static final String OPERATOR_BLOCK_EDIT = permission("operator_block.%s.edit");
    public static final String OPERATOR_BLOCK_BREAK = permission("operator_block.%s.break");
    public static final String SELECTOR = permission("selector");

    protected static String permission(String permission) {
        return build(ResourceLocation.DEFAULT_NAMESPACE, permission);
    }

    public static String build(String... parts) {
        return String.join(".", parts);
    }

}
