package me.drex.vanillapermissions.util;

import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.VanillaPermissionsMod;
import me.drex.vanillapermissions.mixin.cached_permission.ServerLoginPacketListenerImplAccessor;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.resources.Identifier;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.drex.vanillapermissions.VanillaPermissionsMod.LOGGER;

// Cache "offline" permission values on player query,
// so they can be used during the player join process with
public class JoinCache {

    public static final Identifier CHECK_PERMISSIONS = VanillaPermissionsMod.id("check_permissions");

    private static final String[] CACHE_ON_QUERY = new String[]{
        Permission.BYPASS_WHITELIST,
        Permission.BYPASS_PLAYER_LIMIT,
        Permission.BYPASS_FORCE_GAMEMODE
    };

    private static final Map<UUID, Map<String, TriState>> cachedPermissions = Collections.synchronizedMap(new HashMap<>());

    public static void initialize() {
        // ensure we are running after luckperms loads their user data
        ServerLoginConnectionEvents.QUERY_START.addPhaseOrdering(Event.DEFAULT_PHASE, CHECK_PERMISSIONS);
        ServerLoginConnectionEvents.QUERY_START.register(CHECK_PERMISSIONS, (handler, server, sender, synchronizer) -> {
            GameProfile profile = ((ServerLoginPacketListenerImplAccessor) handler).getAuthenticatedProfile();
            HashMap<String, TriState> cachedPermissions = new HashMap<>();
            //? if >= 1.21.9 {
            var uuid = profile.id();
            var name = profile.name();
            //?} else {
            /*var uuid = profile.getId();
            var name = profile.getName();
            *///?}

            JoinCache.cachedPermissions.put(uuid, cachedPermissions);
            for (String cachedPermission : CACHE_ON_QUERY) {
                try {
                    TriState result = Permissions.getPermissionValue(uuid, cachedPermission).join();
                    cachedPermissions.put(cachedPermission, result);
                } catch (Exception e) {
                    LOGGER.error("Failed to cache permission {} for {}", cachedPermission, name, e);
                }
            }
        });
        ServerPlayConnectionEvents.JOIN.register((serverGamePacketListener, packetSender, minecraftServer) -> {
            cachedPermissions.remove(serverGamePacketListener.player.getUUID());
        });
    }

    public static TriState getCachedPermissions(UUID uuid, String permission) {
        Map<String, TriState> cachedPermissions = JoinCache.cachedPermissions.getOrDefault(uuid, Collections.emptyMap());
        return cachedPermissions.getOrDefault(permission, TriState.DEFAULT);
    }
}
