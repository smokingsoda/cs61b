package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    public static void addHexagon(int length) {
        if (length <= 0) {
            System.out.println("Wrong length");
            System.exit(0);
        }
        String returnString = HexWorld.upperHex(length);
        returnString = returnString + HexWorld.lowerHex(length);
        System.out.println(returnString);

    }
    public static String appendSpaces(int number, String inputString) {
        for (int i = 0; i < number; i++) {
            inputString += " ";
        }
        return inputString;
    }

    public static String appendTiles(int number, String inputString) {
        for (int i = 0; i < number; i++) {
            inputString += "a";
        }
        return inputString;
    }

    public static String upperHex(int length) {
        int maxLength = length + 2 * (length - 1);
        String returnString = "";
        for (int i = length; i <= maxLength; i = i + 2) {
            returnString = HexWorld.appendSpaces((maxLength - i)/2, returnString);
            returnString = HexWorld.appendTiles(i, returnString) + "\n";
        }
        return returnString;
    }

    public static String lowerHex(int length) {
        int maxLength = length + 2 * (length - 1);
        String returnString = "";
        for (int i = maxLength; i >= length; i = i - 2) {
            returnString = HexWorld.appendSpaces((maxLength - i)/2, returnString);
            if (i == length) {
                returnString = HexWorld.appendTiles(i, returnString);
                break;
            }
            returnString = HexWorld.appendTiles(i, returnString) + "\n";
        }
        return returnString;
    }
    @Test
    public void test() {
        addHexagon(2);
        addHexagon(3);
        addHexagon(4);
        addHexagon(5);
    }
}
