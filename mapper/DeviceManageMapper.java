package com.sfp.online_confirm_tool_pro.mapper;

import com.sfp.online_confirm_tool_pro.entity.DcuTable;
import jxl.Workbook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.ArrayList;
import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/11/8 15:05
 **/

@Mapper
public interface DeviceManageMapper {

    //设备信息批量插入数据库
    int insertDevice(List<DcuTable> deviceMsgList);

    //判断设备号是否存在
    boolean isExistByDcuNo(@Param("dcu_no")String dcu_no);

    //判断设备ip是否存在
    boolean isExistByDcuIp(@Param("ip")String ip);
}
