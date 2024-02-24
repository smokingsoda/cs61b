package byow.Core;

import edu.princeton.cs.introcs.StdDraw;

public class keyBoardInput implements inputSource{
    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public char next() {
        while(true) {
            if (StdDraw.hasNextKeyTyped()) {
                return Character.toLowerCase(StdDraw.nextKeyTyped());
            }
        }
    }
}
