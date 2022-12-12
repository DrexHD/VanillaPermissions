package me.drex.vanillapermissions.mc114;

import me.drex.vanillapermissions.VanillaPermissionsMod;
import me.drex.vanillapermissions.event.CommandCallback;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;

public class VanillaPermissionsMod1_14 implements DedicatedServerModInitializer {

    @Override
    public void onInitializeServer() {
        if (VanillaPermissionsMod.isMinecraftVersionPresent("~1.14")) {
            CommandRegistrationCallback.EVENT.addPhaseOrdering(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, Event.DEFAULT_PHASE);
            CommandRegistrationCallback.EVENT.register(VanillaPermissionsMod.MODIFY_VANILLA_PERMISSIONS_PHASE, (dispatcher, dedicated) -> CommandCallback.EVENT.invoker().register(dispatcher));
        }
    }

}
