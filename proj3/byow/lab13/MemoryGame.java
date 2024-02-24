package byow.lab13;

import byow.Core.RandomUtils;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.Color;
import java.awt.Font;
import java.util.Random;

public class MemoryGame {
    /** The width of the window of this game. */
    private int width;
    /** The height of the window of this game. */
    private int height;
    /** The current round the user is on. */
    private int round;
    /** The Random object used to randomly generate Strings. */
    private Random rand;
    /** Whether or not the game is over. */
    private boolean gameOver;
    /** Whether or not it is the player's turn. Used in the last section of the
     * spec, 'Helpful UI'. */
    private boolean playerTurn;
    /** The characters we generate random Strings from. */
    private static final char[] CHARACTERS = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    /** Encouraging phrases. Used in the last section of the spec, 'Helpful UI'. */
    private static final String[] ENCOURAGEMENT = {"You can do this!", "I believe in you!",
                                                   "You got this!", "You're a star!", "Go Bears!",
                                                   "Too easy for you!", "Wow, so impressive!"};

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Please enter a seed");
            return;
        }

        long seed = Long.parseLong(args[0]);
        MemoryGame game = new MemoryGame(40, 40, seed);
        game.startGame();
    }

    public MemoryGame(int width, int height, long seed) {
        /* Sets up StdDraw so that it has a width by height grid of 16 by 16 squares as its canvas
         * Also sets up the scale so the top left is (0,0) and the bottom right is (width, height)
         */
        this.width = width;
        this.height = height;
        this.round = 1;
        this.rand = new Random(seed);
        this.playerTurn = false;
        StdDraw.setCanvasSize(this.width * 16, this.height * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, this.width);
        StdDraw.setYscale(0, this.height);
        StdDraw.clear(Color.BLACK);
        StdDraw.enableDoubleBuffering();

        //TODO: Initialize random number generator
    }

    public String generateRandomString(int n) {
        //TODO: Generate random string of letters of length n
        String returnString = "";
        for (int i = 0; i < n; i++) {
            returnString = returnString + CHARACTERS[rand.nextInt(26)];
        }
        return returnString;
    }

    public void drawFrame(String s) {
        //TODO: Take the string and display it in the center of the screen
        //TODO: If game is not over, display relevant game information at the top of the screen
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(this.width / 2, this.height / 2, s);
        if (playerTurn) {
            StdDraw.textLeft(0, height - 1, "Round:" + this.round);
            StdDraw.text(width / 2, height - 1, "Watch");
            StdDraw.textRight(width, height - 1, ENCOURAGEMENT[rand.nextInt(ENCOURAGEMENT.length)]);
        }
        StdDraw.show();
    }

    public void flashSequence(String letters) {
        //TODO: Display each character in letters, making sure to blank the screen between letters
        for (int i = 0; i < letters.length(); i++) {
            String letter = Character.toString(letters.charAt(i));
            drawFrame(letter);
            long start = System.nanoTime();
            while(System.nanoTime() - start < 1_000_000_000.0) {
            }
            drawFrame("");
            start = System.nanoTime();
            while(System.nanoTime() - start < 500_000_000.0) {
            }
        }
    }

    public String solicitNCharsInput(int n) {
        //TODO: Read n letters of player input
        String returnString = "";
        int count = 0;
        while (count < n) {
            if (StdDraw.hasNextKeyTyped()) {
                returnString = returnString + StdDraw.nextKeyTyped();
                count += 1;
            }
        }
        return returnString;
    }

    public void startGame() {
        //TODO: Set any relevant variables before the game starts

        //TODO: Establish Engine loop
        while (!gameOver) {
            playerTurn = false;
            drawFrame("Round: " + round);
            long start = System.nanoTime();
            while(System.nanoTime() - start <1_000_000_000.0) {
            }
            String randomString = generateRandomString(round);
            playerTurn = true;
            flashSequence(randomString);
            String userString = solicitNCharsInput(round);
            isGameOver(randomString, userString);
        }
        drawFrame("Game Over! You made it to round: " + round);
    }

    public boolean isGameOver(String expectedString, String actualString) {
        if (!expectedString.equals(actualString)) {
            this.gameOver = true;
            this.playerTurn = false;
            return true;
        } else {
            this.round += 1;
            return false;
        }
    }
}
