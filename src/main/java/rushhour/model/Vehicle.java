package rushhour.model;

public class Vehicle {
    private char symbol;
    private Position back;
    private Position front;
    private boolean isHorizontal;

    public Vehicle(char symbol, Position back, Position front){
        this.symbol = symbol;
        this.back = back;
        this.front = front;

        //calculate is its horizontal or vertical
        this.isHorizontal = back.getRow() == front.getRow() && back.getCol()<front.getCol();
    }
    public Vehicle(Vehicle other){
        this.symbol = other.getSymbol();
        this.back = other.getBack();
        this.front = other.getFront();

        //calculate is its horizontal or vertical
        this.isHorizontal = back.getRow() == front.getRow() && back.getCol()<front.getCol();
    }
    public Position getBack() {
        return back;
    }
    public Position getFront() {
        return front;
    }
    public char getSymbol() {
        return symbol;
    }
    public boolean getHorizontal(){
        return isHorizontal;
    }
    public void setBack(int row, int col){
        this.back = new Position(row, col);
    }
    public void setFront(int row, int col){
        this.back = new Position(row, col);
    }
    public boolean isOccupied(Position position){
        if(back.getCol() == position.getCol() && back.getRow() == position.getRow()){
           return true; 
        }
        else if(front.getCol()== position.getCol() && front.getRow() == position.getRow()){
            return true;
        }
        
        //add middle
        return false;
    }
    public boolean move(Direction dir){
        if(dir == Direction.DOWN){
            this.back = new Position(back.getRow()+1,back.getCol());
            this.front = new Position(front.getRow()+1,front.getCol());
            return true;
        }
        else if(dir == Direction.LEFT){
            this.back = new Position(back.getRow(), back.getCol()-1);
            this.front = new Position(front.getRow(), front.getCol()-1);
            return true;
        }
        else if(dir == Direction.RIGHT){
            this.back = new Position(back.getRow(), back.getCol()+1);
            this.front = new Position(front.getRow(), front.getCol()+1);
            return true;
        }
        else if(dir == Direction.UP){
            this.back = new Position(back.getRow()-1, back.getCol());
            this.front = new Position(front.getRow()-1, front.getCol());
            return true;
        }

        return false;

    }

    @Override
    public String toString(){
        return "Color:" + symbol + " = Position: [" + front + "," + back + "]"; 
    }
    
}
