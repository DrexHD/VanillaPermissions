package me.drex.vanillapermissions;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.mixin.CommandNodeAccessor;
import me.drex.vanillapermissions.mixin.cached_permission.ServerLoginPacketListenerImplAccessor;
import me.drex.vanillapermissions.util.IConnection;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.Connection;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

import static me.drex.vanillapermissions.util.Permission.build;

public class VanillaPermissionsMod implements ModInitializer {

    public static final String MOD_ID = "vanilla-permissions";
    public static final ResourceLocation MODIFY_VANILLA_PERMISSIONS_PHASE = ResourceLocation.fromNamespaceAndPath(MOD_ID, "modify_vanilla_permissions");
    public static final ResourceLocation CHECK_PERMISSIONS = ResourceLocation.fromNamespaceAndPath(MOD_ID, "check_permissions");
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String[] CACHE_ON_QUERY = new String[]{
        Permission.BYPASS_WHITELIST,
        Permission.BYPASS_PLAYER_LIMIT,
        Permission.BYPASS_FORCE_GAMEMODE
    };

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
        CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, registryAccess, environment) -> {
            for (CommandNode<CommandSourceStack> node : dispatcher.getRoot().getChildren()) {
                alterCommandChildNode(dispatcher, node);
            }
            LOGGER.info("Loaded Fabric Permissions");
        });

        // ensure we are running after luckperms loads their user data
        ServerLoginConnectionEvents.QUERY_START.addPhaseOrdering(Event.DEFAULT_PHASE, CHECK_PERMISSIONS);
        ServerLoginConnectionEvents.QUERY_START.register(CHECK_PERMISSIONS, (handler, server, sender, synchronizer) -> {
            GameProfile profile = ((ServerLoginPacketListenerImplAccessor) handler).getAuthenticatedProfile();
            Connection connection = ((ServerLoginPacketListenerImplAccessor) handler).getConnection();
            for (String cachedPermission : CACHE_ON_QUERY) {
                try {
                    Boolean result = Permissions.check(profile, cachedPermission, false).join();
                    ((IConnection) connection).vanillaPermissions$cachePermission(cachedPermission, result);
                } catch (Exception e) {
                    LOGGER.error("Failed to cache permission {} for {}", cachedPermission, profile.getName(), e);
                }

            }
        });
    }

    @SuppressWarnings("unchecked")
    private void alterCommandChildNode(CommandDispatcher<CommandSourceStack> dispatcher, CommandNode<CommandSourceStack> commandNode) {
        var name = build(dispatcher.getPath(commandNode).toArray(new String[]{}));
        LOGGER.debug("Alter command node {}", name);
        for (CommandNode<CommandSourceStack> child : commandNode.getChildren()) {
            alterCommandChildNode(dispatcher, child);
        }
        ((CommandNodeAccessor<CommandSourceStack>) commandNode).setRequirement(createPredicate(name, commandNode.getRequirement()));
    }

    private Predicate<CommandSourceStack> createPredicate(String name, Predicate<CommandSourceStack> fallback) {
        return source -> {
            try {
                TriState triState = Permissions.getPermissionValue(source, Permission.COMMAND.formatted(name));
                return triState.orElseGet(() -> fallback.test(source));
            } catch (Throwable ignored) {
                // Fallback if permission check failed
                return fallback.test(source);
            }
        };
    }

}
