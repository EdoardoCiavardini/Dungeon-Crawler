package utils;

import java.util.Scanner;

public final class InputUtils {
    private static final Scanner SCANNER = new Scanner(System.in);

    private InputUtils() {}

    public static int readIntInRange(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String line = SCANNER.nextLine();
            try {
                int value = Integer.parseInt(line.trim());
                if (value < min || value > max) {
                    System.out.println("Valore fuori intervallo (" + min + "-" + max + "). Riprova.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Input non valido. Inserisci un numero intero.");
            } catch (Exception e) {
                System.out.println("Errore di input. Riprova.");
            }
        }
    }

    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = SCANNER.nextLine();
                if (line == null || line.trim().isEmpty()) {
                    System.out.println("Il testo non può essere vuoto. Riprova.");
                    continue;
                }
                return line.trim();
            } catch (Exception e) {
                System.out.println("Errore di input. Riprova.");
            }
        }
    }
}
