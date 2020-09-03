package com.skyland.skymining.commands;

import java.lang.reflect.Field;
import java.util.Random;

import com.skyland.skymining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MinaCommand extends Command {

    private static Main m = (Main)Bukkit.getPluginManager().getPlugin("SkyMiningWorld");

    private Main plugin;

    public MinaCommand(Main plugin) {
        super(m.getConfig().getString("Mundo"));
        setPlugin(plugin);
        register();
    }

    private void register() {
        try {
            final Field cmap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            cmap.setAccessible(true);
            final CommandMap map = (CommandMap) cmap.get(Bukkit.getServer());
            map.register("skyland", (Command) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean execute(CommandSender sender, String arg1, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player p = (Player) sender;
        if (p.getWorld().getName().equalsIgnoreCase("mina")) {
            p.sendMessage(m.getConfig().getString("Mensagens.Jogador-na-mina").replace('&', '§').replace("{jogador}", p.getName()));
            return true;
        }

        World world = Bukkit.getWorld(m.getConfig().getString("Mundo"));
        Random random = new Random();

        int x = random.nextInt(2500) + 1;
        int z = random.nextInt(2500) + 1;
        int y = world.getHighestBlockYAt(x, z) - 1;

        Location teleportLocation = new Location(world, x, y, z);
        p.teleport(teleportLocation);

        for(String msg : Main.getPlugin(Main.class).getConfig().getStringList("Ao-entrar-na-mina")) {
            p.sendMessage(msg.replace("&", "§").replace("{jogador}", p.getName().replace("{mundo}", m.getConfig().getString("Mundo"))));
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
