package com.geekbrains.cloud;

import java.io.IOException;

public class PathInRequest implements CloudMessage{

    public String getName() {
        return name;
    }

//    public String getList() {
//        return list;
//    }


    private  String name;
    //private  String list;
public PathInRequest(String filename, String nameList)throws IOException{
    this.name = filename;
    //this.list = nameList;


}


//                    String file = listView.getSelectionModel().getSelectedItem();
//                    Path path = Paths.get(file);
//                    File fileDir = new File(file);
//                    // если это папка и она не пустая
//                    if (Files.isDirectory(path)&& fileDir.list().length>0) {
//                        Platform.runLater(() -> {
//                            listView.getItems().clear();
//                            listView.getItems().addAll(getFiles(homeDir));
//                        });
//
//                        listView.getItems().addAll(getFiles(file));
//
//
//
//                    }
//                }
//                // если это папка и она существует
//
//            }
//
//        });
//    }

}
