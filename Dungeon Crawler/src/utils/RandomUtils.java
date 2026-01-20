package utils;

import model.items.ItemType;
import model.png.Blacksmith;
import model.png.Healer;
import model.png.Merchant;
import model.png.NPC;

import java.util.Random;

public final class RandomUtils {
    private static final Random RNG = new Random();

    private RandomUtils() {}

    public static int nextInt(int bound) {
        try {
            if (bound <= 0) return 0;
            return RNG.nextInt(bound);
        } catch (Exception e) {
            return 0;
        }
    }

    public static ItemType randomItem() {
        int roll = nextInt(3);
        return switch (roll) {
            case 0 -> ItemType.POZIONE;
            case 1 -> ItemType.BOMBA;
            default -> ItemType.MONETA;
        };
    }

    public static ItemType randomMonsterLoot() {
        // Bottino casuale
        return randomItem();
    }

    public static NPC randomNPC() {
        int r = nextInt(3);
        return switch (r) {
            case 0 -> new Merchant();
            case 1 -> new Healer();
            default -> new Blacksmith();
        };
    }
}
