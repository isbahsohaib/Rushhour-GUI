package rushhour.model;

public enum Direction {
    UP("up"),
    RIGHT("right"),
    DOWN("down"),
    LEFT("left");

    public final String name;
    private Direction(String name){
        this.name = name;
    }

    public static void main(String[] args) 
    {
        //iterate over Direction object
        for(Direction dir : Direction.values()){ //first time using .values() in class 
            System.out.println(dir);
        }    
    }
}
