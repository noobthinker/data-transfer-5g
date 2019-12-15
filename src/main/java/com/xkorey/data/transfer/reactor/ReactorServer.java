package com.xkorey.data.transfer.reactor;

import com.xkorey.data.transfer.service.OriginTextService;
import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.tcp.TcpServer;

@Slf4j
@Component
public class ReactorServer {

  @Autowired private OriginTextService service;

  public void start() {
    DisposableServer server =
        TcpServer.create()
            .wiretap(true)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
            .handle(
                (in, out) ->
                    in.receive()
                        .asString()
                        .flatMap(
                            s -> {
                              log.info("s {}", s);
                              service.save(s);
                              return out.sendString(Mono.just(s + "??"));
                            })
                        .log("reactor-tcp-server"))
            .port(8686)
            .bindNow();
    server.onDispose().block();
  }
}
