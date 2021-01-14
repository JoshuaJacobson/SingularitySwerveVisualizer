import org.junit.Test;
import static org.junit.Assert.*;

import data.Point;

public class PointTest {
    @Test
    public void rotateZero() {

        Point originalPoint = new Point (3, 4);
        System.out.println(originalPoint.rotation());
        assertEquals(originalPoint.rotate(0).getX(), originalPoint.getX(), 0.001);
        assertEquals(originalPoint.rotate(0).getY(), originalPoint.getY(), 0.001);

        assertEquals(originalPoint.rotate(2*Math.PI).getX(), originalPoint.getX(), 0.001);
        assertEquals(originalPoint.rotate(2*Math.PI).getY(), originalPoint.getY(), 0.001);

        assertNotEquals(originalPoint.rotate(Math.PI).getX(), originalPoint.getX(), 0.001);
        assertNotEquals(originalPoint.rotate(Math.PI).getY(), originalPoint.getY(), 0.001);
    }

    @Test
    public void rotateZeroNegative() {

        Point originalPoint = new Point (3, -4);
        System.out.println(originalPoint.rotation());
        assertEquals(originalPoint.rotate(0).getX(), originalPoint.getX(), 0.001);
        assertEquals(originalPoint.rotate(0).getY(), originalPoint.getY(), 0.001);

        assertEquals(originalPoint.rotate(2*Math.PI).getX(), originalPoint.getX(), 0.001);
        assertEquals(originalPoint.rotate(2*Math.PI).getY(), originalPoint.getY(), 0.001);

        assertNotEquals(originalPoint.rotate(Math.PI).getX(), originalPoint.getX(), 0.001);
        assertNotEquals(originalPoint.rotate(Math.PI).getY(), originalPoint.getY(), 0.001);
    }
}
