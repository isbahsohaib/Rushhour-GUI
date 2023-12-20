package rushhour.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import rushhour.view.RushHourSolver;
public class RushHour 
{
    public final static int BOARD_DIM = 6;
    public final char RED_SYMBOL = 'R';
    public final char EMPTY_SYMBOL = '-';
    public final Position EXIT_POS = new Position(2, 5);
    private List<Vehicle> vehicles = new LinkedList<>();
    private int moveCount = 0;
    //game will end when red car is at 4 and 5 
    private RushHourObserver observer;
    public RushHour(String filename) throws FileNotFoundException
    {
        
        Scanner input = new Scanner(new File(filename));
        input.useDelimiter(",");

        while(input.hasNextLine()){
            String line = input.nextLine();
            String[] information = line.split(",");

            //creates a exitPosition and frontPosition for vehicle
            Position exitPosition = new Position( Integer.valueOf(information[1]), Integer.valueOf(information[2]));
            Position frontPosition = new Position( Integer.valueOf(information[3]), Integer.valueOf(information[4]));
            Vehicle vehicle1 = new Vehicle(information[0].charAt(0), exitPosition, frontPosition);
            vehicles.add(vehicle1);
        }


    }
    public RushHour(RushHour other){ //deep copy
        this.vehicles = other.getVehicles();
        List<Vehicle> copyVehicles = new ArrayList<>();

        for(Vehicle vehicle:vehicles){
            Position exitPosition = new Position(vehicle.getBack().getRow(), vehicle.getBack().getCol());
            Position frontPosition = new Position(vehicle.getFront().getRow(),vehicle.getFront().getCol());
            Vehicle vehicle1 = new Vehicle(vehicle.getSymbol(), exitPosition, frontPosition);
            copyVehicles.add(vehicle1);
        }
        vehicles = copyVehicles;
        moveCount = other.moveCount;
        observer = null;
        
    }
    public static int getDim(){
        return BOARD_DIM;
    }
    public void registerObserver(RushHourObserver observer){
        this.observer = observer;
    }
    private void notifyObserver(Vehicle vehicle){
        if(observer != null){
            this.observer.vehicleMoved(vehicle);
        }
    }
    public void change(RushHourObserver observer){
        this.observer = observer;
    }
    public void moveVehicle(Move move) throws RushHourException{
        //RushHourException should be thrown with a descriptive message.
        Vehicle vehicle = null;
        //goes through the vehicles list to find the vehicle we wanna move
        for(Vehicle vehicle1: vehicles){
            if(vehicle1.getSymbol()==move.getSymbol()){
                vehicle = vehicle1;
                break;
            }
        }
        // throws exception when An invalid vehicle symbol was given
        if(vehicle == null){
            System.out.println(new RushHourException(" the vehicle is unable to move due to an invalid symbol"));
            return;
        }
        // throws an exception An invalid direction is given, e.g. Direction.UP is specified for a horizontal vehicle
        else if(vehicle.getHorizontal() && (move.getDir() == Direction.UP || move.getDir() == Direction.DOWN)){
       
            System.out.println(new RushHourException(" the vehicle is unable to move due to an invalid direction (RIGHT OR LEFT)"));
            return;
        }
        else if (!vehicle.getHorizontal() && (move.getDir() == Direction.LEFT || move.getDir() == Direction.RIGHT)){
            System.out.println(new RushHourException(" the vehicle is unable to move due to an invalid direction (UP OR DOWN)"));
            return;
            
        }
        
        
        //this will make a temporary vehical that will check to see if the next move is invalid or not
        Position tempBack = new Position(vehicle.getBack().getRow(), vehicle.getBack().getCol());
        Position tempFront = new Position(vehicle.getFront().getRow(), vehicle.getFront().getCol());
        Vehicle tempVehicle = new Vehicle(vehicle.getSymbol(), tempBack, tempFront);
        tempVehicle.move(move.getDir());
        //will go through a for loop making sure the vehical will not be taking up the positon of another vehicle
        vehicles.remove(vehicle);
        for(Vehicle vehicle2: vehicles){
            //checks to see if the vehicle is not taking the place of another vehicle
            if(tempVehicle.getBack().isEqual(vehicle2.getBack()) || tempVehicle.getFront().isEqual(vehicle2.getFront())){
               
                System.out.println(new RushHourException("vehicle is taking up another vehicles spot"));
                vehicles.add(vehicle);
                return;

            }
            else if(tempVehicle.getBack().isEqual(vehicle2.getFront()) || tempVehicle.getFront().isEqual(vehicle2.getBack())){
                System.out.println(new RushHourException("vehicle is taking up another vehicles spot"));
                vehicles.add(vehicle);
                return;
            }
            
            //this will help us see whether the vehicle is off the grido rnot
            else if(tempVehicle.getFront().getCol() <= 0 || tempVehicle.getFront().getRow() < 0 || tempVehicle.getBack().getCol() < 0 || tempVehicle.getBack().getRow() < 0){
                System.out.println(new RushHourException("out of bounds tried to go below 0"));
                vehicles.add(vehicle);
                return;
            }
            else if(tempVehicle.getFront().getCol() >= BOARD_DIM || tempVehicle.getFront().getRow() >= BOARD_DIM || tempVehicle.getBack().getCol() >= BOARD_DIM || tempVehicle.getBack().getRow() >= BOARD_DIM){
                System.out.println(new RushHourException("out of bounds tried to go above 6"));
                vehicles.add(vehicle);
                return;
            }
        }
  
        vehicles.add(vehicle);

        // if all these tests are passed then the vehicle will be allowed to move 
        // there may be just a problem with this part
        vehicle.move(move.getDir());
        
        moveCount += 1;
        notifyObserver(vehicle);
        
        
    }
    public boolean isGameOver(){
        //checks to see if the game is over by checking the position for the red vehicle
        Vehicle redVehicle = null;
        for(Vehicle vehicle: vehicles){
            if(vehicle.getSymbol()== RED_SYMBOL){
                redVehicle = vehicle;
                break;
            }
        }
        if(redVehicle != null){
            return redVehicle.getFront().getCol()== EXIT_POS.getCol() && redVehicle.getBack().getRow() ==EXIT_POS.getRow();
            
        }
        else{
            return false;
        }
    }
    public List<Move> getPossibleMoves(){
        // List<Move> solutions = new ArrayList<>();
        List<Move> solution = new LinkedList<>();

        for (Vehicle vehicle : vehicles){
                if(vehicle.getHorizontal()){
                    //checks if its a valid move
                    if(vehicle.getBack().getCol()!=0 && vehicle.getBack().getCol()!=BOARD_DIM-1){
                        Move move = new Move(vehicle.getSymbol(), Direction.LEFT);
                        solution.add(move);
                    }
                    if(vehicle.getFront().getCol()!=0 && vehicle.getFront().getCol()!=BOARD_DIM-1){
                        Move move = new Move(vehicle.getSymbol(), Direction.RIGHT);
                        solution.add(move);
                    }
                }
                if(!vehicle.getHorizontal()){
                    if(vehicle.getBack().getRow()!=0 && vehicle.getBack().getRow()!=BOARD_DIM-1){
                        Move move = new Move(vehicle.getSymbol(), Direction.UP);
                        solution.add(move);
                    }
                    if(vehicle.getFront().getRow()!=0 && vehicle.getFront().getRow()!=BOARD_DIM-1){
                        Move move = new Move(vehicle.getSymbol(), Direction.DOWN);
                        solution.add(move);
                    }
                }
            
                
        }
        Vehicle currentVehicle = null;
        List<Move> copySolution = solution;
        List<Move> badSolutions = new LinkedList<>();
        for(Move move: solution){
            //System.out.println(move);
            for(Vehicle vehicle:vehicles){
                if(vehicle.getSymbol()==move.getSymbol()){
                    currentVehicle = vehicle;
                    break;
                }
            }
            
            vehicles.remove(currentVehicle);
            //get position for vehicle
            //then
            
        Position tempBack = new Position(currentVehicle.getBack().getRow(), currentVehicle.getBack().getCol());
        Position tempFront = new Position(currentVehicle.getFront().getRow(), currentVehicle.getFront().getCol());
        Vehicle tempVehicle = new Vehicle(currentVehicle.getSymbol(), tempBack, tempFront);
        tempVehicle.move(move.getDir());
            for(Vehicle vehicle2:vehicles){
                if(tempVehicle.getBack().isEqual(vehicle2.getBack()) || tempVehicle.getFront().isEqual(vehicle2.getFront())){
                    badSolutions.add(move);
                }
                else if(tempVehicle.getBack().isEqual(vehicle2.getFront()) || tempVehicle.getFront().isEqual(vehicle2.getBack())){
                    badSolutions.add(move);
                }
                
                
            }
           
            vehicles.add(currentVehicle);
        }
        //System.out.println(badSolutions);
        for(Move move: badSolutions){
            if(copySolution.contains(move)){
                copySolution.remove(move);
            }
        }
        return copySolution;
    }
    // public List<Move> getPossibleMoves(){
    //     List<Move> solutions = new ArrayList<>();
    //     List<Move> solution = new LinkedList<>();

