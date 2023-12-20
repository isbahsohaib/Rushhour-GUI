package rushhour.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RushHourTest {
    @Test
    public void toStringTest(){
        //setup
        Position back = new Position(1, 1);
        Position front = new Position(1, 2);
        Vehicle vehicle = new Vehicle('R', back, front);
        String expected = "Color:R = Position: [[1,2],[1,1]]";
        //invoke
        String actual = vehicle.toString();
        //analyze
        assertEquals(expected, actual);

    }

}
