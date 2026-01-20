package model.png;

import model.creatures.Hero;

public class Healer extends NPC {
    public Healer() {
        super("Guaritore", "La luce ti protegga. Lascia che io curi le tue ferite.");
    }

    @Override
    public void interact(Hero hero) {
        say();
        hero.healByHealer();
    }
}
