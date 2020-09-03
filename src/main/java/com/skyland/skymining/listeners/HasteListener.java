package com.skyland.skymining.listeners;

import com.skyland.skymining.Main;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HasteListener implements Listener {

	private static Main m = (Main) Bukkit.getPluginManager().getPlugin("MinaCore");

	@EventHandler
	public void onWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		World world = player.getWorld();
		if (world.getName().equalsIgnoreCase(m.getConfig().getString("Mundo"))) {
			player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 999999, 1));
		} else if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING)) {
			player.removePotionEffect(PotionEffectType.FAST_DIGGING);
		}
	}
}
