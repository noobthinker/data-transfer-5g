package com.xkorey.data.transfer.dao;

import com.xkorey.data.transfer.bean.OriginText;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OriginTextDao extends JpaRepository<OriginText, String> {

}
