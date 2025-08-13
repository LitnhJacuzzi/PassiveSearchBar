package team.dovecotmc.passivesearchbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.ParentElement;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.TextFieldWidget;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin implements ParentElement {
	@Shadow
	private TextFieldWidget searchBox;
	
	@Redirect(method = "setSelectedTab", at = @At(value = "INVOKE", target = 
			"Lnet/minecraft/client/gui/widget/TextFieldWidget;setFocusUnlocked(Z)V"))
	private void passivesearchbar$disableFocusLocked(TextFieldWidget searchBar, boolean focusUnlocked) {
		searchBar.setFocusUnlocked(true);
	}
	
	@Redirect(method = "setSelectedTab", at = @At(value = "INVOKE", target = 
			"Lnet/minecraft/client/gui/widget/TextFieldWidget;method_25365(Z)V"))
	private void passivesearchbar$disableInitialFocus(TextFieldWidget searchBar, boolean focused) {
		setFocused(null);
	}
	
	@Inject(method = "mouseClicked", at = @At("RETURN"))
	private void passivesearchbar$stealSearchBarFocus(
			double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if((getFocused() == searchBox) && (mouseX < searchBox.getX() || mouseX >= (searchBox.getX() + searchBox.getWidth()) || 
				mouseY < searchBox.getY() || mouseY >= (searchBox.getY() + searchBox.getHeight()))) {
			setFocused(null);
		}
	}
}
