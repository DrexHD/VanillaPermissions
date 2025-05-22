package me.drex.vanillapermissions.mixin.cached_permission;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerLoginPacketListenerImpl.class)
public interface ServerLoginPacketListenerImplAccessor {
    //? if >= 1.20.2 {
    @Accessor
    //?} else {
    /*@Accessor("gameProfile")
    *///?}
    GameProfile getAuthenticatedProfile();
}
