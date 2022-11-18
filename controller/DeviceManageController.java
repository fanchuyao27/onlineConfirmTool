package com.sfp.online_confirm_tool_pro.controller;

import com.sfp.online_confirm_tool_pro.entity.DcuTable;
import com.sfp.online_confirm_tool_pro.service.DeviceManageService;
import com.sfp.online_confirm_tool_pro.utils.Result;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fcy
 * @version 1.0.0
 * @description
 * @createTime 2022/11/8 14:33
 **/

@RestController
public class DeviceManageController {

    @Autowired
    private DeviceManageService deviceManageServiceImpl;

    private Result result;

    /**
     * 通过上传excel批量添加设备
     *
     * @param file
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addDevice")
    public String addDevice(MultipartFile file) {
        InputStream is;
        Workbook rwb;
        List<DcuTable> deviceMsgList;

        try {
            // 取得upload文件的流
            is = file.getInputStream();
            rwb = Workbook.getWorkbook(is);

            try {
                deviceMsgList = deviceManageServiceImpl.fileToDeviceList(rwb);
                if (deviceMsgList.isEmpty()) {
                    result = new Result(1, "文件内容为空");
                } else {

                    //插入数据库
                    try {
                        int saveResult = deviceManageServiceImpl.insertDevice(deviceMsgList);
                        if (saveResult > 0) {
                            result = new Result(0, "上传成功");
                        } else {
                            result = new Result(1, "上传失败");
                        }

                    } catch (Exception e) {
                        result = new Result(1, "数据库异常，上传失败");
                    }
                }
            } catch (Exception e) {
                result = new Result(1, e.getMessage());
            }
        } catch (BiffException e) {
            result = new Result(1, "文件异常，上传失败");
        } finally {
            return result.toString();
        }
    }


    /**
     * 新加设备
     */
    @RequestMapping(value = "/createDevice", method = RequestMethod.POST)
    public String createDevice(String dcu_no, String ip1, String ip2, String ip3, String address, String remark, Integer dcuType) {
        Result result = new Result();
        List<DcuTable> deviceMsgList = new ArrayList<>();
        DcuTable dcuMsg = new DcuTable();
        StringBuilder toIp = new StringBuilder();
        String ip = null;

        try {
            // 拼接ip
            if (dcuType == 0) {
                if(ipCheck(ip1) && ipCheck(ip2) && ipCheck(ip3)) {
                    toIp.append(ip1).append("&").append(ip2).append("&").append(ip3);
                    ip = toIp.toString();
                }else {
                    result = new Result(1,"ip地址错误！");
                }
            } else {
                if(ipCheck(ip1)) {
                    ip = ip1;
                }else {
                    result = new Result(1,"ip地址错误！");
                }
            }
            // 判断设备号，设备ip是否已存在
            boolean validation = true;
            if (dcu_no != null && !dcu_no.equals("") && deviceManageServiceImpl.isExistByDcuNo(dcu_no)) {
                result = new Result(1, "设备号已存在");
                validation = false;
            } else if (ip != null && !ip.equals("") && deviceManageServiceImpl.isExistByDcuIp(ip)) {
                result = new Result(1, "ip已存在");
                validation = false;
            }

            if (validation) {
                if (dcuType == 0 || dcuType == 1) {
                    dcuMsg.setPort(2404);
                    dcuMsg.setAuto_get_recorder(0);
                } else if (dcuType == 2 || dcuType == 3) {
                    dcuMsg.setPort(35678);
                }
                if (dcuType == 2) {
                    dcuMsg.setAuto_get_recorder(2);
                }
                if (dcuType == 3) {
                    dcuMsg.setAuto_get_recorder(8);
                }

                dcuMsg.setIp(ip);
                dcuMsg.setDcu_no(dcu_no);
                dcuMsg.setRemark(remark);
                dcuMsg.setAddress(address);
                deviceMsgList.add(dcuMsg);

                int insertResult = deviceManageServiceImpl.insertDevice(deviceMsgList);
                if (insertResult == 0) {
                    result = new Result(1, "添加成功");
                } else {
                    result = new Result(0, "添加失败");
                }
            }
        } catch (Exception e) {
            result = new Result(1, "数据异常，未能成功获取数据");
        } finally {
            return result.toString();
        }
    }



    /**
     * 判断是否是正规的ip地址
     */
    private boolean ipCheck(String text) {
        if (text != null && !text.isEmpty()) {
            //定义正则表达式。
            String regex = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])"
                    + "(\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)){3}$";
            // 判断IP地址是否与正则表达式匹配。
            if (text.matches(regex)) {
                return true;
            }
        }
        return false;
    }
}
