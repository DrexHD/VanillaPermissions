package me.drex.vanillapermissions.event.permission;

import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.util.TriState;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OfflinePermissions {

    /**
     * Gets the {@link TriState state} of a {@code permission} for the given player.
     *
     * @param uuid       the player uuid
     * @param permission the permission
     * @return the state of the permission
     */
    static @NotNull CompletableFuture<TriState> getPermissionValue(@NotNull UUID uuid, @NotNull String permission) {
        Objects.requireNonNull(uuid, "uuid");
        Objects.requireNonNull(permission, "permission");
        return OfflinePermissionCheckEvent.EVENT.invoker().onPermissionCheck(uuid, permission);
    }

    /**
     * Gets the {@link Boolean value} of a {@code permission} for the given player.
     *
     * @param uuid       the player uuid
     * @param permission the permission
     * @param fallback if permission is not set
     * @return the state of the permission
     */
    static @NotNull CompletableFuture<Boolean> check(@NotNull UUID uuid, @NotNull String permission, boolean fallback) {
        return getPermissionValue(uuid, permission).thenApplyAsync(triState -> triState.orElse(fallback));
    }

    /**
     * Gets the {@link Boolean value} of a {@code permission} for the given player.
     *
     * @param profile       the player profile
     * @param permission the permission
     * @return the state of the permission
     */
    static @NotNull CompletableFuture<Boolean> check(@NotNull GameProfile profile, @NotNull String permission, boolean fallback) {
        return check(profile.getId(), permission, fallback);
    }

}
