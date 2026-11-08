package itamio.itemmodelfix.mixin;

import itamio.itemmodelfix.config.Config;
import itamio.itemmodelfix.model.ItemModelGenerationType;
import itamio.itemmodelfix.model.ItemModelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.render.model.json.ItemModelGenerator;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelElement;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteContents;
import net.minecraft.client.util.SpriteIdentifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
	/**
	 * Vanilla shrinks generated UVs in BakedQuadFactory.bake() by lerping toward
	 * the center with Sprite.getAnimationFrameDelta(). That delta lives only on
	 * the atlas Sprite, while addLayerElements receives SpriteContents, so we
	 * capture the Sprite for each SpriteContents during create() (which
	 * synchronously calls addLayerElements on the same thread).
	 */
	private static final ThreadLocal<Map<SpriteContents, Float>> DELTAS = ThreadLocal.withInitial(HashMap::new);

	@Inject(method = "create", at = @At("HEAD"))
	private void onCreateHead(Function<SpriteIdentifier, Sprite> textureGetter, JsonUnbakedModel blockModel, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		DELTAS.get().clear();
	}

	@ModifyVariable(method = "create", at = @At("HEAD"), argsOnly = true)
	private Function<SpriteIdentifier, Sprite> wrapTextureGetter(Function<SpriteIdentifier, Sprite> original) {
		return id -> {
			Sprite sprite = original.apply(id);
			DELTAS.get().put(sprite.getContents(), sprite.getAnimationFrameDelta());
			return sprite;
		};
	}

	@Inject(method = "create", at = @At("RETURN"))
	private void onCreateReturn(Function<SpriteIdentifier, Sprite> textureGetter, JsonUnbakedModel blockModel, CallbackInfoReturnable<JsonUnbakedModel> cir) {
		DELTAS.remove();
	}

	private static float frameDelta(SpriteContents sprite) {
		Float delta = DELTAS.get().get(sprite);
		return delta != null ? delta : 0.0F;
	}

	@Inject(at = @At("HEAD"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/SpriteContents;)Ljava/util/List;", cancellable = true)
	private void onHeadAddLayerElements(int layer, String key, SpriteContents sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		ItemModelGenerationType type = Config.get().generationType;
		if (type == ItemModelGenerationType.OUTLINE) {
			cir.setReturnValue(ItemModelUtil.createOutlineLayerElements(layer, key, sprite, frameDelta(sprite)));
		} else if (type == ItemModelGenerationType.PIXEL) {
			cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
		}
	}

	@Inject(at = @At("RETURN"), method = "addLayerElements(ILjava/lang/String;Lnet/minecraft/client/texture/SpriteContents;)Ljava/util/List;")
	private void onReturnAddLayerElements(int layer, String key, SpriteContents sprite, CallbackInfoReturnable<List<ModelElement>> cir) {
		if (Config.get().generationType == ItemModelGenerationType.UNLERP) {
			ItemModelUtil.unlerpElements(cir.getReturnValue(), frameDelta(sprite));
		}
	}
}
