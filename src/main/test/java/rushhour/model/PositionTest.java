package rushhour.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PositionTest {
    @Test
    public void colTest(){
        //setup
        Position position = new Position(1, 2);
        int expected = 2;
        //invoke
        int actual = position.getCol();
        //analyze
        assertEquals(expected, actual);

    }
    
     @Test
     public void rowTest(){
         //setup
         Position position = new Position(1, 2);
         int expected = 1;
         //invoke
         int actual = position.getRow();
         //analyze
         assertEquals(expected, actual);

     }

     @Test
     public void isEqualTestTrue()
     {
         //setup
         Position position1 = new Position(2, 4);
         Position position2 = new Position(2, 4);
         boolean expected = true;
         //invoke
         boolean actual = position1.isEqual(position2);
         //analyze
         assertEquals(expected, actual);

     }

     @Test
     public void isEqualTestFalse(){
         //setup
         Position position1 = new Position(1, 4);
         Position position2 = new Position(2, 4);
         boolean expected = false;
         //invoke
         boolean actual = position1.isEqual(position2);
         //analyze
         assertEquals(expected, actual);

     }

     @Test
     public void toStringTest(){
         //setup
         Position position = new Position(3, 3);
         String expected = "[3,3]";
         //invoke
         String actual = position.toString();
         //analyze
         assertEquals(expected, actual);


     }
}

