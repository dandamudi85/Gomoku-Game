import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.Scene;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;

public class Gomoku1 extends Application {

    boolean x = true;

    String[][] markedGrid = new String[15][15];

    String winCandidate = "";

    int xWins = 0;
    int oWins = 0;
    int draws = 0;
    int filledCount = 0;
    Button xwin = null;
    Button owin = null;
    Button draw = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        GridPane root = new GridPane();

        readGameHistory();

        createButtons(root,markedGrid);
        menuButtons(root,primaryStage);


        Scene scene=new Scene(root,700,500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Gomoku1");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void readGameHistory()
    {
        try {
            File file = new File("./gameData.ser");
            if(file.exists())
            {
                FileInputStream fileIn = new FileInputStream(file);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                HashMap<String,Integer> data = (HashMap<String,Integer>) in.readObject();
                if(data != null)
                {
                    xWins = data.get("xwins");
                    oWins = data.get("owins");
                    draws = data.get("draws");
                }
                in.close();
                fileIn.close();
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void saveGameHistory()
    {
        try
        {
            HashMap<String,Integer> data = new HashMap<>();
            data.put("xwins",xWins);
            data.put("owins",oWins);
            data.put("draws",draws);

            FileOutputStream fileOut = new FileOutputStream("./gameData.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(data);
            out.close();
            fileOut.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void menuButtons(GridPane root,Stage primaryStage)
    {
        try
        {
            Button xWinButton = new Button();
            xwin = xWinButton;
            xWinButton.setStyle("-fx-font-weight: bold");
            xWinButton.setAlignment(Pos.BASELINE_LEFT);
            xWinButton.setMaxSize(200, 30);
            xWinButton.setPrefSize(200, 30);
            xWinButton.setMinSize(200, 30);
            xWinButton.setText("X wins : " + xWins);
            xWinButton.setDisable(true);

            Button oWinButton = new Button();
            owin = oWinButton;
            oWinButton.setStyle("-fx-font-weight: bold");
            oWinButton.setAlignment(Pos.BASELINE_LEFT);
            oWinButton.setMaxSize(200, 30);
            oWinButton.setPrefSize(200, 30);
            oWinButton.setMinSize(200, 30);
            oWinButton.setText("O wins : " + oWins);
            oWinButton.setDisable(true);

            Button drawButton = new Button();
            draw = drawButton;
            drawButton.setStyle("-fx-font-weight: bold");
            drawButton.setAlignment(Pos.BASELINE_LEFT);
            drawButton.setMaxSize(200, 30);
            drawButton.setPrefSize(200, 30);
            drawButton.setMinSize(200, 30);
            drawButton.setText("Draws : " + draws);
            drawButton.setDisable(true);

            Button quit = new Button();
            quit.setText("Quit");
            quit.setMaxSize(100, 30);
            quit.setPrefSize(100, 30);
            quit.setMinSize(100, 30);

            Button reset = new Button();
            reset.setText("Reset");
            reset.setMaxSize(100, 30);
            reset.setPrefSize(100, 30);
            reset.setMinSize(100, 30);

            root.add(xWinButton,15,0);
            root.add(oWinButton,15,1);
            root.add(drawButton,15,2);
            root.add(reset,15,11);
            root.add(quit,15,12);

            quit.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    saveGameHistory();
                    primaryStage.close();
                }
            });
            reset.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    xWins = 0;
                    oWins = 0;
                    draws = 0;
                    updateButtonText("X wins : ");
                    updateButtonText("O wins : ");
                    updateButtonText("Draws : ");
                    saveGameHistory();
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void updateButtonText(String text)
    {
        try {

            if("X wins : ".equalsIgnoreCase(text))
            {
                xwin.setText("X wins : "+xWins);

            }
            else if("O wins : ".equalsIgnoreCase(text))
            {
                owin.setText("O wins : "+oWins);
            }
            else if("Draws : ".equalsIgnoreCase(text))
            {
                draw.setText("Draws : " + draws);
            }


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void createButtons(GridPane root,String[][] markedGrid)
    {
        try {


            for (int i = 0; i < 15; i++) {

                for (int j = 0; j < 15; j++) {

                    Button btn = new Button();

                    int[] position = new int[2];
                    position[0] = j;
                    position[1] = i;
                    btn.setUserData(position);

                    btn.setMaxSize(30, 30);
                    btn.setPrefSize(30, 30);
                    btn.setMinSize(30, 30);
                    btn.setStyle("-fx-background-radius: 0");
                    btn.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent arg0){
                            int[] buttonPosition = (int[]) btn.getUserData();
                            if (x) {
                                btn.setText("x");
                                btn.setTextFill(Color.BLUE);
                                markPosition(position,"x",markedGrid);
                            } else {
                                btn.setText("o");
                                btn.setTextFill(Color.RED);
                                markPosition(position,"o",markedGrid);
                            }
                            if("x".equalsIgnoreCase(winCandidate))
                            {
                                //After win disable buttons
                                disableGrid(root);
                                xWins = xWins + 1;
                                updateButtonText("X wins : ");
                                System.out.println("X WON");
                            }
                            if("o".equalsIgnoreCase(winCandidate))
                            {
                                //After win disable buttons
                                disableGrid(root);
                                oWins = oWins + 1;
                                updateButtonText("O wins : ");
                                System.out.println("O WON");
                            }
                            if(filledCount == 225)
                            {
                                draws = draws + 1;
                                updateButtonText("Draws : ");
                                System.out.println("DRAW");
                            }


                            System.out.println("i : " + buttonPosition[0] + " j :" + buttonPosition[1]);
                            btn.setDisable(true);
                            x = !x;

                        }
                    });
                    root.add(btn, i, j);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception in creating buttons : " + Arrays.toString(e.getStackTrace()));
        }
    }

    private void disableGrid(GridPane root)
    {
        try {
            for (Node n : root.getChildren()) {
                int column = GridPane.getColumnIndex(n);
                if(column<=14)
                {
                    n.setDisable(true);
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void markPosition(int[] position,String value,String[][] markedGrid)
    {
        try
        {
            filledCount++;
            markedGrid[position[0]][position[1]] = value;
            calculateWin(markedGrid);
        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

    private void calculateWin(String[][] markedGrid)
    {
        try
        {
            //Horizontal win scan
            for(int i=0;i<15;i++)
            {
                int xCount = 0;
                int oCount = 0;
                for(int j=0;j<15;j++)
                {
                    if("x".equalsIgnoreCase(markedGrid[i][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[i][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }
                }
            }

            //vertical win scan
            for(int j=0;j<15;j++)
            {
                int xCount = 0;
                int oCount = 0;
                for(int i=0;i<15;i++)
                {
                    if("x".equalsIgnoreCase(markedGrid[i][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[i][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }
                }
            }

            //Diagnol win scan (O,0) to (14,14)
            for(int i=0;i<15;i++)
            {
                int k = i;
                int xCount = 0;
                int oCount = 0;
                for(int j=0;j<=i;j++)
                {
                    if("x".equalsIgnoreCase(markedGrid[k][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[k][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }
                    k--;
                }
            }
            int k = 0;
            for(int i=14;i>=0;i--)
            {
                int l=14;
                int xCount = 0;
                int oCount = 0;
                k = k + 1;
                for(int j=k;j<=14;j++)
                {
                    if("x".equalsIgnoreCase(markedGrid[l][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[l][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }

                    //System.out.print("(" + l + "," + j + ")");
                    l--;
                }

                //System.out.println();

            }

            //Diagnol win scan (14,0) to (0,14)
            int jIndexMax = 0;
            int iIndexMax = 14;
            for(int i=14;i>=0;i--)
            {
                int iIndex = iIndexMax;
                int xCount = 0;
                int oCount = 0;
                for(int j=jIndexMax;j>=0;j--)
                {
                    if("x".equalsIgnoreCase(markedGrid[iIndex][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[iIndex][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }
                    iIndex--;
                }
                jIndexMax = jIndexMax + 1;
            }

            int iIndexMin = 0;
            for(int i=14;i>=0;i--)
            {
                int l=0;
                int xCount = 0;
                int oCount = 0;
                iIndexMin = iIndexMin + 1;
                for(int j=iIndexMin;j<=14;j++)
                {
                    if("x".equalsIgnoreCase(markedGrid[l][j]))
                    {
                        xCount = xCount + 1;
                        oCount = 0;
                    }
                    else if("o".equalsIgnoreCase(markedGrid[l][j]))
                    {
                        oCount = oCount + 1;
                        xCount = 0;
                    }
                    else
                    {
                        xCount = 0;
                        oCount = 0;
                    }

                    if(xCount == 5)
                    {
                        winCandidate = "x";
                        return;
                    }
                    if(oCount == 5)
                    {
                        winCandidate = "o";
                        return;
                    }

                    //System.out.print("(" + l + "," + j + ")");
                    l++;
                }

                //System.out.println();

            }


        }
        catch (Exception e)
        {
            e.getStackTrace();
        }
    }

}
