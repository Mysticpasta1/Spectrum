package de.dafuqs.spectrum.compat.REI;

import de.dafuqs.spectrum.*;
import de.dafuqs.spectrum.blocks.idols.*;
import de.dafuqs.spectrum.compat.REI.plugins.*;
import de.dafuqs.spectrum.data_loaders.*;
import de.dafuqs.spectrum.inventories.*;
import de.dafuqs.spectrum.recipe.*;
import de.dafuqs.spectrum.recipe.anvil_crushing.*;
import de.dafuqs.spectrum.recipe.cinderhearth.*;
import de.dafuqs.spectrum.recipe.crystallarieum.*;
import de.dafuqs.spectrum.recipe.enchanter.*;
import de.dafuqs.spectrum.recipe.enchantment_upgrade.*;
import de.dafuqs.spectrum.recipe.fluid_converting.*;
import de.dafuqs.spectrum.recipe.fusion_shrine.*;
import de.dafuqs.spectrum.recipe.ink_converting.*;
import de.dafuqs.spectrum.recipe.pedestal.*;
import de.dafuqs.spectrum.recipe.potion_workshop.*;
import de.dafuqs.spectrum.recipe.spirit_instiller.*;
import de.dafuqs.spectrum.recipe.titration_barrel.*;
import de.dafuqs.spectrum.registries.*;
import dev.architectury.event.*;
import me.shedaniel.math.*;
import me.shedaniel.rei.api.client.plugins.*;
import me.shedaniel.rei.api.client.registry.category.*;
import me.shedaniel.rei.api.client.registry.display.*;
import me.shedaniel.rei.api.client.registry.screen.*;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.client.registry.transfer.simple.SimpleTransferHandler;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.entry.*;
import me.shedaniel.rei.api.common.transfer.info.stack.SlotAccessor;
import me.shedaniel.rei.api.common.util.*;
import me.shedaniel.rei.plugin.common.*;
import net.fabricmc.api.*;
import net.minecraft.block.*;
import net.minecraft.screen.ScreenHandler;

import java.util.List;
import java.util.stream.Stream;

@Environment(EnvType.CLIENT)
public class REIClientIntegration implements REIClientPlugin {
	
