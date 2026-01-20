package model.png;

import model.creatures.Hero;

public class Blacksmith extends NPC {
    public Blacksmith() {
        super("Fabbro", "Posso rinforzare le tue armi.");
    }

    @Override
    public void interact(Hero hero) {
        say();
        hero.improveAttackByBlacksmith();
    }
}
