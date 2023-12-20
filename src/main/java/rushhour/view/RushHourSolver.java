package rushhour.view;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import backtracker.Backtracker;
import backtracker.Configuration;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;

//all progress is made together as a team
public class RushHourSolver implements Configuration<RushHourSolver>{
    private RushHour rushHour;
    private List<Move> moves;
    private Set<String> visited =  new HashSet<>();
    public RushHourSolver(RushHour rushHour){
        this.rushHour = rushHour;
        this.moves = new LinkedList<>();
    }
    public RushHourSolver(RushHour rushHour, List<Move> moves, Set<String> visited){
        this.rushHour = rushHour;
        this.moves = moves;
        this.visited = visited;
    }
    @Override
    public Collection<RushHourSolver> getSuccessors() {
        //create list/collection of possible moves
        //use last move in possible moves to check if still valid
        //for each move in possible moves, create new move with first move, then call again with next config
        Collection<RushHourSolver> successors = new ArrayList<>();//list of successors
        List<Move> possibleMoves = rushHour.getPossibleMoves();
        List<Move> copyMoves = new LinkedList<>(moves);
        for(Move move: possibleMoves){
            try {
                RushHour copyRushHour = new RushHour(rushHour);
                copyRushHour.moveVehicle(move);
                if(!visited.contains(copyRushHour.toString())){
                    copyMoves.add(move);
                    visited.add(copyRushHour.toString());
                    RushHourSolver successor = new RushHourSolver(copyRushHour, copyMoves,visited);
                    successors.add(successor);
                }
            } catch (RushHourException e) {
                e.printStackTrace();
            }
        }
        return successors;
    }
    public String toString(){
        return "List: " +moves;
    }
    @Override
    public boolean isValid() {
        return true;
    }
    @Override
    public boolean isGoal() {
        return rushHour.isGameOver();
    }
    public List<Move> getMoves() {
        return moves;
    }
    public RushHour getRushHour() {
        return rushHour;
    }
    public RushHourSolver solve(RushHour rushHour){
        Backtracker<RushHourSolver> backtracker = new Backtracker<>(false);
        RushHourSolver initConfig = new RushHourSolver(rushHour);
        RushHourSolver rushHourSolver = backtracker.solve(initConfig);
        if(rushHourSolver!= null){
            return rushHourSolver;
        }
        else{
           System.out.println("No solution found");
            return null;
        }
        
    }
    public static void main(String[] args) throws FileNotFoundException {
        RushHour rushHour = new RushHour("data/08_00.csv");
        RushHourSolver rushHourSolver = new RushHourSolver(rushHour);
        System.out.println(rushHourSolver.solve(rushHour));
        System.out.println(rushHour);
    }
}
