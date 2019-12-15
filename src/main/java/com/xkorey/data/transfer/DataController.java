package com.xkorey.data.transfer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@Slf4j
public class DataController {

  @RequestMapping({"/", "/index", "/index.html"})
  public Map dataReceiver(HttpServletRequest request) {
    return request.getParameterMap();
  }

  @RequestMapping("/1.php")
  public String phpReceiver(String data) {
    log.info("data {}", data);
    return data;
  }

  @RequestMapping("/{str}")
  public String stringAll(@PathVariable("str") String str) {
    log.info("txt {}", str);
    return str;
  }
}
