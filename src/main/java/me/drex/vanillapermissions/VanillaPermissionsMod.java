package me.drex.vanillapermissions;

import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.mixin.CommandNodeAccessor;
import me.drex.vanillapermissions.util.JoinCache;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

import static me.drex.vanillapermissions.util.Permission.build;

public class VanillaPermissionsMod implements ModInitializer {

    public static final String MOD_ID = "vanilla-permissions";
    public static final ResourceLocation MODIFY_VANILLA_PERMISSIONS_PHASE = id("modify_vanilla_permissions");
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
        CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, registryAccess, environment) -> {
            for (CommandNode<CommandSourceStack> node : dispatcher.getRoot().getChildren()) {
                alterCommandChildNode(node.getName(), node);
            }
            LOGGER.info("Loaded Fabric Permissions");
        });
        JoinCache.initialize();
    }

    @SuppressWarnings("unchecked")
    private void alterCommandChildNode(String path, CommandNode<CommandSourceStack> commandNode) {
        LOGGER.debug("Alter command node {}", path);
        for (CommandNode<CommandSourceStack> child : commandNode.getChildren()) {
            alterCommandChildNode(build(path, child.getName()), child);
        }
        ((CommandNodeAccessor<CommandSourceStack>) commandNode).setRequirement(createPredicate(path, commandNode.getRequirement()));
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

    public static ResourceLocation id(String path) {
        //? if >= 1.21 {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
        //?} else {
        /*return new ResourceLocation(MOD_ID, path);
        *///?}
    }

}
