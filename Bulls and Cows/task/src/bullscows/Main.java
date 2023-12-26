package bullscows;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        System.out.println("Please, enter the length of the secret code:");
        Scanner scanner = new Scanner(System.in);

        int codeLength = validateInput(scanner.next());
        System.out.println("Input the number of possible symbols in the code:");

        int numPossibleSymbols = validateInput(scanner.next());

        if (codeLength > numPossibleSymbols || numPossibleSymbols > 36) {
            System.out.println("Error: Invalid inputs. Please ensure that the length of the secret code is not greater than the number of possible symbols and the number of possible symbols is between 1 and 36.");
            System.exit(0);
        }

        String secretCode = generateSecretCode(codeLength, numPossibleSymbols);
        System.out.println("The secret is prepared: " + "*".repeat(codeLength) + " (" + generateCodeRange(numPossibleSymbols) + ").");
        System.out.println("Okay, let's start a game!");

        int turn = 1;


        while (true) {
            System.out.println("Turn " + turn + ":");
            System.out.print("> ");
            String guess = scanner.next();


            if (guess.length() != codeLength || !guess.matches("[0-9a-z]+")) {
                System.out.println("Error: Invalid guess. Please enter a valid code of length " + codeLength + " with symbols 0-9 and/or a-z.");
                System.exit(0);
            }

            String grade = gradeAttempt(secretCode, guess);
            System.out.println("Grade: " + grade);

            if (grade.equals(codeLength + " bulls")) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }

            turn++;
        }
    }

    private static int validateInput(String input) {
        try {
            int value = Integer.parseInt(input);
            if (value <= 0) {
                throw new NumberFormatException();
            }
            return value;
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid input. Please enter a positive integer.");
            System.exit(0);
            return -1;
        }
    }


    private static String generateSecretCode(int codeLength, int numPossibleSymbols) {
        Random random = new Random();
        Set<Character> uniqueCharacters = random.ints(0, numPossibleSymbols)
                .mapToObj(i -> (char) (i < 10 ? '0' + i : 'a' + i - 10))
                .distinct()
                .limit(codeLength)
                .collect(HashSet::new, Set::add, Set::addAll);

        StringBuilder secretCodeBuilder = new StringBuilder();
        uniqueCharacters.forEach(secretCodeBuilder::append);

        return secretCodeBuilder.toString();
    }

    private static String generateCodeRange(int numPossibleSymbols) {
        StringBuilder range = new StringBuilder();

        for (int i = 0; i < numPossibleSymbols; i++) {
            char symbol = (char) (i < 10 ? i + '0' : i + 'a' - 10);
            range.append(symbol);
        }

        return range.toString();
    }

    private static String gradeAttempt(String secretCode, String guess) {
        int bulls = 0;
        int cows = 0;

        Set<Character> secretSet = new HashSet<>();
        Set<Character> guessSet = new HashSet<>();

        for (int i = 0; i < secretCode.length(); i++) {
            char secretChar = secretCode.charAt(i);
            char guessChar = guess.charAt(i);

            if (secretChar == guessChar) {
                bulls++;
            } else {
                secretSet.add(secretChar);
                guessSet.add(guessChar);
            }
        }

        for (char c : guessSet) {
            if (secretSet.contains(c)) {
                cows++;
            }
        }

        StringBuilder gradeBuilder = new StringBuilder();
        if (bulls > 0) {
            gradeBuilder.append(bulls).append(" bull").append(bulls > 1 ? "s" : "");
        }
        if (cows > 0) {
            if (bulls > 0) {
                gradeBuilder.append(" and ");
            }
            gradeBuilder.append(cows).append(" cow").append(cows > 1 ? "s" : "");
        }

        return !gradeBuilder.isEmpty() ? gradeBuilder.toString() : "None";
    }
}
