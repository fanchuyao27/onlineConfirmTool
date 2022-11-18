package com.sfp.online_confirm_tool_pro.service;

import com.sfp.online_confirm_tool_pro.entity.DcuTable;
import jxl.Workbook;

import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/11/8 14:59
 **/

public interface DeviceManageService {

    //批量插入数据库
    int insertDevice(List<DcuTable> deviceMsgList);

    // 将file文件转换成list
    List<DcuTable> fileToDeviceList(Workbook rwb) throws Exception;

    //判断设备号是否存在
    boolean isExistByDcuIp(String dcu_no);

    //判断设备ip是否存在
    boolean isExistByDcuNo(String ip);
}
