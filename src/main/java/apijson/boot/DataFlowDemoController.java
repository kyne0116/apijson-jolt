package apijson.boot;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bazaarvoice.jolt.Chainr;
import com.bazaarvoice.jolt.JsonUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 完整数据流演示控制器
 * 演示：数据库 → 扁平化APIJSON格式 → JOLT转换 → ECharts图表格式
 */
@RestController
@RequestMapping("/dataflow")
@CrossOrigin
public class DataFlowDemoController {

    private final JdbcTemplate jdbcTemplate;

    public DataFlowDemoController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 完整数据流演示 - 年级分布
     */
    @GetMapping("/grade-distribution")
    public JSONObject getGradeDistributionFlow() {
        JSONObject result = new JSONObject();
        
        try {
            // 步骤1: 从数据库直接获取数据
            String sql = "SELECT grade, COUNT(*) as count FROM Student WHERE status = 1 GROUP BY grade ORDER BY grade";
            List<Map<String, Object>> rawData = jdbcTemplate.queryForList(sql);
            result.put("step1_raw_data", rawData);
            
            // 步骤2: 转换为APIJSON扁平化格式
            JSONArray studentArray = new JSONArray();
            for (Map<String, Object> row : rawData) {
                JSONObject student = new JSONObject();
                student.put("grade", row.get("grade"));
                student.put("count", ((Number) row.get("count")).intValue());
                studentArray.add(student);
            }
            
            JSONObject apijsonFormat = new JSONObject();
            apijsonFormat.put("Student[]", studentArray);
            result.put("step2_apijson_format", apijsonFormat);
            
            // 步骤3: JOLT转换为ECharts格式
            List<Object> joltSpec = getGradeDistributionSpec();
            Chainr chainr = Chainr.fromSpec(joltSpec);
            Object echartsData = chainr.transform(apijsonFormat);
            result.put("step3_jolt_spec", joltSpec);
            result.put("step3_echarts_data", echartsData);
            
            // 步骤4: 生成最终ECharts配置
            JSONObject echartsConfig = generateBarChartConfig(echartsData, "学生年级分布");
            result.put("step4_echarts_config", echartsConfig);
            
            result.put("success", true);
            result.put("message", "完整数据流演示成功");
            result.put("dataflow", "数据库 → APIJSON格式 → JOLT转换 → ECharts配置");
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 完整数据流演示 - 性别分布
     */
    @GetMapping("/gender-distribution")
    public JSONObject getGenderDistributionFlow() {
        JSONObject result = new JSONObject();
        
        try {
            // 步骤1: 从数据库获取性别分布数据
            String sql = """
                SELECT 
                    CASE gender WHEN 0 THEN '男' WHEN 1 THEN '女' ELSE '未知' END as gender,
                    COUNT(*) as count 
                FROM Student 
                WHERE status = 1 
                GROUP BY gender 
                ORDER BY gender
                """;
            List<Map<String, Object>> rawData = jdbcTemplate.queryForList(sql);
            result.put("step1_raw_data", rawData);
            
            // 步骤2: 转换为APIJSON扁平化格式
            JSONArray studentArray = new JSONArray();
            for (Map<String, Object> row : rawData) {
                JSONObject student = new JSONObject();
                student.put("gender", row.get("gender"));
                student.put("count", ((Number) row.get("count")).intValue());
                studentArray.add(student);
            }
            
            JSONObject apijsonFormat = new JSONObject();
            apijsonFormat.put("Student[]", studentArray);
            result.put("step2_apijson_format", apijsonFormat);
            
            // 步骤3: JOLT转换为ECharts格式
            List<Object> joltSpec = getGenderDistributionSpec();
            Chainr chainr = Chainr.fromSpec(joltSpec);
            Object echartsData = chainr.transform(apijsonFormat);
            result.put("step3_jolt_spec", joltSpec);
            result.put("step3_echarts_data", echartsData);
            
            // 步骤4: 生成最终ECharts饼图配置
            JSONObject echartsConfig = generatePieChartConfig(echartsData, "学生性别分布");
            result.put("step4_echarts_config", echartsConfig);
            
            result.put("success", true);
            result.put("message", "性别分布数据流演示成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 完整数据流演示 - 年龄分布
     */
    @GetMapping("/age-distribution")
    public JSONObject getAgeDistributionFlow() {
        JSONObject result = new JSONObject();
        
        try {
            // 步骤1: 从数据库获取年龄分布数据
            String sql = "SELECT age, COUNT(*) as count FROM Student WHERE status = 1 GROUP BY age ORDER BY age";
            List<Map<String, Object>> rawData = jdbcTemplate.queryForList(sql);
            result.put("step1_raw_data", rawData);
            
            // 步骤2: 转换为APIJSON扁平化格式
            JSONArray studentArray = new JSONArray();
            for (Map<String, Object> row : rawData) {
                JSONObject student = new JSONObject();
                student.put("age", ((Number) row.get("age")).intValue());
                student.put("count", ((Number) row.get("count")).intValue());
                studentArray.add(student);
            }
            
            JSONObject apijsonFormat = new JSONObject();
            apijsonFormat.put("Student[]", studentArray);
            result.put("step2_apijson_format", apijsonFormat);
            
            // 步骤3: JOLT转换为ECharts格式
            List<Object> joltSpec = getAgeDistributionSpec();
            Chainr chainr = Chainr.fromSpec(joltSpec);
            Object echartsData = chainr.transform(apijsonFormat);
            result.put("step3_jolt_spec", joltSpec);
            result.put("step3_echarts_data", echartsData);
            
            // 步骤4: 生成最终ECharts折线图配置
            JSONObject echartsConfig = generateLineChartConfig(echartsData, "学生年龄分布");
            result.put("step4_echarts_config", echartsConfig);
            
            result.put("success", true);
            result.put("message", "年龄分布数据流演示成功");
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    /**
     * 测试APIJSON格式数据的JOLT转换
     */
    @PostMapping("/test-jolt-transform")
    public JSONObject testJoltTransform(@RequestBody JSONObject request) {
        JSONObject result = new JSONObject();
        
        try {
            Object inputData = request.get("data");
            String transformType = request.getString("type");
            
            List<Object> joltSpec;
            switch (transformType) {
                case "grade":
                    joltSpec = getGradeDistributionSpec();
                    break;
                case "gender":
                    joltSpec = getGenderDistributionSpec();
                    break;
                case "age":
                    joltSpec = getAgeDistributionSpec();
                    break;
                default:
                    throw new IllegalArgumentException("不支持的转换类型: " + transformType);
            }
            
            Chainr chainr = Chainr.fromSpec(joltSpec);
            Object output = chainr.transform(inputData);
            
            result.put("success", true);
            result.put("input", inputData);
            result.put("jolt_spec", joltSpec);
            result.put("output", output);
            result.put("transform_type", transformType);
            
        } catch (Exception e) {
            e.printStackTrace();
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    // JOLT转换规格定义
    private List<Object> getGradeDistributionSpec() {
        return JsonUtils.jsonToList(
            "[{" +
                "\"operation\": \"shift\"," +
                "\"spec\": {" +
                    "\"Student\\\\[\\\\]\": {" +
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
                    "\"Student\\\\[\\\\]\": {" +
                        "\"*\": {" +
                            "\"gender\": \"[&1].name\"," +
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
                    "\"Student\\\\[\\\\]\": {" +
                        "\"*\": {" +
                            "\"age\": \"categories[]\"," +
                            "\"count\": \"values[]\"" +
                        "}" +
                    "}" +
                "}" +
            "}]"
        );
    }

    // ECharts配置生成方法
    private JSONObject generateBarChartConfig(Object data, String title) {
        JSONObject config = JSON.parseObject("{}");
        config.put("title", JSON.parseObject("{\"text\":\"" + title + "\"}"));
        config.put("tooltip", JSON.parseObject("{}"));
        config.put("xAxis", JSON.parseObject("{\"type\":\"category\",\"data\":" + JSON.toJSONString(((JSONObject)data).get("categories")) + "}"));
        config.put("yAxis", JSON.parseObject("{\"type\":\"value\"}"));
        config.put("series", JSON.parseArray("[{\"type\":\"bar\",\"data\":" + JSON.toJSONString(((JSONObject)data).get("values")) + "}]"));
        return config;
    }

    private JSONObject generatePieChartConfig(Object data, String title) {
        JSONObject config = JSON.parseObject("{}");
        config.put("title", JSON.parseObject("{\"text\":\"" + title + "\",\"left\":\"center\"}"));
        config.put("tooltip", JSON.parseObject("{\"trigger\":\"item\"}"));
        config.put("series", JSON.parseArray("[{\"type\":\"pie\",\"radius\":\"50%\",\"data\":" + JSON.toJSONString(data) + "}]"));
        return config;
    }

    private JSONObject generateLineChartConfig(Object data, String title) {
        JSONObject config = JSON.parseObject("{}");
        config.put("title", JSON.parseObject("{\"text\":\"" + title + "\"}"));
        config.put("tooltip", JSON.parseObject("{\"trigger\":\"axis\"}"));
        config.put("xAxis", JSON.parseObject("{\"type\":\"category\",\"data\":" + JSON.toJSONString(((JSONObject)data).get("categories")) + "}"));
        config.put("yAxis", JSON.parseObject("{\"type\":\"value\"}"));
        config.put("series", JSON.parseArray("[{\"type\":\"line\",\"data\":" + JSON.toJSONString(((JSONObject)data).get("values")) + "}]"));
        return config;
    }

    /**
     * 获取系统状态信息
     */
    @GetMapping("/status")
    public JSONObject getSystemStatus() {
        JSONObject status = new JSONObject();
        
        try {
            // 检查数据库连接
            Integer studentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Student", Integer.class);
            Integer parentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM Parent", Integer.class);
            
            status.put("success", true);
            status.put("database_connected", true);
            status.put("student_count", studentCount);
            status.put("parent_count", parentCount);
            status.put("dataflow_available", true);
            status.put("jolt_transform_available", true);
            status.put("echarts_config_available", true);
            status.put("message", "数据流演示系统正常运行");
            status.put("available_endpoints", new String[]{
                "/dataflow/grade-distribution",
                "/dataflow/gender-distribution", 
                "/dataflow/age-distribution",
                "/dataflow/test-jolt-transform",
                "/dataflow/status"
            });
            
        } catch (Exception e) {
            e.printStackTrace();
            status.put("success", false);
            status.put("error", e.getMessage());
            status.put("database_connected", false);
        }
        
        return status;
    }
}