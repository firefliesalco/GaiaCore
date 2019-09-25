package com.firefliesalco.www.enchants;

import com.firefliesalco.www.Changeables;
import javafx.scene.control.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class EnchantHandler {




    private HashMap<CustomEnchantment, Integer> enchants = new HashMap<>(); // Contains the list of possible enchants

    /**
     * Loads the enchants when the EnchantHandler class is intialized
     */
    public EnchantHandler(){
        loadEnchants();
    }

    /**
     * Loads the enchants
     */
    private void loadEnchants(){

        //addEnchant(new CustomEnchantment(name, maxLevel, maxIncreaseLevel, startingPrice, priceIncrease, displayItem, displayColor), slot);
        addEnchant(new CustomEnchantment("Fortune", 5000, 80, 1000, 50, Material.GOLD_INGOT, ChatColor.GOLD), 12);
        addEnchant(new CustomEnchantment("Confetti", 1000, 500, 3000, 100, Material.PINK_WOOL, ChatColor.LIGHT_PURPLE), 14);
        addEnchant(new CustomEnchantment("Chipper", 500, 500, 5000, 1000, Material.HOPPER, ChatColor.DARK_GRAY), 13);
        addEnchant(new CustomEnchantment("Boss Slayer", 100, 100, 10000, 10000, Material.IRON_SWORD, ChatColor.RED), 15);
    }

    /**
     * Adds a specific enchant to the menu and registers it
     * @param ce the enchant to add
     * @param position where to add it in the menu.
     */
    private void addEnchant(CustomEnchantment ce, int position) {
        enchants.put(ce, position);
    }

    /**
     *
     * Returns the level of the specified enchant for the item held in the given player's hand.
     * @param ce the enchant
     * @param p the player to check
     * @return the enchant level
     */
    public long getEnchantLevel(CustomEnchantment ce, Player p){
        List<String> lore = getLore(p);

        for(String s : lore){
            if(s.contains(ce.getDisplayName())){
                return Long.parseLong(s.split(" ")[s.split(" ").length-1]);
            }
        }
        return 0;


    }

    /**
     * Gets the lore for the player's held item
     * @param p The player to get the lore from
     * @return the lore of the item
     */
    private List<String> getLore(Player p){
        ItemStack is;
        ItemMeta im;
        List<String> lore;
        if(p == null || (is = p.getInventory().getItemInMainHand()) == null || (im = is.getItemMeta()) == null || (is.getType() != Material.DIAMOND_PICKAXE) || (lore = im.getLore()) == null)
            return new ArrayList<>();
        return lore;
    }

    /**
     * Returns the level of the enchant with the given name for the item held in the given player's hand
     * @param ce the enchant
     * @param p the player to check
     * @return the enchant level
     */
    public long getEnchantLevel(String ce, Player p){
        List<String> lore = getLore(p);
        for(String s : p.getInventory().getItemInMainHand().getItemMeta().getLore()){
            if(s.contains(ce)){
                return Long.parseLong(s.split(" ")[s.split(" ").length-1]);
            }
        }
        return 0;


    }

    /**
     * Sets the enchant level for a specified enchant for the specified player's held item.
     * @param ce The enchant to set
     * @param p The player to set the level for
     * @param level The level of enchant to set
     */
    public void setEnchantLevel(CustomEnchantment ce, Player p, long level){
        ItemStack is;
        ItemMeta im;
        List<String> lore;
        if(p == null || (is = p.getInventory().getItemInMainHand()) == null || (im = is.getItemMeta()) == null || (is.getType() != Material.DIAMOND_PICKAXE)) return;
        lore = im.getLore();
        if(lore == null)
            lore = new ArrayList<>();
        boolean containsEnchant = false;
        for(int i = 0; i < lore.size(); i++){
            if(lore.get(i).contains(ce.getDisplayName())){
                containsEnchant = true;
                lore.set(i, ce.getColor() + ce.getDisplayName() + " " + level);
            }
        }
        if(!containsEnchant) lore.add(ce.getColor() + ce.getDisplayName() + " " + level);
        im.setLore(lore);
        is.setItemMeta(im);





    }

    /**
     * Creates a new inventory menu for the player that shows all the possible enchants they can buy.
     * @param p the player to create the inventory for
     * @return the created inventory
     */
    public Inventory getEnchantInventory(Player p){
        Inventory inv = Bukkit.createInventory(null, 27, "Enchant Pickaxe");
        for(CustomEnchantment ce : enchants.keySet()){
            ItemStack item = new ItemStack(ce.getIcon());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ce.getColor() + ce.getDisplayName());
            List<String> lore = new ArrayList<>();
            lore.add("");
            lore.add(Changeables.CHAT + "Current Level: " + Changeables.CHAT_INFO + getEnchantLevel(ce, p));
            lore.add(Changeables.CHAT + "Max Level: " + Changeables.CHAT_INFO + ce.getMaxLevel());
            lore.add(Changeables.CHAT + "Current Price: " + Changeables.CHAT_INFO + ce.getPrice(getEnchantLevel(ce, p)));
            lore.add("");
            lore.add(Changeables.CHAT + "Left Click to " + Changeables.CHAT_INFO + " Buy 1");
            lore.add(Changeables.CHAT + "Right Click to " + Changeables.CHAT_INFO + " Buy Max");
            meta.setLore(lore);
            item.setItemMeta(meta);
            inv.setItem(enchants.get(ce), item);
        }





        return inv;
    }

    /**
     * Gets the enchant with the specified name, or null if none are found.
     * @param s The name of the enchant
     * @return the enchant with name s, or null
     */
    public CustomEnchantment getEnchant(String s){
        for(CustomEnchantment ce : enchants.keySet()){
            if(ce.getDisplayName().equalsIgnoreCase(s))
                return ce;
        }
        return null;
    }

}
