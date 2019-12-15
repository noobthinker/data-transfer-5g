package com.xkorey.data.transfer;

import com.xkorey.data.transfer.netty.Server;
import com.xkorey.data.transfer.reactor.ReactorServer;
import com.xkorey.data.transfer.vertx.VertxServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
@Slf4j
public class TransferApplication {

  @Autowired Server server;

  @Autowired ReactorServer reactorServer;

  @Autowired VertxServer vertxServer;

  public static void main(String[] args) {
    SpringApplication.run(TransferApplication.class, args);
  }

  @PostConstruct
  void init() throws InterruptedException {
    Thread t =
        new Thread(
            () -> {
              log.info("reactor netty start");
              reactorServer.start();
            });
    Thread tt =
        new Thread(
            () -> {
              log.info("netty start");
              try {
                server.start();
              } catch (InterruptedException e) {
                e.printStackTrace();
              }
            });
    t.start();
    tt.start();
    vertxServer.start();
    log.info("start..");
  }
}
