package rushhour.view;
//reset button not working
//visual somewhat working but causing trouble in places
import java.util.LinkedList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import rushhour.model.Move;
import rushhour.model.Position;
import rushhour.model.RushHour;
import rushhour.model.RushHourException;
import rushhour.model.Vehicle;

public class VehicleView {
    private Vehicle vehicle; //this is related to the backend vehicle

    public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");

    private RushHour rushHourModel;

    private Label label;

    private Label moveStatus;
    public static List<Button> buttonList = new LinkedList<>();
    public List<Button> allButtons = new LinkedList<>();
    private GridPane pane;
    public VehicleView(Label moveStatus,Label label,RushHour rushHourModel,Vehicle vehicle, Color color, GridPane pane) throws RushHourException{
        this.rushHourModel = rushHourModel;
        this.vehicle = vehicle;
        this.label = label;
        this.moveStatus = moveStatus;
        this.pane = pane;
        // add buttons
        Button backButton = makeButton(pane,vehicle,color,'B');
        Position backPosition = vehicle.getBack();
        pane.add(backButton, backPosition.getCol(), backPosition.getRow());
        //front buttons
        Button frontButton = makeButton(pane,vehicle,color,'F');
        Position frontPosition = vehicle.getFront();
        pane.add(frontButton, frontPosition.getCol(), frontPosition.getRow());

        allButtons.add(frontButton);
        allButtons.add(backButton);

        Button hintButton = new Button("Hint");
        Label label2 = new Label("Hint: ");
        pane.add(label2, 2, RushHour.getDim());
        hintButton.setPadding(Insets.EMPTY);
        hintButton.setOnAction(new HintButton(hintButton,label2, rushHourModel));
        
        
        pane.add(hintButton, 5, RushHour.getDim());

        buttonList.add(hintButton);
        buttonList.add(backButton);
        buttonList.add(frontButton);
        // buttons.add(frontButton);
        // buttons.add(backButton);
    }

    public Button makeButton(GridPane pane,Vehicle vehicle,Color color,char frontOrBack){ //choose a specific number to make button different on
        Button button = new Button("",new ImageView(BLANK_IMAGE));
        //sets the background image
        
        button.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
        button.setPadding(Insets.EMPTY);

        button.setOnAction(new MoveMaker(color,button,pane,buttonList,moveStatus,label,rushHourModel,vehicle,frontOrBack));
        
        // rushHourModel.register(new GUIupdater(imageView));
        return button;
    }
    public void updateButtons(Vehicle updatedVehicle){
        this.vehicle = updatedVehicle;
        for(Button button:allButtons){
            pane.getChildren().remove(button);
        }
        pane.add(allButtons.get(0), updatedVehicle.getFront().getCol(), updatedVehicle.getFront().getRow());
        pane.add(allButtons.get(1), updatedVehicle.getBack().getCol(), updatedVehicle.getBack().getRow());
    }

}


//depending which button is pressed (start or end) changed direction
//maybe pass in param from front/back button?
class HintButton implements EventHandler<ActionEvent>{
    private RushHour rushHourModel;
    private Label status;
    private Button button;
    public HintButton(Button button,Label status,RushHour rushHourModel){
        this.rushHourModel = rushHourModel;
        this.status = status;
        this.button = button;
    }
    @Override
    public void handle(ActionEvent event) {
        // if(rushHourModel.isGameOver()){
        //     button.setDisable(true);
        // }
        List<Move> moves = rushHourModel.getPossibleMoves();
        moves.get((int)Math.random()*moves.size());
        status.setText("Hint: " + moves.get(0));
        
    }
    
}
class MoveMaker implements EventHandler<ActionEvent> {
    public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");
    private static final Exception RushHourException = null;
    private RushHour rushhourModel;
    private Vehicle vehicle;
    private char frontOrBack;
    private Label status;
    private Label moveStatus;
    private List<Button> buttonList;
    private GridPane gridPane;
    private Button button;
    private Color color;
    public MoveMaker(Color color, Button button,GridPane gridPane,List<Button> buttonList,Label moveStatus,Label status,RushHour rushHourModel,Vehicle vehicle, char frontOrBack){
        this.rushhourModel = rushHourModel;
        this.vehicle = vehicle;
        this.frontOrBack = frontOrBack;
        this.status = status;
        this.moveStatus = moveStatus;
        this.buttonList = buttonList;
        this.gridPane = gridPane;
        this.button = button;
        this.color = color;
    }

