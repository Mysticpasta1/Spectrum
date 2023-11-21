package de.dafuqs.spectrum.compat.patchouli.pages;

import com.mojang.blaze3d.systems.*;
import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import net.minecraft.client.gui.*;
import net.minecraft.item.*;
import net.minecraft.recipe.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.jetbrains.annotations.*;
import vazkii.patchouli.client.book.gui.*;

public class PageAnvilCrushing extends PageGatedRecipe<AnvilCrushingRecipe> {
	
	private static final Identifier BACKGROUND_TEXTURE = SpectrumCommon.locate("textures/gui/container/anvil_crushing.png");
	
	public PageAnvilCrushing() {
		super(SpectrumRecipeTypes.ANVIL_CRUSHING);
	}
	
	@Override
	protected ItemStack getRecipeOutput(World world, AnvilCrushingRecipe recipe) {
		if (recipe == null) {
			return ItemStack.EMPTY;
		} else {
			return recipe.getOutput(world.getRegistryManager());
		}
	}
	
	@Override
	protected void drawRecipe(DrawContext drawContext, @NotNull AnvilCrushingRecipe recipe, int recipeX, int recipeY, int mouseX, int mouseY) {
		RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
		RenderSystem.enableBlend();
		
		// dirt  wall
		drawContext.drawTexture(BACKGROUND_TEXTURE, recipeX, recipeY + 4, 0, 0, 84, 48, 256,256);

		parent.drawCenteredStringNoShadow(drawContext, getTitle().asOrderedText(), GuiBook.PAGE_WIDTH / 2, recipeY - 10, book.headerColor);
		
		// the ingredients
		Ingredient ingredient = recipe.getIngredients().get(0);
		parent.renderIngredient(drawContext, recipeX + 16, recipeY + 35, mouseX, mouseY, ingredient);
		
		// the anvil
		parent.renderItemStack(drawContext, recipeX + 16, recipeY + 15, mouseX, mouseY, recipe.createIcon());
		
		// the output
		parent.renderItemStack(drawContext, recipeX + 64, recipeY + 29, mouseX, mouseY, recipe.getOutput());
	}
	
	@Override
	protected int getRecipeHeight() {
		return 73;
	}
	
}