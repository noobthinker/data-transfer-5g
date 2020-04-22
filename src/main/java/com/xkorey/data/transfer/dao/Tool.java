package com.xkorey.data.transfer.dao;

import cn.hutool.core.lang.Tuple;

import java.util.Date;

public interface Tool {
    Tuple havZNumber(String number);

    void updateData(String number, Integer status, Date begin, Date end, Number deep);
}
