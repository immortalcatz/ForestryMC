/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.arboriculture;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import forestry.api.arboriculture.EnumVanillaWoodType;
import forestry.api.arboriculture.IWoodAccess;
import forestry.api.arboriculture.IWoodType;
import forestry.api.arboriculture.WoodBlockKind;
import forestry.arboriculture.blocks.BlockArbDoor;
import forestry.arboriculture.blocks.BlockForestryFenceGate;
import forestry.arboriculture.blocks.BlockForestryStairs;
import forestry.arboriculture.blocks.fence.BlockForestryFence;
import forestry.arboriculture.blocks.log.BlockForestryLog;
import forestry.arboriculture.blocks.planks.BlockForestryPlanks;
import forestry.arboriculture.blocks.property.PropertyWoodType;
import forestry.arboriculture.blocks.slab.BlockForestrySlab;
import forestry.core.utils.Log;

public class WoodAccess implements IWoodAccess {
	private static final Map<WoodBlockKind, WoodMap> woodMaps = new EnumMap<>(WoodBlockKind.class);

	static {
		for (WoodBlockKind woodBlockKind : WoodBlockKind.values()) {
			woodMaps.put(woodBlockKind, new WoodMap(woodBlockKind));
		}
		registerVanilla();
	}

	private static class WoodMap {
		@Nonnull
		private final Map<IWoodType, ItemStack> normalItems = new HashMap<>();
		@Nonnull
		private final Map<IWoodType, ItemStack> fireproofItems = new HashMap<>();
		@Nonnull
		private final Map<IWoodType, IBlockState> normalBlocks = new HashMap<>();
		@Nonnull
		private final Map<IWoodType, IBlockState> fireproofBlocks = new HashMap<>();
		@Nonnull
		private final WoodBlockKind woodBlockKind;

		public WoodMap(@Nonnull WoodBlockKind woodBlockKind) {
			this.woodBlockKind = woodBlockKind;
		}

		@Nonnull
		public String getName() {
			return woodBlockKind.name();
		}

		@Nonnull
		public Map<IWoodType, ItemStack> getItem(boolean fireproof) {
			return fireproof ? this.fireproofItems : this.normalItems;
		}

		@Nonnull
		public Map<IWoodType, IBlockState> getBlock(boolean fireproof) {
			return fireproof ? this.fireproofBlocks : this.normalBlocks;
		}
	}

	public static void registerLogs(List<? extends BlockForestryLog> blocks) {
		for (BlockForestryLog block : blocks) {
			registerWithVariants(block, WoodBlockKind.LOG, block.getVariant());
		}
	}

	public static void registerPlanks(List<? extends BlockForestryPlanks> blocks) {
		for (BlockForestryPlanks block : blocks) {
			registerWithVariants(block, WoodBlockKind.PLANKS, block.getVariant());
		}
	}

	public static void registerSlabs(List<? extends BlockForestrySlab> blocks) {
		for (BlockForestrySlab block : blocks) {
			registerWithVariants(block, WoodBlockKind.SLAB, block.getVariant());
		}
	}

	public static void registerFences(List<? extends BlockForestryFence> blocks) {
		for (BlockForestryFence block : blocks) {
			registerWithVariants(block, WoodBlockKind.FENCE, block.getVariant());
		}
	}
	
	public static void registerFenceGates(List<BlockForestryFenceGate> blocks) {
		for (BlockForestryFenceGate block : blocks) {
			registerWithoutVariants(block, WoodBlockKind.FENCE_GATE);
		}
	}

	public static void registerStairs(List<? extends BlockForestryStairs> blocks) {
		for (BlockForestryStairs block : blocks) {
			registerWithoutVariants(block, WoodBlockKind.STAIRS);
		}
	}

	public static void registerDoors(List<BlockArbDoor> blocks) {
		for (BlockArbDoor block : blocks) {
			registerWithoutVariants(block, WoodBlockKind.DOOR);
		}
	}

