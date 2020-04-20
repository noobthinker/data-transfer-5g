package com.xkorey.data.transfer.bean;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@Entity
@Table(name = "origin4_text")
public class Origin4Text {

    @Id
    private String id;

    private Date createdTime;
    private Integer address;

    private Float f1,f2;

    private String z_number;

    private Integer z_status;
}
