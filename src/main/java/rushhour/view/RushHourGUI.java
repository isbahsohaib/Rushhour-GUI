package rushhour.view;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import rushhour.model.Move;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.RushHourObserver;
import rushhour.model.Vehicle;

public class RushHourGUI extends Application{
    private RushHour rushHourModel; //this represents the rushhour
    public static final Image SQUARE_IMG = new Image("file:media/images/square.png");
    public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");
    private Map<Character,VehicleView> vehicleViewMap;
    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Rules of the game: ");
        System.out.println("Click on the front of the vehicle you wanna move (or back)");
        System.out.println("Then when you are done moving it, click on the back part of");
        System.out.println("your vehicle to keep them together!");
        System.out.println("If you did not do this! it may skip over the command and not place boxes");
        System.out.println("correctly!");
        Scanner input = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enter a filename \n(ex: 03_00, 04_00...):");
        String filename = "data/" + input.nextLine() + ".csv";
        input.close();

        this.rushHourModel = new RushHour(filename);
        this.vehicleViewMap = new HashMap<>();
        rushHourModel.registerObserver(new GUIupdater(rushHourModel,vehicleViewMap));
        //ImageView imageView = new ImageView(SQUARE_IMG);
        //System.out.println(SQUARE_IMG);
        GridPane gridPane = new GridPane();
        for(int col = 0; col < RushHour.getDim(); col ++){
            for(int row = 0; row < RushHour.getDim(); row++){
                //background
                if(col == 5 && row == 2){
                    Label label = new Label("<EXIT", new ImageView(BLANK_IMAGE));
                    label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            label.setPadding(Insets.EMPTY);
                    gridPane.add(label, col, row);
                }
                else{
                    Label label = makeLabel();
                    gridPane.add(label, col, row);
                }
                
                
            }
        }
        Label status = new Label("New Game");
        status.setTextFill(Color.GREEN);
        Label moveStatus = new Label("Moves: " + 0);
        status.setTextFill(Color.GREEN);
        //status.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            status.setPadding(Insets.EMPTY);
        gridPane.add(status,0, RushHour.getDim());
        gridPane.add(moveStatus,3, RushHour.getDim());
        
        //gridPane.add(makeButton(1, 1), 1, 1);
        //make vehicles



        Button resetButton = new Button("Reset");
        resetButton.setPadding(Insets.EMPTY);
        //reset the game
        resetButton.setOnAction(new ResetButton(filename,moveStatus, gridPane,VehicleView.buttonList,status, rushHourModel,vehicleViewMap));
        gridPane.add(resetButton, 4, RushHour.getDim());
        Button solve = new Button("Solve");
        solve.setPadding(Insets.EMPTY);
        solve.setOnAction(new SolveButton(VehicleView.buttonList,status,moveStatus,resetButton,rushHourModel,filename));
        // //reset the game
        
        gridPane.add(solve, 1, RushHour.getDim());
        Collection<Vehicle> cars = rushHourModel.getVehicles();
        

        for(Vehicle vehicle: cars){
            VehicleView vv = null;
            if(vehicle.getSymbol()=='O'){
                vv = new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.ORANGE, gridPane);
            }
            else if(vehicle.getSymbol()=='A'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.AQUA, gridPane);
            }
            else if(vehicle.getSymbol()=='B'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.BLACK, gridPane);
            }
            else if(vehicle.getSymbol()=='C'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.DARKGREEN, gridPane);
            }
            else if(vehicle.getSymbol()=='D'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.DARKBLUE, gridPane);
            }
            else if(vehicle.getSymbol()=='P'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.PINK, gridPane);
            }
            else if(vehicle.getSymbol()=='R'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.RED, gridPane);
            }
            else if(vehicle.getSymbol()=='Q'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.CHOCOLATE, gridPane);
            }
            else if(vehicle.getSymbol()=='E'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.PURPLE, gridPane);
            }
            else if(vehicle.getSymbol()=='F'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.YELLOW, gridPane);
            }
            else if(vehicle.getSymbol()=='G'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.GOLD, gridPane);
            }
            else if(vehicle.getSymbol()=='H'){
                vv =new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.HOTPINK, gridPane);
            }
            else{
                vv = new VehicleView(moveStatus,status,rushHourModel,vehicle, Color.INDIGO, gridPane);
            }
            vehicleViewMap.put(vehicle.getSymbol(), vv);
        }
        //gridPane.add(new ImageView(SQUARE_IMG),1, 1);
        
        primaryStage.setScene(new Scene(gridPane));
        primaryStage.setTitle("RushHour Game");
        primaryStage.show();
        }

        public Label makeLabel(){ //choose a specific number to make button different on
            Label label = new Label("",new ImageView(BLANK_IMAGE));
            label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            label.setPadding(Insets.EMPTY);
            return label;
        }
    

    public static void main(String[] args) {
        launch(args);
    }
     
}
class SolveButton implements EventHandler<ActionEvent>{
    private RushHour rushHourModel;
    private Button resetButton;
    private Label moveStatus;
    private Label status;
    private List<Button> buttonList;
    public SolveButton(List<Button> buttonList,Label status,Label moveStatus,Button resetButton,RushHour rushHourModel, String filename) throws FileNotFoundException{
        this.rushHourModel = rushHourModel;
        this.resetButton = resetButton;
        this.moveStatus = moveStatus;
        this.status = status;
        this.buttonList = buttonList;
    }
    @Override
    public void handle(ActionEvent event) {

        RushHourSolver rushHourSolver = new RushHourSolver(rushHourModel);
        try {
            List<Move> moves = rushHourSolver.solve(rushHourModel).getMoves();
            
            for(Move move:moves){
            
                try {
                    rushHourModel.moveVehicle(move);
                } catch (RushHourException e) {
                    e.printStackTrace();
                }
                rushHourModel.setMoveCount(rushHourModel.getMoveCount()+1);
                moveStatus.setText("Moves: "+rushHourModel.getMoveCount());
        }
        while(!rushHourModel.isGameOver()){
            RushHourSolver rushHourSolver2 = new RushHourSolver(rushHourModel);
        List<Move> moves2 = rushHourSolver2.solve(rushHourModel).getMoves();
        for(Move move:moves2){
                try {
                    rushHourModel.moveVehicle(move);
                } catch (RushHourException e) {
                    
                }
                rushHourModel.setMoveCount(rushHourModel.getMoveCount()+1);
                moveStatus.setText("Moves: "+rushHourModel.getMoveCount());
        }
        }
        System.out.println(rushHourModel.isGameOver());
        status.setText("Congratulations!");
        status.setTextFill(Color.GREEN);
        System.out.println("Congratulations!");
        for(Button button: buttonList){
            button.setDisable(true);
        }
        //throw new UnsupportedOperationException("Unimplemented method 'handle'");
        } catch (Exception e) {
            status.setText("No Solution!");
            status.setTextFill(Color.RED);
            rushHourModel.setMoveCount(rushHourModel.getMoveCount()+1);
                moveStatus.setText("Moves: "+rushHourModel.getMoveCount());
        }
        
}
}
//resets the complete game
class ResetButton implements EventHandler<ActionEvent>{
    private Label status;
    private List<Button> buttonList;
    private GridPane gridPane;
    private Label moveStatus;
    private String filename;
    private RushHour rushhourModel;

