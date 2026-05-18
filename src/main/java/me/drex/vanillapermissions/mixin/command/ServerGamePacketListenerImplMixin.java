package me.drex.vanillapermissions.mixin.command;

//? if >= 1.21.6 {

import static net.minecraft.commands.Commands.getParseException;

import java.util.function.Predicate;
import java.util.stream.Stream;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
//? if > 1.21.10 {
import net.minecraft.server.permissions.PermissionCheck;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;
//? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow
    public ServerPlayer player;

    @Redirect(
        method = "handleChangeGameMode",
        at = @At(
            value = "INVOKE",
            //? if > 1.21.10 {
            target = "Lnet/minecraft/server/permissions/PermissionCheck;check(Lnet/minecraft/server/permissions/PermissionSet;)Z"
            //? } else {
            /*target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
            *///? }
        )
    )
    public boolean changeGameModePermissionCheck(
        //? if > 1.21.10 {
        PermissionCheck instance, PermissionSet permissionSet,
        //? } else {
        /*ServerPlayer instance, int i,
        *///? }
        @Local(argsOnly = true) ServerboundChangeGameModePacket packet
    ) {
        return checkPermission("gamemode " + packet.mode().getName());
    }

    @Inject(
        method = "handleChangeGameMode",
        at = @At(
            value = "INVOKE",
            target = "Lorg/slf4j/Logger;warn(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"
        )
    )
    public void sendFeedback(ServerboundChangeGameModePacket packet, CallbackInfo ci) {
        this.player.sendSystemMessage(Component.translatable("commands.help.failed").withStyle(ChatFormatting.RED));
    }

    //? if > 1.21.11 {
    @ModifyExpressionValue(
        method = "sendGameRuleValues",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"
        )
    )
    public boolean readGameRulePermissionCheck(boolean original) {
        return true;
    }

    @ModifyExpressionValue(
        method = "sendGameRuleValues",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/gamerules/GameRules;availableRules()Ljava/util/stream/Stream;"
        )
    )
    public <T> Stream<GameRule<T>> readGameRuleFilter(Stream<GameRule<T>> rules) {
        return rules.filter(rule -> checkPermission("gamerule " + rule));
    }

    @ModifyExpressionValue(
        method = "handleSetGameRule",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/permissions/PermissionSet;hasPermission(Lnet/minecraft/server/permissions/Permission;)Z"
        )
    )
    public boolean changeGameRulePermissionCheck(boolean original) {
        return true;
    }

    @WrapOperation(
        method = "handleSetGameRule",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;setGameRuleValue(Lnet/minecraft/world/level/gamerules/GameRules;Lnet/minecraft/world/level/gamerules/GameRule;Ljava/lang/String;)V"
        )
    )
    public void changeGameRuleFilter(
        ServerGamePacketListenerImpl instance, GameRules rules, GameRule<?> rule, String value, Operation<Void> original
    ) {
        if (checkPermission("gamerule " + rule + " " + value)) {
            original.call(instance, rules, rule, value);
        }
    }

    @ModifyArg(
        method = "broadcastGameRuleChangeToOperators",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"
        )
    )
    public Predicate<ServerPlayer> addAdminBroadcastReceivePermission(Predicate<ServerPlayer> original) {
        return player -> Permissions.getPermissionValue(player, Permission.ADMIN_BROADCAST_RECEIVE)
            .orElseGet(() -> original.test(player));
    }
    //? }

    @Unique
    private boolean checkPermission(String command) {
        var parsed = player.level().getServer().getCommands().getDispatcher()
            .parse(command, player.createCommandSourceStack());
        return getParseException(parsed) == null;
    }
}
//?} else {

/*import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class ServerGamePacketListenerImplMixin {

}
*///?}
