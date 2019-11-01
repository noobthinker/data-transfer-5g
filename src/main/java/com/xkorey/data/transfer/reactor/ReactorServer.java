package com.xkorey.data.transfer.reactor;

import io.netty.channel.ChannelOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.netty.DisposableServer;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.resources.LoopResources;
import reactor.netty.tcp.TcpServer;

import java.nio.charset.Charset;

@Slf4j
@Component
public class ReactorServer {

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
                              return out.sendString(Mono.just(s + "??"));
                            })
                        .log("reactor-tcp-server"))
            .port(8686)
            .bindNow();
    server.onDispose().block();
  }
}
