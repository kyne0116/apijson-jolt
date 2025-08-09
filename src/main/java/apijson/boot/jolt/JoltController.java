package apijson.boot.jolt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * JOLT JSON转换控制器 - 独立版本
 */
@RestController
@RequestMapping("/jolt")
@CrossOrigin(origins = "*")
public class JoltController {
    
    @PostMapping("/transform")
    public JSONObject transform(@RequestBody JSONObject request) {
        try {
            Object input = request.get("input");
            Object specObject = request.get("spec");
            
            if (input == null || specObject == null) {
                return createErrorResponse("缺少必要参数: input 或 spec");
            }
            
            List<Object> spec;
            if (specObject instanceof List) {
                spec = (List<Object>) specObject;
            } else {
                return createErrorResponse("spec必须是数组格式");
            }
            
            Chainr chainr = Chainr.fromSpec(spec);
            Object output = chainr.transform(input);
            
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("input", input);
            response.put("spec", spec);
            response.put("output", output);
            response.put("message", "转换成功");
            
            return response;
            
        } catch (Exception e) {
            e.printStackTrace();
            return createErrorResponse("转换失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/grade-distribution")
    public JSONObject transformGradeDistribution(@RequestBody JSONObject request) {
        try {
            List<Object> spec = getGradeDistributionSpec();
            Chainr chainr = Chainr.fromSpec(spec);
            Object output = chainr.transform(request);
            
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("type", "grade-distribution");
            response.put("chartType", "bar");
            response.put("data", output);
            
            return response;
            
        } catch (Exception e) {
            return createErrorResponse("年级分布数据转换失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/gender-distribution")
    public JSONObject transformGenderDistribution(@RequestBody JSONObject request) {
        try {
            List<Object> spec = getGenderDistributionSpec();
            Chainr chainr = Chainr.fromSpec(spec);
            Object output = chainr.transform(request);
            
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("type", "gender-distribution");
            response.put("chartType", "pie");
            response.put("data", output);
            
            return response;
            
        } catch (Exception e) {
            return createErrorResponse("性别分布数据转换失败: " + e.getMessage());
        }
    }
    
    @PostMapping("/age-distribution")
    public JSONObject transformAgeDistribution(@RequestBody JSONObject request) {
        try {
            List<Object> spec = getAgeDistributionSpec();
            Chainr chainr = Chainr.fromSpec(spec);
            Object output = chainr.transform(request);
            
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("type", "age-distribution");
            response.put("chartType", "line");
            response.put("data", output);
            
            return response;
            
        } catch (Exception e) {
            return createErrorResponse("年龄分布数据转换失败: " + e.getMessage());
        }
    }
    
    @GetMapping("/info")
    public JSONObject getInfo() {
        JSONObject response = new JSONObject();
        response.put("success", true);
        response.put("service", "JOLT JSON转换服务");
        response.put("version", "1.0.0");
        response.put("description", "基于Bazaarvoice JOLT的JSON转换服务，专用于APIJSON数据格式转换");
        response.put("supportedTransforms", new String[]{
            "grade-distribution", "gender-distribution", "age-distribution"
        });
        response.put("joltVersion", "0.1.7");
        
        return response;
    }
    
    @GetMapping("/test")
    public JSONObject test() {
        try {
            JSONObject testInput = new JSONObject();
            testInput.put("Student[]", JSON.parseArray(
                "[{\"grade\":\"七年级\",\"count\":3},{\"grade\":\"八年级\",\"count\":2}]"
            ));
            
            List<Object> spec = getGradeDistributionSpec();
            Chainr chainr = Chainr.fromSpec(spec);
            Object output = chainr.transform(testInput);
            
            JSONObject response = new JSONObject();
            response.put("success", true);
            response.put("message", "JOLT转换测试成功");
            response.put("testInput", testInput);
            response.put("testOutput", output);
            response.put("spec", spec);
            
            return response;
            
        } catch (Exception e) {
            return createErrorResponse("JOLT转换测试失败: " + e.getMessage());
        }
    }
    
    private JSONObject createErrorResponse(String message) {
        JSONObject response = new JSONObject();
        response.put("success", false);
        response.put("error", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
    
    private List<Object> getGradeDistributionSpec() {
        return JsonUtils.jsonToList(
            "[{" +
                "\"operation\": \"shift\"," +
                "\"spec\": {" +
                    "\"Student[]\": {" +
                        "\"*\": {" +
                            "\"grade\": \"categories[]\"," +
                            "\"count\": \"values[]\"" +
                        "}" +
                    "}" +
                "}" +
            "}]"
        );
    }
    
    private List<Object> getGenderDistributionSpec() {
        return JsonUtils.jsonToList(
            "[{" +
                "\"operation\": \"shift\"," +
                "\"spec\": {" +
                    "\"Student[]\": {" +
                        "\"*\": {" +
                            "\"gender\": \"[&1].gender\"," +
                            "\"count\": \"[&1].value\"" +
                        "}" +
                    "}" +
                "}" +
            "}]"
        );
    }
    
    private List<Object> getAgeDistributionSpec() {
        return JsonUtils.jsonToList(
            "[{" +
                "\"operation\": \"shift\"," +
                "\"spec\": {" +
                    "\"Student[]\": {" +
                        "\"*\": {" +
                            "\"age\": \"categories[]\"," +
                            "\"count\": \"values[]\"" +
                        "}" +
                    "}" +
                "}" +
            "}]"
        );
    }
}