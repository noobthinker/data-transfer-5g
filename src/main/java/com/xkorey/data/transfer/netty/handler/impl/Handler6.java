package com.xkorey.data.transfer.netty.handler.impl;

import com.xkorey.data.transfer.bean.OriginText;
import com.xkorey.data.transfer.netty.handler.Handler;
import com.xkorey.data.transfer.service.OriginTextService;

import java.util.ArrayList;
import java.util.List;

public class Handler6 implements Handler {



  @Override
  public void target(List<String> hexList, OriginTextService service) {
    OriginText text = new OriginText();
    List<Float> numbers = new ArrayList<>();
    int addr = 0;
    for (String fs : hexList) {
      if (fs.length() == 2 && 0==addr) {
        addr = Integer.parseInt(fs, 16);
        continue;
      }
      Long i = Long.parseLong(fs, 16);
      Float f = Float.intBitsToFloat(i.intValue());
      numbers.add(f);
    }
    int i = 0;
    text.setAddress(addr);
    text.setMomentQuality(numbers.get(i++));
    text.setAccumulateQuality(numbers.get(i++));
    text.setMomentVolume(numbers.get(i++));
    text.setAccumulateVolume(numbers.get(i++));
    text.setDensity(numbers.get(i++));
    text.setTemperature(numbers.get(i++));
    service.save(text);
  }
}
