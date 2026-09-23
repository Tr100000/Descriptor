package io.github.tr100000.descriptor.gui;

import io.github.tr100000.descriptor.config.DescriptorConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.decoration.painting.PaintingVariant;

public class PaintingPreviewTooltipComponent implements ClientTooltipComponent {
    private final int baseWidth;
    private final int baseHeight;
    private final TextureAtlasSprite sprite;

    public PaintingPreviewTooltipComponent(Holder<PaintingVariant> variantHolder) {
        PaintingVariant variant = variantHolder.value();

        if (variant.width() >= variant.height()) {
            this.baseWidth = 64;
            this.baseHeight = baseWidth / variant.width() * variant.height();
        }
        else {
            this.baseHeight = 64;
            this.baseWidth = baseHeight / variant.height() * variant.width();
        }

        Identifier assetId = variant.assetId();
        this.sprite = Minecraft.getInstance().getAtlasManager().get(new SpriteId(Sheets.PAINTINGS_SHEET, assetId));
    }

    private int scale(int base) {
        return Math.max(1, Mth.floor(base * DescriptorConfig.INSTANCE.paintings.previewTooltipScale.getValue()));
    }

    @Override
    public int getWidth(Font font) {
        return scale(baseWidth);
    }

    @Override
    public int getHeight(Font font) {
        return scale(baseHeight);
    }

    @Override
    public void extractImage(Font font, int x, int y, int w, int h, GuiGraphicsExtractor graphics) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, x, y, getWidth(font), getHeight(font));
    }
}
