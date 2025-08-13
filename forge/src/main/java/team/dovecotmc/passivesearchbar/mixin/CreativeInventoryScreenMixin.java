package team.dovecotmc.passivesearchbar.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin implements ContainerEventHandler {
	@Shadow
	private EditBox searchBox;
	
	@Redirect(method = "selectTab", at = @At(value = "INVOKE", target = 
			"Lnet/minecraft/client/gui/components/EditBox;setCanLoseFocus(Z)V"))
	private void passivesearchbar$disableFocusLocked(EditBox searchBar, boolean focusUnlocked) {
		searchBar.setCanLoseFocus(true);
	}
	
	@Redirect(method = "selectTab", at = @At(value = "INVOKE", target = 
			"Lnet/minecraft/client/gui/components/EditBox;setFocused(Z)V"))
	private void passivesearchbar$disableInitialFocus(EditBox searchBar, boolean focused) {
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
