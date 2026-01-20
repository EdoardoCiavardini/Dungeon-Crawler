package model.png;

import model.creatures.Hero;
import model.items.ItemType;

public abstract class NPC {
    private final String type;
    private final String dialog;

    protected NPC(String type, String dialog) {
        this.type = type;
        this.dialog = dialog == null ? "" : dialog;
    }

    public String getType() { return type; }
    public String getDialog() { return dialog; }

    public abstract void interact(Hero hero);

    protected void say() {
        System.out.println(type + ": \"" + dialog + "\"");
    }

    protected void giveItem(Hero hero, ItemType item) {
        hero.addItem(item);
        System.out.println(type + " ti dà: " + item);
    }
}
