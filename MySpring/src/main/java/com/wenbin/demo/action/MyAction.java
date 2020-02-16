package com.wenbin.demo.action;

import com.wenbin.demo.service.IModifyService;
import com.wenbin.demo.service.IQueryService;
import com.wenbin.formework.annotation.MyAutowired;
import com.wenbin.formework.annotation.MyController;
import com.wenbin.formework.annotation.MyRequestMapping;
import com.wenbin.formework.annotation.MyRequestParam;
import com.wenbin.formework.webmvc.servlet.MyModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@MyController
@MyRequestMapping("/web")
public class MyAction {

    @MyAutowired
	IQueryService queryService;
    @MyAutowired
	IModifyService modifyService;

    @MyRequestMapping("/query.json")
    public MyModelAndView query(HttpServletRequest request, HttpServletResponse response,
								@MyRequestParam("name") String name) {
        String result = queryService.query(name);
        return out(response, result);
    }

    @MyRequestMapping("/add*.json")
    public MyModelAndView add(HttpServletRequest request, HttpServletResponse response,
                              @MyRequestParam("name") String name, @MyRequestParam("addr") String addr) {
        String result = null;
        try {
            result = modifyService.add(name, addr);
            return out(response, result);
        } catch (Exception e) {
//			e.printStackTrace();
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("detail", e.getMessage());
//			System.out.println(Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]",""));
            model.put("stackTrace", Arrays.toString(e.getStackTrace()).replaceAll("\\[|\\]", ""));
            return new MyModelAndView("500", model);
        }

    }

    @MyRequestMapping("/remove.json")
    public MyModelAndView remove(HttpServletRequest request, HttpServletResponse response,
                                 @MyRequestParam("id") Integer id) {
        String result = modifyService.remove(id);
        return out(response, result);
    }

    @MyRequestMapping("/edit.json")
    public MyModelAndView edit(HttpServletRequest request, HttpServletResponse response,
                               @MyRequestParam("id") Integer id,
                               @MyRequestParam("name") String name) {
        String result = modifyService.edit(id, name);
        return out(response, result);
    }


    private MyModelAndView out(HttpServletResponse resp, String str) {
        try {
            resp.getWriter().write(str);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
