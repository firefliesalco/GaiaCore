package com.firefliesalco.www.skills;

public class Multi implements Comparable<Multi> {

    private int priority; // When the multiplier is taken into account
    public boolean add; // Is this added or multiplied?

    /**
     * Creates a new multiplier
     * @param add is the multiplier additive
     * @param priority what position is the multiplier in being applied
     */
    public Multi(boolean add, int priority){
        this.add = add;
        this.priority = priority;
    }

    /**
     * Compares how soon the multi should take effect
     * @param multi the multi being compared
     * @return -1, 0, 1 : before, the same, after
     */
    public int compareTo(Multi multi){
        return Integer.compare(priority, multi.priority);
    }



}