    //     for (Vehicle vehicle : vehicles){
    //         Move move = null;
    //             if(vehicle.getBack().getCol()!=0 && vehicle.getBack().getCol()!=BOARD_DIM-1){
    //                 if(vehicle.getHorizontal()) {move = new Move(vehicle.getSymbol(), Direction.LEFT);}
    //                 else {move = new Move(vehicle.getSymbol(), Direction.UP);}
    //                 if(move!=null){
    //                     solution.add(move);
    //                 }
                    
    //             }
    //             if(vehicle.getFront().getCol()!=0 && vehicle.getFront().getCol()!=BOARD_DIM-1){
    //                 if(vehicle.getHorizontal()) {move = new Move(vehicle.getSymbol(), Direction.RIGHT);}
    //                 else {move = new Move(vehicle.getSymbol(), Direction.DOWN);}
    //                 if(move!=null){
    //                     solution.add(move);
    //                 }
    //             }   
            
            
    //         for (Vehicle vehicle2 : vehicles){
    //             if (!vehicle.equals(vehicle2)){
    //                 if (vehicle.getHorizontal()){
    //                     //check space to the left is free (col - 1)
    //                     Position tempBL = new Position(vehicle2.getBack().getRow(), vehicle2.getBack().getCol() - 1);
    //                     Position tempFL = new Position(vehicle2.getFront().getRow(), vehicle2.getFront().getCol() - 1);
    //                     //check if middle space is occupied too (by taking distance ((d1 + d2) / 2) for position
    //                     //then if that middle position is occupied
    //                     if (!vehicle.isOccupied(tempBL) || !vehicle.isOccupied(tempFL)){
    //                         if ((vehicle2.getFront().getCol() + vehicle2.getBack().getCol()) % 2 == 0){
    //                             int dist = vehicle2.getFront().getCol() + vehicle2.getBack().getCol() / 2;
    //                             Position tempML = new Position(vehicle2.getFront().getRow(), dist - 1);
    //                             if (!vehicle.isOccupied(tempML)) {
    //                                 move = new Move(vehicle.getSymbol(), Direction.LEFT);
    //                             }
    //                         }
    //                         else {move = new Move(vehicle.getSymbol(), Direction.LEFT);}
    //                     }

