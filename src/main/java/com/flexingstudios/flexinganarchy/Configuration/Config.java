package com.flexingstudios.flexinganarchy.Configuration;

import com.flexingstudios.FlexingNetwork.api.conf.Configuration;
import com.flexingstudios.flexinganarchy.FlexingAnarchy;

public class Config extends Configuration {
    private final FlexingAnarchy plugin;

    public Config(FlexingAnarchy plugin) {
        super(FlexingAnarchy.INSTANCE);
        this.plugin = plugin;

        addDefault(Function.LOBBY_WORLD, "world");
        addDefault(Function.LOC_LOBBY_X, "0.5");
        addDefault(Function.LOC_LOBBY_Y, "100.0");
        addDefault(Function.LOC_LOBBY_Z, "0.5");
        addDefault(Function.LOC_LOBBY_PITCH, "0.0");
        addDefault(Function.LOC_LOBBY_YAW, "0.0");
        addDefault(Function.PVP_MANAGER_COMBAT_DURATION, 15);
        addDefault(Function.PVP_MANAGER_VANISH_TIMEOUT, 5);
        addDefault(Function.PVP_MANAGER_ACTIVATION_SUBJECTS, new String[]{"Player"});

        save();
    }
}
