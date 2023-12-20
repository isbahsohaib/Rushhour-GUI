package rushhour.model;

import org.junit.jupiter.api.Test;

public class DirectionTest {
    @Test
    public void testUp(){
    //setup
    Direction up = Direction.UP;
    //invoke/analyze
    assert(up.equals(Direction.UP));
    }

    
}
