import org.junit.Test;
import static org.junit.Assert.*;

import data.Point;
import data.Wheel;
import data.WheelLocation;
import robot.Robot;

public class RobotTest {
    @Test
    public void testSpinningDifferentialFR() {
        double width = 3;
        double length = 4;
        Robot rectangle = new Robot(width,length);
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(6,8))
        ), 0, 0.001);

        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(width*2.0 -1,length * 2.0))
        ), 0.0849, 0.001);

        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(width*2.0 +1,length*2.0))
        ), -0.07532, 0.001);
        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(-length*2,width))
        ), Math.PI/2, 0.001);
    }
    @Test
    public void testSpinningDifferentialBR() {
        double width = 3;
        double length = 4;

        Robot rectangle = new Robot(width,length);
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, -length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(width*2,-length*2))
        ), 0, 0.001);

        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, -length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(width*2.0 -1,-length * 2.0))
        ), -0.0849, 0.001);

        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, -length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(width*2.0 +1,-length*2.0))
        ), 0.07532, 0.001);
        
        assertEquals(rectangle.spinningDifferential(
            new Wheel(WheelLocation.FrontRight, new Point(width / 2.0, -length / 2.0)),
            new Wheel(WheelLocation.FrontRight, new Point(length*2,width*2))
        ), Math.PI/2, 0.001);
    }
}
