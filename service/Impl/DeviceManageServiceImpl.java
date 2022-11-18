package com.sfp.online_confirm_tool_pro.service.Impl;

import com.sfp.online_confirm_tool_pro.entity.DcuTable;
import com.sfp.online_confirm_tool_pro.mapper.DeviceManageMapper;
import com.sfp.online_confirm_tool_pro.service.DeviceManageService;
import com.sfp.online_confirm_tool_pro.utils.Result;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/11/8 14:59
 **/

@Service
public class DeviceManageServiceImpl implements DeviceManageService {

    @Resource
    private DeviceManageMapper deviceManageMapper;


    @Override
    public int insertDevice(List<DcuTable> deviceMsgList) {
        return deviceManageMapper.insertDevice(deviceMsgList);
    }

    @Override
    public List<DcuTable> fileToDeviceList(Workbook rwb) throws Exception {
        List<DcuTable> deviceMsgList = new ArrayList<>();

        try {
            Sheet[] sheets = rwb.getSheets();
            // 读取4个sheet
            Sheet sheet1 = sheets[0];
            Sheet sheet2 = sheets[1];
            Sheet sheet3 = sheets[2];
            Sheet sheet4 = sheets[3];

            String dcu_no;
            String ip;
            String address;
            String okIp;
            String remark;

            // 处理SGS的信息
            for (int i = 1; i < sheet1.getRows(); i=i+3) {
                // 遍历sheet中的行，保存为数组
                // 第一行为表格标题栏，i从1开始
                Cell[] cellArray = sheet1.getRow(i);//单元格数组
                Cell[] cellArray1 = sheet1.getRow(i+1);//单元格数组
                Cell[] cellArray2 = sheet1.getRow(i+2);//单元格数组

                // 处理每个单元格的数据
                StringBuilder toIp = new StringBuilder();
                dcu_no = cellArray[0].getContents().trim();
                address = cellArray[3].getContents().trim();
                String ip1 = cellArray[2].getContents().trim();
                String ip2 = cellArray1[2].getContents().trim();
                String ip3 = cellArray2[2].getContents().trim();

                // 拼接ip
                toIp.append(ip1).append("&").append(ip2).append("&").append(ip3);
                okIp = toIp.toString();

                // 设置数据
                DcuTable deviceMsg = new DcuTable();
                deviceMsg.setDcu_no(dcu_no);
                deviceMsg.setAddress(address);
                deviceMsg.setIp(okIp);
                deviceMsg.setAuto_get_recorder(0);

                // 添加到列表
                deviceMsgList.add(deviceMsg);
            }



            // 处理BDCU的信息
            for (int i = 1; i < sheet2.getRows(); i++) {
                //遍历sheet中的行，保存为数组
                //前两行为表格标题栏，i从1开始
                Cell[] cellArray = sheet2.getRow(i);//单元格数组

                // 处理每个单元格的数据
                dcu_no = cellArray[0].getContents().trim();
                ip = cellArray[1].getContents().trim();
                address = cellArray[2].getContents().trim();

                // 设置数据
                DcuTable deviceMsg = new DcuTable();
                deviceMsg.setDcu_no(dcu_no);
                deviceMsg.setAddress(address);
                deviceMsg.setIp(ip);
                deviceMsg.setAuto_get_recorder(2);

                // 添加到列表
                deviceMsgList.add(deviceMsg);
            }

            // 处理RCMU的信息
            for (int i = 1; i < sheet3.getRows(); i++) {
                //遍历sheet中的行，保存为数组
                //前两行为表格标题栏，i从1开始
                Cell[] cellArray = sheet3.getRow(i);//单元格数组

                // 处理每个单元格的数据
                dcu_no = cellArray[0].getContents().trim();
                ip = cellArray[1].getContents().trim();
                address = cellArray[2].getContents().trim();
             // 设置数据
                DcuTable deviceMsg = new DcuTable();
                deviceMsg.setDcu_no(dcu_no);
                deviceMsg.setAddress(address);
                deviceMsg.setIp(ip);
                deviceMsg.setAuto_get_recorder(0);

                // 添加到列表
                deviceMsgList.add(deviceMsg);
            }

            // 处理FXU的信息
            for (int i = 1; i < sheet4.getRows(); i++) {
                //遍历sheet中的行，保存为数组
                //前两行为表格标题栏，i从1开始
                Cell[] cellArray = sheet4.getRow(i);//单元格数组

                // 处理每个单元格的数据
                dcu_no = cellArray[0].getContents().trim();
                ip = cellArray[1].getContents().trim();
                address = cellArray[2].getContents().trim();
                remark = cellArray[3].getContents().trim();

                DcuTable deviceMsg = new DcuTable();
                deviceMsg.setDcu_no(dcu_no);
                deviceMsg.setAddress(address);
                deviceMsg.setIp(ip);
                deviceMsg.setRemark(remark);
                deviceMsg.setAuto_get_recorder(8);

                // 添加到列表
                deviceMsgList.add(deviceMsg);

            }

        } catch (Exception e) {
            deviceMsgList.clear();
            throw new Exception("文件内容未知错误");
        }
        return deviceMsgList;
    }

    @Override
    public boolean isExistByDcuIp(String ip) {
        return deviceManageMapper.isExistByDcuIp(ip);
    }

    @Override
    public boolean isExistByDcuNo(String dcu_no) {
        return deviceManageMapper.isExistByDcuNo(dcu_no);
    }
}
