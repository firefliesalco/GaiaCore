package com.firefliesalco.www.skills;

import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.skills.skills.SkillChipmunk;
import com.firefliesalco.www.skills.skills.SkillProspector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.awt.*;
import java.util.ArrayList;

public class SkillHandler {

    public ArrayList<Skill> skillList = new ArrayList<>();
    public GaiaCore core;

    public SkillTree prestigeTree;
    public SkillTree masteryTree;
    public SkillTree ascensionTree;


    public SkillHandler(GaiaCore core){
        this.core = core;
        prestigeTree = new SkillTree(this, "Prestige", 0);
        masteryTree = new SkillTree(this, "Mastery", 1);
        ascensionTree = new SkillTree(this, "Ascension", 2);
        addSkills();
    }

    public void addSkills(){
        //Prestige Tree Skills
        addSkill(new SkillProspector(), prestigeTree);
        addSkill(new SkillChipmunk(), prestigeTree);

    }

    public void addSkill(Skill s, SkillTree st){
        skillList.add(s);
        st.skillList.add(s);
    }


    public void skillInventoryClick(InventoryClickEvent event){
        if(event.getCurrentItem()==null||event.getCurrentItem().getType()== Material.BLACK_STAINED_GLASS_PANE||event.getCurrentItem().getItemMeta()==null)
            return;
        SkillTree tree = getTree(event.getInventory().getName().split(" ")[0]);

        String[] pos = event.getInventory().getItem(80).getItemMeta().getLore().get(0).split(": ")[1].split(", ");
        int currentX = Integer.parseInt(pos[0]);
        int currentY = Integer.parseInt(pos[1]);
        Player p = (Player)event.getWhoClicked();
        switch(event.getRawSlot()){
            case 4:
                tree.openSkills(p, currentX, currentY-1);
                break;
            case 36:
                tree.openSkills(p, currentX-1, currentY);
                break;
            case 44:
                tree.openSkills(p, currentX+1, currentY);
                break;
            case 76:
                tree.openSkills(p, currentX, currentY+1);
                break;
            case 80:
                tree.openSkills(p, 0, 0);
                break;
            default:
                tree.skillClick(p, event.getCurrentItem(), currentX, currentY);
        }
    }

    public void openSkills(Player p){
        Inmate inmate = core.getInmate(p);
        Inventory inv = Bukkit.createInventory(null, 9, "Skill Trees");

        ItemStack prestige = new ItemStack(Material.GOLD_NUGGET);
        ItemMeta prestigeMeta = prestige.getItemMeta();
        prestigeMeta.setDisplayName(ChatColor.GOLD + "Prestige Skills");
        List<String> prestigeLore = new ArrayList<>();
        prestigeLore.add(ChatColor.GRAY + "" + inmate.points[0] + " Points");
        prestigeMeta.setLore(prestigeLore);
        prestige.setItemMeta(prestigeMeta);
        inv.setItem(2, prestige);

        ItemStack mastery = new ItemStack(Material.GOLD_INGOT);
        ItemMeta masteryMeta = mastery.getItemMeta();
        masteryMeta.setDisplayName(ChatColor.GOLD + "Mastery Skills");
        List<String> masteryLore = new ArrayList<>();
        masteryLore.add(ChatColor.GRAY + "" + inmate.points[1] + " Points");
        masteryMeta.setLore(masteryLore);
        mastery.setItemMeta(masteryMeta);
        inv.setItem(4, mastery);

        ItemStack asc = new ItemStack(Material.GOLD_BLOCK);
        ItemMeta ascM = asc.getItemMeta();
        ascM.setDisplayName(ChatColor.GOLD + "Ascension Skills");
        List<String> ascL = new ArrayList<>();
        ascL.add(ChatColor.GRAY + "" + inmate.points[2] + " Points");
        ascM.setLore(ascL);
        asc.setItemMeta(ascM);
        inv.setItem(6, asc);

        p.openInventory(inv);

    }

    public Skill getSkill(String name){
        for(Skill s : skillList){
            if(s.getName().equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public SkillTree getTree(String s){
        switch (s){
            case "Prestige": {
                return prestigeTree;
            }
            case "Mastery": {
                return masteryTree;
            }
            case "Ascension": {
                return ascensionTree;
            }
        }
        return prestigeTree;
    }

}
