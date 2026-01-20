package game;

import model.creatures.*;
import model.items.ItemType;
import model.png.*;
import utils.InputUtils;
import utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private Hero hero;
    private final List<String> defeatedMonsterNames = new ArrayList<>();
    private int defeatedMinorMonsters = 0; // esclude il Drago
    private boolean gameOver = false;

    public void start() {
        printIntro();
        createHero();
        mainLoop();
        printSummary();
    }

    private void printIntro() {
        System.out.println("=== Dungeon Crawler ===");
        System.out.println("Esplora il dungeon, affronta mostri, interagisci con PNG e gestisci il tuo inventario.");
        System.out.println("Sconfiggi il Drago a fine dungeon per vincere. Se i tuoi PV scendono a 0, la partita termina.");
        System.out.println();
    }

    private void createHero() {
        System.out.println("Scegli la classe del tuo eroe:");
        System.out.println("1) Guerriero");
        System.out.println("2) Mago");
        System.out.println("3) Elfo");

        int choice = InputUtils.readIntInRange("Inserisci 1-3: ", 1, 3);
        String name = InputUtils.readNonEmptyString("Inserisci il nome del tuo eroe: ");

        switch (choice) {
            case 1 -> hero = new Warrior(name);
            case 2 -> hero = new Mage(name);
            case 3 -> hero = new Elf(name);
            default -> hero = new Warrior(name);
        }

        // Oggetti iniziali minimi
        hero.addItem(ItemType.POZIONE);
        hero.addItem(ItemType.MONETA);
        System.out.println();
        System.out.println("Eroe creato: " + hero);
        System.out.println("Inventario iniziale: " + hero.getInventory());
    }

    private void mainLoop() {
        while (!gameOver) {
            System.out.println();
            System.out.println("Tre porte davanti a te. Quale scegli?");
            System.out.println("1) Porta A");
            System.out.println("2) Porta B");
            System.out.println("3) Porta C");

            int door = InputUtils.readIntInRange("Scegli 1-3: ", 1, 3);
            triggerRandomEvent();
            if (hero.getHealth() <= 0) {
                gameOver = true;
                System.out.println("Sei stato sconfitto...");
                System.out.println("Ritenta...");
            }
        }
    }

    private void triggerRandomEvent() {
        // Probabilità:
        // Mostro 25%, Trappola 20%, PNG 15%, Stanza Vuota 25%, Tesoro 15%
        int roll = RandomUtils.nextInt(100); // 0-99
        if (shouldForceDragon()) {
            startCombat(createDragon());
            return;
        }

        if (roll < 25) {
            startCombat(randomMonster());
        } else if (roll < 45) {
            trapEvent();
        } else if (roll < 60) {
            npcEvent();
        } else if (roll < 85) {
            emptyRoomEvent();
        } else {
            treasureEvent();
        }
    }

    private boolean shouldForceDragon() {
        // Dopo 10 mostri minori sconfitti, il prossimo mostro sarà il Drago
        return defeatedMinorMonsters >= 10;
    }

    private Monster randomMonster() {
        // Il drago può apparire casualmente come qualsiasi altro mostro
        int roll = RandomUtils.nextInt(100);
        if (roll < 5) {
            return createDragon();
        }
        int pick = RandomUtils.nextInt(3);
        return switch (pick) {
            case 0 -> new Goblin(RandomUtils.randomMonsterLoot());
            case 1 -> new Orc(RandomUtils.randomMonsterLoot());
            default -> new Skeleton(RandomUtils.randomMonsterLoot());
        };
    }

    private Dragon createDragon() {
        defeatedMinorMonsters = 0; // reset dopo aver forzato l'incontro
        return new Dragon(RandomUtils.randomMonsterLoot());
    }

    private void trapEvent() {
        System.out.println("Una trappola scatta! Subisci 1 danno.");
        hero.setHealth(Math.max(0, hero.getHealth() - 1));
        System.out.println("PV attuali: " + hero.getHealth() + "/" + hero.getMaxHealth());
        if (hero.getHealth() <= 0) {
            gameOver = true;
        }
    }

    private void npcEvent() {
        NPC npc = RandomUtils.randomNPC();
        System.out.println("Incontri un " + npc.getType() + ".");
        npc.interact(hero);
    }

    private void emptyRoomEvent() {
        System.out.println("La stanza è vuota. Prendi un respiro...");
    }

    private void treasureEvent() {
        ItemType item = RandomUtils.randomItem();
        System.out.println("Trovi un tesoro: " + item);
        hero.addItem(item);
        System.out.println("Inventario: " + hero.getInventory());
    }

    private void startCombat(Monster monster) {
        System.out.println();
        System.out.println("Un " + monster.getName() + " appare! PV: " + monster.getHealth() + " PA: " + monster.getAttack());
        Combat combat = new Combat(hero, monster);
        boolean heroWon = combat.run();
        if (heroWon) {
            defeatedMonsterNames.add(monster.getName());
            if (!(monster instanceof Dragon)) {
                defeatedMinorMonsters++;
            }
            hero.setLevel(hero.getLevel() + 1);
            ItemType loot = monster.getLoot();
            System.out.println("Hai sconfitto il " + monster.getName() + "! Ottieni: " + loot);
            hero.addItem(loot);
            System.out.println("Ora sei livello " + hero.getLevel());
            if (monster instanceof Dragon) {
                System.out.println("Hai sconfitto il Drago! Hai vinto!");
                gameOver = true;
            }
        } else {
            if (hero.getHealth() <= 0) {
                System.out.println("Sei stato ucciso dal " + monster.getName() + ".");
                gameOver = true;
            } else {
                System.out.println("Sei fuggito dal combattimento.");
            }
        }
    }

    private void printSummary() {
        System.out.println();
        System.out.println("=== Riepilogo Partita ===");
        System.out.println("Eroe: " + hero.getName() + " (" + hero.getClassName() + ")");
        System.out.println("PV rimanenti: " + hero.getHealth() + "/" + hero.getMaxHealth());
        System.out.println("Mostri sconfitti: " + defeatedMonsterNames.size());
        System.out.println("Elenco mostri sconfitti: " + (defeatedMonsterNames.isEmpty() ? "-" : String.join(", ", defeatedMonsterNames)));
        if (defeatedMonsterNames.stream().anyMatch(n -> n.equalsIgnoreCase("Drago"))) {
            System.out.println("Messaggio finale: Congratulazioni, hai sconfitto il Drago e vinto la partita!");
        } else if (hero.getHealth() <= 0) {
            System.out.println("Messaggio finale: Non demordere. Ritenta e diventa più forte!");
        } else {
            System.out.println("Messaggio finale: Hai interrotto l'avventura. Torna quando vuoi.");
        }
    }
}
