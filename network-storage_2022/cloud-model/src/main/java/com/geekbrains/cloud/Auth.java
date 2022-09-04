package com.geekbrains.cloud;

import lombok.Data;
@Data

public class Auth implements CloudMessage{
    private final String login;
    private final String password;
}
