package com.blakebr0.pickletweaks.feature.handler;

import com.blakebr0.cucumber.helper.NBTHelper;
import com.blakebr0.pickletweaks.feature.item.MagnetItem;
import com.blakebr0.pickletweaks.network.NetworkHandler;
import com.blakebr0.pickletweaks.network.message.ToggleMagnetMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class ToggleMagnetInInventoryHandler {
    @SubscribeEvent
    public void onMouseClicked(GuiScreenEvent.MouseClickedEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Screen gui = event.getGui();

        if (gui instanceof AbstractContainerScreen<?> && event.getButton() == 1) {
            AbstractContainerScreen<?> container = (AbstractContainerScreen<?>) gui;
            Slot slot = container.getSlotUnderMouse();
            LocalPlayer player = mc.player;

            if (player == null)
                return;

            ItemStack held = player.inventory.getCarried();

            if (slot != null && held.isEmpty() && slot.container == player.inventory) {
                ItemStack stack = slot.getItem();

                if (stack.getItem() instanceof MagnetItem) {
                    NetworkHandler.INSTANCE.sendToServer(new ToggleMagnetMessage(slot.index));

                    player.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 0.5F, NBTHelper.getBoolean(stack, "Enabled") ? 0.5F : 1.0F);

                    event.setCanceled(true);
                }
            }
        }
    }
}
