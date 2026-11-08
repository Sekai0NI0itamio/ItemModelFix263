package itamio.itemmodelfix.mixin;

import itamio.itemmodelfix.config.Config;
import itamio.itemmodelfix.model.ItemModelGenerationType;
import itamio.itemmodelfix.model.ItemModelUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    /**
     * Vanilla shrinks generated UVs in FaceBakery.bakeQuad() by lerping toward
     * the center with TextureAtlasSprite.uvShrinkRatio(). That ratio lives only
     * on the atlas sprite, while processFrames receives SpriteContents, so we
     * capture the sprite for each SpriteContents during generateBlockModel
     * (which synchronously calls processFrames on the same thread).
     */
    private static final ThreadLocal<Map<SpriteContents, Float>> DELTAS = ThreadLocal.withInitial(HashMap::new);

    @Inject(method = "generateBlockModel", at = @At("HEAD"))
    private void onGenerateHead(Function<Material, TextureAtlasSprite> spriteGetter, BlockModel model, CallbackInfoReturnable<BlockModel> cir) {
        DELTAS.get().clear();
    }

    @ModifyVariable(method = "generateBlockModel", at = @At("HEAD"), argsOnly = true)
    private Function<Material, TextureAtlasSprite> wrapSpriteGetter(Function<Material, TextureAtlasSprite> original) {
        return material -> {
            TextureAtlasSprite sprite = original.apply(material);
            DELTAS.get().put(sprite.contents(), sprite.uvShrinkRatio());
            return sprite;
        };
    }

    @Inject(method = "generateBlockModel", at = @At("RETURN"))
    private void onGenerateReturn(Function<Material, TextureAtlasSprite> spriteGetter, BlockModel model, CallbackInfoReturnable<BlockModel> cir) {
        DELTAS.remove();
    }

    private static float frameDelta(SpriteContents sprite) {
        Float delta = DELTAS.get().get(sprite);
        return delta != null ? delta : 0.0F;
    }

    @Inject(at = @At("HEAD"), method = "processFrames(ILjava/lang/String;Lnet/minecraft/client/renderer/texture/SpriteContents;)Ljava/util/List;", cancellable = true)
    private void onHeadProcessFrames(int layer, String key, SpriteContents sprite, CallbackInfoReturnable<List<BlockElement>> cir) {
        ItemModelGenerationType type = Config.get().generationType;
        if (type == ItemModelGenerationType.OUTLINE) {
            cir.setReturnValue(ItemModelUtil.createOutlineLayerElements(layer, key, sprite, frameDelta(sprite)));
        } else if (type == ItemModelGenerationType.PIXEL) {
            cir.setReturnValue(ItemModelUtil.createPixelLayerElements(layer, key, sprite));
        }
    }

    @Inject(at = @At("RETURN"), method = "processFrames(ILjava/lang/String;Lnet/minecraft/client/renderer/texture/SpriteContents;)Ljava/util/List;")
    private void onReturnProcessFrames(int layer, String key, SpriteContents sprite, CallbackInfoReturnable<List<BlockElement>> cir) {
        if (Config.get().generationType == ItemModelGenerationType.UNLERP) {
            ItemModelUtil.unlerpElements(cir.getReturnValue(), frameDelta(sprite));
        }
    }
}
