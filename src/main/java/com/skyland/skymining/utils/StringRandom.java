package com.skyland.skymining.utils;

import org.bukkit.ChatColor;

import java.util.Random;

public final class StringRandom {
	public static final Random RANDOM;
	private static final char[] APPEND;

	static {
		RANDOM = new Random();
		APPEND = new char[] { '§', '\0', '§', '\0', '§', '\0', '§', '\0' };
	}

	public static String randomString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			int n = RANDOM.nextInt(ChatColor.values().length) - 1;
			if (n == -1)
				n = 0;
			sb.append(ChatColor.values()[n]);
		}
		return sb.toString();
	}
}
