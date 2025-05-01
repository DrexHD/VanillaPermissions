package me.drex.vanillapermissions.mixin.cached_permission.bypass.whitelist;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import me.drex.vanillapermissions.util.Arguments;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.net.SocketAddress;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginPacketListenerImplMixin {

    @Shadow
    @Final
    Connection connection;

    @WrapOperation(
        method = "verifyLoginAndFinishConnectionSetup",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/players/PlayerList;canPlayerLogin(Ljava/net/SocketAddress;Lcom/mojang/authlib/GameProfile;)Lnet/minecraft/network/chat/Component;"
        )
    )
    public Component addConnectionArgument(PlayerList instance, SocketAddress socketAddress, GameProfile gameProfile, Operation<Component> original) {
        Connection previous = Arguments.CONNECTION.get();
        Arguments.CONNECTION.set(this.connection);
        try {
            return original.call(instance, socketAddress, gameProfile);
        } finally {
            Arguments.CONNECTION.set(previous);
        }
    }
}
