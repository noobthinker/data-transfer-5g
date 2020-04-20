package com.xkorey.data.transfer.netty;

import com.xkorey.data.transfer.netty.handler.Handler;
import com.xkorey.data.transfer.netty.handler.impl.Handler6;
import com.xkorey.data.transfer.service.OriginTextService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;

@Component
@Slf4j
public class Server6 {

  @Autowired private OriginTextService service;

  Handler handler = null;

  private ChannelInboundHandlerAdapter handlerAdapter;

  @PostConstruct
  void init() {
    handler = new Handler6();
    handlerAdapter = new ServerHandler(service, handler);
  }

  public void start() throws InterruptedException {
    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      ServerBootstrap sb = new ServerBootstrap();
      sb.option(ChannelOption.SO_BACKLOG, 1024);
      sb.group(group, bossGroup) // 绑定线程池
          .channel(NioServerSocketChannel.class) // 指定使用的channel
          .localAddress(8585) // 绑定监听端口
          .childHandler(
              new ChannelInitializer<SocketChannel>() { // 绑定客户端连接时候触发操作

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  System.out.println("报告");
                  System.out.println("信息：有一客户端链接到本服务端");
                  System.out.println("IP:" + ch.localAddress().getHostName());
                  System.out.println("Port:" + ch.localAddress().getPort());
                  System.out.println("报告完毕");

                  ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                  ch.pipeline().addLast(handlerAdapter); // 客户端触发操作
                  ch.pipeline().addLast(new ByteArrayEncoder());
                }
              });
      ChannelFuture cf = sb.bind().sync(); // 服务器异步创建绑定
      System.out.println(Server.class + " 启动正在监听： " + cf.channel().localAddress());
      //      cf.channel().closeFuture().sync(); // 关闭服务器通道
    } finally {
      //      group.shutdownGracefully().sync(); // 释放线程池资源
      //      bossGroup.shutdownGracefully().sync();
    }
  }
}
