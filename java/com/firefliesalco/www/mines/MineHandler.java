package com.firefliesalco.www.mines;

import com.firefliesalco.www.Changeables;
import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.bosses.BossDifficulty;
import com.firefliesalco.www.bosses.BossReward;
import javafx.scene.control.TextFormatter;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.IChatBaseComponent;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Boss;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import sun.reflect.generics.tree.Tree;

import java.util.*;

public class MineHandler {

    private int bossCountdown = 0; // The count left until the boss spawns

    private ArrayList<Mine> mines = new ArrayList<>(); // The list of  mines
    private GaiaCore core; // The main plugin class
    private Random r = new Random();

    private Map<Player, Integer> bossDamage = new HashMap<>(); // The damage done to the boss by each player

    private int bossHealth = 0; // The boss' current health
    private int maxHealth; // The boss' max health
    private BossDifficulty dif; // The difficulty of the current boss

    /**
     * Passes the plugin to MineHandler and starts the reset loop
     * @param core the plugin instance
     */
    public MineHandler(GaiaCore core){
        this.core = core;
        mines = core.configHandler.loadMines();
        new BukkitRunnable(){
            @Override
            public void run(){
                resetCheck();
            }
        }.runTaskTimer(core, 300L, 400L);
    }

    /**
     * Checks if the mines are ready to be reset
     */
    private void resetCheck(){
        int resetCount = 0;
        for(int i = 0; i < mines.size(); i++){
            Mine m = mines.get(i);
            if(m.getMineType() == MineType.NORMAL && m.getBlocksMined()>= Changeables.MINE_RESET){
                resetCount++;
                m.reset(core);
            }
        }
        if(resetCount > 0)
            core.getServer().broadcastMessage(Changeables.CHAT + "There have been " + Changeables.CHAT_INFO + resetCount + Changeables.CHAT + " mines reset.");
        bossCountdown += resetCount;
        if(bossCountdown >= Changeables.BOSS_REQ){
            if(maxHealth == 0) {
                spawnBoss();
            }
            bossCountdown=0;
        }
    }

    /**
     * Spawns a boss of random difficulty
     */
    private void spawnBoss(){

        List<BossDifficulty> difs = new ArrayList<>();
        for(BossDifficulty d : BossDifficulty.values()) {
            for (int i = 0; i < d.getWeight(); i++) {
                difs.add(d);
            }
        }
        dif = difs.get(r.nextInt(difs.size()));

        int bossHealth2 = (int)(((double)dif.getHealth())*Math.sqrt(core.getServer().getOnlinePlayers().size()));
        core.getServer().broadcastMessage(Changeables.CHAT + "A boss of difficulty " + dif.getColor() + dif.toString() + Changeables.CHAT + " has spawned with " + Changeables.CHAT_INFO + bossHealth2 + Changeables.CHAT + " health!");
        bossHealth = bossHealth2;
        maxHealth = bossHealth2;
        bossDamage = new HashMap<>();

        Mine bm = getBossMine();
        bm.reset(core);

        BukkitRunnable br = new BukkitRunnable() {
            @Override
            public void run() {

                if(bm.getBlocksMined() >= 0.2)
                    bm.reset(core);

            }
        };
        br.runTaskTimer(core, 10L, 200L);
        new BukkitRunnable(){
            @Override
            public void run(){

                if(bossHealth<=0){
                    maxHealth=0;
                    bossKilled();
                    br.cancel();
                    this.cancel();
                }

                String bar = dif.getColor() + dif.toString() + " BOSS: " + createHealthBar(bossHealth,maxHealth,30) + " " + Changeables.CHAT_INFO + bossHealth + Changeables.CHAT + "/" + Changeables.CHAT_INFO + maxHealth;

                for(Player p : core.getServer().getOnlinePlayers()){
                    sendActionBar(p,bar);
                }

            }
        }.runTaskTimer(core, 10L, 10L);
        System.out.println(core.getServer().getScheduler().getPendingTasks().size() + " Running");

    }

