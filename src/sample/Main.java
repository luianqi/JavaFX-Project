package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.*;

import javafx.scene.control.Button;

public class Main extends Application {

    private static final Font FONT = Font.font(18);

    private QuestionPane qPane = new QuestionPane();

    private Parent createContent() {
        HBox root = new HBox();
        root.setPrefSize(600, 500);

        qPane.setQuestion(new Question("What sort of animal is Walt Disney's Dumbo?",
                "Elephant", "Deer", "Donkey", "Rabbit"));

        root.getChildren().add(qPane);
        return root;
    }

    private class QuestionPane extends VBox {
        private Text text = new Text();
        private List<Button> buttons = new ArrayList<>();

        public QuestionPane() {
            super(20);

            text.setFont(FONT);

            HBox hbox = new HBox();
            for (int i = 0; i < 4; i++) {
                Button btn = new Button();
                btn.setFont(FONT);
                btn.setPrefWidth(120);

                buttons.add(btn);
                hbox.getChildren().add(btn);
            }

            setAlignment(Pos.TOP_CENTER);
            getChildren().addAll(text, hbox);
        }

        public void setQuestion(Question question) {
            text.setText(question.name);

            Collections.shuffle(buttons);
            for (int i = 0; i < 4; i++) {
                buttons.get(i).setText(question.answers.get(i));
            }
        }
    }

    private class Question {
        private String name;
        private List<String> answers;

        public Question(String name, String... answers) {
            this.name = name;
            this.answers = Arrays.asList(answers);
        }

        public String getCorrectAnswer() {
            return answers.get(0);
        }

    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
