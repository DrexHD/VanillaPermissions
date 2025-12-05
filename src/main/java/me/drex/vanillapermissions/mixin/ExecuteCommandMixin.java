package me.drex.vanillapermissions.mixin;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import me.drex.vanillapermissions.event.ModifyExecuteCommand;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.commands.ExecuteCommand;
import net.minecraft.server.level.ServerPlayer;
//? if > 1.21.10 {
import me.drex.vanillapermissions.util.MinimumPermissionSet;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.permissions.PermissionSet;
//? }
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

import static com.mojang.brigadier.arguments.IntegerArgumentType.getInteger;
import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;
import static net.minecraft.commands.arguments.EntityArgument.entities;

@Mixin(ExecuteCommand.class)
public abstract class ExecuteCommandMixin {

    @Inject(
        method = "addConditionals",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            remap = false,
            ordinal = 0
        )
    )
    private static void addPermissionConditionArgument(
        CommandNode<CommandSourceStack> node,
        LiteralArgumentBuilder<CommandSourceStack> argumentBuilder,
        boolean positive,
        CommandBuildContext buildContext,
        CallbackInfoReturnable<ArgumentBuilder<CommandSourceStack, ?>> cir
    ) {
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
    }

    @WrapOperation(
        method = "register",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;then(Lcom/mojang/brigadier/builder/ArgumentBuilder;)Lcom/mojang/brigadier/builder/ArgumentBuilder;",
            remap = false,
            ordinal = 0
        )
    )
    private static ArgumentBuilder<CommandSourceStack, ?> addModifierArguments(LiteralArgumentBuilder<CommandSourceStack> instance, ArgumentBuilder<CommandSourceStack, ?> argumentBuilder, Operation<ArgumentBuilder<CommandSourceStack, ?>> original, @Local LiteralCommandNode<CommandSourceStack> root) {
        ModifyExecuteCommand.ADD_MODIFIER.invoker().addModifiers(instance, root);
        instance.then(
            literal("feedback").then(
                literal("entity").then(
                    argument("targets", entities()).fork(root, context -> {
                        List<CommandSourceStack> list = Lists.newArrayList();
                        ((CommandSourceStackAccessor) context.getSource()).setSilent(false);
                        for (Entity entity : EntityArgument.getOptionalEntities(context, "targets")) {
                            //? if >= 1.21.2 {
                            CommandSource source = CommandSource.NULL;
                            if (entity instanceof ServerPlayer player) {
                                source = player.commandSource();
                            }
                            list.add(context.getSource().withSource(source));
                            //?} else {
                            /*list.add(context.getSource().withSource(entity));
                             *///?}
                        }
                        return list;
                    })
                )
            ).then(
                literal("silent").redirect(root, context -> context.getSource().withSuppressedOutput())
            ).then(
                literal("console").redirect(root, context -> {
                    ((CommandSourceStackAccessor) context.getSource()).setSilent(false);
                    return context.getSource().withSource(context.getSource().getServer());
                })
            )
        ).then(
            literal("oplevel").then(
                literal("entity").then(
                    argument("targets", entities()).fork(root, context -> {
                        List<CommandSourceStack> list = Lists.newArrayList();
                        for (Entity entity : EntityArgument.getOptionalEntities(context, "targets")) {
                            //? if > 1.21.10 {
                            PermissionSet permissionSet = ((CommandSourceStackAccessor) context.getSource()).invokePermissions();
                            if (entity instanceof ServerPlayerAccessor accessor) {
                                permissionSet = MinimumPermissionSet.of(permissionSet, accessor.invokePermissions());
                            }
                            list.add(context.getSource().withPermission(permissionSet));
                            //? } else {
                            /*int originalPermissionLevel = ((CommandSourceStackAccessor) context.getSource()).getPermissionLevel();
                            int newPermissionLevel = 0;
                            if (entity instanceof ServerPlayerAccessor accessor) {
                                newPermissionLevel = accessor.invokeGetPermissionLevel();
                            }
                            int level = Math.min(originalPermissionLevel, newPermissionLevel);
                            list.add(context.getSource().withPermission(level));
                            *///? }
                        }
                        return list;
                    })
                )
            ).then(
                argument("level", integer(0, 4)).redirect(root, context -> {
                        int newPermissionLevel = getInteger(context, "level");
                        //? if > 1.21.10 {
                        PermissionSet permissionSet = ((CommandSourceStackAccessor) context.getSource()).invokePermissions();
                        return context.getSource().withPermission(MinimumPermissionSet.of(permissionSet, LevelBasedPermissionSet.forLevel(PermissionLevel.byId(newPermissionLevel))));
                        //? } else {
                        /*int originalPermissionLevel = ((CommandSourceStackAccessor) context.getSource()).getPermissionLevel();
                        int level = Math.min(originalPermissionLevel, newPermissionLevel);
                        return context.getSource().withPermission(level);
                        *///? }
                    }
                )
            )
        );
        return original.call(instance, argumentBuilder);
    }

    @Shadow
    private static ArgumentBuilder<CommandSourceStack, ?> addConditional(CommandNode<CommandSourceStack> root, ArgumentBuilder<CommandSourceStack, ?> builder, boolean positive, ExecuteCommand.CommandPredicate condition) {
        return null;
    }


}
