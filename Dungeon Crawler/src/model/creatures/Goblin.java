package model.creatures;

import model.items.ItemType;

public class Goblin extends Monster {
    public Goblin(ItemType loot) {
        super("Goblin", 20, 5, loot);
    }
}
