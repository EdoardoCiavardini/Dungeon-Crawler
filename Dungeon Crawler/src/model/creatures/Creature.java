package model.creatures;

public abstract class Creature {
    private final String name;

    protected Creature(String name) {
        this.name = name == null || name.trim().isEmpty() ? "SenzaNome" : name.trim();
    }

    public String getName() {
        return name;
    }
}