	private static void registerVanilla() {
		IBlockState defaultLogState = Blocks.LOG.getDefaultState();
		List<EnumVanillaWoodType> oldLogTypes = Arrays.asList(EnumVanillaWoodType.OAK, EnumVanillaWoodType.SPRUCE, EnumVanillaWoodType.BIRCH, EnumVanillaWoodType.JUNGLE);
		for (EnumVanillaWoodType woodType : oldLogTypes) {
			BlockPlanks.EnumType vanillaType = woodType.getVanillaType();
			ItemStack itemStack = new ItemStack(Blocks.LOG, 1, vanillaType.getMetadata());
			IBlockState blockState = defaultLogState.withProperty(BlockOldLog.VARIANT, vanillaType);
			register(woodType, WoodBlockKind.LOG, false, blockState, itemStack);
		}

		IBlockState defaultLog2State = Blocks.LOG2.getDefaultState();
		List<EnumVanillaWoodType> newLogTypes = Arrays.asList(EnumVanillaWoodType.ACACIA, EnumVanillaWoodType.DARK_OAK);
		for (EnumVanillaWoodType woodType : newLogTypes) {
			BlockPlanks.EnumType vanillaType = woodType.getVanillaType();
			ItemStack itemStack = new ItemStack(Blocks.LOG2, 1, vanillaType.getMetadata() - 4);
			IBlockState blockState = defaultLog2State.withProperty(BlockNewLog.VARIANT, vanillaType);
			register(woodType, WoodBlockKind.LOG, false, blockState, itemStack);
		}

		IBlockState defaultPlanksState = Blocks.PLANKS.getDefaultState();
		IBlockState defaultSlabState = Blocks.WOODEN_SLAB.getDefaultState();
		for (EnumVanillaWoodType woodType : EnumVanillaWoodType.VALUES) {
			BlockPlanks.EnumType vanillaType = woodType.getVanillaType();

			ItemStack plankStack = new ItemStack(Blocks.PLANKS, 1, vanillaType.getMetadata());
			IBlockState plankState = defaultPlanksState.withProperty(BlockPlanks.VARIANT, vanillaType);
			register(woodType, WoodBlockKind.PLANKS, false, plankState, plankStack);

			ItemStack slabStack = new ItemStack(Blocks.WOODEN_SLAB, 1, vanillaType.getMetadata());
			IBlockState slabState = defaultSlabState.withProperty(BlockWoodSlab.VARIANT, vanillaType);
			register(woodType, WoodBlockKind.SLAB, false, slabState, slabStack);
		}

		register(EnumVanillaWoodType.OAK, WoodBlockKind.FENCE, false, Blocks.OAK_FENCE.getDefaultState(), new ItemStack(Blocks.OAK_FENCE));
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.FENCE, false, Blocks.SPRUCE_FENCE.getDefaultState(), new ItemStack(Blocks.SPRUCE_FENCE));
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.FENCE, false, Blocks.BIRCH_FENCE.getDefaultState(), new ItemStack(Blocks.BIRCH_FENCE));
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.FENCE, false, Blocks.JUNGLE_FENCE.getDefaultState(), new ItemStack(Blocks.JUNGLE_FENCE));
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.FENCE, false, Blocks.ACACIA_FENCE.getDefaultState(), new ItemStack(Blocks.ACACIA_FENCE));
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.FENCE, false, Blocks.DARK_OAK_FENCE.getDefaultState(), new ItemStack(Blocks.DARK_OAK_FENCE));

		register(EnumVanillaWoodType.OAK, WoodBlockKind.FENCE_GATE, false, Blocks.OAK_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.OAK_FENCE_GATE));
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.FENCE_GATE, false, Blocks.SPRUCE_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.SPRUCE_FENCE_GATE));
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.FENCE_GATE, false, Blocks.BIRCH_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.BIRCH_FENCE_GATE));
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.FENCE_GATE, false, Blocks.JUNGLE_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.JUNGLE_FENCE_GATE));
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.FENCE_GATE, false, Blocks.ACACIA_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.ACACIA_FENCE_GATE));
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.FENCE_GATE, false, Blocks.DARK_OAK_FENCE_GATE.getDefaultState(), new ItemStack(Blocks.DARK_OAK_FENCE_GATE));

		register(EnumVanillaWoodType.OAK, WoodBlockKind.STAIRS, false, Blocks.OAK_STAIRS.getDefaultState(), new ItemStack(Blocks.OAK_STAIRS));
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.STAIRS, false, Blocks.SPRUCE_STAIRS.getDefaultState(), new ItemStack(Blocks.SPRUCE_STAIRS));
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.STAIRS, false, Blocks.BIRCH_STAIRS.getDefaultState(), new ItemStack(Blocks.BIRCH_STAIRS));
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.STAIRS, false, Blocks.JUNGLE_STAIRS.getDefaultState(), new ItemStack(Blocks.JUNGLE_STAIRS));
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.STAIRS, false, Blocks.ACACIA_STAIRS.getDefaultState(), new ItemStack(Blocks.ACACIA_STAIRS));
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.STAIRS, false, Blocks.DARK_OAK_STAIRS.getDefaultState(), new ItemStack(Blocks.DARK_OAK_STAIRS));

		register(EnumVanillaWoodType.OAK, WoodBlockKind.DOOR, false, Blocks.OAK_DOOR.getDefaultState(), new ItemStack(Items.OAK_DOOR));
		register(EnumVanillaWoodType.SPRUCE, WoodBlockKind.DOOR, false, Blocks.SPRUCE_DOOR.getDefaultState(), new ItemStack(Items.SPRUCE_DOOR));
		register(EnumVanillaWoodType.BIRCH, WoodBlockKind.DOOR, false, Blocks.BIRCH_DOOR.getDefaultState(), new ItemStack(Items.BIRCH_DOOR));
		register(EnumVanillaWoodType.JUNGLE, WoodBlockKind.DOOR, false, Blocks.JUNGLE_DOOR.getDefaultState(), new ItemStack(Items.JUNGLE_DOOR));
		register(EnumVanillaWoodType.ACACIA, WoodBlockKind.DOOR, false, Blocks.ACACIA_DOOR.getDefaultState(), new ItemStack(Items.ACACIA_DOOR));
		register(EnumVanillaWoodType.DARK_OAK, WoodBlockKind.DOOR, false, Blocks.DARK_OAK_DOOR.getDefaultState(), new ItemStack(Items.DARK_OAK_DOOR));
	}

	private static <T extends Block & IWoodTyped, V extends Enum<V> & IWoodType> void registerWithVariants(T woodTyped, WoodBlockKind woodBlockKind, PropertyWoodType<V> property) {
		boolean fireproof = woodTyped.isFireproof();

		for (V value : property.getAllowedValues()) {
			IBlockState blockState = woodTyped.getDefaultState().withProperty(property, value);
			int meta = woodTyped.getMetaFromState(blockState);
			IWoodType woodType = woodTyped.getWoodType(meta);
			ItemStack itemStack = new ItemStack(woodTyped, 1, meta);
			register(woodType, woodBlockKind, fireproof, blockState, itemStack);
		}
	}

	/**
	 * Register wood blocks that have no variant property
	 */
	private static <T extends Block & IWoodTyped> void registerWithoutVariants(T woodTyped, WoodBlockKind woodBlockKind) {
		boolean fireproof = woodTyped.isFireproof();
		IBlockState blockState = woodTyped.getDefaultState();
		IWoodType woodType = woodTyped.getWoodType(0);
		ItemStack itemStack = new ItemStack(woodTyped);
		register(woodType, woodBlockKind, fireproof, blockState, itemStack);
	}

	private static void register(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof, IBlockState blockState, ItemStack itemStack) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		if (itemStack == null || itemStack.getItem() == null) {
			throw new NullPointerException("Invalid itemStack: " + itemStack);
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		woodMap.getItem(fireproof).put(woodType, itemStack);
		woodMap.getBlock(fireproof).put(woodType, blockState);
	}

	@Override
	public ItemStack getStack(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		ItemStack itemStack = woodMap.getItem(fireproof).get(woodType);
		if (itemStack == null) {
			Log.error("No stack found for {} {} {}", woodType, woodMap.getName(), fireproof ? "fireproof" : "");
			return null;
		}
		return itemStack.copy();
	}

	@Override
	public IBlockState getBlock(IWoodType woodType, WoodBlockKind woodBlockKind, boolean fireproof) {
		if (woodBlockKind == WoodBlockKind.DOOR) {
			fireproof = true;
		}
		WoodMap woodMap = woodMaps.get(woodBlockKind);
		IBlockState blockState = woodMap.getBlock(fireproof).get(woodType);
		if (blockState == null) {
			Log.error("No block found for {} {} {}", woodType, woodMap.getName(), fireproof ? "fireproof" : "non-fireproof");
			return null;
		}
		return blockState;
	}
}
