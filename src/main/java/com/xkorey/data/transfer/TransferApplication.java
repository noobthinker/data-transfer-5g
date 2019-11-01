package com.xkorey.data.transfer;

import com.xkorey.data.transfer.netty.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class TransferApplication {

    @Autowired
    Server server;

  public static void main(String[] args) {
    SpringApplication.run(TransferApplication.class, args);
  }

  @PostConstruct
    void init() throws InterruptedException {
      server.start();
  }
}
