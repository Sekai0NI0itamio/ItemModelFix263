package itamio.itemmodelfix.mixin;

import itamio.itemmodelfix.ItemModelFix;
import itamio.itemmodelfix.config.Config;
import itamio.itemmodelfix.model.ItemModelGenerationType;
import itamio.itemmodelfix.model.ItemModelUtil;
import java.util.List;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.texture.Sprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	@Inject(at = @At("HEAD"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;", cancellable = true)
	private void onHeadAddLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		ItemModelGenerationType type = Config.get().generationType;
		if (type == ItemModelGenerationType.OUTLINE) {
			cir.setReturnValue(ItemModelUtil.createOutlineLayerElements(layer, key, sprite));
		} else if (type == ItemModelGenerationType.PIXEL) {
			cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
		}
	}

	@Inject(at = @At("RETURN"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/Sprite;)Ljava/util/List;")
	private void onReturnAddLayerElements(int layer, String key, Sprite sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		if (Config.get().generationType == ItemModelGenerationType.UNLERP) {
			ItemModelUtil.unlerpElements(cir.getReturnValue(), sprite.getAnimationFrameDelta());
		}
	}
}
