package com.xkorey.data.transfer.dao;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Tuple;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
@Slf4j
public class ToolDaoImpl implements Tool {

  @Autowired @PersistenceContext private EntityManager entityManager;

  public Tuple havZNumber(String number) {
    List result =
        entityManager
            .createNativeQuery(
                "select HITPILENO,zt,Sgkssj,Shijizc,SGJSSJ from jiaobanzhuang where HITPILENO='"
                    + number
                    + "'")
            .getResultList();
    if (result.size() == 0) {
      return null;
    }

    Object[] r = (Object[]) result.get(0);
    if (StrUtil.isBlank("" + r[3])) {
      return new Tuple(r[0], r[1], r[2], r[3], r[4]);
    } else {
      return new Tuple(r[0], r[1], r[2], "0", r[4]);
    }
  }

  public void updateData(String number, Integer status, Date begin, Date end, Number deep) {
    String sql = "update jiaobanzhuang set # where HITPILENO='" + number + "'";
    List<String> sub = new ArrayList();
    boolean set = false;
    if (null != status) {
      sub.add(" zt='" + status + "'");
      set = true;
    }
    if (null != begin) {
      sub.add(
          " Sgkssj = to_date('" + DateUtil.formatDateTime(begin) + "','yyyy-mm-dd hh24:mi:ss')");
      set = true;
    }
    if (null != end) {
      sub.add(" SGJSSJ = to_date('" + DateUtil.formatDateTime(end) + "','yyyy-mm-dd hh24:mi:ss')");
      set = true;
    }
    if (null != deep) {
      String _deep = NumberUtil.decimalFormat("#.0000", deep.doubleValue());
      if (_deep.length() > 10) {
        StrUtil.sub(_deep, 0, 10);
      }
      sub.add(" Shijizc = '" + _deep + "'");
      set = true;
    }
    if (set) {
      sql = sql.replace("#", CollectionUtil.join(sub, ","));
      log.info("update sql: {}", sql);
      entityManager.createNativeQuery(sql).executeUpdate();
    }
  }
}
