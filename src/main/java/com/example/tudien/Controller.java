package com.example.tudien;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    Label label; //Thông tin từ cần tra
    @FXML
    ListView<String> listView; //Danh sách các từ
    @FXML
    ListView<String> historyList; //Lịch sử tra
    @FXML
    TextField text; //Từ cần tra
    @FXML
    Label wordExp; //Nghĩa từ cần tra
    @FXML
    ImageView imageVolume; //Icon phát âm
    @FXML
    ImageView imageAdd; //Icon thêm từ
    @FXML
    ImageView imageModify; //Icon sửa từ
    @FXML
    ImageView imageDelete; //Icon xóa từ
    @FXML
    ImageView imageSearch; //Icon tra từ
    @FXML
    Button add;
    @FXML
    Button search;
    @FXML
    Button modify;
    @FXML
    Button delete;

    Image volumeIcon = new Image(new File("src/main/java/com/example/tudien/image/volume.png").toURI().toString());
    Image addIcon = new Image(new File("src/main/java/com/example/tudien/image/add.png").toURI().toString());
    Image modifyIcon = new Image(new File("src/main/java/com/example/tudien/image/edit.png").toURI().toString());
    Image deleteIcon = new Image(new File("src/main/java/com/example/tudien/image/remove.png").toURI().toString());
    Image searchIcon = new Image(new File("src/main/java/com/example/tudien/image/search.png").toURI().toString());

    Alert alertYes = new Alert(Alert.AlertType.WARNING, "Từ đã tồn tại");
    Alert alertAdd = new Alert(Alert.AlertType.INFORMATION, "Thêm thành công");
    Alert alertNo = new Alert(Alert.AlertType.WARNING, "Không tìm thấy từ");
    Alert alertDel = new Alert(Alert.AlertType.INFORMATION, "Xóa thành công");
    Alert alertMo = new Alert(Alert.AlertType.INFORMATION, "Sửa thành công");
    Alert alertError = new Alert(Alert.AlertType.WARNING, "Vui lòng nhập đầy đủ");

    public static int index = 0;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        /* Khi bấm vào ô tra từ sẽ hiện ra tất cả các từ */
        showWord();
