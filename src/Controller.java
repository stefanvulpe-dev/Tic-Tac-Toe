import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class Controller {

    private String [][] boardValues = new String[3][3];

    private String [] winningConditions = {"XXX", "OOO"};

    private ArrayList<String> combos = new ArrayList<>();

    private boolean turnX = true;

    private boolean turnO = false;

    private boolean gameHasEnded = false;

    @FXML
    private Button button;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private GridPane gameBoard;

    @FXML
    private Label endGameMessage;

    @FXML
    private Line line;

    @FXML
    private Hyperlink hyperlink;

    @FXML
    private ImageView imageView;

    @FXML
    private ImageView infoImageView;

    @FXML
    private Label info;

    @FXML
    public void initialize() {
        gameBoard.setAlignment(Pos.CENTER);

        line.setVisible(false);

        gameBoard.setOnMouseEntered(mouseEvent -> gameBoard.setCursor(Cursor.HAND));

        button.setOnMouseClicked(mouseEvent -> {
            ObservableList<Node> panes = gameBoard.getChildren();
            for(Node node : panes) {
                Label label = (Label) ((Pane) node).getChildren().get(0);
                label.setText("");
            }
            endGameMessage.setText("");
            combos.clear();
            turnX = true;
            turnO = false;
            gameHasEnded = false;
            line.setVisible(false);
        });

        button.setOnMouseEntered(mouseEvent -> button.setCursor(Cursor.HAND));

        hyperlink.setOnMouseClicked(mouseEvent -> {
            Desktop d = Desktop.getDesktop();
            try {
                d.browse(new URI("https://github.com/Dagmmar/Tic-Tac-Toe"));
            } catch (IOException | URISyntaxException e) {
                endGameMessage.setText("Sorry, an error has occurred");
            }
        });

        imageView.setOnMouseEntered(mouseEvent -> imageView.setCursor(Cursor.HAND));

        imageView.setOnMouseClicked(mouseEvent -> {

            Desktop d = Desktop.getDesktop();
            try {
                d.browse(new URI("https://github.com/Dagmmar/Tic-Tac-Toe"));
            } catch (IOException | URISyntaxException e) {
                endGameMessage.setText("Sorry, an error has occurred");
            }
        });

        info.setOnMouseEntered(mouseEvent -> info.setCursor(Cursor.HAND));

        info.setOnMouseClicked(mouseEvent -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainAnchorPane.getScene().getWindow());
            try {
                Parent root = FXMLLoader.load(getClass().getResource("dialog.fxml"));
                dialog.getDialogPane().setContent(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("How to Play");

            dialog.show();

        });

        infoImageView.setOnMouseEntered(mouseEvent -> infoImageView.setCursor(Cursor.HAND));

        infoImageView.setOnMouseClicked(mouseEvent -> {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainAnchorPane.getScene().getWindow());
            try {
                Parent root = FXMLLoader.load(getClass().getResource("dialog.fxml"));
                dialog.getDialogPane().setContent(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.setTitle("How to Play");

            dialog.show();
        });

    }

    @FXML
    public void handleClick(MouseEvent mouseEvent) {
        Label label = (Label) ((Pane) mouseEvent.getSource()).getChildren().get(0);

        boolean isEmpty = label.getText().equals("");

        if(isEmpty && mouseEvent.getButton().equals(MouseButton.PRIMARY) && turnX && !gameHasEnded) {
            label.setText("X");
            turnX = false;
            turnO = true;
            checkWinner(gameBoard);
        } else if(isEmpty && mouseEvent.getButton().equals(MouseButton.SECONDARY) && turnO && !gameHasEnded) {
            label.setText("O");
            turnO = false;
            turnX = true;
            checkWinner(gameBoard);
        }
    }

    private boolean boardIsFull() {
        for(int i = 0; i < 3; i ++) {
            for(int j = 0; j < 3; j ++) {
                if(boardValues[i][j].equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    private void checkWinner(GridPane gridBoard) {
        ObservableList<Node> nodes = gridBoard.getChildren();
        for(Node node : nodes) {
            boardValues[GridPane.getRowIndex(node) == null ? 0 : GridPane.getRowIndex(node)][GridPane.getColumnIndex(node) == null ? 0 : GridPane.getColumnIndex(node)] =
                    ((Label) ((Pane) node).getChildren().get(0)).getText();
        }

        // Rows
        combos.add(0, boardValues[0][0] + boardValues[0][1] + boardValues[0][2]);
        combos.add(1, boardValues[1][0] + boardValues[1][1] + boardValues[1][2]);
        combos.add(2, boardValues[2][0] + boardValues[2][1] + boardValues[2][2]);

        // Columns
        combos.add(3, boardValues[0][0] + boardValues[1][0] + boardValues[2][0]);
        combos.add(4, boardValues[0][1] + boardValues[1][1] + boardValues[2][1]);
        combos.add(5, boardValues[0][2] + boardValues[1][2] + boardValues[2][2]);

        //Diagonals
        combos.add(6, boardValues[0][0] + boardValues[1][1] + boardValues[2][2]);
        combos.add(7, boardValues[0][2] + boardValues[1][1] + boardValues[2][0]);

        for(int i = 0; i < combos.size() && !gameHasEnded; i ++) {
            if(combos.get(i).equals(winningConditions[0])) {
                endGameMessage.setText("Player X Won");
                gameHasEnded = true;
            } else if(combos.get(i).equals(winningConditions[1])) {
                endGameMessage.setText("Player O Won");
                gameHasEnded = true;
            }
            if(gameHasEnded) {
                decideWhereToPlayAnimation(i);
            }
        }

        if(boardIsFull() && !gameHasEnded) {
            endGameMessage.setText("Tie");
            gameHasEnded = true;
        }

    }

    private void decideWhereToPlayAnimation(int i) {
        switch (i) {
            case 0:
                playWinAnimation("first-row");
            break;

            case 1:
                playWinAnimation("second-row");
            break;

            case 2:
                playWinAnimation("third-row");
            break;

            case 3:
                playWinAnimation("first-column");
            break;

            case 4:
                playWinAnimation("second-column");
            break;

            case 5:
                playWinAnimation("third-column");
            break;

            case 6:
                playWinAnimation("first-diagonal");
            break;

            case 7:
                playWinAnimation("second-diagonal");
            break;
        }
    }

    private void playWinAnimation(String location) {

        Timeline timeline = new Timeline();

        line.setVisible(true);


        switch (location) {
            case "first-row":
                line.setStartX(gameBoard.getLayoutX() + 70);
                line.setStartY(gameBoard.getLayoutY() + 98);
                line.setEndX(gameBoard.getLayoutX() + 70);
                line.setEndY(gameBoard.getLayoutY() + 98);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 430),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 98)));
            break;

            case "second-row":
                line.setStartX(gameBoard.getLayoutX() + 70);
                line.setStartY(gameBoard.getLayoutY() + 251);
                line.setEndX(gameBoard.getLayoutX() + 70);
                line.setEndY(gameBoard.getLayoutY() + 251);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 430),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 251)));
            break;

            case "third-row":
                line.setStartX(gameBoard.getLayoutX() + 70);
                line.setStartY(gameBoard.getLayoutY() + 405);
                line.setEndX(gameBoard.getLayoutX() + 70);
                line.setEndY(gameBoard.getLayoutY() + 405);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 430),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 405)));
            break;

            case "first-column":
                line.setStartX(gameBoard.getLayoutX() + 97);
                line.setStartY(gameBoard.getLayoutY() + 65);
                line.setEndX(gameBoard.getLayoutX() + 97);
                line.setEndY(gameBoard.getLayoutY() + 65);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 97),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 438)));
            break;

            case "second-column":
                line.setStartX(gameBoard.getLayoutX() + 250);
                line.setStartY(gameBoard.getLayoutY() + 65);
                line.setEndX(gameBoard.getLayoutX() + 250);
                line.setEndY(gameBoard.getLayoutY() + 65);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 250),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 438)));
            break;

            case "third-column":
                line.setStartX(gameBoard.getLayoutX() + 403);
                line.setStartY(gameBoard.getLayoutY() + 65);
                line.setEndX(gameBoard.getLayoutX() + 403);
                line.setEndY(gameBoard.getLayoutY() + 65);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 403),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 438)));
            break;

            case "first-diagonal":
                line.setStartX(gameBoard.getLayoutX() + 97);
                line.setStartY(gameBoard.getLayoutY() + 98);
                line.setEndX(gameBoard.getLayoutX() + 97);
                line.setEndY(gameBoard.getLayoutY() + 98);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 403),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 404)));
            break;

            case "second-diagonal":
                line.setStartX(gameBoard.getLayoutX() + 403);
                line.setStartY(gameBoard.getLayoutY() + 98);
                line.setEndX(gameBoard.getLayoutX() + 403);
                line.setEndY(gameBoard.getLayoutY() + 98);
                timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(1),
                        new KeyValue(line.endXProperty(), gameBoard.getLayoutX() + 97),
                        new KeyValue(line.endYProperty(), gameBoard.getLayoutY() + 404)));
                break;
        }

        timeline.play();

    }


//    @FXML
//    private void displayPosition(MouseEvent event) {
//        endGameMessage.setText("X: " + event.getX() + " Y: " + event.getY());
//    }

}
