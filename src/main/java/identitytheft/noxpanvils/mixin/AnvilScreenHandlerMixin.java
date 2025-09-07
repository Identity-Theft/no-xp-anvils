package identitytheft.noxpanvils.mixin;

import identitytheft.noxpanvils.NoXpAnvilsConfig;
import net.minecraft.component.ComponentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.*;
import net.minecraft.screen.slot.ForgingSlotsManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilScreenHandler.class)
public abstract class AnvilScreenHandlerMixin extends ForgingScreenHandler {
	@Shadow @Final private Property levelCost;

	public AnvilScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
		super(type, syncId, playerInventory, context);
	}

	@Shadow
	protected abstract ForgingSlotsManager getForgingSlotsManager();

	@Unique
	private boolean CheckConfig()
	{
		var firstStack = this.input.getStack(0);
		var secondStack = this.input.getStack(1);

		var firstItem = this.input.getStack(0).getItem();
		var secondItem = this.input.getStack(1).getItem();

		if (firstItem == Items.AIR) return false;

		if (secondItem == Items.AIR && NoXpAnvilsConfig.rename) return false;
		if (firstItem.canRepair(firstStack, secondStack) && NoXpAnvilsConfig.repair) return false;
		if (firstItem != Items.ENCHANTED_BOOK && secondItem == Items.ENCHANTED_BOOK && NoXpAnvilsConfig.enchant) return false;
		if (firstItem == Items.ENCHANTED_BOOK && secondItem == Items.ENCHANTED_BOOK && NoXpAnvilsConfig.combineBooks) return false;
		if (firstItem == secondItem && NoXpAnvilsConfig.combineGear) return false;

		return true;
	}

	@Inject(method = "canTakeOutput", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$canTakeOutput(PlayerEntity player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (CheckConfig()) cir.setReturnValue(true);
	}
	@Inject(method = "onTakeOutput", at = @At("HEAD"))
	private void noxpanvils$onTakeOutput(PlayerEntity player, ItemStack stack, CallbackInfo ci) {
		if (CheckConfig()) levelCost.set(0);
	}

	@Inject(method = "updateResult", at = @At("TAIL"))
	private void noxpanvils$updateResult(CallbackInfo ci) {
		if (CheckConfig()) levelCost.set(0);
	}

	@Redirect(method = {"updateResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getOrDefault(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private Object noxpanvils$getOrDefault(ItemStack instance, ComponentType componentType, Object o) {
		if (CheckConfig()) return 0;
		return instance.getOrDefault(componentType, o);
	}

	@Redirect(method = {"updateResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/Property;get()I"))
	private int noxpanvils$getItemLevelCost(Property instance) {
		if (CheckConfig()) return 0;
		return instance.get();
	}

	@Redirect(method = {"updateResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;set(Lnet/minecraft/component/ComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> T noxpanvils$setRepairCost(ItemStack instance, ComponentType<? super T> type, T value) {
		if (CheckConfig()) return null;
		return instance.set(type, value);
	}

    @ModifyConstant(method = "updateResult", constant = @Constant(intValue = 40, ordinal = 2))
    private int noxpanvils$maxValue(int input) {
		if (CheckConfig()) return Integer.MAX_VALUE;
		return 40;
    }

	@Inject(method = "getLevelCost", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$getLevelCost(CallbackInfoReturnable<Integer> cir) {
		if (CheckConfig()) cir.setReturnValue(0);
	}
}