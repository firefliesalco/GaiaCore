package com.firefliesalco.www.bosses;

import com.firefliesalco.www.Inmate;

public class BossReward {
    private int amount; // The amount given for this reward
    private RewardType type; // The reward type given
    BossReward(RewardType type, int amount){
        this.type = type;
        this.amount = amount;
    }

    /**
     * Claims the reward for specified inmate
     * @param i the inmate to recieve the reward
     */
    public void claim(Inmate i){
        type.claim(i, amount);
    }

    public int getAmount(){
        return amount;
    }

    public RewardType getType(){
        return type;
    }

    /**
     * The types of rewards
     */
    public enum RewardType {
        TOKENS("tokens"){
            public void claim(Inmate i, int amount){
                i.tokens += amount;
            }
        },
        SKILL_POINTS("skill point(s)"){
            public void claim(Inmate i, int amount){
                i.points[0] += amount;
            }
        },
        PERM_POINT("permanent skill point");

        // Claims the reward for Inmate i with a specified amount
        public void claim(Inmate i, int amount){
            i.permPoints++;
        }
        private String name; // The name of the reward type

        /**
         * Creates a new reward type
         * @param name the name of the reward type
         */
        RewardType(String name){
            this.name = name;
        }

        /**
         * Gets the name of the reward type
         * @return the name of the reward type
         */
        public String getName(){
            return name;
        }
    }
}