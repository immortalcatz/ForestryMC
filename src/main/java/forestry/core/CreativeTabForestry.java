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
package forestry.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.common.registry.GameRegistry;

import forestry.api.core.ForestryAPI;
import forestry.api.core.Tabs;
import forestry.core.config.Constants;
import forestry.plugins.ForestryPluginUids;

public class CreativeTabForestry extends CreativeTabs {

	static {
		if (ForestryAPI.enabledPlugins.contains(ForestryPluginUids.FARMING)) {
			Tabs.tabAgriculture= new CreativeTabForestry(1, "agriculture");
		}
		
		if (ForestryAPI.enabledPlugins.contains(ForestryPluginUids.APICULTURE)) {
			Tabs.tabApiculture = new CreativeTabForestry(2, "apiculture");
		}

		if (ForestryAPI.enabledPlugins.contains(ForestryPluginUids.ARBORICULTURE)) {
			Tabs.tabArboriculture = new CreativeTabForestry(3, "arboriculture");
		}

		if (ForestryAPI.enabledPlugins.contains(ForestryPluginUids.LEPIDOPTEROLOGY)) {
			Tabs.tabLepidopterology = new CreativeTabForestry(4, "lepidopterology");
		}
	}

	public static final CreativeTabs tabForestry = new CreativeTabForestry(0, Constants.MOD_ID);

	private final int icon;

	private CreativeTabForestry(int icon, String label) {
		super(label);
		this.icon = icon;
	}

	@Override
	public ItemStack getIconItemStack() {
		Item iconItem;
		switch (icon) {
			case 1:
				iconItem = GameRegistry.findItem(Constants.MOD_ID, "ffarm");
				break;
			case 2:
				iconItem = GameRegistry.findItem(Constants.MOD_ID, "beeDroneGE");
				break;
			case 3:
				iconItem = GameRegistry.findItem(Constants.MOD_ID, "sapling");
				break;
			case 4:
				iconItem = GameRegistry.findItem(Constants.MOD_ID, "butterflyGE");
				break;
			default:
				iconItem = GameRegistry.findItem(Constants.MOD_ID, "fertilizerCompound");
				break;
		}
		if (iconItem == null) {
			iconItem = PluginCore.items.wrench;
		}
		return new ItemStack(iconItem);
	}

	@Override
	public Item getTabIconItem() {
		return null; // not used due to overridden getIconItemStack
	}
}
