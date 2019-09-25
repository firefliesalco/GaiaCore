package com.firefliesalco.www.handlers;

import com.firefliesalco.www.GaiaCore;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatHandler implements Listener {

    private GaiaCore core; // The main class of the plugin

    /**
     * Creates the ChatHandler instance with the core passed in
     * @param core The plugin instance
     */
    public ChatHandler(GaiaCore core){
        this.core = core;
    }

    /**
     * Handles the prefix of players when talking in chat
     * @param e The chat event
     */
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e){
        e.setFormat(core.getInmate(e.getPlayer()).getRankDisplay() + " " + ChatColor.GREEN + "%1$s" + ChatColor.WHITE + ": %2$s");
    }

}
