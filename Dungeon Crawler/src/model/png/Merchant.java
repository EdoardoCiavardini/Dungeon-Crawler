package model.png;

import model.creatures.Hero;
import model.items.ItemType;
import utils.RandomUtils;
import utils.InputUtils;

import java.util.ArrayList;
import java.util.List;

public class Merchant extends NPC {

    private final List<ItemType> stock = new ArrayList<>();

    public Merchant() {
        super("Mercante", "Benvenuto! Ho merci utili per la tua avventura.");
        // Oggetti iniziali casuali (no monete in vendita)
        int count = 3;
        for (int i = 0; i < count; i++) {
            ItemType item = RandomUtils.randomItem();
            if (item != ItemType.MONETA) {
                stock.add(item);
            } else {
                stock.add(ItemType.POZIONE);
            }
        }
    }

    @Override
    public void interact(Hero hero) {
        say();
        long coins = hero.getInventory().stream().filter(i -> i == ItemType.MONETA).count();
        System.out.println("Possiedi " + coins + " MONETA/E.");
        if (coins <= 0) {
            System.out.println("Non hai monete. Torna quando ne avrai.");
            return;
        }
        if (stock.isEmpty()) {
            System.out.println("Purtroppo al momento non ho articoli.");
            return;
        }

        System.out.println("Articoli in vendita (1 MONETA ciascuno):");
        for (int i = 0; i < stock.size(); i++) {
            System.out.println((i + 1) + ") " + stock.get(i));
        }
        int choice = InputUtils.readIntInRange("Scegli l'articolo (1-" + stock.size() + "): ", 1, stock.size());
        ItemType selected = stock.get(choice - 1);

        // Consumo una MONETA
        boolean removed = hero.removeItem(ItemType.MONETA);
        if (!removed) {
            System.out.println("Errore: moneta non trovata (inventario cambiato). Operazione annullata.");
            return;
        }
        hero.addItem(selected);
        stock.remove(choice - 1);
        System.out.println("Hai acquistato: " + selected + ". Grazie e buona fortuna!");
    }
}
