package identitytheft.noxpanvils.mixin;

import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
	@Redirect(method = {"drawForeground"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/text/Text;translatable(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/text/MutableText;"))
	private MutableText noxpanvils$costText(String key, Object[] args) {
		return null;
	}
}