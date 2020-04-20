package com.xkorey.data.transfer.dao;

import com.xkorey.data.transfer.bean.Origin4Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface Origin4TextDao extends JpaRepository<Origin4Text, String> {
}
