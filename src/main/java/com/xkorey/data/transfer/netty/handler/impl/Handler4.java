package com.xkorey.data.transfer.netty.handler.impl;

import com.xkorey.data.transfer.bean.Origin4Text;
import com.xkorey.data.transfer.netty.handler.Handler;
import com.xkorey.data.transfer.service.OriginTextService;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Handler4 implements Handler {

    @Override
    public void target(List<String> hexList, OriginTextService service) {
        Origin4Text text = new Origin4Text();
        List<Number> numbers = new ArrayList<>();
        int addr = 0;
        for (String fs : hexList) {
            if (fs.length() == 2 && 0==addr) {
                addr = Integer.parseInt(fs, 16);
                continue;
            }
            if(fs.length() == 2){
                numbers.add(Integer.parseInt(fs, 16));
                continue;
            }
            Long i = Long.parseLong(fs, 16);
            Float f = Float.intBitsToFloat(i.intValue());
            numbers.add(f);
        }
//        第一个是  钻进速度  第二个是深度   第三个是桩号（0-9999）  第4个状态
        text.setAddress(addr);
        text.setF1(numbers.get(0).floatValue());
        text.setF2(numbers.get(1).floatValue());
        text.setZ_number(numbers.get(2).intValue()+"");
        text.setZ_status(numbers.get(3).intValue());
        service.save(text);

    }
}
