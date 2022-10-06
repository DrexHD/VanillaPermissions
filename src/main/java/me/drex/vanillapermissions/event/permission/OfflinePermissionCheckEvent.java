package me.drex.vanillapermissions.event.permission;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.fabricmc.fabric.api.util.TriState;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface OfflinePermissionCheckEvent {
    Event<OfflinePermissionCheckEvent> EVENT = EventFactory.createArrayBacked(OfflinePermissionCheckEvent.class, (callbacks) -> (uuid, permission) -> {
        for (OfflinePermissionCheckEvent callback : callbacks) {
            CompletableFuture<TriState> future = callback.onPermissionCheck(uuid, permission);
            if (future.isDone() && future.join() == TriState.DEFAULT) continue;
            return future;
        }

        return CompletableFuture.completedFuture(TriState.DEFAULT);
    });

    @NotNull
    CompletableFuture<TriState> onPermissionCheck(@NotNull UUID uuid, @NotNull String permission);
}

