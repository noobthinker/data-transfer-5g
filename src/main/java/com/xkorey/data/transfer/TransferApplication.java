package com.xkorey.data.transfer;

import cn.hutool.core.thread.ThreadUtil;
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
      ThreadUtil.execute(()->{
          log.info("netty start");
          server.start();
      });
    log.info("start..");
  }
}
