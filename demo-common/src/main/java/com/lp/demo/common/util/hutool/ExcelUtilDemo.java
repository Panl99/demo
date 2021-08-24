package com.lp.demo.common.util.hutool;

import cn.hutool.core.io.IoUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.lp.demo.common.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author lp
 * @date 2021/8/24 15:25
 **/
@RestController
@RequestMapping(value = "excel")
@Slf4j
public class ExcelUtilDemo {

    @GetMapping(value = "writeExcel")
    public void writeExcel(HttpServletResponse response) {
        List<UserDto> list = new LinkedList<>();
        list.add(UserDto.initUserDto());
        list.add(UserDto.initUserDto());
        list.add(UserDto.initUserDto());
        ExcelWriter writer = new ExcelWriter(true, "设备激活表");
        ServletOutputStream sos = null;
        try {
            List<Map<String, String>> rows = new LinkedList<>();
            for (int i = 0; i < list.size(); i++) {
                UserDto devicePuid = list.get(i);
                String mac = devicePuid.getName().toUpperCase();
                String secret = devicePuid.getAddress();
                Map<String, String> map = new LinkedHashMap<>();
                map.put("mac", mac);
                map.put("secret", secret);
                rows.add(map);
            }
            writer.write(rows);
            response.setHeader("Content-Disposition", "attachment;fileName=" + System.currentTimeMillis() + ".xlsx");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
            sos = response.getOutputStream();
            writer.flush(sos, true);
        } catch (Exception e) {
            log.error("get device active excel error, e = {}", e.getMessage());
        } finally {
            writer.close();
            IoUtil.close(sos);
        }
    }
}
