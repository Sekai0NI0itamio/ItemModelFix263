package itamio.itemmodelfix.model;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import itamio.itemmodelfix.util.MathUtil;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.BlockElementFace;
import net.minecraft.client.renderer.block.model.BlockFaceUV;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

public class ItemModelUtil {
    public static void unlerpElements(List<BlockElement> elements, float delta) {
        for (BlockElement element : elements) {
            for (BlockElementFace face : element.faces.values()) {
                MathUtil.unlerpUVs(face.uv.uvs, delta);
            }
        }
    }

    public static List<BlockElement> createOutlineLayerElements(int layer, String key, SpriteContents sprite, float animationFrameDelta) {
        List<BlockElement> elements = new ArrayList<>();

        int width = sprite.width();
        int height = sprite.height();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        int[] frames = sprite.getUniqueFrames().toArray();

        Map<Direction, BlockElementFace> map = new EnumMap<>(Direction.class);
        map.put(Direction.SOUTH, new BlockElementFace(null, layer, key, createUnlerpedTexture(new float[] { 0.0F, 0.0F, 16.0F, 16.0F }, 0, animationFrameDelta)));
        map.put(Direction.NORTH, new BlockElementFace(null, layer, key, createUnlerpedTexture(new float[] { 16.0F, 0.0F, 0.0F, 16.0F }, 0, animationFrameDelta)));
        elements.add(new BlockElement(new Vector3f(0.0F, 0.0F, 7.5F), new Vector3f(16.0F, 16.0F, 8.5F), map, null, true));

        int first1 = -1;
        int first2 = -1;
        int last1 = -1;
        int last2 = -1;

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.DOWN)) {
                        if (first1 == -1) {
                            first1 = x;
                        }
                        last1 = x;
                    }
                    if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.UP)) {
                        if (first2 == -1) {
                            first2 = x;
                        }
                        last2 = x;
                    }
                } else {
                    if (first1 != -1) {
                        elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
                        first1 = -1;
                    }
                    if (first2 != -1) {
                        elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
                        first2 = -1;
                    }
                }
            }

            if (first1 != -1) {
                elements.add(createHorizontalOutlineElement(Direction.DOWN, layer, key, first1, last1, y, height, animationFrameDelta, xFactor, yFactor));
                first1 = -1;
            }
            if (first2 != -1) {
                elements.add(createHorizontalOutlineElement(Direction.UP, layer, key, first2, last2, y, height, animationFrameDelta, xFactor, yFactor));
                first2 = -1;
            }
        }

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.RIGHT)) {
                        if (first1 == -1) {
                            first1 = y;
                        }
                        last1 = y;
                    }
                    if (doesPixelHaveEdge(sprite, frames, x, y, PixelDirection.LEFT)) {
                        if (first2 == -1) {
                            first2 = y;
                        }
                        last2 = y;
                    }
                } else {
                    if (first1 != -1) {
                        elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
                        first1 = -1;
                    }
                    if (first2 != -1) {
                        elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
                        first2 = -1;
                    }
                }
            }

            if (first1 != -1) {
                elements.add(createVerticalOutlineElement(Direction.EAST, layer, key, first1, last1, x, height, animationFrameDelta, xFactor, yFactor));
                first1 = -1;
            }
            if (first2 != -1) {
                elements.add(createVerticalOutlineElement(Direction.WEST, layer, key, first2, last2, x, height, animationFrameDelta, xFactor, yFactor));
                first2 = -1;
            }
        }

        return elements;
    }

    public static BlockElement createHorizontalOutlineElement(Direction direction, int layer, String key, int start, int end, int y, int height, float animationFrameDelta, float xFactor, float yFactor) {
        Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new BlockElementFace(null, layer, key, createUnlerpedTexture(new float[] { start / xFactor, y / yFactor, (end + 1) / xFactor, (y + 1) / yFactor }, 0, animationFrameDelta)));
        return new BlockElement(new Vector3f(start / xFactor, (height - (y + 1)) / yFactor, 7.5F), new Vector3f((end + 1) / xFactor, (height - y) / yFactor, 8.5F), faces, null, true);
    }

    public static BlockElement createVerticalOutlineElement(Direction direction, int layer, String key, int start, int end, int x, int height, float animationFrameDelta, float xFactor, float yFactor) {
        Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
        faces.put(direction, new BlockElementFace(null, layer, key, createUnlerpedTexture(new float[] { (x + 1) / xFactor, start / yFactor, x / xFactor, (end + 1) / yFactor }, 0, animationFrameDelta)));
        return new BlockElement(new Vector3f(x / xFactor, (height - (end + 1)) / yFactor, 7.5F), new Vector3f((x + 1) / xFactor, (height - start) / yFactor, 8.5F), faces, null, true);
    }

    public static BlockFaceUV createUnlerpedTexture(float[] uvs, int rotation, float delta) {
        return new BlockFaceUV(MathUtil.unlerpUVs(uvs, delta), rotation);
    }

    public static List<BlockElement> createPixelLayerElements(int layer, String key, SpriteContents sprite) {
        List<BlockElement> elements = new ArrayList<>();

        int width = sprite.width();
        int height = sprite.height();
        float xFactor = width / 16.0F;
        float yFactor = height / 16.0F;
        int[] frames = sprite.getUniqueFrames().toArray();

        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (!isPixelAlwaysTransparent(sprite, frames, x, y)) {
                    Map<Direction, BlockElementFace> faces = new EnumMap<>(Direction.class);
                    BlockElementFace face = new BlockElementFace(null, layer, key, new BlockFaceUV(new float[] { x / xFactor, y / yFactor, (x + 1) / xFactor, (y + 1) / yFactor }, 0));
                    BlockElementFace flippedFace = new BlockElementFace(null, layer, key, new BlockFaceUV(new float[] { (x + 1) / xFactor, y / yFactor, x / xFactor, (y + 1) / yFactor }, 0));

                    faces.put(Direction.SOUTH, face);
                    faces.put(Direction.NORTH, flippedFace);
                    for (PixelDirection pixelDirection : PixelDirection.VALUES) {
                        if (doesPixelHaveEdge(sprite, frames, x, y, pixelDirection)) {
                            faces.put(pixelDirection.getDirection(), pixelDirection.isVertical() ? face : flippedFace);
                        }
                    }

                    elements.add(new BlockElement(new Vector3f(x / xFactor, (height - (y + 1)) / yFactor, 7.5F), new Vector3f((x + 1) / xFactor, (height - y) / yFactor, 8.5F), faces, null, true));
                }
            }
        }

        return elements;
    }

    public static boolean isPixelOutsideSprite(SpriteContents sprite, int x, int y) {
        return x < 0 || y < 0 || x >= sprite.width() || y >= sprite.height();
    }

    public static boolean isPixelTransparent(SpriteContents sprite, int frame, int x, int y) {
        return isPixelOutsideSprite(sprite, x, y) ? true : sprite.isTransparent(frame, x, y);
    }

    public static boolean isPixelAlwaysTransparent(SpriteContents sprite, int[] frames, int x, int y) {
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y)) {
                return false;
            }
        }
        return true;
    }

    public static boolean doesPixelHaveEdge(SpriteContents sprite, int[] frames, int x, int y, PixelDirection direction) {
        int x1 = x + direction.getOffsetX();
        int y1 = y + direction.getOffsetY();
        if (isPixelOutsideSprite(sprite, x1, y1)) {
            return true;
        }
        for (int frame : frames) {
            if (!isPixelTransparent(sprite, frame, x, y) && isPixelTransparent(sprite, frame, x1, y1)) {
                return true;
            }
        }
        return false;
    }
}
