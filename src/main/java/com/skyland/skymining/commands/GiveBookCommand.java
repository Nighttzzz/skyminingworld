package com.skyland.skymining.commands;

import java.lang.reflect.Field;
import java.util.Arrays;

import com.skyland.skymining.Main;
import com.skyland.skymining.utils.ItemBuilder;
import com.skyland.skymining.utils.NBTItemStack;
import com.skyland.skymining.utils.StringRandom;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GiveBookCommand extends Command {
	private Main plugin;

	public GiveBookCommand(Main plugin) {
		super("givebook");
		setPlugin(plugin);
		register();
	}

	private void register() {
		try {
			final Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
			cmap.setAccessible(true);
			final CommandMap map = (CommandMap) cmap.get(Bukkit.getServer());
			map.register("paladinsmc", (Command) this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] args) {

		if (!sender.hasPermission("paladinsmina.admin")) {
			sender.sendMessage("§cVoc§ n§o possui permiss§o para executar este comando.");
			return true;
		}

		if (args.length != 2) {
			sender.sendMessage("§cUtilize /givebook <player> <quania>");
			return true;
		}

		Player p = Bukkit.getPlayerExact(args[0]);
		if (p == null) {
			sender.sendMessage("§cJogador informado não se encontra no servidor!");
			return true;
		}

		if (!isNumber(args[1])) {
			sender.sendMessage("§cNúmero informado é invalido!");
			return true;
		}

		Integer quantia = Integer.parseInt(args[1]);

		ItemStack item = new ItemBuilder(Material.ENCHANTED_BOOK)
				.setName("§eLivro Encantado" + StringRandom.randomString())
				.setLore(Arrays.asList("§7Derretedor I", "", "§7Colocando esse encantamento na sua picareta,",
						"§7ao minerar você irá receber", "§7os minerios derretidos."))
				.setAmount(quantia).toItemStack();
		final NBTItemStack nbt = new NBTItemStack(item);
		nbt.setBoolean("isBossBook", true);
		item = nbt.getItem();

		p.getInventory().addItem(item);
		sender.sendMessage("§aVocê enviou ao player §f'" + p.getName() + "' §aa quantia de " + quantia + " livro(s).");

		return false;
	}

	public boolean isNumber(String s) {
		try {
			Integer.parseInt(s);
			return true;
		} catch (NumberFormatException e) {
		}
		return false;
	}

	public Main getPlugin() {
		return plugin;
	}

	public void setPlugin(Main plugin) {
		this.plugin = plugin;
	}

}
