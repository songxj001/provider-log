package com.jk.controller;

import com.alibaba.fastjson.JSON;
import com.jk.common.ConstantConf;
import com.jk.common.ResultBean;
import com.jk.model.LogBean;
import com.jk.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("log")
@RefreshScope
public class LogController {

    @Autowired
    private LogService logService;

    @Value("${content}")
    private String content;

    @RequestMapping(value = "test")
    public String test(){
        System.out.println(content);
        return content;
    }

    /**
     * 保存日志
     * @param log 示例参数 {"log":"测试","logTime":"2017-01-01 12:12:12","ip":"127.0.0.1","requestMsg":"?id=123&name=666","responseMsg":"成功","ipAddress":"局域网","name":"saveLog"}
     * @return
     */
    @RequestMapping(value = "saveLog")
    public Boolean saveLog(@RequestParam String log){
        try{
            LogBean logBean = JSON.parseObject(log, LogBean.class);
            logService.saveLog(logBean);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 查询日志列表
     * @param page 第几页 （必传）
     * @param rows 每页多少条（必传）
     * @return
     */
    @RequestMapping(value = "getLogList")
    public ResultBean getLogList(@RequestParam(value = "page") Integer page, @RequestParam(value = "rows") Integer rows){
        ResultBean resultBean = new ResultBean();
        try {
            HashMap<String, Object> data = new HashMap<>();
            List<LogBean> logList = logService.getLogList(page, rows);
            int count = logService.getLogCount();
            data.put("total",count);
            data.put("rows",logList);
            resultBean.setCode(ConstantConf.SUCCESS);
            resultBean.setData(data);
            resultBean.setMsg("成功");
        }catch (Exception e){
            resultBean.setCode(ConstantConf.ERROR);
            resultBean.setMsg(e.getMessage());
        }
        return resultBean;
    }
}
