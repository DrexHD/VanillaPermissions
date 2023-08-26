package me.drex.vanillapermissions;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.mixin.CommandNodeAccessor;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Predicate;

import static me.drex.vanillapermissions.util.Permission.build;

public class VanillaPermissionsMod implements DedicatedServerModInitializer {

    public static final String MOD_ID = "vanilla-permissions";
    private static final Logger LOGGER = LogManager.getLogger();
    public static final ResourceLocation MODIFY_VANILLA_PERMISSIONS_PHASE = new ResourceLocation(MOD_ID, "modify_vanilla_permissions");

    @Override
    public void onInitializeServer() {
        CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
        CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, registryAccess, environment) -> {
            for (CommandNode<CommandSourceStack> node : dispatcher.getRoot().getChildren()) {
                alterCommandChildNode(dispatcher, node);
            }
            LOGGER.info("Loaded Fabric Permissions");
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
