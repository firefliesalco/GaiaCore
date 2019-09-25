package com.firefliesalco.www.handlers;

import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

    private GaiaCore core; // The main class of the plugin

    /**
     * Passes in the core and begins a loop to update the scoreboard
     * @param core The main plugin
     */
    public ScoreboardHandler(GaiaCore core){
        this.core = core;
        new BukkitRunnable(){
            public void run(){
                for(Player p : core.getServer().getOnlinePlayers())
                    createScoreboard(p);
            }
        }.runTaskTimer(core, 200L, 100L);
    }

    /**
     * Creates the scoreboard for p and adds the data
     * @param p The player to create the scoreboard for
     */
    private void createScoreboard(Player p){
        Inmate in = core.getInmate(p);
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("prisonboard", "dummy", ChatColor.GREEN + "" + ChatColor.BOLD + "Gaia" + ChatColor.GRAY + ChatColor.BOLD + "Prison");

        obj.getScore(ChatColor.BLUE + "").setScore(3);

        String balance = ChatColor.GRAY + "Balance: " + ChatColor.GREEN + "$" + in.getNiceMoney();
        obj.getScore(balance).setScore(2);

        String tokens = ChatColor.GRAY + "Tokens: " + ChatColor.GREEN + "" + in.getNiceTokens();
        obj.getScore(tokens).setScore(1);

        obj.getScore(ChatColor.GRAY + "Rank: " + in.getCompressedDisplay()).setScore(4);

        String points = ChatColor.GRAY + "Skill Points: " + ChatColor.GREEN + in.points[0] + "|" + in.points[1] + "|" + in.points[2];
        obj.getScore(points).setScore(0);



        obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        p.setScoreboard(board);

    }

}
