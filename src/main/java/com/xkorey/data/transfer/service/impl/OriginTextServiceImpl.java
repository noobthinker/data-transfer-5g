package com.xkorey.data.transfer.service.impl;

import cn.hutool.core.lang.Snowflake;
import com.xkorey.data.transfer.bean.OriginText;
import com.xkorey.data.transfer.dao.OriginTextDao;
import com.xkorey.data.transfer.service.OriginTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
@Slf4j
public class OriginTextServiceImpl implements OriginTextService {

  @Autowired private OriginTextDao textDao;

  @Autowired private Snowflake snowflake;

  @Override
  @Transactional
  public void save(OriginText text) {
    log.info("text {}", text);
    textDao.save(text);
  }

  @Override
  @Transactional
  public void save(String text) {
    OriginText txt = new OriginText();
    txt.setId(snowflake.nextIdStr());
    txt.setCreatedTime(new Date());
    txt.setTxt(text);
    textDao.save(txt);
  }
}
