package me.drex.vanillapermissions.mc118;

import me.drex.vanillapermissions.event.CommandCallback;
import me.drex.vanillapermissions.VanillaPermissionsMod;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;

public class VanillaPermissionsMod1_18 implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        if (VanillaPermissionsMod.isMinecraftVersionPresent("~1.18")) {
            CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
            CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, dedicated) -> CommandCallback.EVENT.invoker().register(dispatcher));
        }
    }

}
