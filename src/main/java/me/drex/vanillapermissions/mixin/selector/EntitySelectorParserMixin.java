package me.drex.vanillapermissions.mixin.selector;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.drex.vanillapermissions.util.Permission;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntitySelectorParser.class)
public abstract class EntitySelectorParserMixin {

    @ModifyExpressionValue(
        method = "allowSelectors",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/commands/SharedSuggestionProvider;hasPermission(I)Z"
        )
    )
    private static <S> boolean addSelectorPermission(boolean original, S object) {
        if (object instanceof CommandSourceStack source) {
            return Permissions.check(source, Permission.SELECTOR, original);
        }
        return original;
    }

}
