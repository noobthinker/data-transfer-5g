package com.xkorey.data.transfer.bean;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@Entity
public class OriginText {
  @Id private String id;
  private String txt;
  private Date createdTime;
  private Float momentQuality,
      accumulateQuality,
      momentVolume,
      accumulateVolume,
      density,
      temperature;
}
