package rushhour.model;

public class Position 
{

    private int row;
    private int col;

    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    public int getRow()
    {
        return row;
    }

    public int getCol()
    {
        return col;
    }

    //add hashmap and hashset if needed
    public boolean isEqual(Position pos2){
        return this.getCol() == pos2.getCol()&& this.getRow() == pos2.getRow();
    }

    public String toString(){
        return "[" + row + "," + col + "]";
    }


}
