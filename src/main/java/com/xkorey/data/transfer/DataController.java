package com.xkorey.data.transfer;

import com.xkorey.data.transfer.service.OriginTextService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
public class DataController {

  @Autowired
  private OriginTextService service;

  @RequestMapping({"/", "/index", "/index.html"})
  public Map dataReceiver(HttpServletRequest request) {
    return request.getParameterMap();
  }

  @RequestMapping("/1.php")
  public String phpReceiver(String data) {
    log.info("data {}", data);
    service.save(data);
    return data;
  }

  @RequestMapping("/{str}")
  public String stringAll(@PathVariable("str") String str) {
    log.info("txt {}", str);
    return str;
  }
}
