package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class VehicleTest {
    @Test
    public void getBackTest(){
        //setup
        Position back = new Position(1, 1);
        Position front = new Position(1, 2);
        Vehicle checkBack = new Vehicle('R', back, front);
        Position expected = back;
        //invoke
        Position actual = checkBack.getBack();
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getFrontTest(){
        //setup
        Position back = new Position(1, 1);
        Position front = new Position(1, 2);
        Vehicle checkBack = new Vehicle('R', back, front);
        Position expected = front;
        //invoke
        Position actual = checkBack.getFront();
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void getSymbolTest(){
        //setup
        Position back = new Position(1, 1);
        Position front = new Position(1, 2);
        Vehicle checkBack = new Vehicle('R', back, front);
        char expected = 'R';
        //invoke
        char actual = checkBack.getSymbol();
        //analyze
        assertEquals(expected, actual);
    }

    @Test
    public void isHorizontalTest(){
        //setup
        Position back = new Position(1, 1);
        Position front = new Position(1, 2);
        Vehicle checkHorizontal = new Vehicle('C', back, front);
        boolean expected = true;
        //invoke
        boolean actual = checkHorizontal.getHorizontal();
        //analyze
        assertEquals(expected, actual);
    }
}
