package com.cwelth.intimepresence;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

@SideOnly(Side.CLIENT)
public class KeyBindings {
    public static KeyBinding hudSwitch;

    public static void init(){
        hudSwitch = new KeyBinding("key.hudswitch", Keyboard.KEY_EQUALS, "key.intimepresence");
        ClientRegistry.registerKeyBinding(hudSwitch);
    }
}
