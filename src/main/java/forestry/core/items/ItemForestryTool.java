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
package forestry.core.items;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import forestry.core.PluginCore;
import forestry.core.utils.ItemStackUtil;

public class ItemForestryTool extends ItemForestry {
	private final ItemStack remnants;
	private float efficiencyOnProperMaterial;

	public ItemForestryTool(ItemStack remnants) {
		this.maxStackSize = 1;
		efficiencyOnProperMaterial = 6F;
		setMaxDamage(200);
		this.remnants = remnants;
		if (remnants != null) {
			MinecraftForge.EVENT_BUS.register(this);
		}
	}

	public void setEfficiencyOnProperMaterial(float efficiencyOnProperMaterial) {
		this.efficiencyOnProperMaterial = efficiencyOnProperMaterial;
	}

	@Override
	public float getStrVsBlock(ItemStack itemstack, IBlockState state) {
		for (String type : getToolClasses(itemstack)) {
			if (state.getBlock().isToolEffective(type, state)) {
				return efficiencyOnProperMaterial;
			}
		}
		return super.getStrVsBlock(itemstack, state);
	}
	
	@Override
    public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ){
    	if(this == PluginCore.items.bronzeShovel){
	        if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack)){
	            return EnumActionResult.FAIL;
	        }else{
	            IBlockState iblockstate = worldIn.getBlockState(pos);
	            Block block = iblockstate.getBlock();
	
	            if (facing != EnumFacing.DOWN && worldIn.getBlockState(pos.up()).getMaterial() == Material.AIR && block == Blocks.GRASS){
	                IBlockState iblockstate1 = Blocks.GRASS_PATH.getDefaultState();
	                worldIn.playSound(playerIn, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
	
	                if (!worldIn.isRemote){
	                    worldIn.setBlockState(pos, iblockstate1, 11);
	                    stack.damageItem(1, playerIn);
	                }
	
	                return EnumActionResult.SUCCESS;
	            }else{
	                return EnumActionResult.PASS;
	            }
	        }
    	}
    	return EnumActionResult.PASS;
    }

	@SubscribeEvent
	public void onDestroyCurrentItem(PlayerDestroyItemEvent event) {
		if (event.getOriginal() == null || event.getOriginal().getItem() != this) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;

		if (!world.isRemote && remnants != null) {
			ItemStackUtil.dropItemStackAsEntity(remnants.copy(), world, player.posX, player.posY, player.posZ);
		}
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
		Block block = state.getBlock();
		if (block.getBlockHardness(state, worldIn, pos) != 0) {
			stack.damageItem(1, entityLiving);
		}
		return true;
	}

	@Override
	public boolean isFull3D() {
		return true;
	}
}
