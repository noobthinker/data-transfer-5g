package com.xkorey.data.transfer.netty;

import cn.hutool.core.util.HexUtil;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.xkorey.data.transfer.dao.Tool;
import com.xkorey.data.transfer.netty.handler.Handler;
import com.xkorey.data.transfer.service.OriginTextService;
import com.xkorey.data.transfer.util.DaoHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@ChannelHandler.Sharable
@AllArgsConstructor
@NoArgsConstructor
public class ServerHandler extends ChannelInboundHandlerAdapter {

  private OriginTextService service;

  private Handler handler;

  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("client active");
  }

  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("client Inactive");
  }

  private List<String> hexList(ByteBuf buf){
    byte[] con = new byte[buf.readableBytes()];
    buf.readBytes(con);
    return hexNumbers(con);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // 第一种：接收字符串时的处理
    ByteBuf buf = (ByteBuf) msg;
    // 处理消息
//    DaoHelper.DAO.tool.set(tool);
    handler.target(hexList(buf),service);
//    DaoHelper.DAO.tool.remove();
    ctx.writeAndFlush("success");
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    System.out.println("服务端接收数据完毕..");
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.close();
    System.out.println("异常信息：\r\n" + cause.getMessage());
  }

  private List<String> hexNumbers(byte[] data) {
    String hexStr = ModbusUtil.toHex(data);

    log.info("hex string {}", hexStr);
    List<String> result = new ArrayList<>();
    final String subStr = StringUtils.remove(hexStr, " ");
    result.add(hexStr.substring(0,2));
    int step = 8;
    int begin = 6;
    IntStream.range(0, 6)
        .forEach(i -> result.add(subStr.substring(begin + i * step, begin + (i + 1) * step)));
    return result;
  }

}
