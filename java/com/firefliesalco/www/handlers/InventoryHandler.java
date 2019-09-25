package com.firefliesalco.www.handlers;

import com.firefliesalco.www.Changeables;
import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.enchants.CustomEnchantment;
import com.firefliesalco.www.skills.SkillTree;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;

public class InventoryHandler implements Listener {

    private GaiaCore core; // The main class of the plugin

    /**
     * Passes the main class into the InventoryHandler
     * @param core the plugin instance
     */
    public InventoryHandler(GaiaCore core){
        this.core = core;
    }

    /**
     * Handles the clicking in menus to execute the correct action
     * @param event the inventory click event
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        if(inv.getName().equals("Enchant Pickaxe")){
            Player p = (Player) event.getWhoClicked();
            event.setCancelled(true);
            //if(inv == p.getOpenInventory()) {
                if (event.getCurrentItem() != null) {
                    CustomEnchantment enchant = core.enchantHandler.getEnchant(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
                    System.out.println(enchant);
                    long currentLevel = core.enchantHandler.getEnchantLevel(enchant, p);
                    if (currentLevel < enchant.getMaxLevel()) {

                        if (enchant.getPrice(currentLevel) <= core.getInmate(p).tokens) {
                            if (event.getClick() == ClickType.LEFT) {
                                p.sendMessage(Changeables.CHAT + "Enchant " + Changeables.CHAT_INFO + enchant.getDisplayName() + Changeables.CHAT + " upgraded!");
                                core.enchantHandler.setEnchantLevel(enchant, p, currentLevel + 1);
                                core.getInmate(p).tokens -= enchant.getPrice(currentLevel);
                            } else if (event.getClick() == ClickType.RIGHT) {
                                long nextLevel = currentLevel;
                                long totalPrice = enchant.getPrice(nextLevel);
                                while (nextLevel < enchant.getMaxLevel() && nextLevel < enchant.getMaxIncreaseLevel() && core.getInmate(p).tokens >= totalPrice + enchant.getPrice(nextLevel + 1))
                                    totalPrice += enchant.getPrice(++nextLevel);
                                long more = 0;
                                if (enchant.getPrice(enchant.getMaxIncreaseLevel()) == 0)
                                    more = Long.MAX_VALUE;
                                else
                                    more = (core.getInmate(p).tokens - totalPrice) / enchant.getPrice(enchant.getMaxIncreaseLevel());
                                more = Math.min(enchant.getMaxLevel() - nextLevel, more);
                                p.sendMessage(Changeables.CHAT + "Enchant " + Changeables.CHAT_INFO + enchant.getDisplayName() + Changeables.CHAT + " upgraded " + Changeables.CHAT_INFO + (more + nextLevel - currentLevel) + Changeables.CHAT + " times");
                                core.enchantHandler.setEnchantLevel(enchant, p, nextLevel + more);
                                core.getInmate(p).tokens -= totalPrice + more * enchant.getPrice(enchant.getMaxIncreaseLevel());
                            }
                        } else {
                            p.sendMessage(Changeables.ERROR + "Can't afford enchant.");
                        }

                    } else {
                        p.sendMessage(Changeables.ERROR + "Enchant already max level.");
                    }
                //}
            }
        }

        if(event.getInventory().getName().equals("Skill Trees")){
            event.setCancelled(true);
            if(event.getCurrentItem()!=null&&event.getRawSlot()<81&&event.getRawSlot()>=0&&event.getCurrentItem().getType()!= Material.AIR) {
                SkillTree tree = core.skillHandler.getTree(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName().split(" ")[0]));
                tree.openSkills((Player)event.getWhoClicked(), 0, 0);
            }
        }

        if(event.getInventory().getName().contains("Skills")){
            event.setCancelled(true);
            if(event.getCurrentItem()!=null&&event.getRawSlot()<81&&event.getRawSlot()>=0&&event.getCurrentItem().getType()!= Material.AIR)
                core.skillHandler.skillInventoryClick(event);
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction() == (Action.RIGHT_CLICK_AIR) || event.getAction() == (Action.RIGHT_CLICK_BLOCK)){
            if(event.hasItem() && event.getItem().getType() == Material.DIAMOND_PICKAXE && event.getPlayer().isSneaking())
                event.getPlayer().openInventory(core.enchantHandler.getEnchantInventory(event.getPlayer()));
        }
    }

}
