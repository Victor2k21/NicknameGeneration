import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static AtomicInteger threeLetterWords = new AtomicInteger(0);
    static AtomicInteger fourLetterWords = new AtomicInteger(0);
    static AtomicInteger fiveLetterWords = new AtomicInteger(0);
    static final int NUMBER_OF_WORDS = 100_000;

    static String[] texts = new String[NUMBER_OF_WORDS];

    static void increaseCounter(String word) {
        switch (word.length()) {
            case 3 -> threeLetterWords.incrementAndGet();
            case 4 -> fourLetterWords.incrementAndGet();
            case 5 -> fiveLetterWords.incrementAndGet();
            default -> throw new RuntimeException("Ошибка генерации слова: " + word);
        }
    }

    public static void main(String[] args) throws InterruptedException {


        Runnable palindrome = () -> {
            for (String word : texts) {
                String reversWord = new StringBuilder(word).reverse().toString();
                if (word.equals(reversWord)) increaseCounter(word);
            }
        };

        Runnable sameLetters = () -> {
            for (String word : texts) {
                char[] letters = word.toCharArray();
                boolean isSameLetters = true;
                char firstLetter = letters[0];
                int i = 0;
                while ((i < letters.length - 1) && isSameLetters) {
                    i++;
                    isSameLetters = firstLetter == letters[i];
                }
                if (isSameLetters) increaseCounter(word);
            }
        };

        Runnable ascendingLetters = () -> {
            for (String word : texts) {
                byte[] letters = word.getBytes();
                boolean isAscending = true;
                byte currentLetter = letters[0];
                int i = 0;
                while ((i < letters.length - 1) && isAscending) {
                    i++;
                    isAscending = currentLetter <= letters[i];
                    currentLetter = letters[i];
                }
                if (isAscending) increaseCounter(word);
            }
        };


        Random random = new Random();

        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }

        
        Thread palindromeThread = new Thread(palindrome);
        palindromeThread.start();
        Thread sameLettersThread = new Thread(sameLetters);
        sameLettersThread.start();
        Thread ascendingLettersThread = new Thread(ascendingLetters);
        ascendingLettersThread.start();

        palindromeThread.join();
        sameLettersThread.join();
        ascendingLettersThread.join();

        System.out.println("Красивых слов с длиной 3: " + threeLetterWords + " шт");
        System.out.println("Красивых слов с длиной 4: " + fourLetterWords + " шт");
        System.out.println("Красивых слов с длиной 5: " + fiveLetterWords + " шт");
    }


    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}