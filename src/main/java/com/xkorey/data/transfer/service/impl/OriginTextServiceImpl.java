package com.xkorey.data.transfer.service.impl;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.NumberUtil;
import com.xkorey.data.transfer.bean.Origin4Text;
import com.xkorey.data.transfer.bean.OriginText;
import com.xkorey.data.transfer.dao.Origin4TextDao;
import com.xkorey.data.transfer.dao.OriginTextDao;
import com.xkorey.data.transfer.dao.Tool;
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

  @Autowired private Origin4TextDao origin4TextDao;

  @Autowired private Tool tool;

  @Override
  @Transactional
  public void save(OriginText text) {
    log.info("text {}", text);
    text.setId(snowflake.nextIdStr());
    text.setCreatedTime(new Date());
    textDao.save(text);
  }

  @Override
  @Transactional
  public void save(Origin4Text text) {
    log.info("text {}", text);
    text.setId(snowflake.nextIdStr());
    text.setCreatedTime(new Date());
    Tuple result = tool.havZNumber(text.getZ_number());
    Integer status = null;
    Number deep = null;
    Date begin = null, end = null;
    if (null == result.get(1)) {
      status = text.getZ_status();
      begin = new Date();
    }
    if (null != result.get(3)) {
      Number _deep = NumberUtil.parseNumber(result.get(3));
      if (text.getF2().compareTo(_deep.floatValue()) > 0) {
        deep = _deep;
      }
    } else {
      deep = result.get(3);
    }
    if (null != text.getZ_status() && 2 == text.getZ_status()) {
      end = new Date();
    }
    tool.updateData(text.getZ_number(), status, begin, end, deep);
    origin4TextDao.save(text);
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
