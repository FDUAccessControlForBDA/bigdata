package web.fdu_ac_service.controllers;

import org.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import service.fdu_ac_service.model.ACConstants;
import service.fdu_ac_service.model.Rule;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @RequestMapping("/")
    @ResponseBody
    public String returnTest() {
        System.out.println("success");
        return "success";
    }

    @RequestMapping("/getIds")
    @ResponseBody
    public Map<String, Object> getId(HttpServletRequest request) {
        Map<String, Object> rm = new HashMap<>();
        // response.addHeader("Access-Control-Allow-Origin", "*");
        String tableIds = request.getParameter("id");

        try {
            JSONArray jsonArray = new JSONArray();
            rm.put("result", "success");
            rm.put("message", "success");
            rm.put("code", "200");
            rm.put("id", tableIds);
        } catch (Exception e) {
            e.printStackTrace();
            rm.put("result", "error");
            rm.put("message", "error");
        }
        return rm;
    }

}
