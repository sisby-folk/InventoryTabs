package folk.sisby.inventory_tabs.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.LockableContainerBlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class MixinBlockEntity {
    @Inject(method = "toSyncedNbt", at = @At("RETURN"))
    public void sendCustomNames(CallbackInfoReturnable<NbtCompound> cir) {
        if (((BlockEntity) (Object) this) instanceof LockableContainerBlockEntity lcbe) {
            cir.getReturnValue().putString("CustomName", Text.Serializer.toJson(lcbe.getCustomName()));
        }
    }
}