    //                     //check space to right (col + 1)
    //                     Position tempBR = new Position(vehicle2.getBack().getRow(), vehicle2.getBack().getCol() + 1);
    //                     Position tempFR = new Position(vehicle2.getFront().getRow(), vehicle2.getFront().getCol() + 1);
    //                     if (!vehicle.isOccupied(tempBR) || !vehicle.isOccupied(tempFR)){
    //                         if ((vehicle2.getFront().getCol() + vehicle2.getBack().getCol()) % 2 == 0){
    //                             int dist = vehicle2.getFront().getCol() + vehicle2.getBack().getCol() / 2;
    //                             Position tempMR = new Position(vehicle2.getFront().getRow(), dist + 1);
    //                             if (!vehicle.isOccupied(tempMR)) {
    //                                 move = new Move(vehicle.getSymbol(), Direction.RIGHT);
    //                             }
    //                         }
    //                         else {move = new Move(vehicle.getSymbol(), Direction.RIGHT);}
    //                     }
    //                 }

    //                 if (!vehicle.getHorizontal()){
    //                     //check space up is free (row - 1)
    //                     Position tempBU = new Position(vehicle2.getBack().getRow() - 1, vehicle2.getBack().getCol());
    //                     Position tempFU = new Position(vehicle2.getFront().getRow() - 1, vehicle2.getFront().getCol());
    //                     if (!vehicle.isOccupied(tempBU) || !vehicle.isOccupied(tempFU)){
    //                         if ((vehicle2.getFront().getCol() + vehicle2.getBack().getCol()) % 2 == 0){
    //                             int dist = vehicle2.getFront().getRow() + vehicle2.getBack().getRow() / 2;
    //                             Position tempMU = new Position(dist - 1, vehicle2.getFront().getCol());
    //                             if (!vehicle.isOccupied(tempMU)) {
    //                                 move = new Move(vehicle.getSymbol(), Direction.UP);
    //                             }
    //                         }
    //                         else {move = new Move(vehicle.getSymbol(), Direction.UP);}
    //                     }

