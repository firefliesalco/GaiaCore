package com.firefliesalco.www;

import com.firefliesalco.www.skills.Multi;
import com.firefliesalco.www.skills.Skill;
import com.firefliesalco.www.skills.SkillTree;
import org.bukkit.ChatColor;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Inmate {

    public HashMap<Skill, Integer> skillLevels = new HashMap<>();
    public int[] ranks = new int[4];
    public long money = 0, tokens = 0;
    public double multi = 1.0;
    public int[] points = new int[3];
    public int permPoints = 0;

    public Map<String, Multi> multiNames = new HashMap<>();
    public Map<Multi, Double> multiValues = new TreeMap<>();

    public Inmate(HashMap<Skill, Integer> skillLevels, int[] ranks, long money, long tokens, int[] points, int permPoints){
        this.skillLevels = skillLevels;
        this.ranks = ranks;
        this.money = money;
        this.tokens = tokens;
        this.points = points;
        this.permPoints = permPoints;

        for(Skill s : skillLevels.keySet()){
            s.onBuy(this, skillLevels.get(s));
        }
    }


    public String getRankDisplay(){
        return (ranks[3]>0?ChatColor.WHITE + "[" + ChatColor.RED + "A" + ranks[3] + ChatColor.WHITE + "]":"")+(ranks[2]>0? ChatColor.WHITE + "[" + ChatColor.GOLD + "M" + ranks[2] + ChatColor.WHITE + "]":"") + (ranks[1]>0? ChatColor.WHITE + "[" + ChatColor.AQUA + "P" + ranks[1] + ChatColor.WHITE + "]":"") + ChatColor.WHITE + "[" + ChatColor.GRAY + ranks[0] + ChatColor.WHITE + "]";
    }

    public String getCompressedDisplay(){
        return (ranks[3]>0?ChatColor.RED + "A" + ranks[3]:"") + (ranks[2]>0?ChatColor.GOLD + "M" + ranks[2] : "") + (ranks[1]>0? ChatColor.AQUA + "P" + ranks[1] : "") + ChatColor.GRAY + "R" + ranks[0];
    }

    public void updateMulti(){
        double temp = 1.0;
        for(Multi m : multiValues.keySet()){
            if(m.add)
                temp+=multiValues.get(m);
            else
                temp*=multiValues.get(m);
        }
        multi = temp;
    }

    public void setMulti(String name, double value){
        multiValues.put(multiNames.get(name), value);
    }

    public void setMulti(String name, Multi m, double value){
        if(!multiNames.containsKey(name)) {
            multiNames.put(name, m);
        }
        multiValues.put(m, value);
    }

    public void removeMulti(String name){
        if(multiNames.containsKey(name)){
            Multi s = multiNames.get(name);
            multiNames.remove(name);
            multiValues.remove(s);
        }
    }

    public int getSkillLevel(Skill s){
        if(skillLevels.containsKey(s))
            return skillLevels.get(s);
        return 0;
    }

    public int getSkillLevel(String st){
        for(Skill s : skillLevels.keySet()){
            if(s.getName().equalsIgnoreCase(st))
                return skillLevels.get(s);
        }
        return 0;
    }

    public void resetSkills(SkillTree tree){
        for(Skill s : tree.skillList){
            if(skillLevels.containsKey(s)){
                s.onRemove(this);
                skillLevels.remove(s);
            }
            updateMulti();
        }
    }

    public String getNiceMoney(){
        if(money < 1000)
            return money + "";
        String m = Long.toString(money);
        int e = m.length()-1;
        return m.charAt(0) + "." + m.charAt(1) + m.charAt(2) + "e" + e;
    }

    public String getNiceTokens(){
        if(tokens < 1000)
            return tokens + "";
        String m = Long.toString(tokens);
        int e = m.length()-1;
        return m.charAt(0) + "." + m.charAt(1) + m.charAt(2) + "e" + e;
    }


}
