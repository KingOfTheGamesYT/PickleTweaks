package com.blakebr0.pickletweaks.tweaks;

import java.util.ListIterator;

import com.blakebr0.cucumber.lib.Colors;
import com.blakebr0.cucumber.util.Utils;
import com.blakebr0.pickletweaks.config.ModConfig;

import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TweakToolBreaking {
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onBreakingBlock(PlayerEvent.BreakSpeed event) {
		if (!ModConfig.confBrokenTools) { return; }
		if (event.getEntityPlayer() == null) { return; }

		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if (stack.isEmpty()) { return; }
        if (!(stack.getItem() instanceof ItemTool) 
        		//&& !(stack.getItem() instanceof ItemSword) 
        		) { return; }

        if (stack.isItemStackDamageable()) {
        	if (stack.getItemDamage() >= stack.getMaxDamage()) {
        		event.setNewSpeed(0.0F);
        	}
        }
	}
	
	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onUseHoe(UseHoeEvent event) {
		if (!ModConfig.confBrokenTools) { return; }
		if (event.getEntityPlayer() == null) { return; }
		
		ItemStack stack = event.getEntityPlayer().getHeldItemMainhand();
		if (stack.isEmpty()) { return; }
		if (!(stack.getItem() instanceof ItemHoe)) { return; }
		
		if (stack.isItemStackDamageable()) {
			if (stack.getItemDamage() >= stack.getMaxDamage()) {
				event.setCanceled(true);
			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onItemTooltip(ItemTooltipEvent event) {
		if (!ModConfig.confBrokenTools) { return; }
		if (event.getEntityPlayer() == null) { return; }
		
		ItemStack stack = event.getItemStack();
		if (!(stack.getItem() instanceof ItemTool) 
        		//&& !(stack.getItem() instanceof ItemSword) 
        		&& !(stack.getItem() instanceof ItemHoe)) { return; }

		ListIterator<String> itr = event.getToolTip().listIterator();
		if (stack.isItemStackDamageable()) {
			if (stack.getItemDamage() >= stack.getMaxDamage()) {
            	while (itr.hasNext()) {
            		itr.next();
            		itr.add(Colors.RED + Colors.ITALICS + Utils.localize("tooltip.pt.broken"));
                	break;
            	}
			}
		}
	}
}
