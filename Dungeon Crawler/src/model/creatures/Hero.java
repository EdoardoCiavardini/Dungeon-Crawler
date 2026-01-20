package model.creatures;

import model.items.ItemType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Hero extends Creature {

    private int level;
    private int maxHealth;
    private int health;
    private int attack;
    private int magicAttack; // usato da Mago ed Elfo
    private final List<ItemType> inventory = new ArrayList<>();

    protected Hero(String name, int baseHealth, int baseAttack, int baseMagic) {
        super(name);
        this.level = 1;
        this.maxHealth = Math.max(1, baseHealth);
        this.health = this.maxHealth;
        this.attack = Math.max(0, baseAttack);
        this.magicAttack = Math.max(0, baseMagic);
    }

    public String getClassName() {
        return getClass().getSimpleName();
    }

    public boolean canCastSpell() {
        return magicAttack > 0;
    }

    public void addItem(ItemType item) {
        if (item != null) {
            inventory.add(item);
        }
    }

    public boolean removeItem(ItemType item) {
        return inventory.remove(item);
    }

    public List<ItemType> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    // Interazioni PNG
    public void improveAttackByBlacksmith() {
        this.attack += 1;
        System.out.println("Il Fabbro ripara le tue armi. PA +1 (ora " + attack + ").");
    }

    public void healByHealer() {
        this.maxHealth += 1;
        this.health = this.maxHealth;
        System.out.println("Il Guaritore aumenta i tuoi PV massimi di 1 e ti cura completamente. PV max: " + maxHealth);
    }

    // Getters/Setters
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = Math.max(1, level); }

    public int getMaxHealth() { return maxHealth; }
    public int getHealth() { return health; }
    public void setHealth(int health) { this.health = Math.min(Math.max(0, health), maxHealth); }

    public int getAttack() { return attack; }
    public void setAttack(int attack) { this.attack = Math.max(0, attack); }

    public int getMagicAttack() { return magicAttack; }
    public void setMagicAttack(int magicAttack) { this.magicAttack = Math.max(0, magicAttack); }

    @Override
    public String toString() {
        return getName() + " [" + getClassName() + "] PV: " + health + "/" + maxHealth + " PA: " + attack
                + (canCastSpell() ? " | AM: " + magicAttack : "") + " | Livello: " + level;
    }
}
