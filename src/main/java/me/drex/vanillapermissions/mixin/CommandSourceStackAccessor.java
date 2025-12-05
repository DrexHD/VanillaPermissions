package me.drex.vanillapermissions.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
//? if > 1.21.10 {
import net.minecraft.server.permissions.PermissionSet;
//? }
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CommandSourceStack.class)
public interface CommandSourceStackAccessor {

    //? if > 1.21.10 {
    @Invoker
    PermissionSet invokePermissions();
    //? } else {
    /*@Accessor
    int getPermissionLevel();
    *///? }


    @Accessor
    CommandSource getSource();

    @Accessor
    @Mutable
    void setSilent(boolean silent);

}
