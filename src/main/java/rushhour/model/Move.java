package rushhour.model;

public class Move {
    private char symbol;
    private Direction dir;

    public Move(char symbol, Direction direction){
        this.symbol = symbol;
        this.dir = direction;
    }

    public Direction getDir() {
        return dir;
    }
    
    public char getSymbol() {
        return symbol;
    }
    public String toString(){
        return symbol +" " + dir;
    }
}