    /**
     * Sends a message in the action bar to the player
     * @param player The player to receive the message
     * @param message The message to be sent
     */
    private static void sendActionBar(Player player, String message){

        CraftPlayer p = (CraftPlayer) player;

        IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
        PacketPlayOutChat packet = new PacketPlayOutChat(cbc, ChatMessageType.GAME_INFO);
        p.getHandle().playerConnection.sendPacket(packet);
    }

    /**
     * Creates the health bar for the boss
     * @param currentHealth the current health
     * @param maxHealth the max health
     * @param size how many characters the health bar should be
     * @return the health bar put together
     */
    private String createHealthBar(int currentHealth, int maxHealth, int size){
        String output = "" + ChatColor.GREEN;
        int goodCount = (int)Math.ceil((double)size*(double)currentHealth/(double)maxHealth);
        for(int i = 0; i < size; i++){
            if(i==goodCount)
                output += ChatColor.RED;
            output += "|";
        }

        return output;
    }

    /**
     * Gets which mine the block is in, or null if none
     * @param b the block to check
     * @return the mine the block is in, or null if none
     */
    public Mine getMine(Block b){
        for(Mine m : mines){
            if(within(b, m.getL1(), m.getL2()))
                return m;
        }
        return null;
    }

    /**
     * Checks if the block is between two locations
     * @param block the block
     * @param a one corner
     * @param b the other corner
     * @return is the block within locations a and b
     */
    private boolean within(Block block, Location a, Location b){
        Location c = block.getLocation();
        return c.getX()>=a.getX()  &&  c.getY()>=a.getY()  &&  c.getZ()>=a.getZ()  &&
                c.getX() <= b.getX() && c.getY() <= b.getY() && c.getZ() <= b.getZ();
    }

    /**
     * Handles the boss being killed
     */
    private void bossKilled(){
        core.getServer().broadcastMessage(Changeables.CHAT + "The boss has been slain! ");
        Map<Player, Integer> sortedMap = sortMap(bossDamage);
        System.out.println(sortedMap.size() + " " + bossDamage.size());
        int i = 0;
        BossReward[][] allRewards = dif.getRewards();
        for(Player p : sortedMap.keySet()){
            if(++i==6)
                break;
            core.getServer().broadcastMessage(Changeables.CHAT + "  #" + i + ": " + Changeables.CHAT_INFO + p.getName() + Changeables.CHAT + " - " + Changeables.CHAT_INFO + sortedMap.get(p));
            if(i-1 < allRewards.length){
                Inmate in = core.getInmate(p);
                BossReward[] rewards = allRewards[i-1];
                String[] text = new String[rewards.length];
                int j = 0;
                for(BossReward reward : rewards){
                    reward.claim(in);
                    text[j++] = Changeables.CHAT_INFO + "" + reward.getAmount() + "x " + reward.getType().getName();
                }
                p.sendMessage(Changeables.CHAT + "You placed " + Changeables.CHAT_INFO + i + Changeables.CHAT + " in the boss fight and received " + String.join(Changeables.CHAT + " and ", text));
            }
        }
        System.out.println("end");
    }

    /**
     * Sorts the map
     * @param unsortedMap the map to be sorted
     * @return the map, sorted
     */
    private Map<Player, Integer> sortMap(Map<Player, Integer> unsortedMap){

        TreeMap<Player, Integer> map = new TreeMap<>((o1, o2) -> -unsortedMap.get(o1).compareTo(unsortedMap.get(o2)));
        map.putAll(unsortedMap);
        return map;
    }

    /**
     * Gets the boss mine
     * @return the boss mine
     */
    private Mine getBossMine(){
        for(Mine m : mines){
            if(m.getMineType() == MineType.BOSS)
                return m;
        }
        return null;
    }

    public List<Mine> getMines(){
        return mines;
    }

    public int getBossHealth(){
        return bossHealth;
    }

    public Map<Player, Integer> getBossDamage(){
        return bossDamage;
    }

    public void setBossHealth(int bossHealth) {
        this.bossHealth = bossHealth;
    }
}
