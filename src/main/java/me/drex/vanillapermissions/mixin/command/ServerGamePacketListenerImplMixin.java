package me.drex.vanillapermissions.mixin.command;

//? if >= 1.21.6 {

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChangeGameModePacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
            target = "Lnet/minecraft/server/level/ServerPlayer;hasPermissions(I)Z"
        )
    )
    public boolean changeGameModePermissionCheck(
        ServerPlayer instance, int i, @Local(argsOnly = true) ServerboundChangeGameModePacket packet
    ) {
        Commands commands = instance.getServer().getCommands();
        ParseResults<CommandSourceStack> parseResults = commands.getDispatcher().parse("gamemode " + packet.mode().getName(), player.createCommandSourceStack());
        CommandSyntaxException exception = Commands.getParseException(parseResults);
        return exception == null;
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
}
//?} else {

/*import org.spongepowered.asm.mixin.Mixin;
import net.minecraft.server.MinecraftServer;

@Mixin(MinecraftServer.class)
public abstract class ServerGamePacketListenerImplMixin {

}
*///?}