    Map<Character, VehicleView> vehicleViewMap;
    public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");
    public static final Image SQUARE_IMG = new Image("file:media/images/square.png");
    public ResetButton(String filename,Label moveStatus,GridPane gridPane,List<Button> buttonList,Label status,RushHour rushHourModel, Map<Character, VehicleView> vehicleViewMap) throws FileNotFoundException{
        this.rushhourModel = new RushHour(filename);
        this.status = status;
        this.buttonList = buttonList;
        this.gridPane = gridPane;
        this.moveStatus = moveStatus;
        this.filename = filename;
        this.vehicleViewMap = new HashMap<>();
    }
    @Override
    public void handle(ActionEvent event) {
        try {
            this.rushhourModel = new RushHour(filename);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(Button button: buttonList){
            button.setDisable(false);
        }
        
        
        for(int col = 0; col < RushHour.getDim(); col ++){
            for(int row = 0; row < RushHour.getDim(); row++){

            Label label = new Label("",new ImageView(BLANK_IMAGE));
            label.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            label.setPadding(Insets.EMPTY);
            gridPane.add(label, col, row);
            }
        }

        status.setText("New Game");
        status.setTextFill(Color.GREEN);
        moveStatus.setText("Moves: ");
        status.setTextFill(Color.GREEN);
        rushhourModel.setMoveCount(0);
        //status.setBackground(new Background(new BackgroundFill(Color.PINK, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
        status.setPadding(Insets.EMPTY);
        // gridPane.add(status,0, RushHour.getDim());
        // gridPane.add(moveStatus,3, RushHour.getDim());
        
        Collection<Vehicle> cars = rushhourModel.getVehicles();
        
        for(Vehicle vehicle: cars){
            VehicleView vv = null;
            if(vehicle.getSymbol()=='O'){
                try {
                    vv = new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.ORANGE, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='A'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.AQUA, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='B'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.BLACK, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='C'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.DARKGREEN, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='D'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.DARKBLUE, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='P'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.PINK, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='R'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.RED, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='Q'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.CHOCOLATE, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='E'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.PURPLE, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='F'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.YELLOW, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='G'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.GOLD, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else if(vehicle.getSymbol()=='H'){
                try {
                    vv =new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.HOTPINK, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else{
                try {
                    vv = new VehicleView(moveStatus,status,rushhourModel,vehicle, Color.INDIGO, gridPane);
                } catch (RushHourException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            
            vehicleViewMap.put(vehicle.getSymbol(), vv);
        }
        rushhourModel.registerObserver(new GUIupdater(rushhourModel,vehicleViewMap));
        status.setText("Game Reset!");
        

    }
    
}

class GUIupdater implements RushHourObserver{
    public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");
    private Map<Character,VehicleView> vehicleViewMap;
    public GUIupdater(RushHour rushHourModel,Map<Character,VehicleView> vehicleViewMap){
        this.vehicleViewMap = vehicleViewMap;
    }
    @Override
    public void vehicleMoved(Vehicle vehicle) {
        //replace only last button with empty space
        VehicleView vehicleView = vehicleViewMap.get(vehicle.getSymbol());
        vehicleView.updateButtons(vehicle);

        
    }
    public Label makeLabel(){ //choose a specific number to make button different on
        Label label = new Label("",new ImageView(BLANK_IMAGE));
        label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
        label.setPadding(Insets.EMPTY);
        return label;
    }
}
