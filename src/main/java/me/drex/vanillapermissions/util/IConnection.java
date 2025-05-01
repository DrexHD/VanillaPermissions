package me.drex.vanillapermissions.util;

public interface IConnection {
    void vanillaPermissions$cachePermission(String permission, boolean value);

    boolean vanillaPermissions$getCachedPermission(String permission);

    void vanillaPermissions$clearCachedPermissions();
}
