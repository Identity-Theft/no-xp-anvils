package identitytheft.noxpanvils.mixin;

import identitytheft.noxpanvils.config.Config;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin extends ItemCombinerMenu {
	@Shadow @Final private DataSlot cost;

	public AnvilMenuMixin(final @Nullable MenuType<?> menuType, final int containerId, final Inventory inventory, final ContainerLevelAccess access, final ItemCombinerMenuSlotDefinition itemInputSlots) {
		super(menuType, containerId, inventory, access, itemInputSlots);
	}

	@Unique
	private boolean CheckConfig()
	{
		var firstStack = this.inputSlots.getItem(0);
		var secondStack = this.inputSlots.getItem(1);

		var firstItem = this.inputSlots.getItem(0).getItem();
		var secondItem = this.inputSlots.getItem(1).getItem();

        return firstItem != Items.AIR && (secondItem != Items.AIR || Config.HANDLER.instance().rename)
				&& (!firstStack.isValidRepairItem(secondStack) || Config.HANDLER.instance().repair)
				&& (firstItem == Items.ENCHANTED_BOOK || secondItem != Items.ENCHANTED_BOOK || Config.HANDLER.instance().enchant)
				&& (firstItem != Items.ENCHANTED_BOOK || secondItem != Items.ENCHANTED_BOOK || Config.HANDLER.instance().combineBooks)
				&& (firstItem != secondItem || Config.HANDLER.instance().combineGear);
    }

	@Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$mayPickup(Player player, boolean present, CallbackInfoReturnable<Boolean> cir) {
		if (CheckConfig()) cir.setReturnValue(true);
	}
	@Inject(method = "onTake", at = @At("HEAD"))
	private void noxpanvils$onTake(Player player, ItemStack stack, CallbackInfo ci) {
		if (CheckConfig()) cost.set(0);
	}

	@Inject(method = "createResult", at = @At("TAIL"))
	private void noxpanvils$updateResult(CallbackInfo ci) {
		if (CheckConfig()) cost.set(0);
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;getOrDefault(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> T noxpanvils$getOrDefault(ItemStack instance, DataComponentType<T> dataComponentType, T o) {
		if (dataComponentType == DataComponents.REPAIR_COST && CheckConfig()) return o;
		return instance.getOrDefault(dataComponentType, o);
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/DataSlot;get()I"))
	private int noxpanvils$getItemLevelCost(DataSlot instance) {
		if (CheckConfig()) return 0;
		return instance.get();
	}

	@Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;set(Lnet/minecraft/core/component/DataComponentType;Ljava/lang/Object;)Ljava/lang/Object;"))
	private <T> T noxpanvils$setRepairCost(ItemStack instance, DataComponentType<T> type, T value) {
		if (type == DataComponents.REPAIR_COST && CheckConfig()) return null;
		return instance.set(type, value);
	}

	@Inject(method = "getCost", at = @At("HEAD"), cancellable = true)
	private void noxpanvils$getCost(CallbackInfoReturnable<Integer> cir) {
		if (CheckConfig()) cir.setReturnValue(0);
	}
}