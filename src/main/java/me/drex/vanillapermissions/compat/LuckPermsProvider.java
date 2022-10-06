package me.drex.vanillapermissions.compat;

import me.drex.vanillapermissions.event.permission.OfflinePermissionCheckEvent;
import net.fabricmc.fabric.api.util.TriState;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class LuckPermsProvider implements PermissionProvider {

    public static final LuckPermsProvider INSTANCE = new LuckPermsProvider();

    private LuckPermsProvider() {
    }

    @Override
    public void load() {
        OfflinePermissionCheckEvent.EVENT.register((uuid, permission) -> {
            LuckPerms luckPerms = net.luckperms.api.LuckPermsProvider.get();
            UserManager userManager = luckPerms.getUserManager();
            if (userManager.isLoaded(uuid)) {
                return CompletableFuture.completedFuture(
                        getPermission(Objects.requireNonNull(userManager.getUser(uuid), "user"), permission)
                );
            } else {
                return userManager.loadUser(uuid).thenApplyAsync(user -> getPermission(user, permission));
            }
        });
    }

    private TriState getPermission(User user, String permission) {
        return switch (user.getCachedData().getPermissionData().checkPermission(permission)) {
            case TRUE -> TriState.TRUE;
            case FALSE -> TriState.FALSE;
            case UNDEFINED -> TriState.DEFAULT;
        };
    }

    @Override
    public String modId() {
        return "luckperms";
    }
}
