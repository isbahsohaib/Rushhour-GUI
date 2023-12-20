package rushhour.view;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javafx.scene.control.skin.TextInputControlSkin.Direction;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;

public class RushHourCLI {

    public static void playGame(String filename) throws FileNotFoundException, IOException, RushHourException{
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        RushHour rushHour = new RushHour(filename);
        
        System.out.println("Choose the different controls: ");
        System.out.println("help - displays a list of commands");
        System.out.println("<symbol> <direction> - attempts to move the vehicle identified by the given ");
        System.out.println("hint - displays an available move. You may display any one of the possible moves to display.");
        System.out.println("reset - resets to a new game using the filename given at the start, i.e. do not prompt the user for another filename.");
        System.out.println("quit - displays a goodbye message and the method exits");

        System.out.println(rushHour);
        System.out.println("Enter your choice: ");
        String choice = input.nextLine();
        while(!choice.equals("quit")){
            if(choice.equals("help")){
                System.out.println("Choose the different controls: ");
                System.out.println("help - displays a list of commands");
                System.out.println("<symbol> <direction> - attempts to move the vehicle identified by the given ");
                System.out.println("hint - displays an available move. You may display any one of the possible moves to display.");
                System.out.println("reset - resets to a new game using the filename given at the start, i.e. do not prompt the user for another filename.");
                System.out.println("quit - displays a goodbye message and the method exits");
                System.out.println("solve - Solves the game automatically");
                System.out.println("Enter your choice: ");
                choice = input.nextLine();
            }
            else if(choice.equals("solve")){
                RushHourSolver rushHourSolver = new RushHourSolver(rushHour);
                //List<Move> moves = rushHourSolver.solve(rushHour).getMoves();
                try {
                    List<Move> moves = rushHourSolver.solve(rushHour).getMoves();
        
                    for(Move move: moves){
                        rushHour.moveVehicle(move);
                        rushHour.setMoveCount(rushHour.getMoveCount()+1);
                    }
                    while(!rushHour.isGameOver()){
                        RushHourSolver rushHourSolver2 = new RushHourSolver(rushHour);
                    List<Move> moves2 = rushHourSolver2.solve(rushHour).getMoves();
                    System.out.println(moves2);
                    for(Move move:moves2){
                        
                            System.out.println(move);
                            try {
                                rushHour.moveVehicle(move);
                            } catch (RushHourException e) {
                                
                            }
                            rushHour.setMoveCount(rushHour.getMoveCount()+1);
                    }
                    }   
                } catch (Exception e) {
                    // TODO: handle exception
                }
                System.out.println(rushHour);
                break;
            }
            else if(choice.equals("hint")){
                List<Move> possibleMoves = rushHour.getPossibleMoves();
                System.out.println(possibleMoves);
                System.out.println("Enter your choice: ");
                choice = input.nextLine();
            }
            else if(choice.equals("reset")){
                playGame(filename);
            }
            else if(choice.equals("quit")){
                break;
            }
            else{
                rushhour.model.Direction direction = null;
                char symbol = 'p';

                String[] choices = choice.split(" ");
                //System.out.println(choices.length);
                if(choices.length == 2){
                    System.out.println(Arrays.toString(choices));
                    if(choices[0].length()==1){
                        symbol = choices[0].toUpperCase().charAt(0);
                    }

                    //iterates over all the values in the Direction enum and compares to choice
                    String direct = choices[1].toUpperCase();
                    for (Direction dir : Direction.values()){
                        //System.out.println(dir.name().toLowerCase());
                        if(direct.equals(dir.name())){
                            //System.out.println(dir.name());
                            direction = rushhour.model.Direction.valueOf(dir.name());
                        }
                    }

                    Move move = new Move(symbol, direction);
                    System.out.println(move);
                    rushHour.moveVehicle(move);
                   
                    //letters disappear when they go over boundary but the mistake is still noted
                    System.out.println(rushHour);
                    System.out.println("Moves made: " + rushHour.getMoveCount());
                        if(rushHour.isGameOver()){
                            System.out.println("YOU WON!");
                            break;
                        }
                    
                }
                System.out.println("Enter your choice: ");
                choice = input.nextLine();
            }

        }
        input.close();
    }
    public static void main(String[] args) throws IOException, RushHourException {
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter a filename \n(ex: 03_00, 05_01...):");
        String filename = "data/" + input.nextLine() + ".csv";
        playGame(filename);
        input.close();
       
    }
}
