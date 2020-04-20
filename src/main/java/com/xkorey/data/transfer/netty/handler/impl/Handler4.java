package com.xkorey.data.transfer.netty.handler.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.HexUtil;
import com.xkorey.data.transfer.bean.Origin4Text;
import com.xkorey.data.transfer.netty.handler.Handler;
import com.xkorey.data.transfer.service.OriginTextService;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.List;

@Slf4j
public class Handler4 implements Handler {

    @Override
    public void target(List<String> hexList, OriginTextService service) {
        Origin4Text text = new Origin4Text();
        if(CollectionUtil.isEmpty(hexList)){
            log.error("解析错误");
        }
        if(CollectionUtil.isNotEmpty(hexList) && hexList.size()!=4){
            log.error("长度不正确 {}",hexList.size());
        }
        String h = hexList.get(0);
        if(2==h.length()){
            int addr = Integer.parseInt(h, 16);
            text.setAddress(addr);
        }else{
            Long i = Long.parseLong(h, 16);
            Float f = Float.intBitsToFloat(i.intValue());
            text.setF1(f);
        }
        h = hexList.get(1);
        if(2==h.length()){
            int addr = Integer.parseInt(h, 16);
            text.setAddress(addr);
        }else{
            Long i = Long.parseLong(h, 16);
            Float f = Float.intBitsToFloat(i.intValue());
            text.setF2(f);
        }
        h = hexList.get(2);
        if(2==h.length()){
            int addr = Integer.parseInt(h, 16);
            text.setAddress(addr);
        }else{
            String number = HexUtil.decodeHexStr(h, Charset.forName("GBK"));
            text.setZ_number(number);
        }
        h = hexList.get(3);
        int status = Integer.parseInt(h, 16);
        text.setZ_status(status);
        service.save(text);
    }
}
