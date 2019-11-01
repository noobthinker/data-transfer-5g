package com.xkorey.data.transfer.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class Client {
  public static void main(String[] args) throws InterruptedException {
      EventLoopGroup group = new NioEventLoopGroup();
      try {
          Bootstrap b = new Bootstrap();
          b.group(group) // 注册线程池
                  .channel(NioSocketChannel.class) // 使用NioSocketChannel来作为连接用的channel类
                  .remoteAddress(new InetSocketAddress("localhost", 8585)) // 绑定连接端口和host信息
                  .handler(new ChannelInitializer<SocketChannel>() { // 绑定连接初始化器
                      @Override
                      protected void initChannel(SocketChannel ch) throws Exception {
                          System.out.println("正在连接中...");
                          ch.pipeline().addLast(new StringEncoder(Charset.forName("GBK")));
                          ch.pipeline().addLast(new ClientHandler());
                          ch.pipeline().addLast(new ByteArrayEncoder());
                          ch.pipeline().addLast(new ChunkedWriteHandler());
                      }
                  });
          // System.out.println("服务端连接成功..");

          ChannelFuture cf = b.connect().sync(); // 异步连接服务器
          System.out.println("服务端连接成功..."); // 连接完成

          cf.channel().closeFuture().sync(); // 异步等待关闭连接channel
          System.out.println("连接已关闭.."); // 关闭完成

      } finally {
          group.shutdownGracefully().sync(); // 释放线程池资源
      }
  }

    static class  ClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端与服务端通道-开启：" + ctx.channel().localAddress() + "channelActive");
            String sendInfo = "1234567891112131415161718192021221322425262728293031";
            System.out.println("客户端准备发送的数据包：" + sendInfo);
            ctx.writeAndFlush(Unpooled.copiedBuffer(sendInfo, CharsetUtil.UTF_8)); // 必须有flush

        }

        public void channelInactive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("客户端与服务端通道-关闭：" + ctx.channel().localAddress() + "channelInactive");
        }

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
            System.out.println("读取客户端通道信息..");
            ByteBuf buf = msg.readBytes(msg.readableBytes());
            System.out.println(
                    "客户端接收到的服务端信息:" + ByteBufUtil.hexDump(buf) + "; 数据包为:" + buf.toString(Charset.forName("utf-8")));
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            ctx.close();
            System.out.println("异常退出:" + cause.getMessage());
        }
    }
}
