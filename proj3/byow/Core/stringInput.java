package byow.Core;

public class stringInput implements inputSource{
    private String inputString;

    public stringInput(String input) {
        this.inputString = input;
    }
    @Override
    public boolean hasNext() {
        return inputString.length() > 0;
    }

    @Override
    public char next() {
        char c = Character.toLowerCase(inputString.charAt(0));
        inputString = inputString.substring(1);
        return c;
    }
}
