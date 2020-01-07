package com.xkorey.data.transfer.netty;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.ghgande.j2mod.modbus.msg.ReadMultipleRegistersRequest;
import com.ghgande.j2mod.modbus.util.ModbusUtil;
import com.xkorey.data.transfer.bean.OriginText;
import com.xkorey.data.transfer.service.OriginTextService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@Component("nettyServerHandler")
@ChannelHandler.Sharable
public class ServerHandler extends ChannelInboundHandlerAdapter {

  @Autowired private OriginTextService service;

  ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(1000, 2);

  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    log.info("client active");
  }

  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    log.info("client Inactive");
  }

  private OriginText getBean(ByteBuf buf) {
    OriginText text = new OriginText();
    byte[] con = new byte[buf.readableBytes()];
    buf.readBytes(con);
    try {
      List<String> hexList = hexNumbers(con);
      List<Float> numbers = new ArrayList<>();
      for (String fs : hexList) {
        Long i = Long.parseLong(fs, 16);
        Float f = Float.intBitsToFloat(i.intValue());
        numbers.add(f);
      }
      int i=0;
      text.setMomentQuality(numbers.get(i++));
      text.setAccumulateQuality(numbers.get(i++));
      text.setMomentVolume(numbers.get(i++));
      text.setAccumulateVolume(numbers.get(i++));
      text.setDensity(numbers.get(i++));
      text.setTemperature(numbers.get(i++));
      return text;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  private String getMessage(ByteBuf buf) {
    byte[] con = new byte[buf.readableBytes()];
    buf.readBytes(con);
    try {
      List<String> hexList = hexNumbers(con);
      List<String> numbers = new ArrayList<>();
      for (String fs : hexList) {
        Long i = Long.parseLong(fs, 16);
        Float f = Float.intBitsToFloat(i.intValue());
        numbers.add(f.toString());
      }
      if (CollectionUtil.isNotEmpty(numbers)) {
        return CollUtil.join(numbers, ",");
      }
      return StringUtils.join(new String(con, "GBK"));
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    // 第一种：接收字符串时的处理
    ByteBuf buf = (ByteBuf) msg;
    //        ReadMultipleRegistersResponse res;
    //        req.setUnitID(1);
    //        req.setTransactionID(1000);
    //        req.setHeadless(true);
    //        BytesOutputStream byteOutputStream = new BytesOutputStream(req.getDataLength());
    //        try {
    //            req.writeTo(byteOutputStream);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //        byte[] bs = byteOutputStream.toByteArray();
    //        int[] crc = ModbusUtil.calculateCRC(bs,0,bs.length);
    //        ByteBuffer bf = ByteBuffer.allocate(bs.length+2);
    //        bf.put(bs);
    //        bf.putInt(crc[1]);
    //        bf.putInt(crc[0]);
    OriginText bean = getBean(buf);
//    String rev = getMessage(buf);
//    log.info("receive {}", rev);
    service.save(bean);
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
    int step = 8;
    int begin = 6;
    IntStream.range(0, 6)
        .forEach(i -> result.add(subStr.substring(begin + i * step, begin + (i + 1) * step)));
    return result;
  }
}
