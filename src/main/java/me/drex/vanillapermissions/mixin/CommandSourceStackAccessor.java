package me.drex.vanillapermissions.mixin;

import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(CommandSourceStack.class)
public interface CommandSourceStackAccessor {

    @Accessor
    int getPermissionLevel();

    @Accessor
    CommandSource getSource();

    @Accessor
    @Mutable
    void setSilent(boolean silent);

}
