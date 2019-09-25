package com.firefliesalco.www.handlers;

import com.firefliesalco.www.GaiaCore;
import com.firefliesalco.www.Inmate;
import com.firefliesalco.www.mines.Mine;
import com.firefliesalco.www.mines.MineType;
import com.firefliesalco.www.skills.Skill;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.*;

public class ConfigHandler {

    private GaiaCore core; // The main class of the plugin
    private FileConfiguration config; // The configuration being edited

    /**
     * Initializes the ConfigHandler, passes in the main class, and gets the default file configuration
     * @param core The plugin instance
     */
    public ConfigHandler(GaiaCore core){
        this.core = core;
        this.config = core.getConfig();

    }

    /**
     * Gets the value at the specified path, or the specified default value if it is not found.
     * @param path The path to get the data from
     * @param def The default value
     * @return The data at the path or the default value
     */
    private int get(String path, int def){
        if(generate(path, def))
            return config.getInt(path);
        return def;
    }

    /**
     * Gets the value at the specified path, or the specified default value if it is not found.
     * @param path The path to get the data from
     * @param def The default value
     * @return The data at the path or the default value
     */
    public long get(String path, long def){
        if(generate(path, def))
            return config.getLong(path);
        return def;
    }

    /**
     * Gets the value at the specified path, or the specified default value if it is not found.
     * @param path The path to get the data from
     * @param def The default value
     * @return The data at the path or the default value
     */
    private String get(String path, String def){
        if(generate(path, def))
            return config.getString(path);
        return def;
    }

    /**
     * Converts a location to a string so it can be stored in the configuration
     * @param l the location to convert
     * @return the converted location
     */
    private String toString(Location l){
        return l.getX() + ";" + l.getY() + ";" + l.getZ() + ";" + l.getWorld().getName();
    }

    /**
     * Parses a string back into a location.
     * @param s the string to parse
     * @return the parsed location
     */
    private Location parseLocation(String s){
        String[] split = s.split(";");
        World w = core.getServer().getWorld(split[3]);
        return new Location(w, Double.parseDouble(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
    }

    /**
     * Gets the value at the specified path, or the specified default value if it is not found.
     * @param path The path to get the data from
     * @param def The default value
     * @return The data at the path or the default value
     */
    private int[] get(String path, int[] def){
        if(generate(path, def)){
            List<Integer> list = config.getIntegerList(path);
            int[] arr = new int[def.length];
            for(int i = 0; i < def.length; i++){
                if(i >= list.size())
                    arr[i]=def[i];
                else
                    arr[i] = list.get(i);
            }
            return arr;
        }
        return def;
    }

    /**
     * Sets the value at the specified path
     * @param path the path to set the value of
     * @param obj the value to set
     */
    private void set(String path, Object obj){
        config.set(path, obj);
    }

    /**
     * Sets the default value at a path if the path does not exist
     * @param path the path to check
     * @param def the default value to set if the path doesn't exist
     * @return If the path existed before being set
     */
    private boolean generate(String path, Object def){
        Object value = config.get(path);
        if(value!=null)
            return true;
        config.set(path, def);
        return false;
    }

    /**
     * Saves the server data
     */
    public void save(){

        for(Player p : core.getServer().getOnlinePlayers()){
            savePlayer(p);
        }
        int mine = 0;
        for(Mine m : core.mineHandler.getMines()){
            saveMine(m, mine++);
        }


        core.saveConfig();
    }

    /**
     * Loads the mines from the config file
     * @return the list of mines
     */
    public ArrayList<Mine> loadMines(){
        ArrayList<Mine> mines = new ArrayList<>();
        ConfigurationSection cs = config.getConfigurationSection("mineData");
        if(cs != null){
            for(String key : cs.getKeys(false)){
                int pos = Integer.parseInt(key);
                mines.add(loadMine(pos));
            }
        }
        return mines;
    }

    /**
     * Saves the specified mine data to the config file
     * @param m the mine to save
     * @param pos the position to save the mine in
     */
    private void saveMine(Mine m, int pos){

        set("mineData." + pos + ".l1", toString(m.getL1()));
        set("mineData." + pos + ".l2", toString(m.getL2()));
        set("mineData." + pos + ".type", m.getMineType().toString());
        set("mineData." + pos + ".block", m.getBlock().toString());

    }

    /**
     * Loads the data of a specified mine position
     * @param pos the position of the mine in the config file
     * @return The Mine Object in the position specified
     */
    private Mine loadMine(int pos){

        Location l1 = parseLocation(get("mineData." + pos + ".l1", "0;64;0;world"));
        Location l2 = parseLocation(get("mineData." + pos + ".l2", "0;64;0;world"));
        MineType type = MineType.valueOf(get("mineData." + pos + ".type", "NORMAL"));
        Material block = Material.valueOf(get("mineData." + pos + ".block", "STONE"));
        return new Mine(l1, l2, block, type);

    }

    /**
     * Loads the specified player
     * @param p the player to load
     */
    public void loadPlayer(Player p){
        String uuid = p.getUniqueId().toString();
        HashMap<Skill, Integer> skills = new HashMap<>();
        ConfigurationSection skillsSection = config.getConfigurationSection("playerData." + uuid + ".skills");
        if(skillsSection != null){
            for(String key : skillsSection.getKeys(false)){
                Skill s = core.skillHandler.getSkill(key);
                if(s != null)
                    skills.put(s, skillsSection.getInt(key));
            }
        }
        core.inmates.put(p.getUniqueId().toString(), new Inmate(skills, get("playerData." + uuid + ".ranks", new int[]{1,0,0,0}), get("playerData." + uuid + ".balance", 0)
        , get("playerData."+uuid+".tokens", 0), get("playerData." + uuid + ".points", new int[]{0,0,0}), get("playerData." + uuid + ".permPoints", 0)));

    }

    /**
     * Saves the specified player
     * @param p the player to save
     */
    public void savePlayer(Player p){

        String uuid = p.getUniqueId().toString();
        Inmate i = core.inmates.get(uuid);
        List<Integer> list = new ArrayList<>();
        for(int j : i.ranks)
            list.add(j);
        set("playerData." + uuid + ".ranks", list);
        set("playerData." + uuid + ".balance", i.money);
        set("playerData." + uuid + ".tokens", i.tokens);
        set("playerData." + uuid + ".points", i.points);
        set("playerData." + uuid + ".permPoints", i.permPoints);
        for(Skill s : i.skillLevels.keySet()){
            set("playerData." + uuid+ ".skills." + s.getName(), i.skillLevels.get(s));
        }

    }


}
