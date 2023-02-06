package me.drex.vanillapermissions.mixin;

import com.google.common.collect.Lists;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import me.drex.vanillapermissions.event.ModifyExecuteCommand;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static com.mojang.brigadier.arguments.IntegerArgumentType.*;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.EntityArgument.entities;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Inject(method = "<clinit>", at = @At("HEAD"))
    private static void vanillaPermissions_modifyExecuteCommand(CallbackInfo ci) {
        ModifyExecuteCommand.ADD_CONDITIONAL.register((node, argumentBuilder, positive) -> {
            argumentBuilder.then(
                    literal("permission")
                            .then(
                                    argument("entity", EntityArgument.entity())
                                            .then(
                                                    addConditional(
                                                            node,
                                                            argument("permission", StringArgumentType.word()),
                                                            positive,
                                                            context -> Permissions.check(EntityArgument.getEntity(context, "entity"), StringArgumentType.getString(context, "permission"))
                                                    )
                                            )
                            )
            );
        });
        ModifyExecuteCommand.ADD_MODIFIER.register((instance, literalCommandNode) -> {
            instance.then(
                    literal("feedback").then(
                            literal("entity").then(
                                    argument("targets", entities()).fork(literalCommandNode, context -> {
                                        List<CommandSourceStack> list = Lists.newArrayList();
                                        ((CommandSourceStackAccessor) context.getSource()).setSilent(false);
                                        for (Entity entity : EntityArgument.getOptionalEntities(context, "targets")) {
                                            list.add(context.getSource().withSource(entity));
                                        }
                                        return list;
                                    })
                            )
                    ).then(
                            literal("silent").redirect(literalCommandNode, context -> context.getSource().withSuppressedOutput())
                    ).then(
                            literal("console").redirect(literalCommandNode, context -> {
                                ((CommandSourceStackAccessor) context.getSource()).setSilent(false);
                                return context.getSource().withSource(context.getSource().getServer());
                            })
                    )
            ).then(
                    literal("oplevel").then(
                            literal("entity").then(
                                    argument("targets", entities()).fork(literalCommandNode, context -> {
                                        List<CommandSourceStack> list = Lists.newArrayList();
                                        int originalPermissionLevel = ((CommandSourceStackAccessor) context.getSource()).getPermissionLevel();
                                        for (Entity entity : EntityArgument.getOptionalEntities(context, "targets")) {
                                            int newPermissionLevel = ((EntityAccessor) entity).invokeGetPermissionLevel();
                                            list.add(context.getSource().withPermission(Math.min(originalPermissionLevel, newPermissionLevel)));
                                        }
                                        return list;
                                    })
                            )
                    ).then(
                            argument("level", integer(0, 4)).redirect(literalCommandNode, context -> {
                                        int originalPermissionLevel = ((CommandSourceStackAccessor) context.getSource()).getPermissionLevel();
                                        int newPermissionLevel = getInteger(context, "level");
                                        return context.getSource().withPermission(Math.min(originalPermissionLevel, newPermissionLevel));
                                    }
                            )
                    )
            );
        });
    }

    @Shadow
    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> builder, boolean positive, ExecuteCommand.CommandPredicate condition) {
        return null;
    }


}
