package com.skyland.skymining.listeners;

import java.util.List;
import java.util.Random;

import com.skyland.skymining.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreak implements Listener {

    private static Main m = (Main) Bukkit.getPluginManager().getPlugin("MinaCore");

    protected final String minaWorldName = m.getConfig().getString("Mundo");
    protected final Random random = new Random();

    public BlockBreak(Main plugin) {
        super();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    void event(WorldLoadEvent e) {
        if (e.getWorld().getName().equals(m.getConfig().getString("Mundo")))
            e.getWorld().setSpawnFlags(false, false);
    }

    @EventHandler
    public void onSpawnItem(ItemSpawnEvent e) {
        Material itemType = e.getEntity().getItemStack().getType();
        if (itemType.equals(Material.COBBLESTONE)
                || itemType.equals(Material.GRAVEL)
                || itemType.equals(Material.DIRT)
                || itemType == Material.STONE) {
            if (e.getLocation().getWorld().getName().equalsIgnoreCase(minaWorldName)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBreakStone(BlockBreakEvent e) {
        Player p = e.getPlayer();
        ItemStack item = p.getItemInHand();

        if (item == null || item.getType() == Material.AIR)
            return;

        Block block = e.getBlock();

        if (block.getType() == Material.STONE && block.getWorld().getName().equalsIgnoreCase(minaWorldName)) {
            block.getDrops().clear();

            if (block.getY() >= 13) {
                sendDrop(p, setDrop(1), block.getLocation());
            } else if (block.getY() < 13){
                sendDrop(p, setDrop(2), block.getLocation());
            }
        }
    }

    private ItemStack setDrop(int type) {
        switch (type) {
            case 1: {
                if (percentChance(1.5))
                    return new ItemStack(Material.GOLD_ORE);
                else if (percentChance(2.1))
                    return new ItemStack(Material.IRON_ORE);
                else if (percentChance(5.3))
                    return new ItemStack(Material.COAL);
            }
            case 2: {
                if (percentChance(0.5))
                    return new ItemStack(Material.EMERALD);
                else if (percentChance(0.8))
                    return new ItemStack(Material.DIAMOND);
                else if (percentChance(1.8))
                    return new ItemStack(Material.GOLD_ORE);
                else if (percentChance(2.5))
                    return new ItemStack(Material.IRON_ORE);
                else if (percentChance(3))
                    return new ItemStack(Material.REDSTONE);
                else if (percentChance(3.7))
                    return new ItemStack(Material.INK_SACK, 1, (short) 4);
                else if (percentChance(4.3))
                    return new ItemStack(Material.COAL);
            }
        }
        return new ItemStack(Material.AIR);
    }

    private void sendDrop(Player p, ItemStack drop, Location loc) {
        ItemStack item = p.getItemInHand();

        if (item == null || item.getType().equals(Material.AIR))
            return;

        if (drop == null || drop.getType().equals(Material.AIR))
            return;

        int xp = 0;
        if (item.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS)) {
            int level = 0;
            int lenchant = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);

            if (lenchant == 1) {
                level = 0;
            } else if (lenchant >= 2) {
                level = 1;
            }

            drop.setAmount(1);
            xp = 0;
            if (drop.getType() == Material.EMERALD || drop.getType() == Material.DIAMOND) {
                int a = random.nextInt(level) + 1;
                drop.setAmount(1 + a);
                xp = 5;
            } else if (drop.getType() == Material.COAL) {
                xp = 1;
            } else if (drop.getType().equals(Material.COBBLESTONE)) {
                drop = null;
            } else if (drop.getType() == Material.LAPIS_BLOCK) {
                xp = 4;
                drop = new ItemStack(Material.INK_SACK, 1, (short) 4);
                drop.setAmount(2 * (level + 1)); // era 3*
            } else if (drop.getType() == Material.REDSTONE) {
                xp = 3;
                drop.setAmount(1 * (level + 1)); // era 2*
            }
        } else if (item.containsEnchantment(Enchantment.SILK_TOUCH)) {
            drop.setAmount(1);
            xp = 0;
            if (drop.getType() == Material.EMERALD) {
                drop = new ItemStack(Material.EMERALD_ORE);
            } else if (drop.getType() == Material.DIAMOND) {
                drop = new ItemStack(Material.DIAMOND_ORE);
            } else if (drop.getType() == Material.COAL) {
                drop = new ItemStack(Material.COAL_ORE);
            } else if (drop.getType() == Material.LAPIS_BLOCK) {
                drop = new ItemStack(Material.LAPIS_ORE);
            } else if (drop.getType() == Material.REDSTONE) {
                drop = new ItemStack(Material.REDSTONE_ORE);
            }
        } else {
            drop.setAmount(1);
            xp = 0;
            if (drop.getType() == Material.EMERALD || drop.getType() == Material.DIAMOND) {
                xp = 5;
            } else if (drop.getType() == Material.COAL) {
                xp = 1;
            } else if (drop.getType() == Material.LAPIS_BLOCK) {
                xp = 4;
                drop = new ItemStack(Material.INK_SACK, 1, (short) 4);
                drop.setAmount(3);
            } else if (drop.getType().equals(Material.COBBLESTONE)) {
                drop = null;
            } else if (drop.getType() == Material.REDSTONE) {
                xp = 3;
                drop.setAmount(2);
            }
        }

        /*
         * NBTItemStack nbt = new NBTItemStack(item);
         *
         * if (nbt.getBoolean("isEnchantDerreter")) { if
         * (drop.getType().equals(Material.IRON_ORE)) { drop = new
         * ItemStack(Material.IRON_INGOT); } else if
         * (drop.getType().equals(Material.GOLD_ORE)) { drop = new
         * ItemStack(Material.GOLD_INGOT); } }
         */
        if (hasSmeltEnchant(item)) {
            if (drop.getType().equals(Material.IRON_ORE)) {
                drop = new ItemStack(Material.IRON_INGOT);
            } else if (drop.getType().equals(Material.GOLD_ORE)) {
                drop = new ItemStack(Material.GOLD_INGOT);
            }
        }

        if (p.getInventory().firstEmpty() == -1) {
            p.sendMessage("§cSeu inventário está cheio.");
            loc.getWorld().dropItemNaturally(loc, drop);
        } else {
            p.getInventory().addItem(drop);
        }
        if (xp > 0) {
            p.giveExp(xp);
        }
    }

    protected final boolean hasSmeltEnchant(ItemStack item) {
        if ((!item.hasItemMeta()) || (!item.getItemMeta().hasLore()))
            return false;
        List<String> lore = item.getItemMeta().getLore();
        for (String str : lore) {
            if (str.contains("§7Derretedor I"))
                return true;
        }
        return false;
    }

    protected final boolean percentChance(double percent) {

        if (percent < 0 || percent > 100)
            throw new IllegalArgumentException("A percentagem nao pode ser maior do que 100 nem menor do que 0");

        double result = random.nextDouble() * 100;

        return result <= percent;

    }
}