package com.firefliesalco.www.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomEnchantment {

    private String displayName; // The displayed name of the enchantment
    private long maxLevel; // The max level an enchantment can reach
    private int startingPrice; // The starting price of an enchantment
    private int priceIncrease; // The increase in price per level of an enchantment
    private long maxIncreaseLevel; // The max level the price continues to increase for
    private Material icon; // The material displayed in the enchantment menu
    private ChatColor color; // The color of the name in the enchantment menu


    /**
     * Creates a new enchantment
     * @param name the enchantment's display name
     * @param maxLevel the max level an enchantment can reach
     * @param maxIncreaseLevel the max level at which the enchantment will increase in price
     * @param startingPrice the starting price to upgrade the enchantment
     * @param priceIncrease the increase in price, per level, for the enchantment
     * @param icon the material to represent the enchantment in the menu
     * @param color the color for the enchantment's name
     */
    CustomEnchantment(String name, long maxLevel, long maxIncreaseLevel, int startingPrice, int priceIncrease, Material icon, ChatColor color){
        this.displayName = name;
        this.maxIncreaseLevel = maxIncreaseLevel;
        this.maxLevel = maxLevel;
        this.startingPrice = startingPrice;
        this.priceIncrease = priceIncrease;
        this.icon = icon;
        this.color = color;
    }

    /**
     * Returns the calculated price to increase the level of the enchantment for a given level
     * @param level the level to be calculated
     * @return the price for the level
     */
    public int getPrice(long level){
        return (int)Math.min(maxIncreaseLevel, level) * priceIncrease + startingPrice;
    }

    public String getDisplayName(){
        return displayName;
    }

    ChatColor getColor(){
        return color;
    }
    Material getIcon(){
        return icon;
    }

    public long getMaxLevel(){
        return maxLevel;
    }

    public long getMaxIncreaseLevel(){
        return maxIncreaseLevel;
    }





}
