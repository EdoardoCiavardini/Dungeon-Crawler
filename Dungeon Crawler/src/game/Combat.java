package game;

import model.creatures.Hero;
import model.creatures.Monster;
import model.items.ItemType;
import utils.InputUtils;
import utils.RandomUtils;

public class Combat {

    private final Hero hero;
    private final Monster monster;
    private boolean heroDefending = false;

    public Combat(Hero hero, Monster monster) {
        this.hero = hero;
        this.monster = monster;
    }

    public boolean run() {
        while (hero.getHealth() > 0 && monster.getHealth() > 0) {
            System.out.println();
            System.out.println("=== Combattimento ===");
            System.out.println(hero.getName() + " PV: " + hero.getHealth() + "/" + hero.getMaxHealth()
                    + " | PA: " + hero.getAttack() + " | Livello: " + hero.getLevel());
            System.out.println(monster.getName() + " PV: " + monster.getHealth() + " | PA: " + monster.getAttack());

            printActions();
            int choice = InputUtils.readIntInRange("Scegli un'azione (1-5" + (hero.canCastSpell() ? "/6" : "") + "): ",
                    1, hero.canCastSpell() ? 6 : 5);

            boolean turnConsumed = true;
            switch (choice) {
                case 1 -> playerAttack();
                case 2 -> playerDefend();
                case 3 -> playerUseItem();
                case 4 -> {
                    boolean escaped = playerEscape();
                    if (escaped) return false;
                    else turnConsumed = false; // se non scappa, non consuma il turno
                }
                case 5 -> System.out.println("Passi il turno (nessuna azione).");
                case 6 -> {
                    if (hero.canCastSpell()) playerCastSpell();
                    else {
                        System.out.println("Non puoi lanciare incantesimi.");
                        turnConsumed = false;
                    }
                }
                default -> {
                    System.out.println("Scelta non valida.");
                    turnConsumed = false;
                }
            }

            if (monster.getHealth() <= 0) break;

            if (turnConsumed) {
                monsterTurn();
                heroDefending = false; // effetto difesa dura un turno
            }
        }
        return hero.getHealth() > 0;
    }

    private void printActions() {
        System.out.println("1) Attacca");
        System.out.println("2) Difendi (danno subito -50% prossimo turno)");
        System.out.println("3) Usa Oggetto");
        System.out.println("4) Scappa");
        System.out.println("5) Passa");
        if (hero.canCastSpell()) {
            System.out.println("6) Lancia Incantesimo");
        }
    }

    private void playerAttack() {
        int damage = Math.max(0, hero.getAttack() + hero.getLevel());
        monster.setHealth(Math.max(0, monster.getHealth() - damage));
        System.out.println("Attacchi e infliggi " + damage + " danni al " + monster.getName() + ".");
    }

    private void playerDefend() {
        heroDefending = true;
        System.out.println("Ti prepari a difenderti. Il prossimo attacco subito sarà ridotto del 50%.");
    }

    private void playerUseItem() {
        if (hero.getInventory().isEmpty()) {
            System.out.println("Inventario vuoto.");
            return;
        }
        System.out.println("Inventario: " + hero.getInventory());
        System.out.println("Scegli oggetto:");
        System.out.println("1) POZIONE (+25 PV)");
        System.out.println("2) BOMBA (35 danni al mostro)");
        System.out.println("3) MONETA (non utilizzabile in combattimento)");
        int choice = InputUtils.readIntInRange("Inserisci 1-3: ", 1, 3);

        ItemType item = switch (choice) {
            case 1 -> ItemType.POZIONE;
            case 2 -> ItemType.BOMBA;
            case 3 -> ItemType.MONETA;
            default -> null;
        };

        if (item == null) {
            System.out.println("Scelta non valida.");
            return;
        }
        if (!hero.removeItem(item)) {
            System.out.println("Non possiedi questo oggetto.");
            return;
        }

        if (item == ItemType.POZIONE) {
            int before = hero.getHealth();
            hero.setHealth(Math.min(hero.getMaxHealth(), hero.getHealth() + 25));
            System.out.println("Usi una POZIONE. PV: " + before + " -> " + hero.getHealth());
        } else if (item == ItemType.BOMBA) {
            int damage = 35;
            monster.setHealth(Math.max(0, monster.getHealth() - damage));
            System.out.println("Lanci una BOMBA! Il " + monster.getName() + " subisce " + damage + " danni.");
        } else {
            System.out.println("La MONETA non può essere usata in combattimento. L'oggetto ti è restituito.");
            hero.addItem(ItemType.MONETA);
        }
    }

    private boolean playerEscape() {
        int dmg = RandomUtils.nextInt(11); // 0-10
        hero.setHealth(Math.max(0, hero.getHealth() - dmg));
        System.out.println("Tentativo di fuga... Subisci " + dmg + " danni durante la fuga.");
        return true; // la fuga riesce sempre (come da specifica: tenta la fuga e subisce danno casuale)
    }

    private void playerCastSpell() {
        int damage = Math.max(0, hero.getMagicAttack() + hero.getLevel());
        monster.setHealth(Math.max(0, monster.getHealth() - damage));
        System.out.println("Lanci un incantesimo e infliggi " + damage + " danni magici al " + monster.getName() + ".");
    }

    private void monsterTurn() {
        int damage = monster.getAttack();
        if (heroDefending) {
            damage = (int) Math.floor(damage * 0.5);
        }
        hero.setHealth(Math.max(0, hero.getHealth() - damage));
        System.out.println("Il " + monster.getName() + " attacca e infligge " + damage + " danni.");
        System.out.println("PV eroe: " + hero.getHealth() + "/" + hero.getMaxHealth());
    }
}
