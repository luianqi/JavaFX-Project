package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import javafx.scene.control.Button;

public class Main extends Application {

    private static final Font FONT = Font.font(18);

    private List<Question> questions;
    private QuestionPane qPane = new QuestionPane();
    private SidePane sPane = new SidePane();
    private static Random r = new Random();

    private Parent createContent() {
        HBox root = new HBox(50);
        root.setPadding(new Insets(50, 50, 50, 50));

        try {
            qPane.setQuestion(selectRandomQuestion());
        } catch (Exception e) {
            e.printStackTrace();
        }

        root.getChildren().addAll(qPane, sPane);
        return root;
    }

    private void nextQuestion() {
        try {
            qPane.setQuestion(selectRandomQuestion());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sPane.selectNext();
    }

    private List<Question> availableQuestions() {
        return questions.stream().filter(Question::isAvailable).collect(Collectors.toList());
    }

    private Question selectRandomQuestion() throws Exception {
        if (availableQuestions().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Congratulations!");
            alert.setContentText("You win!");
            alert.setHeaderText(null);
            alert.show();
            throw new Exception("We are out of questions.");
        }
        Question q = availableQuestions().get(r.nextInt(availableQuestions().size()));
        q.setAvailable(false);
        return q;
    }

    private class SidePane extends VBox {
        private int current = 1;
        public SidePane() {
            super(10);

            for (int i = 15; i > 0; i--) {
                Text text = new Text("Question " + i);
                text.setFill(i == current ? Color.BLACK : Color.GRAY);

                getChildren().add(text);
            }
        }
        public void selectNext() {
            if (current == 15) {
                return;
            }

            Text text = (Text)getChildren().get(15 - current);
            text.setFill(Color.GRAY);
            current++;
            text = (Text)getChildren().get(15 - current);
            text.setFill(Color.BLACK);
        }
    }

    private class QuestionPane extends VBox {
        private Text text = new Text();
        private List<Button> buttons = new ArrayList<>();
        private Question current;

        public QuestionPane() {
            super(20);

            text.setFont(FONT);

            HBox hbox = new HBox();
            for (int i = 0; i < 4; i++) {
                Button btn = new Button();
                btn.setFont(FONT);
                btn.setPrefWidth(120);
                btn.setOnAction(event -> {
                    if (btn.getText().equals(current.getCorrectAnswer())) {
                        nextQuestion();
                    }
                    else {
                        System.out.println("Incorrect");
                    }
                });

                buttons.add(btn);
                hbox.getChildren().add(btn);
            }

            setAlignment(Pos.CENTER);
            getChildren().addAll(text, hbox);
        }

        public void setQuestion(Question question) {
            current = question;
            text.setText(question.name);

            List<String> answers = new LinkedList<>(question.answers);

            Collections.shuffle(answers);

            for (int i = 0; i < answers.size(); i++) {
                buttons.get(i).setText(answers.get(i));
                buttons.get(i).setManaged(true);
                buttons.get(i).setVisible(true);
            }

            for (int i = answers.size(); i < buttons.size(); i++) {
                buttons.get(i).setManaged(false);
                buttons.get(i).setVisible(false);
            }
        }
    }

    private class Question {
        private String name;
        private List<String> answers;
        private boolean available = true;

        public Question(String name, String... answers) {
            this.name = name;
            this.answers = Arrays.asList(answers);
        }

        public String getCorrectAnswer() {
            return answers.get(0);
        }

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        questions = new LinkedList<>();
        loadQuestions();
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    private void loadQuestions() throws FileNotFoundException {
        System.out.println(Paths.get("").toAbsolutePath().toString());

        File file = new File("C:\\Users\\malik\\OneDrive\\Desktop\\Millionaire Game\\q&a.txt");

        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            questions.add(new Question(scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine()));
            scanner.nextLine();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
