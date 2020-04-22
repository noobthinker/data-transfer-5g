package com.xkorey.data.transfer.util;

import com.xkorey.data.transfer.dao.Tool;

public enum DaoHelper {

    DAO;

    public InheritableThreadLocal<Tool> tool = new InheritableThreadLocal<>();
}
