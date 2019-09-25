package com.firefliesalco.www.mines;

import com.firefliesalco.www.GaiaCore;
import org.bukkit.Material;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Mine {

    private Location l1, l2; // The minimum and maximum corners of the mine
    private Material block; // The block the mine is made out of
    private MineType mineType; // The type of mine

    /**
     * Create a mine
     * @param l1 // The minimum corner of the mine
     * @param l2 // The maximum corner of the mine
     * @param m // The block the mine is made of
     * @param mineType // The type of mine
     */
    public Mine(Location l1, Location l2, Material m, MineType mineType){
        this.l1 = l1;
        this.l2 = l2;
        this.block = m;
        this.mineType = mineType;
    }

    /**
     * Resets the mine and teleports players
     * @param core the plugin
     */
    void reset(GaiaCore core){
        for(Player p : core.getServer().getOnlinePlayers()){
            if(isInside(p.getLocation()))
                teleport(p);
        }
        for(int x = l1.getBlockX(); x <= l2.getBlockX(); x++){
            for(int y = l1.getBlockY(); y <= l2.getBlockY(); y++){
                for(int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++){
                    Block b = l1.getWorld().getBlockAt(x, y, z);
                    if(b.getType() == Material.AIR)
                        b.setType(block);
                }
            }
        }
    }

    /**
     * Checks if the location is inside the mine
     * @param l the location
     * @return is the location inside the mine
     */
    private boolean isInside(Location l){
        if(l.getX() >= l1.getX() && l.getX() <= l2.getX()){
            if(l.getY() >= l1.getY() && l.getY() <= l2.getY()){
                return (l.getZ() >= l1.getZ() && l.getZ() <= l2.getZ());
            }
        }
        return false;
    }

    /**
     * Teleports the player one block increased in the x, y, and z dimensions from the maximum point of the mine.
     * @param p the player to teleport
     */
    private void teleport(Player p){
        p.teleport(l2.clone().add(1,1,1));
    }

    /**
     * Gets the number of block mined
     * @return the number of blocks mined
     */
    double getBlocksMined(){
        double mined = 0;
        double total = 0;
        for(int x = l1.getBlockX(); x <= l2.getBlockX(); x++){
            for(int y = l1.getBlockY(); y <= l2.getBlockY(); y++){
                for(int z = l1.getBlockZ(); z <= l2.getBlockZ(); z++){
                    Block b = l1.getWorld().getBlockAt(x, y, z);
                    total++;
                    if(b.getType() == Material.AIR)
                        mined++;
                }
            }
        }
        return mined/total;
    }


    public MineType getMineType(){
        return mineType;
    }

    public Location getL1() {
        return l1;
    }

    public Location getL2() {
        return l2;
    }

    public Material getBlock(){
        return block;
    }
}

