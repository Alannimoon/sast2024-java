package homework.Wordle;

import java.util.*;

public class Wordle {
    static final int ALPHABET_SIZE = 26;            // The size of the alphabet
    static final int WORD_LENGTH = 5;               // The length of words
    static final int TOTAL_CHANCES = 6;             // The chances in total

    // Guess `word` at state `s`
    public static State guess(State s) {
        // TODO begin
        s.chancesLeft--;

        // Initialize local variable to keep count of correct chars
        Map<Character, Integer> answerCharCounts = new HashMap<>();
        for (char c : s.answer.toCharArray()) {
            answerCharCounts.put(c, answerCharCounts.getOrDefault(c, 0) + 1);
        }

        // 2. Update the wordState according to the guess
        for (int i = 0; i < s.word.length(); i++) {
            char guessChar = s.word.charAt(i);
            if (guessChar == s.answer.charAt(i)) {
                s.wordState[i] = Color.GREEN;
                int count = answerCharCounts.get(guessChar) - 1;
                answerCharCounts.put(guessChar, count);
            }
        }

        for (int i = 0; i < s.word.length(); i++) {
            char guessChar = s.word.charAt(i);
            if (s.wordState[i] != Color.GREEN) {
                if (s.answer.contains(Character.toString(guessChar)) && answerCharCounts.get(guessChar) > 0) {
                    s.wordState[i] = Color.YELLOW;
                    int count = answerCharCounts.get(guessChar) - 1;
                    answerCharCounts.put(guessChar, count);
                } else {
                    s.wordState[i] = Color.RED;
                }
            }
        }

        // 3. Update alphabetState based on wordState
        for (int i = 0; i < s.word.length(); i++) {
            char ch = s.word.charAt(i);
            Color color = s.wordState[i];
            int alphabetIndex = ch - 'A';
            if (color == Color.GREEN && s.alphabetState[alphabetIndex] != Color.GREEN) {
                s.alphabetState[alphabetIndex] = Color.GREEN;
            } else if (color == Color.YELLOW && s.alphabetState[alphabetIndex] != Color.GREEN && s.alphabetState[alphabetIndex] != Color.YELLOW) {
                s.alphabetState[alphabetIndex] = Color.YELLOW;
            } else if (color == Color.RED && s.alphabetState[alphabetIndex] == Color.GRAY) {
                s.alphabetState[alphabetIndex] = Color.RED;
            }
        }

        // 4. Determine game status
        boolean allGreen = true;
        for (Color c : s.wordState) {
            if (c != Color.GREEN) {
                allGreen = false;
                break;
            }
        }
        if (allGreen) {
            s.status = GameStatus.WON;
        } else if (s.chancesLeft == 0) {
            s.status = GameStatus.LOST;
        }
        // TODO end
        return s;

    }
    public static void main(String[] args) {
        // Read word sets from files
        WordSet wordSet = new WordSet("assets/wordle/FINAL.txt", "assets/wordle/ACC.txt");

        Scanner input = new Scanner(System.in);
        // Keep asking for an answer if invalid
        String answer;
        do {
            System.out.print("Enter answer: ");
            answer = input.nextLine().toUpperCase().trim();
            if (wordSet.isNotFinalWord(answer)) {
                System.out.println("INVALID ANSWER");
            }
        } while (wordSet.isNotFinalWord(answer));

        State state = new State(answer);
        while (state.status == GameStatus.RUNNING) {
            // Keep asking for a word guess if invalid
            String word;
            do {
                System.out.print("Enter word guess: ");
                word = input.nextLine().toUpperCase().trim();
                if (wordSet.isNotAccWord(word)) {
                    System.out.println("INVALID WORD GUESS");
                }
            } while (wordSet.isNotAccWord(word));
            // Try to guess a word
            state.word = word;
            state = guess(state);
            state.show();
        }
        if (state.status == GameStatus.LOST) {
            System.out.println("You lost! The correct answer is " + state.answer + ".");
        } else {
            System.out.println("You won! You only used " + (TOTAL_CHANCES - state.chancesLeft) + " chances.");
        }
    }
}
