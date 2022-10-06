package me.drex.vanillapermissions.mc119;

import com.mojang.brigadier.CommandDispatcher;
import me.drex.vanillapermissions.event.CommandCallback;
import me.drex.vanillapermissions.VanillaPermissionsMod;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.SharedSuggestionProvider;

import java.util.Arrays;
import java.util.List;

public class VanillaPermissionsMod1_19 implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        if (VanillaPermissionsMod.isMinecraftVersionPresent("~1.19")) {
            CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
            CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, registryAccess, environment) -> CommandCallback.EVENT.invoker().register(dispatcher));
        }
    }

    public static boolean hasCommandPermission(String command) {
        return hasCommandPermission(Arrays.asList(command.split(" ")));
    }

    public static boolean hasCommandPermission(List<String> nodes) {
        assert Minecraft.getInstance().player != null;
        CommandDispatcher<SharedSuggestionProvider> dispatcher = Minecraft.getInstance().player.connection.getCommands();
        return dispatcher.findNode(nodes) != null;
    }

}
