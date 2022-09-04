package com.geekbrains.cloud.netty;

import com.geekbrains.cloud.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CloudFileHandler extends SimpleChannelInboundHandler<CloudMessage> {
    private String nik;

    private Path currentDir;
    private DbAuthenticationProvider dbAuthenticationProvider;
    
  //  private BdConnect dbConnection;

    public CloudFileHandler() {
       this.dbAuthenticationProvider = new DbAuthenticationProvider();
       dbAuthenticationProvider.init();

        currentDir = Path.of("server_files").resolve(nik);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

       // ctx.writeAndFlush(new ListFiles(currentDir));// отправляем  спиок файлов при подключении
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CloudMessage cloudMessage) throws Exception {
        // запрос на вход перезаем логин и пароль
        if (cloudMessage instanceof Auth auth) {
            // ищем по базе данных есл находим передаем логин
             nik = dbAuthenticationProvider.getNicknameByLoginAndPassword(auth.getLogin(), auth.getPassword());
            System.out.println("Auth" + nik);
            if (Files.exists(currentDir.resolve(nik))) {
                currentDir = (currentDir.resolve(nik));
                ctx.writeAndFlush(new ListFiles(currentDir));
            } else
                Files.createDirectory(currentDir.resolve(nik));
            currentDir = currentDir.resolve(nik);
            ctx.writeAndFlush(new ListFiles(currentDir));
        }



       else if (cloudMessage instanceof FileRequest fileRequest) {
            ctx.writeAndFlush(new FileMessage(currentDir.resolve(fileRequest.getName())));// передаем путь до дирректории

        } else if (cloudMessage instanceof FileMessage fileMessage) {
            Files.write(currentDir.resolve(fileMessage.getName()), fileMessage.getData());
            ctx.writeAndFlush(new ListFiles(currentDir));
        }
        else if (cloudMessage instanceof PathUpRequest pathUpRequest){
           currentDir = currentDir.getParent();
                ctx.writeAndFlush(new ListFiles(currentDir));

            }

        // переход в поддиректорию
        else if (cloudMessage instanceof PathInRequest pathInRequest){
                 currentDir = (currentDir.resolve(pathInRequest.getName()));// прописываем новый путь до файла
                ctx.writeAndFlush(new ListFiles(currentDir));// передаем
            }
        }

    }

