package me.drex.vanillapermissions.mixin.selector.argument;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ScoreHolderArgument;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collection;
import java.util.function.Supplier;

import static me.drex.vanillapermissions.util.ArgumentPermission.validate;

@Mixin(ScoreHolderArgument.class)
public abstract class ScoreHolderArgumentMixin {

    @Inject(
        method = "getNames(Lcom/mojang/brigadier/context/CommandContext;Ljava/lang/String;Ljava/util/function/Supplier;)Ljava/util/Collection;",
        at = @At("RETURN")
    )
    private static void addScoreHoldersPermission(CommandContext<CommandSourceStack> commandContext, String string, Supplier<Collection<?>> supplier, CallbackInfoReturnable<Collection<?>> cir) throws CommandSyntaxException {
        validate(commandContext, string, cir.getReturnValue());
    }
}
