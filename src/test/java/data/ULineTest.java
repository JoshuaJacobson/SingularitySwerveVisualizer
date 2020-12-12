import org.junit.Test;
import static org.junit.Assert.*;

import data.ULine;
import data.Point;

public class ULineTest {
    @Test
    public void distanceCalculations() {
        ULine line = new ULine(new Point(0,0), new Point(3,4));
        assertEquals(line.getLength(), 5, 0.001);
        assertEquals(line.equalsLength(5), true);
        assertEquals(line.equalsLength(6), false);

        ULine longLine = new ULine(new Point(-1.5, 24.4), new Point(45.3, -23.2));
        assertEquals(longLine.getLength(), 66.753, 0.001);
        assertEquals(longLine.equalsLength(66.753), true);
        assertEquals(longLine.equalsLength(70), false);

        ULine shortLine = new ULine(new Point(2,2), new Point(2,2));
        assertEquals(shortLine.getLength(), 0, 0.001);
        assertEquals(shortLine.equalsLength(0), true);
        assertEquals(shortLine.equalsLength(1), false);
    }
}
