package com.geekbrains.cloud;


import java.io.IOException;
import lombok.Data;

@Data

public class PathUpRequest implements CloudMessage {
    private final String name;

//    public String getNameListView() {
//        return nameListView;
//    }
//
//
//    public String getDir() {
//        return dir;
//    }
//
//    private String dir;
//    private String nameListView;
//
//    public PathUpRequest(String namelist)throws IOException {
//        this.nameListView = namelist;
//
//
//    }
}
