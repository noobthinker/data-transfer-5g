package com.xkorey.data.transfer.netty;

import cn.hutool.core.thread.ThreadUtil;
import com.xkorey.data.transfer.dao.Tool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Server {

  @Autowired private Server6 server6;

  @Autowired private Server4 server4;

  public void start() {
    ThreadUtil.execute(
        () -> {
          try {
            server6.start();
          } catch (InterruptedException e) {
            log.error("server6", e);
          }
        });
    ThreadUtil.execute(
        () -> {
          try {
            server4.start();
          } catch (InterruptedException e) {
            log.error("server6", e);
          }
        });
  }
}
