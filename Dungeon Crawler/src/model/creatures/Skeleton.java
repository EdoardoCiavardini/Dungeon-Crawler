package model.creatures;

import model.items.ItemType;

public class Skeleton extends Monster {
    public Skeleton(ItemType loot) {
        super("Scheletro", 24, 6, loot);
    }
}
