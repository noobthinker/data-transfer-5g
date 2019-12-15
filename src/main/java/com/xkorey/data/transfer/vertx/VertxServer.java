package com.xkorey.data.transfer.vertx;

import com.xkorey.data.transfer.service.OriginTextService;
import io.vertx.core.Vertx;
import io.vertx.core.net.NetServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VertxServer {

  @Autowired private OriginTextService service;

  public void start() {
    // 创建TCP服务器
    NetServer server = Vertx.vertx().createNetServer();
    // 处理连接请求
    server.connectHandler(
        socket -> {
          socket.handler(
              buffer -> {
                // 在这里应该解析报文，封装为协议对象，并找到响应的处理类，得到处理结果，并响应
                System.out.println("接收到的数据为：" + buffer.toString());
                service.save(buffer.toString());
                // 按照协议响应给客户端
                socket.write(buffer.toString() + "???");
              });

          // 监听客户端的退出连接
          socket.closeHandler(
              close -> {
                System.out.println("客户端退出连接");
              });
        });

    // 监听端口
    server.listen(
        8787,
        res -> {
          if (res.succeeded()) {
            System.out.println("服务器启动成功");
          }
        });
  }
}
