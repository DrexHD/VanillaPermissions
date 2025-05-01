package me.drex.vanillapermissions.util;

import net.minecraft.network.Connection;

public class Arguments {
    public static final ThreadLocal<Connection> CONNECTION = new ThreadLocal<>();
}
