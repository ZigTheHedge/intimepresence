package com.cwelth.intimepresence;


import com.cwelth.intimepresence.items.AllItems;
import com.cwelth.intimepresence.proxy.CommonProxy;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import static net.minecraftforge.common.config.Configuration.CATEGORY_GENERAL;

@Mod.EventBusSubscriber
public class EventHandlers {

    @SubscribeEvent
    public void addLootToEndermen(LootTableLoadEvent event)
    {
        if(event.getName().toString().equals("minecraft:entities/enderman"))
        {
            if(Config.genOreDropEnabled) {
                LootPool pool = event.getTable().getPool("main");
                pool.addEntry(new LootEntryItem(AllItems.dimensionalShards, 2, 0, new LootFunction[]{
                        new SetCount(new LootCondition[0], new RandomValueRange(0, 1)),
                        new LootingEnchantBonus(new LootCondition[0], new RandomValueRange(0, 1), 0)}, new LootCondition[0], "intimepresence:dimshards"));
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onKeyPress(InputEvent.KeyInputEvent e)
    {
        if(KeyBindings.hudSwitch.isPressed()) {
            Config.hudVisible = !Config.hudVisible;
            CommonProxy.config.getCategory(CATEGORY_GENERAL).get("hudVisible").set(Config.hudVisible);
            CommonProxy.config.save();
        }
    }
}
