package com.cwelth.intimepresence.items;

import com.cwelth.intimepresence.ModMain;
import com.cwelth.intimepresence.network.SyncAllCaps;
import com.cwelth.intimepresence.player.GhostPlayerProvider;
import com.cwelth.intimepresence.player.IGhostPlayer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class BionicEye extends Item {
    public BionicEye() {
        setRegistryName("bioniceye");
        setUnlocalizedName(ModMain.MODID + ".bioniceye");
        setCreativeTab(ModMain.itpCreativeTab);
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        tooltip.add(TextFormatting.GRAY + I18n.format("bioniceye.tooltip"));
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer entityPlayer, EnumHand enumHand) {
        ItemStack stack = entityPlayer.getHeldItem(enumHand);

        if(entityPlayer.isSneaking())
            return new ActionResult(EnumActionResult.FAIL, stack);

        IGhostPlayer ghostPlayer = entityPlayer.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
        if(ghostPlayer.getHudInstalled())
        {
            if (!worldIn.isRemote)
                entityPlayer.sendMessage(new TextComponentString(I18n.format("bioniceye.alreadyinstalled")));
            return new ActionResult(EnumActionResult.FAIL, stack);
        } else {
            if (!worldIn.isRemote) {
                ghostPlayer.setHudInstalled(true);
                ModMain.network.sendTo(new SyncAllCaps(ghostPlayer.writeToNBT(), entityPlayer.getEntityId()), (EntityPlayerMP) entityPlayer);
            }
            stack.shrink(1);
            return new ActionResult(EnumActionResult.SUCCESS, stack);
        }
    }
}
