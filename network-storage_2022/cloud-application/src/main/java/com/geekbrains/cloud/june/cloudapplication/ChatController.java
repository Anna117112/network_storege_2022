package com.geekbrains.cloud.june.cloudapplication;

import com.geekbrains.cloud.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.net.*;

public class ChatController implements Initializable {
    @FXML
    public TextField server;
    @FXML
    public TextField client;
    @FXML
    public TextField login;
    @FXML
    public TextField password;
    @FXML
    public HBox box;
    @FXML
    public Button boxDelete;


    private String homeDir;

    @FXML
    public ListView<String> clientView;

    @FXML
    public ListView<String> serverView;

    private Network network;
    private String username;
// панель авторизаци должна исчезать послеавторизации но не исчезает чтоне так
    // передаем в нее null после авторизаци передаем
    public void setUsername(String username) {
        boolean usernameIsNull = username == null;
        login.setVisible(usernameIsNull);
        login.setManaged(usernameIsNull);
        password.setManaged(usernameIsNull);

    }
    //читаем сообщения от срвера
    private void readLoop() {
        try {
            while (true) {
                // выводим все файли с сервера
                CloudMessage message = network.read();
                if (message instanceof ListFiles listFiles) {
                    Platform.runLater(() -> {
                        // передаем имя дирретории чтды панель авторизации исчезла
                        username = listFiles.getName();
                        serverView.getItems().clear();
                        serverView.getItems().addAll(listFiles.getFiles()); // выводим все файли с сервера
                        server.clear();
                        server.appendText(listFiles.getName());

                    });

                    // передаем файл с сервера на клиент
                }
                else if (message instanceof FileMessage fileMessage) {
                    Path current = Path.of(homeDir).resolve(fileMessage.getName());
                    Files.write(current, fileMessage.getData());
                    Platform.runLater(() -> {
                        clientView.getItems().clear();
                        clientView.getItems().addAll(getFiles(homeDir));
                    });
                }

                }

            }
         catch (Exception e) {
            System.err.println("Connection lost");
        }
    }

    // post init fx fields
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            homeDir = "client_files";
            Path pat = Path.of(homeDir).toAbsolutePath();
            System.out.println(pat);
            setUsername(null);
            setUsername(username);

            clientView.getItems().clear();
            clientView.getItems().addAll(getFiles(homeDir));
            network = new Network(8189);
            doubleClickClient();
            doubleClickServer();
            Thread readThread = new Thread(this::readLoop);
            readThread.setDaemon(true);
            readThread.start();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private List<String> getFiles(String dir) {
        String[] list = new File(dir).list();
        assert list != null;
        return Arrays.asList(list);
    }
// кнопка предачи файла не сервер
    public void upload(ActionEvent actionEvent) throws IOException {
        //записываем в строку имя выбраного файла в fx
        String file = clientView.getSelectionModel().getSelectedItem();
        System.out.println(file);
        // отправляем путь к файлу
        network.write(new FileMessage(Path.of(homeDir).resolve(file)));
    }
// скачать с сервера
    public void download(ActionEvent actionEvent) throws IOException {
        String file = serverView.getSelectionModel().getSelectedItem();// выбранный файл
        network.write(new FileRequest(file));// передаем на сервер имя файла
    }
    // двойной клик по дирректории на листе клиенат должын раскрыть папки
    public void doubleClickClient() {
        clientView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @java.lang.Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    // если был двойной клик то записываем в строку имя файла по которому был клик
                    String fileClient = clientView.getSelectionModel().getSelectedItem();
                    Path path = Path.of(homeDir).resolve(fileClient);
                    System.out.println( "doubleClickClient   " +path);

                    homeDir= path.toString();
                    // чистим лист
                    clientView.getItems().clear();
                    // передаем в метод имя директории который прописывет правильный путь и возвращает в dir путь до нового файла
                  //  pathDirectory(fileClient);
                    // выводим на экран вложенные папки и файлы
                    clientView.getItems().addAll(getFiles(homeDir));
                    client.clear();
                    client.appendText(homeDir);

//                    try {
//                        network.write(new PathInRequest(fileClient, "clientView"));
//                        System.out.println(fileClient + " отправили ");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                 //   System.out.println(fileClient);


                } else
                    System.out.println(0);
            }
        });
    }
    // двойной клик по списку папок сервера должен открыть вложения
    public void doubleClickServer() {
        serverView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @java.lang.Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    // записываем в стрку имя дирректори/файла на которы клик был два раза
                    String fileServer = serverView.getSelectionModel().getSelectedItem();
                    Path path = Path.of(fileServer).toAbsolutePath();
                    System.out.println( "doubleClickServer   "+path);

                    try {
                        // передаем инфо на сервер
                        network.write(new PathInRequest(fileServer, "serverView"));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else
                    System.out.println(0);
            }
        });
    }
//    private void pathDirectory (String name){
//        System.out.println(name + "pathDirectory");
//
//        Path path = Path.of(dir).resolve(name).toAbsolutePath();
//        System.out.println(path);
//        //System.out.println(path3);
//        String s = path.toString();
//        dir = s.replace('\\', '/');
//        System.out.println(dir);
//    }
//переход в родительскую дирректрию для сервера
    public void upServerFiles(ActionEvent actionEvent) throws IOException {
        network.write(new PathUpRequest(" "));// передаем имя файла на сервер

    }
// переход в родительскую дирректрию для клиента
    public void upClientFiles(ActionEvent actionEvent) throws IOException {


        Path path = Path.of(homeDir).getParent();
        homeDir = path.toString();
       // network.write(new PathUpRequest("c"));

        clientView.getItems().clear();
        clientView.getItems().addAll(getFiles(homeDir));
        client.clear();
        client.appendText(homeDir);
       // C:\Users\adyak\IdeaProjects\geek-cloud-2022-june\client_files\1
    }
    // подключение клиента
    public void auth (ActionEvent actionEvent) throws IOException {
        network.write(new Auth(login.getText(),password.getText()));// передаем логин и пароль
        System.out.println(login.getText() + password.getText());

    }

}







