package com.xkorey.data.transfer.service;

import com.xkorey.data.transfer.bean.Origin4Text;
import com.xkorey.data.transfer.bean.OriginText;

public interface OriginTextService {
    void save(OriginText text);
    void save(Origin4Text text);

    void save(String text);
}
