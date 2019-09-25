package com.firefliesalco.www.skills.skills;

import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.skills.Multi;
import com.firefliesalco.www.skills.Skill;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class SkillProspector extends Skill {

    Multi multi = new Multi(false, 0);

    public int getMaxLevel(){
        return 10;
    }

    public String getName(){
        return "Prospector";
    }

    public String getParentName(){
        return null;
    }

    public int getRequiredLevel() { return 0; }

    public int getX(){
        return 0;
    }

    public int getY(){
        return 0;
    }

    public Material getDisplayMaterial(){
        return Material.GOLD_NUGGET;
    }

    public ChatColor getDisplayColor(){
        return ChatColor.YELLOW;
    }

    public String getDescription(int level){
        if(level == getMaxLevel())
            return "Increases the money you make by &b100%";
        return "Increases the money you make by &b" + ((level+1)*10) + "%";
    }

    public void onBuy(Inmate i, int level){
        i.setMulti("prospector", multi, 1.0 + 0.1 * (double)level);
        i.updateMulti();
    }

    public void onRemove(Inmate i){
        i.removeMulti("prospector");
    }

}
