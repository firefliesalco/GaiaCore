package com.firefliesalco.www.bosses;

import net.md_5.bungee.api.ChatColor;

public enum BossDifficulty {

    EASY(5,10000, ChatColor.GREEN, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 5000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 2500)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 1000)}),
    MEDIUM(5,25000, ChatColor.YELLOW, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 15000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 10000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 5000)}),
    HARD(4,60000, ChatColor.GOLD, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 30000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 20000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 10000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 5000)}),
    EXTREME(3,100000, ChatColor.DARK_RED, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 50000), new BossReward(BossReward.RewardType.SKILL_POINTS, 1)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 35000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 20000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 10000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 5000)}),
    DEMONIC(1,2500000, ChatColor.BLACK, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 250000), new BossReward(BossReward.RewardType.PERM_POINT, 1)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 150000), new BossReward(BossReward.RewardType.SKILL_POINTS, 3)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 75000), new BossReward(BossReward.RewardType.SKILL_POINTS, 2)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 50000), new BossReward(BossReward.RewardType.SKILL_POINTS, 1)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 25000)}, new BossReward[]{new BossReward(BossReward.RewardType.TOKENS, 10000)});
    private int health; // The amount of health the boss has
    private ChatColor color; // The color that shows up in chat for this boss
    private int weight; // The number of chances this boss has of being chosen.
    private BossReward[][] rewards; // The rewards given for this boss.

    /**
     * Creates a new boss difficulty
     * @param weight the chances of a this boss appearing
     * @param health the max health of the boss
     * @param color the color displayed in chat for the boss
     * @param rewards the rewards given for defeating the boss
     */
    BossDifficulty(int weight, int health, ChatColor color, BossReward[]...rewards){
        this.health = health;
        this.weight = weight;
        this.color = color;
        this.rewards = rewards;
    }

    public int getHealth() {
        return health;
    }

    public ChatColor getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public BossReward[][] getRewards() {
        return rewards;
    }
}



