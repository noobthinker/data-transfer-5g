package com.xkorey.data.transfer.conf;

import cn.hutool.core.lang.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfigure {

  @Bean
  public Snowflake snowflake() {
    return new Snowflake(1, 1);
  }
}
