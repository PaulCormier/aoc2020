package aoc2020;

/**
 * Combo Breaker
 * 
 * https://adventofcode.com/2020/day/25
 *
 * @author Paul Cormier
 * 
 */
public class Day25 {

    private static final long CARD_PUBLIC_KEY = 8458505;
    private static final long DOOR_PUBLIC_KEY = 16050997;
    private static final long TEST_CARD_PUBLIC_KEY = 5764801;
    private static final long TEST_DOOR_PUBLIC_KEY = 17807724;

    public static void main(String[] args) {
        part1();
        part2();
    }

    /**
     * What encryption key is the handshake trying to establish?
     */
    private static void part1() {
        // The card transforms the subject number of 7 according to the card's secret
        // loop size. The result is called the card's public key.
        int cardLoopSize = 0;
        long value = 1;
        long subjectNumber = 7;
        while (value != CARD_PUBLIC_KEY) {
            value = transform(value, subjectNumber);
            cardLoopSize++;
        }
        System.out.println("Card loop size: " + cardLoopSize);

        // The door transforms the subject number of 7 according to the door's secret
        // loop size. The result is called the door's public key.
        int doorLoopSize = 0;
        value = 1;
        subjectNumber = 7;
        while (value != DOOR_PUBLIC_KEY) {
            value = transform(value, subjectNumber);
            doorLoopSize++;
        }
        System.out.println("Door loop size: " + doorLoopSize);

        // The card transforms the subject number of the door's public key according to
        // the card's loop size. The result is the encryption key.
        long encryptionKey = 1;
        while (cardLoopSize-- > 0) {
            encryptionKey = transform(encryptionKey, DOOR_PUBLIC_KEY);
        }
        System.out.println("Card encryption key: " + encryptionKey);

        // The door transforms the subject number of the card's public key according to
        // the door's loop size. The result is the same encryption key as the card
        // calculated.
        encryptionKey = 1;
        while (doorLoopSize-- > 0) {
            encryptionKey = transform(encryptionKey, CARD_PUBLIC_KEY);
        }
        System.out.println("Door encryption key: " + encryptionKey);

    }

    /**
     */
    private static void part2() {

    }

    private static long transform(long value, long subjectNumber) {
        // Set the value to itself multiplied by the subject number.
        // Set the value to the remainder after dividing the value by 20201227
        return (value * subjectNumber) % 20201227;
    }

}
