package com.xkorey.data.transfer.netty.handler;

import cn.hutool.core.lang.Snowflake;
import com.xkorey.data.transfer.service.OriginTextService;

import java.util.List;

public interface Handler {

    void target(List<String> hexList, OriginTextService service);

}
