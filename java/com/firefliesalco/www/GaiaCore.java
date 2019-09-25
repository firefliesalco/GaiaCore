package com.firefliesalco.www;

import com.firefliesalco.www.enchants.EnchantHandler;
import com.firefliesalco.www.handlers.*;
import com.firefliesalco.www.mines.MineHandler;
import com.firefliesalco.www.ranks.RankHandler;
import com.firefliesalco.www.skills.SkillHandler;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@SuppressWarnings("all")
public class GaiaCore extends JavaPlugin implements Listener {

    public HashMap<String, Inmate> inmates = new HashMap<>();
    public RankHandler rankHandler;
    public EnchantHandler enchantHandler;
    public ConfigHandler configHandler;
    public SkillHandler skillHandler;
    public MineHandler mineHandler;

    public WorldEditPlugin worldEdit;
    public WorldGuardPlugin worldGuard;

    @Override
    public void onEnable(){

        worldEdit = (WorldEditPlugin)getServer().getPluginManager().getPlugin("WorldEdit");
        worldGuard = (WorldGuardPlugin) getServer().getPluginManager().getPlugin("WorldGuard");

        System.out.println("I'm alive yay - no");

        skillHandler = new SkillHandler(this);
        configHandler = new ConfigHandler(this);


        for(Player p : getServer().getOnlinePlayers()){
            configHandler.loadPlayer(p);
        }

        mineHandler = new MineHandler(this);



        rankHandler = new RankHandler(this);

        new CommandHandler(this);
        this.enchantHandler = new EnchantHandler();
        new ScoreboardHandler(this);


        getServer().getPluginManager().registerEvents(new MiningHandler(this), this);
        getServer().getPluginManager().registerEvents(new InventoryHandler(this), this);
        getServer().getPluginManager().registerEvents(new ChatHandler(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        System.out.println("yes actually is enabled");



    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        configHandler.loadPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        configHandler.savePlayer(event.getPlayer());
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event){
        event.setFormat(event.getFormat().replace("{ranks}", getInmate(event.getPlayer()).getRankDisplay()));
    }

    @Override
    public void onDisable(){
        configHandler.save();
        System.out.println("But I was so young");
    }

    public Inmate getInmate(Player p){
        return inmates.get(p.getUniqueId().toString());
    }




}
