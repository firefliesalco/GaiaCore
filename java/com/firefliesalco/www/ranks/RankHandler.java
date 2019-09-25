package com.firefliesalco.www.ranks;

import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class RankHandler {


    private GaiaCore core; // The main plugin class

    private static final double RANKUP_BASE = 1000.0; // The base price of ranking up

    /**
     * Passes the plugin in to RankHandler
     * @param core the plugin instance
     */
    public  RankHandler(GaiaCore core){
        this.core = core;
    }

    /**
     * Attempts to rank up the player
     * @param arg1 the player attempting to rank up
     */
    public void rankup(Player arg1){
        Inmate inmate = core.getInmate(arg1);
        long price = rankupPrice(inmate);
        if(rankupState(inmate) == 0) {
            if (inmate.money >= price) {
                System.out.println(price);
                inmate.money -= price;
                inmate.ranks[0] += 1;
                arg1.sendMessage(ChatColor.AQUA + "Ranked up to " + ChatColor.GOLD + inmate.ranks[0] + ChatColor.AQUA + "!");
            }else{
                arg1.sendMessage(ChatColor.AQUA + "You require " + ChatColor.GOLD + getNiceMoney(price) + ChatColor.AQUA + " to rankup!");
            }
        } else {
            arg1.sendMessage(ChatColor.AQUA + "You have reached the ymax rank!  Use " + ChatColor.GOLD + "/prestige");
        }


    }

    /**
     * Attempts to rank up the player as much as possible
     * @param arg1 the player to rank up
     */
    public void maxRankup(Player arg1){
        Inmate inmate = core.getInmate(arg1);
        long price = rankupPrice(inmate);
        if(rankupState(inmate)==0) {
            if (inmate.money >= price) {
                while(inmate.ranks[0] < 100 && inmate.money >= price) {
                    inmate.money -= price;
                    inmate.ranks[0] += 1;
                    price = rankupPrice(inmate);
                }
                arg1.sendMessage(ChatColor.AQUA + "Ranked up to " + ChatColor.GOLD + inmate.ranks[0] + ChatColor.AQUA + "!");
            }else{
                arg1.sendMessage(ChatColor.AQUA + "You require " + ChatColor.GOLD + getNiceMoney(price) + ChatColor.AQUA + " to rankup!");
            }
        } else {
            arg1.sendMessage(ChatColor.AQUA + "You have reached the max rank!  Use " + ChatColor.GOLD + "/prestige");
        }


    }

    /**
     * Returns what level of ranking up the player is on
     * @param in the inmate to check
     * @return 0 for ranking up, 1 for prestiging, 2 for mastering, 3 for ascending
     */
    private int rankupState(Inmate in){
        if(in.ranks[0]==100)
            if(in.ranks[1]==500)
                if(in.ranks[2]==100)
                    return 3;
                else
                    return 2;
            else
                return 1;
        else
            return 0;
    }

    /**
     * Attempts to prestige the player
     * @param arg1 the player to prestige
     */
    public void prestige(Player arg1){
        Inmate inmate = core.getInmate(arg1);
        long price = prestigePrice(inmate);
        if(inmate.ranks[0] == 100) {
            if (inmate.ranks[1] < 500) {
                if (inmate.money >= price) {
                    inmate.money = 0;
                    inmate.ranks[1]++;
                    inmate.ranks[0] = 1;
                    inmate.points[0]++;
                    arg1.sendMessage(ChatColor.AQUA + "Prestiged up to " + ChatColor.GOLD + inmate.ranks[1] + ChatColor.AQUA + "!");
                } else {
                    arg1.sendMessage(ChatColor.AQUA + "You require " + ChatColor.GOLD + getNiceMoney(price) + ChatColor.AQUA + " to prestige!");
                }
            } else {
                arg1.sendMessage(ChatColor.AQUA + "You have reached the max prestige!  Use " + ChatColor.GOLD + "/master");
            }
        }else{
            arg1.sendMessage(ChatColor.AQUA + "You must be rank 100 to prestige!");
        }


    }

    /**
     * Attempts to increase the player's mastery level
     * @param arg1 the player to increase
     */
    public void master(Player arg1) {
        Inmate inmate = core.getInmate(arg1);
        long price = masteryPrice(inmate);
        if (inmate.ranks[0] == 100) {
            if (inmate.ranks[1] == 500) {
                if(inmate.ranks[2] < 100) {
                    if (inmate.money >= price) {
                        inmate.money = 0;
                        inmate.ranks[0] = 1;
                        inmate.ranks[1] = 0;
                        inmate.ranks[2]++;
                        inmate.points[0]=inmate.permPoints;
                        inmate.points[1]++;
                        inmate.resetSkills(core.skillHandler.prestigeTree);
                        arg1.sendMessage(ChatColor.AQUA + "Achieved mastery " + ChatColor.GOLD + inmate.ranks[2] + ChatColor.AQUA + "!");
                    } else {
                        arg1.sendMessage(ChatColor.AQUA + "You require " + ChatColor.GOLD + getNiceMoney(price) + ChatColor.AQUA + " to master!");
                    }
                }else{
                    arg1.sendMessage(ChatColor.AQUA + "You have reached the max mastery!  Use " + ChatColor.GOLD + "/ascend");
                }
            } else {
                arg1.sendMessage(ChatColor.AQUA + "You must be prestige 500 to use mastery!");
            }
        } else {
            arg1.sendMessage(ChatColor.AQUA + "You must be rank 100 to use mastery!");
        }


    }

    /**
     * Attempts to ascend the player
     * @param arg1 the player to ascend
     */
    public void ascend(Player arg1) {
        Inmate inmate = core.getInmate(arg1);
        long price = ascensionPrice(inmate);
        if (inmate.ranks[0] == 100) {
            if (inmate.ranks[1] == 500) {
                if (inmate.ranks[2] == 100) {
                    if (inmate.money >= price) {
                        inmate.money = 0;
                        inmate.ranks[0] = 1;
                        inmate.ranks[1] = 0;
                        inmate.ranks[2] = 0;
                        inmate.ranks[3]++;
                        inmate.points[0] = inmate.permPoints;
                        inmate.points[1] = 0;
                        inmate.points[2]++;
                        inmate.resetSkills(core.skillHandler.prestigeTree);
                        inmate.resetSkills(core.skillHandler.masteryTree);
                        arg1.sendMessage(ChatColor.AQUA + "Asended up to " + ChatColor.GOLD + inmate.ranks[3] + ChatColor.AQUA + "!");
                    } else {
                        arg1.sendMessage(ChatColor.AQUA + "You require " + ChatColor.GOLD + getNiceMoney(price) + ChatColor.AQUA + " to ascend!");
                    }
                } else {
                    arg1.sendMessage(ChatColor.AQUA + "You must be mastery 100 to ascend!");
                }
            } else {
                arg1.sendMessage(ChatColor.AQUA + "You must be prestige 500 to ascend!");
            }
        } else {
            arg1.sendMessage(ChatColor.AQUA + "You must be rank 100 to ascend!");
        }
    }

    /**
     * The price for a specified inmate to rankup
     * @param arg1 the inmate
     * @return the price to rankup
     */
    private long rankupPrice(Inmate arg1){
        int currentRank = arg1.ranks[0] - 1;
        int currentPrestige = arg1.ranks[1];
        int currentMastery = arg1.ranks[2];
        int currentAscension = arg1.ranks[3];
        return (long)(RANKUP_BASE * Math.pow((1.01+(.005*(Math.pow(currentPrestige,0.5)+Math.pow(currentMastery,0.75)+(double)currentAscension))),currentRank));
    }

    /**
     * The price for a specified inmate to prestige
     * @param arg1 the inmate
     * @return the price to prestige
     */
    private long prestigePrice(Inmate arg1){
        int currentPrestige = arg1.ranks[1];
        int currentMastery = arg1.ranks[2];
        int currentAscension = arg1.ranks[3];
        return (long)(RANKUP_BASE * Math.pow((1.01+(.005*(Math.pow(currentPrestige,0.5)+Math.pow(currentMastery,0.75)+(double)currentAscension))),130-Math.sqrt(currentPrestige)));
    }

    /**
     * The price for a specified inmate to increase mastery
     * @param arg1 the inmate
     * @return the price to increase mastery
     */
    private long masteryPrice(Inmate arg1){
        int currentMastery = arg1.ranks[2];
        int currentAscension = arg1.ranks[3];
        return (long)(RANKUP_BASE * Math.pow((1.01+(.005*(Math.pow(600-currentMastery,0.5)+Math.pow(currentMastery,0.75)+(double)currentAscension))),130-Math.sqrt(500)));
    }

    /**
     * The price for a specified inmate to ascend
     * @param arg1 the inmate
     * @return the price to ascend
     */
    private long ascensionPrice(Inmate arg1){
        int currentAscension = arg1.ranks[3];
        return (long)(RANKUP_BASE * Math.pow((1.01+(.005*(Math.pow(600-100,0.5)+Math.pow(100.0+(10.0/((double)currentAscension+1.0)),0.75)+(double)currentAscension))),130-Math.sqrt(500)));
    }

    /**
     * The price for a specified rank
     * @param arg1 the rank
     * @return the price to rankup
     */
    private long rankupPrice(int arg1){
        int currentRank = arg1;
        return 100L+(long)(Math.pow(currentRank, 2)-currentRank)*5l;
    }

    /**
     * Formats a money string nicely
     * @param money the string to format
     * @return the formatted string
     */
    private String getNiceMoney(long money){
        if(money < 1000)
            return money + "";
        String m = Long.toString(money);
        int e = m.length()-1;
        return m.charAt(0) + "." + m.charAt(1) + m.charAt(2) + "e" + e;
    }

}
