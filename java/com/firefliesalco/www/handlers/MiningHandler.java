package com.firefliesalco.www.handlers;

import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.mines.Mine;
import com.firefliesalco.www.mines.MineType;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;

public class MiningHandler implements Listener {

    private GaiaCore core; // The main class of the plugin

    /**
     * Initializes the MiningHandler and passes in GaiaCore
     * @param core the plugin instance
     */
    public MiningHandler(GaiaCore core){
        this.core = core;
    }

    /**
     * Handles the players' mining and enchant chances
     * @param event the block break event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if(!event.isCancelled()){
            Mine mine;
            if(event.getPlayer() != null&&(mine=core.mineHandler.getMine(event.getBlock()))!=null) {
                blockBreak(event.getPlayer(), event.getBlock());
                if (chance((int) core.enchantHandler.getEnchantLevel("Confetti", event.getPlayer()), 4000)) {
                    for (int i = 0; i < 5; i++) {
                        Location l = event.getBlock().getLocation();
                        Material[] wools = new Material[]{Material.WHITE_WOOL, Material.PINK_WOOL, Material.YELLOW_WOOL, Material.LIGHT_BLUE_WOOL, Material.LIME_WOOL, Material.PURPLE_WOOL};
                        BlockData data = wools[new Random().nextInt(wools.length)].createBlockData();
                        FallingBlock fb = l.getWorld().spawnFallingBlock(l.add(0, 1, 0), data);

                        fb.setDropItem(false);
                        fb.setCustomName("confetti");
                        fb.setMetadata("owner", new FixedMetadataValue(core, event.getPlayer().getUniqueId().toString()));
                        double angleX = Math.random() * 180 - 90;
                        double angleZ = Math.random() * 180 - 90;
                        float power = (float) Math.random() * 1.8f;
                        float x = (float) Math.sin(angleX) * power / 2f;
                        float y = (float) Math.cos(angleX) * (float) Math.cos(angleZ) * power;
                        float z = (float) Math.sin(angleZ) * power / 2f;
                        fb.setVelocity(new Vector(x, y, z));
                    }
                }
                if (chipperChance(event.getPlayer())) {
                    int minX = mine.getL1().getBlockX();
                    int minZ = mine.getL1().getBlockZ();
                    int maxX = mine.getL2().getBlockX();
                    int maxZ = mine.getL2().getBlockZ();
                    for(int i = minX; i <= maxX; i++){
                        for(int j = minZ; j <= maxZ; j++){
                            if(!(i==event.getBlock().getX()&&j==event.getBlock().getZ()))
                            blockBreak(event.getPlayer(), event.getBlock().getWorld().getBlockAt(i,event.getBlock().getY(),j));
                        }
                    }
                }
            }
        }
    }

    /**
     * Handles block changes:
     * Colored wool "Confetti" hitting the ground
     *
     * @param event the change block event
     */
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event)
    {
        if (event.getEntity() instanceof FallingBlock)
        {
            Block block = event.getBlock();
            if(event.getEntity().getCustomName().equals("confetti")){
                int dy = 1;
                Block b2;
                while((b2=block.getWorld().getBlockAt(block.getX(), block.getY()-dy, block.getZ())).getType() != Material.BEDROCK){
                    dy++;
                    blockBreak(core.getServer().getPlayer(UUID.fromString((String)event.getEntity().getMetadata("owner").get(0).value())), b2);
                }
            }
        }
    }

    /**
     * Removes drops spawned by confetti
     * @param event the item spawn event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event){
        List<Entity> nearby = event.getEntity().getNearbyEntities(1,1,1);
        boolean remove = false;
        for(Entity e : nearby) if(e.getType() == EntityType.FALLING_BLOCK) remove = true;
        if(remove) event.getEntity().remove();
    }

    /**
     * A random chance x in y
     * @param x the chance out of y
     * @param y the chance which x is out of
     * @return a boolean with an x in y probability of being true
     */
    private boolean chance(int x, int y){
        return new Random().nextInt(y) < x;
    }

    /**
     * A method called by block breaks and simulated block breaks
     * @param p the player simulated
     * @param b the block to be broken
     */
    private void blockBreak(Player p, Block b){
        boolean isMine = false;
        Mine mine = core.mineHandler.getMine(b);
        if(mine != null) {
            Inmate i = core.getInmate(p);
            int fortune = 1;
            ItemStack is;
            if (b.getType() != Material.AIR) {

                if(mine.getMineType() == MineType.BOSS){
                    int damage = (int)core.enchantHandler.getEnchantLevel("Boss Slayer", p);
                    if(!core.mineHandler.getBossDamage().containsKey(p))
                        core.mineHandler.getBossDamage().put(p, 0);
                    core.mineHandler.getBossDamage().put(p, core.mineHandler.getBossDamage().get(p)+damage+1);
                    core.mineHandler.setBossHealth(core.mineHandler.getBossHealth()-(damage+1));
                }

                b.setType(Material.AIR);
                if ((is = p.getInventory().getItemInMainHand()) != null && is.containsEnchantment(Enchantment.LOOT_BONUS_BLOCKS))
                    fortune = is.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1;
                double fortuneBonus = Math.pow(fortune, 1.2)/1.2+1.0;
                double mon =  Math.pow(1.03, i.ranks[0]-1);
                mon *= i.multi;
                mon *= fortuneBonus;
                i.money += mon;
                i.tokens++;
            }

        }

    }

    /**
     * A helper method for calculating the chance of 'Chipper' being set off.
     * @param p the player to calculate the chance for
     * @return boolean, true if it was set off
     */
    private boolean chipperChance(Player p){
        Inmate inmate = core.getInmate(p);
        int chipperLevel = (int) core.enchantHandler.getEnchantLevel("Chipper", p);
        int chance = (int)((double)chipperLevel*(1.0+0.1*inmate.getSkillLevel("chipmunk")));
        return chance(chance, 50000);
    }

}
