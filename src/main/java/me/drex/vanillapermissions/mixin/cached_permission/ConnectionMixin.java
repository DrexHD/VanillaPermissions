package me.drex.vanillapermissions.mixin.cached_permission;

import me.drex.vanillapermissions.util.IConnection;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(Connection.class)
public abstract class ConnectionMixin implements IConnection {

    @Unique
    private final Map<String, Boolean> vanillaPermissions$cachedPermissions = new HashMap<>();

    @Override
    public void vanillaPermissions$cachePermission(String permission, boolean value) {
        vanillaPermissions$cachedPermissions.put(permission, value);
    }

    @Override
    public boolean vanillaPermissions$getCachedPermission(String permission) {
        return vanillaPermissions$cachedPermissions.get(permission) == Boolean.TRUE;
    }

    @Override
    public void vanillaPermissions$clearCachedPermissions() {
        vanillaPermissions$cachedPermissions.clear();
    }
}