	@Override
	public void registerCategories(CategoryRegistry registry) {
		registry.add(new PedestalCraftingCategory());
		registry.add(new AnvilCrushingCategory());
		registry.add(new FusionShrineCategory());
		registry.add(new NaturesStaffConversionsCategory());
		registry.add(new EnchanterEnchantingCategory());
		registry.add(new EnchantmentUpgradeCategory());
		registry.add(new PotionWorkshopBrewingCategory());
		registry.add(new PotionWorkshopCraftingCategory());
		registry.add(new PotionWorkshopReactingCategory());
		registry.add(new SpiritInstillingCategory());
		registry.add(new MudConvertingCategory());
		registry.add(new LiquidCrystalConvertingCategory());
		registry.add(new MidnightSolutionConvertingCategory());
		registry.add(new DragonrotConvertingCategory());
		registry.add(new HeatingCategory());
		registry.add(new FreezingCategory());
		registry.add(new InkConvertingCategory());
		registry.add(new CrystallarieumCategory());
		registry.add(new CinderhearthCategory());
		registry.add(new TitrationBarrelCategory());
		
		
		EntryIngredient pedestals = EntryIngredient.of(
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_TOPAZ),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_AMETHYST),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_BASIC_CITRINE),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ALL_BASIC),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_ONYX),
				EntryStacks.of(SpectrumBlocks.PEDESTAL_MOONSTONE)
		);
		registry.addWorkstations(SpectrumPlugins.PEDESTAL_CRAFTING, pedestals);
		if (SpectrumCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			registry.addWorkstations(BuiltinPlugin.CRAFTING, pedestals);
		}
		
		registry.addWorkstations(BuiltinPlugin.CRAFTING, EntryStacks.of(SpectrumItems.CRAFTING_TABLET));
		registry.addWorkstations(BuiltinPlugin.BLASTING, EntryStacks.of(SpectrumBlocks.CINDERHEARTH));
		
		registry.addWorkstations(SpectrumPlugins.ANVIL_CRUSHING, EntryStacks.of(Blocks.ANVIL), EntryStacks.of(SpectrumBlocks.BEDROCK_ANVIL), EntryStacks.of(SpectrumBlocks.STRATINE_FRAGMENT_BLOCK), EntryStacks.of(SpectrumBlocks.PALTAERIA_FRAGMENT_BLOCK));
		registry.addWorkstations(SpectrumPlugins.FUSION_SHRINE, EntryIngredient.of(EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_BASALT), EntryStacks.of(SpectrumBlocks.FUSION_SHRINE_CALCITE)));
		registry.addWorkstations(SpectrumPlugins.NATURES_STAFF, EntryStacks.of(SpectrumItems.NATURES_STAFF));
		registry.addWorkstations(SpectrumPlugins.HEATING, EntryStacks.of(SpectrumBlocks.BLAZE_IDOL));
		registry.addWorkstations(SpectrumPlugins.FREEZING, EntryStacks.of(SpectrumBlocks.POLAR_BEAR_IDOL));
		registry.addWorkstations(SpectrumPlugins.ENCHANTER_CRAFTING, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstations(SpectrumPlugins.ENCHANTMENT_UPGRADE, EntryStacks.of(SpectrumBlocks.ENCHANTER));
		registry.addWorkstations(SpectrumPlugins.MUD_CONVERTING, EntryStacks.of(SpectrumItems.MUD_BUCKET));
		registry.addWorkstations(SpectrumPlugins.LIQUID_CRYSTAL_CONVERTING, EntryStacks.of(SpectrumItems.LIQUID_CRYSTAL_BUCKET));
		registry.addWorkstations(SpectrumPlugins.MIDNIGHT_SOLUTION_CONVERTING, EntryStacks.of(SpectrumItems.MIDNIGHT_SOLUTION_BUCKET));
		registry.addWorkstations(SpectrumPlugins.DRAGONROT_CONVERTING, EntryStacks.of(SpectrumItems.DRAGONROT_BUCKET));
		registry.addWorkstations(SpectrumPlugins.SPIRIT_INSTILLER, EntryStacks.of(SpectrumBlocks.SPIRIT_INSTILLER));
		registry.addWorkstations(SpectrumPlugins.INK_CONVERTING, EntryStacks.of(SpectrumBlocks.COLOR_PICKER));
		registry.addWorkstations(SpectrumPlugins.CRYSTALLARIEUM, EntryStacks.of(SpectrumBlocks.CRYSTALLARIEUM));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_BREWING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_CRAFTING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.POTION_WORKSHOP_REACTING, EntryStacks.of(SpectrumBlocks.POTION_WORKSHOP));
		registry.addWorkstations(SpectrumPlugins.CINDERHEARTH, EntryStacks.of(SpectrumBlocks.CINDERHEARTH));
		registry.addWorkstations(SpectrumPlugins.TITRATION_BARREL, EntryStacks.of(SpectrumBlocks.TITRATION_BARREL));
	}
	
	@Override
	public void registerDisplays(DisplayRegistry registry) {
		registry.registerFiller(AnvilCrushingRecipe.class, AnvilCrushingDisplay::new);
		registry.registerRecipeFiller(PedestalRecipe.class, SpectrumRecipeTypes.PEDESTAL, PedestalCraftingDisplay::new);
		registry.registerRecipeFiller(FusionShrineRecipe.class, SpectrumRecipeTypes.FUSION_SHRINE, FusionShrineDisplay::new);
		registry.registerRecipeFiller(EnchanterRecipe.class, SpectrumRecipeTypes.ENCHANTER, EnchanterEnchantingDisplay::new);
		registry.registerRecipeFiller(EnchantmentUpgradeRecipe.class, SpectrumRecipeTypes.ENCHANTMENT_UPGRADE, EnchantmentUpgradeDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopBrewingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_BREWING, PotionWorkshopBrewingDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopCraftingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_CRAFTING, PotionWorkshopCraftingDisplay::new);
		registry.registerRecipeFiller(SpiritInstillerRecipe.class, SpectrumRecipeTypes.SPIRIT_INSTILLING, SpiritInstillingDisplay::new);
		registry.registerRecipeFiller(MudConvertingRecipe.class, SpectrumRecipeTypes.MUD_CONVERTING, MudConvertingDisplay::new);
		registry.registerRecipeFiller(LiquidCrystalConvertingRecipe.class, SpectrumRecipeTypes.LIQUID_CRYSTAL_CONVERTING, LiquidCrystalConvertingDisplay::new);
		registry.registerRecipeFiller(MidnightSolutionConvertingRecipe.class, SpectrumRecipeTypes.MIDNIGHT_SOLUTION_CONVERTING, MidnightSolutionConvertingDisplay::new);
		registry.registerRecipeFiller(DragonrotConvertingRecipe.class, SpectrumRecipeTypes.DRAGONROT_CONVERTING, DragonrotConvertingDisplay::new);
		registry.registerRecipeFiller(InkConvertingRecipe.class, SpectrumRecipeTypes.INK_CONVERTING, InkConvertingDisplay::new);
		registry.registerRecipeFiller(PotionWorkshopReactingRecipe.class, SpectrumRecipeTypes.POTION_WORKSHOP_REACTING, PotionWorkshopReactingDisplay::new);
		registry.registerRecipeFiller(CrystallarieumRecipe.class, SpectrumRecipeTypes.CRYSTALLARIEUM, CrystallarieumDisplay::new);
		registry.registerRecipeFiller(CinderhearthRecipe.class, SpectrumRecipeTypes.CINDERHEARTH, CinderhearthDisplay::new);
		registry.registerRecipeFiller(ITitrationBarrelRecipe.class, SpectrumRecipeTypes.TITRATION_BARREL, TitrationBarrelDisplay::new);
		
		NaturesStaffConversionDataLoader.CONVERSIONS.forEach((key, value) -> registry.add(new NaturesStaffConversionsDisplay(EntryStacks.of(key), EntryStacks.of(value.getBlock()), NaturesStaffConversionDataLoader.UNLOCK_IDENTIFIERS.getOrDefault(key, null))));
		FreezingIdolBlock.FREEZING_STATE_MAP.forEach((key, value) -> registry.add(new FreezingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key.getBlock()), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		FreezingIdolBlock.FREEZING_MAP.forEach((key, value) -> registry.add(new FreezingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		FirestarterIdolBlock.BURNING_MAP.forEach((key, value) -> registry.add(new HeatingDisplay(BlockToBlockWithChanceDisplay.blockToEntryStack(key), BlockToBlockWithChanceDisplay.blockToEntryStack(value.getLeft().getBlock()), value.getRight())));
		
		
		registry.registerVisibilityPredicate((category, display) -> {
			// do not list recipes in REI at all, until they are unlocked
			// secret recipes are never shown
			if (display instanceof GatedRecipeDisplay gatedRecipeDisplay) {
				if (gatedRecipeDisplay.isSecret()) {
					return EventResult.interruptFalse();
				}
				if (!SpectrumCommon.CONFIG.REIListsRecipesAsNotUnlocked && !gatedRecipeDisplay.isUnlocked()) {
					return EventResult.interruptFalse();
				}
			}
			return EventResult.pass();
		});
		
	}
	
	/**
	 * Where in the screens gui the player has to click
	 * to get to the recipe overview
	 * Only use for recipe types that are crafted in a gui
	 */
	@Override
	public void registerScreens(ScreenRegistry registry) {
		// Since the pedestal can craft both vanilla and pedestal recipes
		// we split the "arrow" part of the gui into two parts => faster access
		registry.registerContainerClickArea(new Rectangle(89, 37, 10, 15), PedestalScreen.class, BuiltinPlugin.CRAFTING);
		registry.registerContainerClickArea(new Rectangle(100, 37, 11, 15), PedestalScreen.class, SpectrumPlugins.PEDESTAL_CRAFTING);
		
		registry.registerContainerClickArea(new Rectangle(28, 41, 10, 42), PotionWorkshopScreen.class, SpectrumPlugins.POTION_WORKSHOP_BREWING);
		registry.registerContainerClickArea(new Rectangle(28, 41, 10, 42), PotionWorkshopScreen.class, SpectrumPlugins.POTION_WORKSHOP_CRAFTING);
		
		registry.registerContainerClickArea(new Rectangle(35, 33, 22, 15), CinderhearthScreen.class, SpectrumPlugins.CINDERHEARTH);
		registry.registerContainerClickArea(new Rectangle(35, 33, 22, 15), CinderhearthScreen.class, BuiltinPlugin.BLASTING);
		
		registry.registerDecider(REIOverlayDecider.INSTANCE);
	}

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void registerTransferHandlers(TransferHandlerRegistry registry) {
		// REI input magic to prevent moving incorrect amount of gem powder yet still complain about a lack of such
		registry.register(SimpleTransferHandlerExtension.create(PedestalScreenHandler.class, SpectrumPlugins.PEDESTAL_CRAFTING,
				new SimpleTransferHandler.IntRange(0, 8),
				List.of(new SimpleTransferHandler.IntRange(9, 14), new SimpleTransferHandler.IntRange(16, 52))));
		if (SpectrumCommon.CONFIG.canPedestalCraftVanillaRecipes()) {
			registry.register(SimpleTransferHandlerExtension.create(PedestalScreenHandler.class, BuiltinPlugin.CRAFTING,
					new SimpleTransferHandler.IntRange(0, 8), new SimpleTransferHandler.IntRange(16, 52)));
		}
		registry.register(SimpleTransferHandlerExtension.create(CinderhearthScreenHandler.class, SpectrumPlugins.CINDERHEARTH,
				new SimpleTransferHandler.IntRange(2, 3), new SimpleTransferHandler.IntRange(11, 47)));
		registry.register(SimpleTransferHandlerExtension.create(CinderhearthScreenHandler.class, BuiltinPlugin.BLASTING,
				new SimpleTransferHandler.IntRange(2, 3), new SimpleTransferHandler.IntRange(11, 47)));
		registry.register(SimpleTransferHandlerExtension.create(PotionWorkshopScreenHandler.class, SpectrumPlugins.POTION_WORKSHOP_BREWING,
				new SimpleTransferHandler.IntRange(0, 9), new SimpleTransferHandler.IntRange(21, 57)));
		registry.register(SimpleTransferHandlerExtension.create(PotionWorkshopScreenHandler.class, SpectrumPlugins.POTION_WORKSHOP_CRAFTING,
				new SimpleTransferHandler.IntRange(0, 9), new SimpleTransferHandler.IntRange(21, 57)));
	}

	@SuppressWarnings("UnstableApiUsage")
	interface SimpleTransferHandlerExtension extends SimpleTransferHandler {
		// Because REI decided to give the create method with the inventory slots argument a different container class type.
		// Pretty much identical to the original otherwise (except with slot handling changed to resemble the EMI counterpart)
		static <C extends ScreenHandler, D extends Display> SimpleTransferHandler create(Class<? extends C> containerClass,
																								 CategoryIdentifier<D> categoryIdentifier,
																								 SimpleTransferHandler.IntRange inputSlots,
																								 SimpleTransferHandler.IntRange inventorySlots) {
			return new SimpleTransferHandler() {
				@Override
				public ApplicabilityResult checkApplicable(Context context) {
					if (!containerClass.isInstance(context.getMenu())
							|| !categoryIdentifier.equals(context.getDisplay().getCategoryIdentifier())
							|| context.getContainerScreen() == null) {
						return ApplicabilityResult.createNotApplicable();
					} else {
						return ApplicabilityResult.createApplicable();
					}
				}

				@Override
				public Iterable<SlotAccessor> getInputSlots(Context context) {
					return context.getMenu()
							.slots.subList(inputSlots.min(), inputSlots.maxExclusive())
							.stream().map(SlotAccessor::fromSlot).toList();
				}

				@Override
				public Iterable<SlotAccessor> getInventorySlots(Context context) {
					return context.getMenu()
							.slots.subList(inventorySlots.min(), inventorySlots.maxExclusive())
							.stream().map(SlotAccessor::fromSlot).toList();
				}
			};
		}
		static <C extends ScreenHandler, D extends Display> SimpleTransferHandler create(Class<? extends C> containerClass,
																						 CategoryIdentifier<D> categoryIdentifier,
																						 SimpleTransferHandler.IntRange inputSlots,
																						 List<IntRange> inventorySlotsRanges) {
			return new SimpleTransferHandler() {
				@Override
				public ApplicabilityResult checkApplicable(Context context) {
					if (!containerClass.isInstance(context.getMenu())
							|| !categoryIdentifier.equals(context.getDisplay().getCategoryIdentifier())
							|| context.getContainerScreen() == null) {
						return ApplicabilityResult.createNotApplicable();
					} else {
						return ApplicabilityResult.createApplicable();
					}
				}

				@Override
				public Iterable<SlotAccessor> getInputSlots(Context context) {
					return context.getMenu()
							.slots.subList(inputSlots.min(), inputSlots.maxExclusive())
							.stream().map(SlotAccessor::fromSlot).toList();
				}

				@Override
				public Iterable<SlotAccessor> getInventorySlots(Context context) {
					Stream<SlotAccessor> s = Stream.empty();
					for (SimpleTransferHandler.IntRange range : inventorySlotsRanges) {
						s = Stream.concat(s, context.getMenu()
								.slots.subList(range.min(), range.maxExclusive())
								.stream().map(SlotAccessor::fromSlot));
					}
					return s.toList();
				}
			};
		}
	}
}
