package com.firefliesalco.www.skills;

import com.firefliesalco.www.Changeables;
import com.firefliesalco.www.Inmate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class SkillTree {

    public ArrayList<Skill> skillList = new ArrayList<Skill>();

    public SkillHandler handler;
    public String name;
    public int currency;

    public SkillTree(SkillHandler handler, String name, int currency) {
        this.name = name;
        this.handler = handler;
        this.currency = currency;
    }

    public void addSkill(Skill s){ skillList.add(s); }

    public void openSkills(Player p, int xCenter, int yCenter){
        Inmate in = handler.core.getInmate(p);
        Inventory inv = Bukkit.createInventory(null, 81, name + " Skills | " + in.points[currency] + " Points");

        ItemStack upArrow = new ItemStack(Material.ARROW);
        ItemMeta upArrowMeta = upArrow.getItemMeta();
        upArrowMeta.setDisplayName("Shift Up");
        upArrow.setItemMeta(upArrowMeta);
        inv.setItem(4, upArrow);

        ItemStack leftArrow = new ItemStack(Material.ARROW);
        ItemMeta leftArrowMeta = leftArrow.getItemMeta();
        leftArrowMeta.setDisplayName("Shift Left");
        leftArrow.setItemMeta(leftArrowMeta);
        inv.setItem(36, leftArrow);

        ItemStack rightArrow = new ItemStack(Material.ARROW);
        ItemMeta rightArrowMeta = rightArrow.getItemMeta();
        rightArrowMeta.setDisplayName("Shift Right");
        rightArrow.setItemMeta(rightArrowMeta);
        inv.setItem(44, rightArrow);

        ItemStack downArrow = new ItemStack(Material.ARROW);
        ItemMeta downArrowMeta = downArrow.getItemMeta();
        downArrowMeta.setDisplayName("Shift Down");
        downArrow.setItemMeta(downArrowMeta);
        inv.setItem(76, downArrow);

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);


        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                int slot = j+i*9;
                if((i==0||j==0||i==8||j==8)&&inv.getItem(slot)==null)
                    inv.setItem(slot, glass);
            }
        }


        for(Skill skill : skillList){
            if(skill.isShown(xCenter, yCenter)){
                int level = in.getSkillLevel(skill);
                Skill parent = getSkill(skill.getParentName());
                boolean locked = skill.getParentName() != null && in.getSkillLevel(parent)<skill.getRequiredLevel();

                ItemStack skillItem = new ItemStack(skill.getDisplayMaterial());
                ItemMeta skillItemMeta = skillItem.getItemMeta();
                String name = skill.getDisplayColor() + skill.getName() + " // ";
                if(locked)
                    name += ChatColor.BOLD + "LOCKED";
                else if(level == skill.getMaxLevel())
                    name += ChatColor.BOLD + "MAXED";
                else
                    name += level + "/" + skill.getMaxLevel();
                skillItemMeta.setDisplayName(name);
                List<String> lore = new ArrayList<>();
                String[] desc = ChatColor.translateAlternateColorCodes('&', skill.getDescription(level)).split(";");
                for(String s : desc)
                    lore.add(ChatColor.GRAY + s);
                if(locked)
                    lore.add(ChatColor.RED + "Requires " + parent.getName() + " " + in.getSkillLevel(parent) + "/" + skill.getRequiredLevel());
                skillItemMeta.setLore(lore);
                skillItem.setItemMeta(skillItemMeta);
                inv.setItem(convertPos(skill.getX()-xCenter, skill.getY()-yCenter), skillItem);
            }
        }


        ItemStack info = new ItemStack(Material.RED_WOOL);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(ChatColor.RED + "Click Me to Reset Position");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Current Position: " + xCenter + ", " + yCenter);
        infoMeta.setLore(lore);
        info.setItemMeta(infoMeta);
        inv.setItem(80, info);

        p.openInventory(inv);

    }




    public int convertPos(int x, int y){
        return 40+y*9+x;
    }


    public void skillClick(Player p, ItemStack is, int x, int y){


        Skill s = getSkill(ChatColor.stripColor(is.getItemMeta().getDisplayName().split(" //")[0]));
        Inmate im = handler.core.getInmate(p);
        int level = im.getSkillLevel(s);
        boolean locked = s.getParentName() != null && im.getSkillLevel(getSkill(s.getParentName()))<s.getRequiredLevel();
        if(level == s.getMaxLevel()){
            p.sendMessage(Changeables.ERROR + "Level of skill already at max!");
        }
        else if(locked){
            p.sendMessage(Changeables.ERROR + "Skill is still locked.");
        }
        else if(im.points[currency] == 0){
            p.sendMessage(Changeables.ERROR + "You don't have enough skill points for this skill!");
        }else{
            im.skillLevels.put(s, level+1);
            s.onBuy(im, level+1);
            p.sendMessage(Changeables.CHAT + "Skill " + Changeables.CHAT_INFO + s.getName() + Changeables.CHAT + " upgraded!");
            im.points[currency]--;
            openSkills(p, x, y);

        }




    }


    public Skill getSkill(String name){
        for(Skill s : skillList){
            if(s.getName().equalsIgnoreCase(name))
                return s;
        }
        return null;
    }


}