    @Override
    public void handle(ActionEvent event){
        //TextInputControlSkin instead of Direction??
           // rushhour.moveVehicle(new Move('D', Direction.DOWN));
        //System.out.println("hi");
        Move move = null;
        
        if(vehicle.getHorizontal()&&frontOrBack=='F'){
            //horizontal front is right
            rushhour.model.Direction direction = rushhour.model.Direction.RIGHT;
            move = new Move(vehicle.getSymbol(), direction);
            System.out.println("Right");
            
        }
        else if(vehicle.getHorizontal()&&frontOrBack=='B'){
            //horizontal back is left
            rushhour.model.Direction direction = rushhour.model.Direction.LEFT;
            move = new Move(vehicle.getSymbol(), direction);
            System.out.println("Left");
        }
        else if(!vehicle.getHorizontal()){
            if(frontOrBack == 'F'){
                //vertical front is down
                rushhour.model.Direction direction = rushhour.model.Direction.DOWN;
                move = new Move(vehicle.getSymbol(), direction);
                System.out.println("Down");
            }
        }
        if(!vehicle.getHorizontal()){
            if(frontOrBack == 'B'){
                //vertical back is up
                rushhour.model.Direction direction = rushhour.model.Direction.UP;
                move = new Move(vehicle.getSymbol(), direction);
                //System.out.println(move);
                System.out.println("Up");
            }
        }

        //the game will not display error status
        if(move != null){
            try {
                Label label = new Label("",new ImageView(BLANK_IMAGE));
            label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
            label.setPadding(Insets.EMPTY);
                rushhourModel.moveVehicle(move);
                status.setText("Nice Move!");
                    //gridPane.add(button, vehicle.getFront().getCol(), vehicle.getFront().getRow());
                    //gridPane.add(button, vehicle.getBack().getCol(), vehicle.getBack().getRow());
                
                
            } catch (Exception e) {
                if(e == RushHourException){
                    status.setText(e.getMessage());
                    status.setTextFill(Color.RED);
                }
                
            }
            
        }
        
        if(rushhourModel.isGameOver()){
            status.setText("Congratulations!");
            status.setTextFill(Color.GREEN);
            
            if(buttonList!= null){
                
                for(Button button: buttonList){
                    button.setDisable(true);
                }
            }
            button.setDisable(true);
            
            
        }
        //UPDATE HINT WITH POSSIBLE MOVES HERE
        //rushhourModel.getPossibleMoves();
        //UPDATE MOVE COUNT HERE
        moveStatus.setText("Moves: " + rushhourModel.getMoveCount());
    }
}
//updates the gui itself
// class GUIupdater implements RushHourObserver{
//     public static final Image BLANK_IMAGE = new Image("file:media/images/reversi/blank.png");
//     private RushHour rushHourModel;
//     public GUIupdater(RushHour rushHourModel){
//         this.rushHourModel = rushHourModel;
//     }
//     @Override
//     public void vehicleMoved(Vehicle vehicle) {
//         //replace only last button with empty space
        
//         Label label = makeLabel();
//         Position backPosition = vehicle.getBack();
//         Position frontPosition = vehicle.getFront();
//         // if(direction == rushhour.model.Direction.DOWN){
//         //     pane.add(label, backPosition.getCol(), backPosition.getRow()-1);
//         // }
//         // else if(direction == rushhour.model.Direction.LEFT){
//         //     pane.add(label, backPosition.getCol()+1, backPosition.getRow());
//         // }
//         // else if(direction == rushhour.model.Direction.RIGHT){
//         //     pane.add(label, backPosition.getCol()-1, backPosition.getRow());
//         // }
//         // else if(direction == rushhour.model.Direction.UP){
//         //     pane.add(label, backPosition.getCol(), backPosition.getRow()+1);
//         // }
//         // //gives
//         // Button backButton = makeButton(pane,vehicle,color,'B');
        
//         // pane.add(backButton, backPosition.getCol(), backPosition.getRow());
//         // //mid buttons
//         // //front buttons
//         // Button frontButton = makeButton(pane,vehicle,color,'F');
        
//         // pane.add(frontButton, frontPosition.getCol(), frontPosition.getRow());
//         // if(buttonList==null){
//         //     label.setText("");
//         //     label.setTextFill(Color.GREEN);
//         // }
//         //buttonList.add(hintButton);


        
//     }
//     public Label makeLabel(){ //choose a specific number to make button different on
//         Label label = new Label("",new ImageView(BLANK_IMAGE));
//         label.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, new Insets(1, 1, 1, 1))));
//         label.setPadding(Insets.EMPTY);
//         return label;
//     }
// }
//EXAMPLES:
// class GUIupdater implements ConnectFourObserver{
//     private GridPane gridPane;
//     private int row;
//     private int col;
//     public GUIupdater(GridPane gridPane,int row,int col){
//         this.gridPane = gridPane;
//         this.row = row;
//         this.col = col;
//     }
//     @Override
//     public void checkerDropped(ConnectFour connectFour) {
//         //button images should be changed
//         if(connectFour.getCurrentPlayer() == Checker.RED){ //puts blak in its place{
//             gridPane.add(new ImageView(ConnectFourGUI.BLACK_IMG),col, row);
//         }
//         else if(connectFour.getCurrentPlayer() == Checker.BLACK){ //puts white in its place{
//             gridPane.add(new ImageView(ConnectFourGUI.WHITE_IMG),col, row);
//         }
//         else{ //puts white in its place{
//             gridPane.add(new ImageView(ConnectFourGUI.BLANK_IMG),col, row);
//         }
//     }

// }
