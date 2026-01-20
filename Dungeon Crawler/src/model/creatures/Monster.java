package model.creatures;

import model.items.ItemType;

public abstract class Monster extends Creature {
    private int health;
    private int attack;
    private final ItemType loot;

    protected Monster(String name, int health, int attack, ItemType loot) {
        super(name);
        this.health = Math.max(1, health);
        this.attack = Math.max(0, attack);
        this.loot = loot == null ? ItemType.POZIONE : loot;
    }

    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.max(0, health); }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = Math.max(0, attack); }

    public ItemType getLoot() { return loot; }
}
