package com.firefliesalco.www.skills;

import com.firefliesalco.www.Inmate;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public abstract class Skill {

    /**
     * The max level for the skill
     * @return the skill's max level
     */
    public abstract int getMaxLevel();

    /**
     * The skill's name
     * @return the skill's name
     */
    public abstract String getName();

    /**
     *
     * @return the name of the parent skill
     */
    public abstract String getParentName();

    /**
     *
     * @return the required level of the parent skill to purchase
     */
    public abstract int getRequiredLevel();

    /**
     *
     * @return the x position in the skill tree
     */
    public abstract int getX();

    /**
     *
     * @return the y position in the skill tree
     */
    public abstract int getY();

    /**
     *
     * @return the material to represent this skill in the menu
     */
    public abstract Material getDisplayMaterial();

    /**
     *
     * @return the color of this skill's name in the menu
     */
    public abstract ChatColor getDisplayColor();

    /**
     *
     * @param level the level of this skill
     * @return the description for a specified level of the skill
     */
    public abstract String getDescription(int level);

    /**
     * what to do upon purchase of the skill
     * @param i what inmate has purchased
     * @param level the level of the skill purchased
     */
    public abstract void onBuy(Inmate i, int level);

    /**
     * what to do upon removal of the skill (usually upon prestiging)
     * @param i the inmate to remove the skill from
     */
    public abstract void onRemove(Inmate i);

    /**
     * If the item should be displayed given the menu's current coordinates
     * @param xCenter the center x of the menu
     * @param yCenter the center y of the menu
     * @return if the item should be displayed
     */
    boolean isShown(int xCenter, int yCenter){
        return getX() >= xCenter - 3 && getX() <= xCenter + 3 && getY() >= yCenter - 3 && getY() <= yCenter + 3;
    }

}