//        text.setOnMouseClicked(mouseEvent -> {
//            if (index == 0) { //chỉ show khi click lần đầu
//                showWord();
//                index = 1;
//            }
//        });
        /* Đọc file lịch sử tra từ để hiển thị */
        DictionaryManagement.insertFromFileHistory();
        /* Hiện ra danh sách các từ gợi ý khi text tra từ thay đổi */
        text.textProperty().addListener((observable, oldValue, newValue) -> {
            searchWord();
        });
        /* Thêm ảnh cho các icon */
        imageVolume.setImage(volumeIcon);
        imageAdd.setImage(addIcon);
        imageModify.setImage(modifyIcon);
        imageDelete.setImage(deleteIcon);
        imageSearch.setImage(searchIcon);

        add.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                add.setStyle("-fx-background-color:  #094580;");
            } else {
                add.setStyle("-fx-background-color:   #033566;");
            }
        });

        delete.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                delete.setStyle("-fx-background-color:  #094580;");
            } else {
                delete.setStyle("-fx-background-color:   #033566;");
            }
        });

        modify.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                modify.setStyle("-fx-background-color:  #094580;");
            } else {
                modify.setStyle("-fx-background-color:   #033566;");
            }
        });

        search.hoverProperty().addListener((ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                search.setStyle("-fx-background-color:  #094580;");
            } else {
                search.setStyle("-fx-background-color:   #033566;");
            }
        });
    }

    /* Hiện thị toàn bộ từ điển */
    public void showWord() {
        ArrayList<String> showWord = new ArrayList<String>();
        for (int i = 0; i < Dictionary.words.size(); i++) {
            showWord.add(Dictionary.words.get(i).getWord_target());
        }
        listView.getItems().clear();
        listView.getItems().addAll(showWord);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                monthChanged(observableValue);
//                historySearch(observableValue.getValue());
                imageVolume.setOnMouseClicked(mouseEvent -> {
                    DictionaryManagement.phatAm(observableValue.getValue());
                });
            }
        });
    }

    /* Hàm tra từ được điền vào trong text */
    public void searchWord() {
        if (text.getText().equals("")) {
            //Nếu text trống sẽ hiện toàn bộ từ điển
            showWord();
        } else {
            ArrayList<String> searchWords1 = new ArrayList<String>();
            for (Word i : Dictionary.words) {
                if (i.getWord_target().indexOf(text.getText()) == 0) {
                    System.out.println(i.getWord_target());
                    searchWords1.add(i.getWord_target());
                }
            }
            listView.getItems().clear();
            listView.getItems().addAll(searchWords1);
            listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                    monthChanged(observableValue);
//                    historySearch(observableValue.getValue());
                }
            });
        }
    }

    /*
    Thêm lịch sử tra từ
     */
    public void historySearch(String x) {
        int flag = 0;
        for (int i = 0; i < DictionaryManagement.historyWords.size(); i++) {
            //Thêm từ được tra vào cuối (nếu đã có trong lịch sử thì xóa đi và thêm vào cuối cùng)
            if (DictionaryManagement.historyWords.get(i).equals(x)) {
                DictionaryManagement.historyWords.remove(i);
                DictionaryManagement.historyWords.add(x);
                flag = 1;
                break;
            }
        }
        if (flag == 0) {
            DictionaryManagement.historyWords.add(x);
        }
        //Đảo chiều mảng lịch sử để đưa từ vừa tra lên đầu
        ArrayList<String> list = new ArrayList<String>();
        for (int i = DictionaryManagement.historyWords.size() - 1; i >= 0; i--) {
            list.add(DictionaryManagement.historyWords.get(i));
        }
        //Hiện lịch sử lên giao diện
        historyList.getItems().clear();
//        historyList.getItems().addAll(DictionaryManagement.historyWords);
        historyList.getItems().addAll(list);
        //Thêm sự thay đổi vào file
        DictionaryManagement.dictionaryExportToFileHistory();
    }

    /* Khi click từng phần tử của listView sẽ hiện thông tin của phần từ đó */
    public void monthChanged(ObservableValue<? extends String> observable) {
        for (Word i : Dictionary.words) {
            if (i.getWord_target().equals(observable.getValue())) {
                label.setText(observable.getValue());
                wordExp.setText(i.getWord_explain());
                historySearch(i.getWord_target());
            }
        }
    }

    /*
    Kiểm tra xem từ đã cho tồn tại hay chưa
     */
    public boolean checkWord(String check) {
        for (Word i : Dictionary.words) {
            if (i.getWord_target().equals(check)) {
                return true;
            }
        }
        return false;
    }

    public void addWord(ActionEvent event) {
        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Thêm từ");
        dialog.setHeaderText("Nhập dữ liệu vào các khung, ấn 'X' để hủy");
        dialog.setResizable(true);
        TextField text1 = new TextField();
        text1.setPromptText("Từ: ");
        TextField text2 = new TextField();
        text2.setPromptText("Nghĩa của từ: ");

        GridPane gridPane = new GridPane();
        gridPane.add(text1, 2, 1);
        gridPane.add(text2, 2, 3);
        dialog.getDialogPane().setContent(gridPane);

        ButtonType buttonTypeOk = new ButtonType("Thêm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(addIcon);

        dialog.showAndWait();
//        if (buttonTypeOk == ButtonType.OK) {
//        })
        if (text1.getText().equals("") && !text2.getText().equals("") || !text1.getText().equals("") && text2.getText().equals("")) {
            alertError.showAndWait();
        }
        if (text1.getText().equals("") || text2.getText().equals("")) {
            return;
        }
        if (checkWord(text1.getText())) {
            //Hiển thị thông báo từ đã tồn tại
            alertYes.showAndWait();
        } else {
            //Hiển thị thông báo thêm thành công
            alertAdd.showAndWait();
        }

        Word newWord = new Word(text1.getText(), text2.getText());
        //Gọi hàm thêm từ
        DictionaryManagement.addWord(newWord);
        //Thêm sự thay đổi tới file
        DictionaryManagement.dictionaryExportToFile();
    }

    public void deleteWord (ActionEvent event) {
        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Xóa từ");
        dialog.setHeaderText("Nhập dữ liệu vào các khung, ấn 'X' để hủy");
        dialog.setResizable(true);
        TextField text1 = new TextField();
        text1.setPromptText("Từ cần xóa: ");

        GridPane gridPane = new GridPane();
        gridPane.add(text1, 2, 1);
        dialog.getDialogPane().setContent(gridPane);

        ButtonType buttonTypeOk = new ButtonType("Xóa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(deleteIcon);

        dialog.showAndWait();

        if (text1.getText().equals("")) {
            return;
        }
        if (checkWord(text1.getText())) {
            //Hiển thị thông báo xóa thành công
            alertDel.showAndWait();
        } else {
            //Hiển thị thông báo không tìm thấy từ cần xóa
            alertNo.showAndWait();
        }
        Word delWord = new Word(text1.getText(), null);
        //Gọi hàm xóa từ
        DictionaryManagement.deleteWord(delWord);
        //Thêm sự thay đổi tới file
        DictionaryManagement.dictionaryExportToFile();
    }

    public void modifyWord (ActionEvent event) {
        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Sửa từ");
        dialog.setHeaderText("Nhập dữ liệu vào các khung, ấn 'X' để hủy");
        dialog.setResizable(true);
        TextField text1 = new TextField();
        text1.setPromptText("Từ cần sửa: ");
        TextField text2 = new TextField();
        text2.setPromptText("Nghĩa mới: ");

        GridPane gridPane = new GridPane();
        gridPane.add(text1, 2, 1);
        gridPane.add(text2, 2, 3);
        dialog.getDialogPane().setContent(gridPane);

        ButtonType buttonTypeOk = new ButtonType("Sửa", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(modifyIcon);

        dialog.showAndWait();

        if (text1.getText().equals("") && !text2.getText().equals("") || !text1.getText().equals("") && text2.getText().equals("")) {
            alertError.showAndWait();
        }
        if (text1.getText().equals("") || text2.getText().equals("")) {
            return;
        }
        if (checkWord(text1.getText())) {
            //Hiển thị thông báo sửa thành công
            alertMo.showAndWait();
        } else {
            //Hiển thị thông báo không tìm thấy từ cần sửa
            alertNo.showAndWait();
        }

        Word modWord = new Word(text1.getText(), text2.getText());
        //Gọi hàm sửa từ
        DictionaryManagement.modifiWord(modWord);
        //Thêm sự thay đổi tới file
        DictionaryManagement.dictionaryExportToFile();
    }

    /* Click vào about sẽ hiện thông tin chương trình */
    public void about(ActionEvent event) {
        Dialog<Word> dialog = new Dialog<>();
        dialog.setTitle("Từ điển Anh - Việt");
        dialog.setHeaderText("Chương trình từ điển được làm bởi nhóm số 14");
        dialog.setResizable(true);
        Label label1 = new Label();
        label1.setText("Vũ Đức Hải\n19020538");
        Label label2 = new Label();
        label2.setText("Nguyễn Tiến Đạt\n19020521");
        Label label3 = new Label();
        label3.setText("Vũ Hoàng Dương\n19020533");

        GridPane gridPane = new GridPane();
        gridPane.add(label1, 1, 1);
        gridPane.add(label2, 1, 2);
        gridPane.add(label3, 1, 3);
        dialog.getDialogPane().setContent(gridPane);
        ButtonType buttonTypeOk = new ButtonType("Đóng", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);
        dialog.showAndWait();
    }

    public boolean SoSanh(String x, String searchWord, int saiSo) {
        int i, j, k, loi;
        if (x.length() < (searchWord.length() - saiSo) || x.length() > (searchWord.length() + saiSo))
            return false;
        i = j = loi = 0;
        while (i < searchWord.length() && j < x.length()) {
            if (searchWord.charAt(i) != x.charAt(j)) {
                loi++;
                for (k = 1; k <= saiSo; k++) {
                    if ((i + k < searchWord.length()) && searchWord.charAt(i + k) == x.charAt(j)) {
                        i += k;
                        break;
                    } else if ((j + k < x.length()) && searchWord.charAt(i) == x.charAt(j + k)) {
                        j += k;
                        break;
                    }
                }
            }
            i++;
            j++;
        }
        loi += searchWord.length() - i + x.length() - j;
        if (loi <= saiSo)
            return true;
        else
            return false;
    }

    public ArrayList adDictionarySearcher(String searchWord) {
        int saiSo;
        ArrayList<String> addWord = new ArrayList<String>();
        saiSo = (int) Math.round(searchWord.length() * 0.5);
        for(Word x : Dictionary.words) {
            if(SoSanh(x.getWord_target(), searchWord, saiSo)) {
                System.out.println(x.getWord_target());
                addWord.add(x.getWord_target());
            }
        }
        return addWord;
    }

    public void addD(ActionEvent event) {
        ArrayList<String> newA = new ArrayList<String>();

        newA = adDictionarySearcher(text.getText());
        listView.getItems().clear();
        listView.getItems().addAll(newA);
    }

    /* Đóng chương trình */
    public void exit(ActionEvent event) {
        System.exit(0);
    }
}