//? if > 1.21.10 {
package me.drex.vanillapermissions.util;

import it.unimi.dsi.fastutil.objects.ReferenceArraySet;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.server.permissions.Permission;
import net.minecraft.server.permissions.PermissionSet;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;

public record MinimumPermissionSet(ReferenceSet<PermissionSet> permissions) implements PermissionSet {

    public static MinimumPermissionSet of(PermissionSet... permissionSets) {
        ReferenceSet<PermissionSet> permissions = new ReferenceArraySet<>();
        Collections.addAll(permissions, permissionSets);
        return new MinimumPermissionSet(permissions);
    }

    public boolean hasPermission(@NotNull Permission permission) {
        for (PermissionSet permissionSet : this.permissions) {
            if (!permissionSet.hasPermission(permission)) {
                return false;
            }
        }
        return true;
    }
}
//? }