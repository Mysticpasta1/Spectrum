package de.dafuqs.spectrum.items.tooltip;

import de.dafuqs.spectrum.*;
import net.fabricmc.api.*;
import net.minecraft.client.font.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.tooltip.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import org.jetbrains.annotations.*;

public class SpectrumTooltipComponent implements TooltipComponent {
	
	public static final Identifier TEXTURE = SpectrumCommon.locate("textures/gui/container/spectrum_tooltips.png");
	
	@Override
	public int getHeight() {
		return 0;
	}
	
	@Override
	public int getWidth(TextRenderer textRenderer) {
		return 0;
	}
	
	public void drawOutline(int x, int y, int columns, int rows, DrawContext drawContext) {
		this.draw(drawContext, x, y, Sprite.BORDER_CORNER_TOP);
		this.draw(drawContext, x + columns * 18 + 1, y, Sprite.BORDER_CORNER_TOP);
		
		int j;
		for (j = 0; j < columns; ++j) {
			this.draw(drawContext, x + 1 + j * 18, y, Sprite.BORDER_HORIZONTAL_TOP);
			this.draw(drawContext, x + 1 + j * 18, y + rows * 20, Sprite.BORDER_HORIZONTAL_BOTTOM);
		}
		
		for (j = 0; j < rows; ++j) {
			this.draw(drawContext, x, y + j * 20 + 1, Sprite.BORDER_VERTICAL);
			this.draw(drawContext, x + columns * 18 + 1, y + j * 20 + 1, Sprite.BORDER_VERTICAL);
		}
		
		this.draw(drawContext, x, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
		this.draw(drawContext, x + columns * 18 + 1, y + rows * 20, Sprite.BORDER_CORNER_BOTTOM);
	}
	
	public void drawSlot(int x, int y, int index, ItemStack itemStack, TextRenderer textRenderer, DrawContext drawContext) {
		this.draw(drawContext, x, y, Sprite.SLOT);

		drawContext.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1, String.valueOf(index));
		drawContext.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
		if (index == 0) {
			HandledScreen.drawSlotHighlight(drawContext, x + 1, y + 1, 0);
		}
	}
	
	public void drawDottedSlot(int x, int y, DrawContext drawContext) {
		this.draw(drawContext, x, y, Sprite.DOTTED_SLOT);
	}
	
	private void draw(DrawContext drawContext, int x, int y, @NotNull Sprite sprite) {
		drawContext.drawTexture(TEXTURE, x, y, sprite.u, sprite.v, sprite.width,sprite.height, 128, 128);
	}
	
	@Environment(EnvType.CLIENT)
	public enum Sprite {
		SLOT(0, 0, 18, 20),
		DOTTED_SLOT(18, 0, 18 + 18, 20),
		BLOCKED_SLOT(0, 40, 18, 20),
		BORDER_VERTICAL(0, 18, 1, 20),
		BORDER_HORIZONTAL_TOP(0, 20, 18, 1),
		BORDER_HORIZONTAL_BOTTOM(0, 60, 18, 1),
		BORDER_CORNER_TOP(0, 20, 1, 1),
		BORDER_CORNER_BOTTOM(0, 60, 1, 1);
		
		public final int u;
		public final int v;
		public final int width;
		public final int height;
		
		Sprite(int u, int v, int width, int height) {
			this.u = u;
			this.v = v;
			this.width = width;
			this.height = height;
		}
	}
}