    //                     //check space down (row + 1)
    //                     Position tempBD = new Position(vehicle2.getBack().getRow() + 1, vehicle2.getBack().getCol());
    //                     Position tempFD = new Position(vehicle2.getFront().getRow() + 1, vehicle2.getFront().getCol());
    //                     if (!vehicle.isOccupied(tempBD) || !vehicle.isOccupied(tempFD)){
    //                         if ((vehicle2.getFront().getCol() + vehicle2.getBack().getCol()) % 2 == 0){
    //                             int dist = vehicle2.getFront().getRow() + vehicle2.getBack().getRow() / 2;
    //                             Position tempMD = new Position(dist + 1, vehicle2.getFront().getCol());
    //                             if (!vehicle.isOccupied(tempMD)) {
    //                                 move = new Move(vehicle.getSymbol(), Direction.DOWN);
    //                             }
    //                         }
    //                         else {move = new Move(vehicle.getSymbol(), Direction.DOWN);}
    //                     }
                        
    //                 }
    //             }
    //             if(move!=null){
    //                     solutions.add(move);
    //                 }
    //         }
    //     }
    //     return solutions;
    // }

    public int getMoveCount(){
        return moveCount;
    }
    public List<Vehicle> getVehicles(){
        return vehicles;
    }
    @Override
    public String toString(){
        //toString() returns a string that can be used to display the board.  Each character in the board should be RushHour.EMPTY_SPACE or an alphabetic letter representing the symbol of a vehicle, including the RED_SYMBOL.
        String myString = "";
        char[][] array = new char[BOARD_DIM][BOARD_DIM];
        for(int row = 0; row < BOARD_DIM; row++){
            for(int col = 0; col < BOARD_DIM; col++){
                array[row][col] = '-';
                
            }
        }
        for(Vehicle vehicle: vehicles){
            for(int row = 0; row < array.length; row++){
                for(int col = 0; col < array.length; col++){
                    
                    if(vehicle.getBack().getRow() == row && vehicle.getBack().getCol() == col){
                        array[row][col] = vehicle.getSymbol();
                    }
                    if((vehicle.getFront().getRow()-vehicle.getBack().getRow())>=1){
                        if(vehicle.getBack().getRow()+1 == row && vehicle.getBack().getCol() == col){
                            array[row][col] = vehicle.getSymbol();
                            //System.out.println(vehicle.getSymbol() + ". " + row + "," + col);
                        }
                    }
                    if((vehicle.getFront().getCol()-vehicle.getBack().getCol())>=1){
                        if(vehicle.getBack().getRow() == row && vehicle.getBack().getCol()+1 == col){
                            array[row][col] = vehicle.getSymbol();
                        }
                    }
                    if(vehicle.getFront().getRow()==row && vehicle.getFront().getCol() == col){
                        array[row][col] = vehicle.getSymbol();
                    }
                }
            }
            }
        
            for(int i = 0; i < array.length; i++){
                for(int j = 0; j < array[i].length; j++){
                    myString += array[i][j];
                }
                if(i == 2){
                    myString += " < EXIT";
                }
                myString += "\n";
            }
        return myString;
    }
    public void setMoveCount(int i){
        this.moveCount = i;
    }
    public static void main(String[] args) throws FileNotFoundException, RushHourException {
        RushHour rushHour = new RushHour("data/03_00.csv");
        System.out.println(rushHour.getVehicles());
        System.out.println(rushHour);
        
        //System.out.println(rushHour.getPossibleMoves());
        // rushHour.moveVehicle(new Move('A', Direction.UP));
        //RushHourSolver rushHourSolver = new RushHourSolver(rushHour);
        //rushHourSolver.solve(rushHour).getMoves();
        System.out.println(rushHour.getPossibleMoves());
        System.out.println(rushHour);
        rushHour.moveVehicle(new Move('O', Direction.DOWN));
        rushHour.moveVehicle(new Move('O', Direction.DOWN));
        rushHour.moveVehicle(new Move('O', Direction.DOWN));
        rushHour.moveVehicle(new Move('A', Direction.DOWN));
        rushHour.moveVehicle(new Move('A', Direction.DOWN));
        rushHour.moveVehicle(new Move('A', Direction.DOWN));
        rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        System.out.println(rushHour.getPossibleMoves());
        System.out.println(rushHour);
        
        //copyRushHour.moveVehicle(new Move('A', Direction.LEFT));
        // System.out.println(rushHour.getVehicles());
        // System.out.println(copyRushHour.getVehicles());
        //System.out.println(rushHour.getVehicles());
        // rushHour.moveVehicle(new Move('C', Direction.DOWN));
        // rushHour.moveVehicle(new Move('C', Direction.DOWN));
        //System.out.println(rushHour);
        //rushHour.moveVehicle(new Move('R', Direction.LEFT));
        // rushHour.moveVehicle(new Move('C', Direction.UP));
        // rushHour.moveVehicle(new Move('C', Direction.UP));
        // rushHour.moveVehicle(new Move('D', Direction.LEFT));
        // rushHour.moveVehicle(new Move('D', Direction.LEFT));
        // rushHour.moveVehicle(new Move('D', Direction.LEFT));
        // rushHour.moveVehicle(new Move('C', Direction.DOWN));
        // rushHour.moveVehicle(new Move('C', Direction.DOWN));
        // rushHour.moveVehicle(new Move('B', Direction.DOWN));
        // rushHour.moveVehicle(new Move('B', Direction.DOWN));
        // rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        // rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        // rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        // rushHour.moveVehicle(new Move('R', Direction.RIGHT));
        
        System.out.println(rushHour.isGameOver());
        //System.out.println(rushHour);

        //System.out.println(rushHour.getPossibleMoves().get(0).getDir()); 
    }

}
