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
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class TimeBattery extends Item {
    public TimeBattery(){
        setRegistryName("timebattery");
        setUnlocalizedName(ModMain.MODID + ".timebattery");
        setCreativeTab(ModMain.itpCreativeTab);
        maxStackSize = 1;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (!stack.hasTagCompound())
            stack.setTagCompound(new NBTTagCompound());

        NBTTagCompound nbt = stack.getTagCompound();
        if(!nbt.hasKey("ischarging"))nbt.setBoolean("ischarging", true);
        if(!nbt.hasKey("charge"))nbt.setInteger("charge", 0);
        stack.setTagCompound(nbt);

        if(nbt.getBoolean("ischarging")) tooltip.add(TextFormatting.GRAY + I18n.format("timebattery.mode") + TextFormatting.DARK_AQUA + I18n.format("timebattery.mode.charge"));
        else tooltip.add(TextFormatting.GRAY + I18n.format("timebattery.mode") + TextFormatting.GOLD + I18n.format("timebattery.mode.discharge"));
        tooltip.add(TextFormatting.GRAY + I18n.format("timebattery.chargelevel", nbt.getInteger("charge")));
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_getMaxItemUseDuration_1_) {
        return 64;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack itemStack) {
        return EnumAction.BOW;
    }

    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer entityPlayer, EnumHand enumHand) {
        if (!worldIn.isRemote) {
            ItemStack stack = entityPlayer.getHeldItem(enumHand);
            if (!stack.hasTagCompound())
                stack.setTagCompound(new NBTTagCompound());

            NBTTagCompound nbt = stack.getTagCompound();
            if(!nbt.hasKey("ischarging"))nbt.setBoolean("ischarging", true);
            if(!nbt.hasKey("charge"))nbt.setInteger("charge", 0);
            if(entityPlayer.isSneaking())
            {
                nbt.setBoolean("ischarging", !nbt.getBoolean("ischarging"));
            } else
            {
                IGhostPlayer player = entityPlayer.getCapability(GhostPlayerProvider.GHOST_PLAYER_CAPABILITY, null);
                if(nbt.getBoolean("ischarging"))
                {
                    if(player.getPresenceTime() >= 400) {
                        nbt.setInteger("charge", nbt.getInteger("charge") + 400);
                        player.setPresenceTime(player.getPresenceTime() - 400);
                        ModMain.network.sendTo(new SyncAllCaps(player.writeToNBT(), entityPlayer.getEntityId()), (EntityPlayerMP) entityPlayer);
                    }
                } else
                {
                    if(nbt.getInteger("charge") >= 400) {
                        nbt.setInteger("charge",  nbt.getInteger("charge") - 400);
                        player.setPresenceTime(player.getPresenceTime() + 400);
                        ModMain.network.sendTo(new SyncAllCaps(player.writeToNBT(), entityPlayer.getEntityId()), (EntityPlayerMP) entityPlayer);
                    }
                }
            }
            stack.setTagCompound(nbt);

            return new ActionResult(EnumActionResult.SUCCESS, entityPlayer.getHeldItem(enumHand));
        }
        return super.onItemRightClick(worldIn, entityPlayer, enumHand);
    }


}
