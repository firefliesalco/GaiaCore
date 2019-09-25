package com.firefliesalco.www.handlers;

import com.firefliesalco.www.Changeables;
import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.enchants.CustomEnchantment;
import com.firefliesalco.www.mines.Mine;
import com.firefliesalco.www.mines.MineType;
import com.firefliesalco.www.skills.Skill;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import javafx.scene.control.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CommandHandler implements CommandExecutor {

    private GaiaCore core; // The main class of the plugin

    /**
     * Initalizes the CommandHandler with the main class passed in and sets this object as the commands' executor.
     *
     * @param core The plugin core
     */
    public CommandHandler(GaiaCore core){
        this.core = core;
        List<String> balance = new ArrayList<>();

        String[] commands = {"balance", "rankup", "prestige", "master", "maxrankup", "skills", "upgrade", "admin", "ascend", "pickaxe"}; // A list of possible commands
        for(String s : commands){
            core.getCommand(s).setExecutor(this);
        }
    }


    /**
     * Controls the actions taken when a command is run.
     * @param sender the command sender
     * @param command the command being sent
     * @param label the label of the command sent
     * @param args the arguments sent with the command
     * @return whether the command should be executed
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if(command.getName().equalsIgnoreCase("admin")) {
            if (admin(sender)) {
                adminCommand(sender, args);
            } else {
                sender.sendMessage("Only firefliesalco can use this command :P");
            }
        } else if(sender instanceof Player){
            Player p = (Player) sender;
            if(command.getName().equalsIgnoreCase("balance")){
                p.sendMessage(ChatColor.GOLD + "$" + core.getInmate(p).money);
            }
            else if(command.getName().equalsIgnoreCase("rankup")){
                core.rankHandler.rankup(p);
            }
            else if(command.getName().equalsIgnoreCase("maxrankup")){
                core.rankHandler.maxRankup(p);
            }
            else if(command.getName().equalsIgnoreCase("prestige")){
                core.rankHandler.prestige(p);
            }
            else if(command.getName().equalsIgnoreCase("master")){
                core.rankHandler.master(p);
            }
            else if(command.getName().equalsIgnoreCase("ascend")){
                core.rankHandler.ascend(p);
            }
            else if(command.getName().equalsIgnoreCase("skills")){
                core.skillHandler.openSkills(p);
            }
            else if(command.getName().equalsIgnoreCase("upgrade")){
                p.openInventory(core.enchantHandler.getEnchantInventory(p));
            }
            else if(command.getName().equalsIgnoreCase("pickaxe")){
                ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
                is.addUnsafeEnchantment(Enchantment.DIG_SPEED, 100);
                ItemMeta im = is.getItemMeta();
                im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS);
                im.setUnbreakable(true);
                is.setItemMeta(im);
                p.getInventory().addItem(is);
            }








        }
        return true;
    }

    /**
     * Handles the execution of admin-specific commands
     * @param s the command's sender
     * @param args the arguments sent with the command
     */
    private void adminCommand(CommandSender s, String[] args){
        String name = args.length==0?"help":args[0];
        if(name.equalsIgnoreCase("help")){
            s.sendMessage("/admin enchant <enchant> <level>");
            s.sendMessage("/admin createmine <NORMAL | BOSS> - Hold block type in hand and use worldedit for selection");
            s.sendMessage("/admin token <player> <amount>");
            s.sendMessage("/admin balance <player> <amount>");
            s.sendMessage("/admin rank <player> <0 | 1 | 2 | 3> <level>");
            s.sendMessage("/admin skillpoints <player> <0 | 1 | 2> <amount>");
            s.sendMessage("/admin resetinmate <player>");
        }else if(name.equalsIgnoreCase("token")){
            if(args.length == 3){
                core.getInmate(core.getServer().getPlayer(args[1])).tokens += Long.parseLong(args[2]);
                s.sendMessage("Gave Tokens");
            }else{
                s.sendMessage("Correct usage: /admin token <player> <amount>");
            }
        }else if(name.equalsIgnoreCase("balance")){
            if(args.length == 3){
                core.getInmate(core.getServer().getPlayer(args[1])).money += Long.parseLong(args[2]);
                s.sendMessage("Gave Money");
            }else{
                s.sendMessage("Correct usage: /admin balance <player> <amount>");
            }
        }else if(name.equalsIgnoreCase("rank")){
            if(args.length == 4){
                core.getInmate(core.getServer().getPlayer(args[1])).ranks[Integer.parseInt(args[2])] = Integer.parseInt(args[3]);
                s.sendMessage("Set Rank");
            }else{
                s.sendMessage("Correct usage: /admin rank <player> <0 | 1 | 2 | 3> <level>");
            }
        }else if(name.equalsIgnoreCase("skillpoints")){
            if(args.length == 4){
                core.getInmate(core.getServer().getPlayer(args[1])).points[Integer.parseInt(args[2])] = Integer.parseInt(args[3]);
                s.sendMessage("Gave Skill Points");
            }else{
                s.sendMessage("Correct usage: /admin skillpoints <player> <0 | 1 | 2> <amount>");
            }
        }else if(name.equalsIgnoreCase("resetinmate")){
            if(args.length == 2){
                Player p = core.getServer().getPlayer(args[1]);
                core.inmates.remove(core.getInmate(p));
                core.inmates.put(p.getUniqueId().toString(), new Inmate(new HashMap<>(), new int[]{1,0,0,0},0,0,new int[3], 0));
                s.sendMessage("Inmate Reset");
            }else{
                s.sendMessage("Correct usage: /admin resetinmate <player>");
            }
        } else if(s instanceof Player){
            Player p = (Player) s;
            if(name.equalsIgnoreCase("enchant")){
                if(args.length >= 3){
                    StringBuilder buf = new StringBuilder(args[1]);

                    for(int i = 2; i < args.length-1; i++){
                        buf.append(" ").append(args[i]);
                    }
                    String string = buf.toString();
                    CustomEnchantment ce = core.enchantHandler.getEnchant(string);
                    if(ce == null)
                        p.sendMessage(Changeables.ERROR + "Invalid enchant: " + string);
                    else {
                        core.enchantHandler.setEnchantLevel(ce, p, Long.parseLong(args[args.length - 1]));
                        p.sendMessage("Enchanted");
                    }
                }else{
                    p.sendMessage("Correct usage: /admin enchant <enchant> <level>");
                }
            }else if(name.equalsIgnoreCase("createmine")){
                if(args.length == 2){
                    if(core.worldEdit != null) {
                        try {
                            Region r = core.worldEdit.getSession(p).getRegionSelector(core.worldEdit.getSession(p).getSelectionWorld()).getRegion();

                            BlockVector3 min = r.getMinimumPoint();
                            BlockVector3 max = r.getMaximumPoint();
                            core.mineHandler.getMines().add(new Mine(new Location(p.getWorld(), min.getX(), min.getY(), min.getZ()), new Location(p.getWorld(), max.getX(), max.getY(), max.getZ()), p.getInventory().getItemInMainHand().getType(), MineType.valueOf(args[0])));
                            p.sendMessage(Changeables.CHAT + "Mine Created");
                        }catch(IncompleteRegionException e){
                            e.printStackTrace();
                        }
                    }else{
                        p.sendMessage(Changeables.ERROR + "Error getting WorldEdit");
                    }
                }else{
                    p.sendMessage("Correct usage: /admin createmine <NORMAL | BOSS> - Hold block type in hand and use worldedit for selection");
                }
            }else{
                p.sendMessage("Command not found. Run /admin help for a list of commands");
            }
        }else{
            s.sendMessage("Command not found. Run /admin help for a list of commands");
        }
    }

    /**
     * Checks if the CommandSender is an admin
     * @param s the command sender
     * @return is the sender an admin
     */
    private boolean admin(CommandSender s){
        return s.hasPermission("prisonadmin")&&((s instanceof Player && (s).getName().equalsIgnoreCase("firefliesalco")));
    }

}
