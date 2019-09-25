package com.firefliesalco.www.skills.skills;

import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.skills.Multi;
import com.firefliesalco.www.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SkillChipmunk extends Skill {

    public int getMaxLevel(){
        return 10;
    }

    public String getName(){
        return "Chipper";
    }

    public String getParentName(){
        return "Prospector";
    }

    public int getRequiredLevel() { return 5; }

    public int getX(){
        return 0;
    }

    public int getY(){
        return -1;
    }

    public Material getDisplayMaterial(){
        return Material.HOPPER;
    }

    public ChatColor getDisplayColor(){
        return ChatColor.GRAY;
    }

    public String getDescription(int level){
        if(level == getMaxLevel())
            return "Increases the chance of activating chipper by &b50%";
        return "Increases the chance of activating chipper by &b" + ((level+1)*10) + "%";
    }

    public void onBuy(Inmate i, int level){

    }

    public void onRemove(Inmate i){}

}
