/*Copyright ©2024 APIJSON Student-Parent Demo System

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.boot;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.time.Duration;

/**
 * 学生家长管理系统APIJSON API完整测试
 * 
 * 测试覆盖：
 * 1. 学生单表CRUD操作
 * 2. 学生单表聚合复杂查询（统计、分组、排序等）
 * 3. 与父亲表关联的简单查询
 * 4. 与父亲表关联的复杂查询
 * 
 * @author Claude Code
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
    "spring.datasource.username=root",
    "spring.datasource.password=Best@2008"
})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("学生家长管理系统API完整测试")
public class StudentParentApiTest {

    @LocalServerPort
    private int port;
    
    private String baseUrl;
    private HttpClient httpClient;
    
    // 测试用的新学生ID
    private Long newStudentId;
    private Long newParentId;
    
    @BeforeAll
    void setUp() {
        baseUrl = "http://localhost:" + port;
        httpClient = HttpClient.newBuilder()
                .build();
        
        System.out.println("=== 学生家长管理系统API测试开始 ===");
        System.out.println("测试服务地址: " + baseUrl);
        
        // 初始化数据库（如果需要）
        try {
            StudentParentDatabaseInit.main(new String[]{});
        } catch (Exception e) {
            System.err.println("数据库初始化失败，但继续测试: " + e.getMessage());
        }
    }
    
    // =================== 工具方法 ===================
    
    /**
     * 发送API请求
     */
    private JSONObject sendRequest(String endpoint, JSONObject requestData) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestData.toJSONString()))
                .build();
        
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("请求: " + endpoint);
        System.out.println("请求体: " + requestData.toJSONString());
        System.out.println("响应状态: " + response.statusCode());
        System.out.println("响应体: " + response.body());
        System.out.println("---");
        
        return JSON.parseObject(response.body());
    }
    
    /**
     * 验证响应成功
     */
    private void assertSuccess(JSONObject response) {
        assertNotNull(response, "响应不能为空");
        assertTrue(response.getBooleanValue("ok"), "响应应该成功: " + response.getString("msg"));
    }
    
    // =================== 学生单表CRUD测试 ===================
    
    @Test
    @Order(1)
    @DisplayName("1.1 查询所有学生")
    void testQueryAllStudents() throws Exception {
        JSONObject request = new JSONObject();
        request.put("Student[]", new JSONObject().fluentPut("Student", new JSONObject()));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "学生列表不能为空");
        assertTrue(students.size() > 0, "应该有学生数据");
        
        System.out.println("✓ 查询到 " + students.size() + " 个学生");
    }
    
    @Test
    @Order(2)
    @DisplayName("1.2 按ID查询学生")
    void testQueryStudentById() throws Exception {
        JSONObject request = new JSONObject();
        request.put("Student", new JSONObject().fluentPut("id", 1));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONObject student = response.getJSONObject("Student");
        assertNotNull(student, "学生信息不能为空");
        assertEquals(1, student.getIntValue("id"), "学生ID应该匹配");
        
        System.out.println("✓ 查询到学生: " + student.getString("name"));
    }
    
    @Test
    @Order(3)
    @DisplayName("1.3 按年级查询学生")
    void testQueryStudentsByGrade() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("grade", "九年级");
        studentQuery.put("@order", "age+"); // 按年龄升序
        request.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "学生列表不能为空");
        
        // 验证所有学生都是九年级
        for (int i = 0; i < students.size(); i++) {
            JSONObject student = students.getJSONObject(i);
            assertEquals("九年级", student.getString("grade"), "年级应该匹配");
        }
        
        System.out.println("✓ 查询到九年级学生 " + students.size() + " 人");
    }
    
    @Test
    @Order(4)
    @DisplayName("1.4 统计学生数量")
    void testCountStudents() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("@column", "count(*):total");
        request.put("Student", studentQuery);
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONObject student = response.getJSONObject("Student");
        assertNotNull(student, "统计结果不能为空");
        
        int total = student.getIntValue("total");
        assertTrue(total > 0, "学生总数应该大于0");
        
        System.out.println("✓ 学生总数: " + total);
    }
    
    @Test
    @Order(5)
    @DisplayName("1.5 新增学生")
    void testAddStudent() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentData = new JSONObject();
        studentData.put("student_no", "S2024TEST");
        studentData.put("name", "测试学生");
        studentData.put("gender", 0);
        studentData.put("age", 16);
        studentData.put("grade", "十年级");
        studentData.put("class_name", "十年级(测试)班");
        studentData.put("phone", "13900999999");
        studentData.put("email", "test@student.edu.cn");
        studentData.put("address", "测试地址");
        
        request.put("Student", studentData);
        
        JSONObject response = sendRequest("/post", request);
        assertSuccess(response);
        
        JSONObject newStudent = response.getJSONObject("Student");
        assertNotNull(newStudent, "新增学生信息不能为空");
        
        newStudentId = newStudent.getLongValue("id");
        assertTrue(newStudentId > 0, "新学生ID应该大于0");
        
        System.out.println("✓ 新增学生成功，ID: " + newStudentId);
    }
    
    @Test
    @Order(6)
    @DisplayName("1.6 修改学生信息")
    void testUpdateStudent() throws Exception {
        assertNotNull(newStudentId, "需要先创建学生");
        
        JSONObject request = new JSONObject();
        JSONObject studentData = new JSONObject();
        studentData.put("id", newStudentId);
        studentData.put("name", "测试学生(已修改)");
        studentData.put("age", 17);
        studentData.put("address", "修改后的地址");
        
        request.put("Student", studentData);
        
        JSONObject response = sendRequest("/put", request);
        assertSuccess(response);
        
        // 验证修改结果
        JSONObject verifyRequest = new JSONObject();
        verifyRequest.put("Student", new JSONObject().fluentPut("id", newStudentId));
        
        JSONObject verifyResponse = sendRequest("/get", verifyRequest);
        assertSuccess(verifyResponse);
        
        JSONObject updatedStudent = verifyResponse.getJSONObject("Student");
        assertEquals("测试学生(已修改)", updatedStudent.getString("name"), "姓名应该已修改");
        assertEquals(17, updatedStudent.getIntValue("age"), "年龄应该已修改");
        
        System.out.println("✓ 学生信息修改成功");
    }
    
    // =================== 学生单表聚合复杂查询测试 ===================
    
    @Test
    @Order(10)
    @DisplayName("2.1 按年级分组统计学生数量")
    void testCountStudentsByGrade() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("@column", "grade, count(*):count");
        studentQuery.put("@group", "grade");
        studentQuery.put("@order", "count-"); // 按数量降序
        
        request.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "统计结果不能为空");
        assertTrue(students.size() > 0, "应该有统计数据");
        
        // 验证每个分组都有grade和count字段
        for (int i = 0; i < students.size(); i++) {
            JSONObject group = students.getJSONObject(i);
            assertNotNull(group.getString("grade"), "年级不能为空");
            assertTrue(group.getIntValue("count") > 0, "数量应该大于0");
        }
        
        System.out.println("✓ 年级分组统计完成，共 " + students.size() + " 个年级");
    }
    
    @Test
    @Order(11)
    @DisplayName("2.2 查询各年级平均年龄")
    void testAverageAgeByGrade() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("@column", "grade, avg(age):average_age, count(*):student_count");
        studentQuery.put("@group", "grade");
        studentQuery.put("@order", "average_age-");
        
        request.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "统计结果不能为空");
        
        for (int i = 0; i < students.size(); i++) {
            JSONObject group = students.getJSONObject(i);
            String grade = group.getString("grade");
            double avgAge = group.getDoubleValue("average_age");
            int count = group.getIntValue("student_count");
            
            System.out.println("年级: " + grade + ", 平均年龄: " + avgAge + ", 学生数: " + count);
        }
        
        System.out.println("✓ 各年级平均年龄统计完成");
    }
    
    @Test
    @Order(12)
    @DisplayName("2.3 分页查询学生（按年龄排序）")
    void testStudentPagination() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("@column", "id,name,age,grade");
        studentQuery.put("@order", "age-, id+"); // 按年龄降序，ID升序
        studentQuery.put("@count", 5); // 每页5条
        studentQuery.put("@page", 0); // 第一页
        
        request.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "学生列表不能为空");
        assertTrue(students.size() <= 5, "分页大小应该不超过5");
        
        // 验证排序：年龄应该是降序
        for (int i = 1; i < students.size(); i++) {
            int prevAge = students.getJSONObject(i-1).getIntValue("age");
            int currAge = students.getJSONObject(i).getIntValue("age");
            assertTrue(prevAge >= currAge, "年龄应该按降序排列");
        }
        
        System.out.println("✓ 分页查询完成，第一页 " + students.size() + " 条记录");
    }
    
    @Test
    @Order(13)
    @DisplayName("2.4 条件筛选：查询年龄在指定范围内的学生")
    void testStudentAgeRange() throws Exception {
        JSONObject request = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("age{}", "[14,16]"); // 年龄在14-16之间
        studentQuery.put("@column", "id,name,age,grade");
        studentQuery.put("@order", "age+, name+");
        
        request.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray students = response.getJSONArray("Student[]");
        assertNotNull(students, "学生列表不能为空");
        
        // 验证所有学生年龄都在14-16之间
        for (int i = 0; i < students.size(); i++) {
            JSONObject student = students.getJSONObject(i);
            int age = student.getIntValue("age");
            assertTrue(age >= 14 && age <= 16, "年龄应该在14-16之间，实际: " + age);
        }
        
        System.out.println("✓ 年龄范围筛选完成，找到 " + students.size() + " 个符合条件的学生");
    }
    
    // =================== 家长表CRUD测试 ===================
    
    @Test
    @Order(20)
    @DisplayName("3.1 查询所有家长")
    void testQueryAllParents() throws Exception {
        JSONObject request = new JSONObject();
        request.put("Parent[]", new JSONObject().fluentPut("Parent", new JSONObject()));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray parents = response.getJSONArray("Parent[]");
        assertNotNull(parents, "家长列表不能为空");
        assertTrue(parents.size() > 0, "应该有家长数据");
        
        System.out.println("✓ 查询到 " + parents.size() + " 个家长");
    }
    
    @Test
    @Order(21)
    @DisplayName("3.2 为新学生添加家长")
    void testAddParent() throws Exception {
        assertNotNull(newStudentId, "需要先创建学生");
        
        JSONObject request = new JSONObject();
        JSONObject parentData = new JSONObject();
        parentData.put("student_id", newStudentId);
        parentData.put("name", "测试家长");
        parentData.put("relationship", "父亲");
        parentData.put("gender", 0);
        parentData.put("age", 45);
        parentData.put("phone", "13800888888");
        parentData.put("email", "testparent@parent.com");
        parentData.put("occupation", "测试工程师");
        parentData.put("work_address", "测试公司");
        parentData.put("is_emergency_contact", 1);
        
        request.put("Parent", parentData);
        
        JSONObject response = sendRequest("/post", request);
        assertSuccess(response);
        
        JSONObject newParent = response.getJSONObject("Parent");
        assertNotNull(newParent, "新增家长信息不能为空");
        
        newParentId = newParent.getLongValue("id");
        assertTrue(newParentId > 0, "新家长ID应该大于0");
        
        System.out.println("✓ 新增家长成功，ID: " + newParentId);
    }
    
    // =================== 关联查询测试 ===================
    
    @Test
    @Order(30)
    @DisplayName("4.1 简单关联查询：查询学生及其所有家长")
    void testSimpleJoinQuery() throws Exception {
        JSONObject request = new JSONObject();
        request.put("Student", new JSONObject().fluentPut("id", 1));
        
        JSONObject parentQuery = new JSONObject();
        parentQuery.put("student_id", 1);
        parentQuery.put("@order", "relationship+");
        request.put("Parent[]", new JSONObject().fluentPut("Parent", parentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONObject student = response.getJSONObject("Student");
        JSONArray parents = response.getJSONArray("Parent[]");
        
        assertNotNull(student, "学生信息不能为空");
        assertNotNull(parents, "家长列表不能为空");
        
        // 验证家长都属于该学生
        for (int i = 0; i < parents.size(); i++) {
            JSONObject parent = parents.getJSONObject(i);
            assertEquals(1, parent.getIntValue("student_id"), "家长应该属于学生ID=1");
        }
        
        System.out.println("✓ 学生: " + student.getString("name") + ", 家长数量: " + parents.size());
    }
    
    @Test
    @Order(31)
    @DisplayName("4.2 复杂关联查询：查询所有父亲及其对应的学生信息")
    void testComplexJoinQuery() throws Exception {
        JSONObject request = new JSONObject();
        
        // 查询所有父亲
        JSONObject parentQuery = new JSONObject();
        parentQuery.put("relationship", "父亲");
        parentQuery.put("@column", "id,name,phone,student_id,occupation");
        parentQuery.put("@order", "name+");
        request.put("Parent[]", new JSONObject().fluentPut("Parent", parentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray parents = response.getJSONArray("Parent[]");
        assertNotNull(parents, "父亲列表不能为空");
        
        // 验证所有都是父亲
        for (int i = 0; i < parents.size(); i++) {
            JSONObject parent = parents.getJSONObject(i);
            assertEquals("父亲", parent.getString("relationship"), "应该都是父亲关系");
            assertTrue(parent.getLongValue("student_id") > 0, "应该有关联的学生ID");
        }
        
        System.out.println("✓ 查询到 " + parents.size() + " 位父亲");
    }
    
    @Test
    @Order(32)
    @DisplayName("4.3 多条件关联查询：查询九年级学生的紧急联系人")
    void testMultiConditionJoinQuery() throws Exception {
        // 先查询九年级学生的ID列表
        JSONObject studentRequest = new JSONObject();
        JSONObject studentQuery = new JSONObject();
        studentQuery.put("grade", "九年级");
        studentQuery.put("@column", "id,name");
        studentRequest.put("Student[]", new JSONObject().fluentPut("Student", studentQuery));
        
        JSONObject studentResponse = sendRequest("/get", studentRequest);
        assertSuccess(studentResponse);
        
        JSONArray students = studentResponse.getJSONArray("Student[]");
        assertNotNull(students, "九年级学生不能为空");
        
        // 提取学生ID列表
        JSONArray studentIds = new JSONArray();
        for (int i = 0; i < students.size(); i++) {
            studentIds.add(students.getJSONObject(i).getLongValue("id"));
        }
        
        // 查询这些学生的紧急联系人
        JSONObject parentRequest = new JSONObject();
        JSONObject parentQuery = new JSONObject();
        parentQuery.put("student_id{}", studentIds); // 学生ID在指定列表中
        parentQuery.put("is_emergency_contact", 1); // 是紧急联系人
        parentQuery.put("@column", "id,name,relationship,phone,student_id");
        parentQuery.put("@order", "student_id+, relationship+");
        
        parentRequest.put("Parent[]", new JSONObject().fluentPut("Parent", parentQuery));
        
        JSONObject parentResponse = sendRequest("/get", parentRequest);
        assertSuccess(parentResponse);
        
        JSONArray emergencyContacts = parentResponse.getJSONArray("Parent[]");
        assertNotNull(emergencyContacts, "紧急联系人列表不能为空");
        
        // 验证查询结果
        for (int i = 0; i < emergencyContacts.size(); i++) {
            JSONObject contact = emergencyContacts.getJSONObject(i);
            assertEquals(1, contact.getIntValue("is_emergency_contact"), "应该是紧急联系人");
            assertTrue(studentIds.contains(contact.getLongValue("student_id")), "应该属于九年级学生");
        }
        
        System.out.println("✓ 九年级学生数: " + students.size() + ", 紧急联系人数: " + emergencyContacts.size());
    }
    
    @Test
    @Order(33)
    @DisplayName("4.4 统计关联查询：按年级统计家长职业分布")
    void testStatisticsJoinQuery() throws Exception {
        JSONObject request = new JSONObject();
        
        // 这是一个复杂的统计查询，需要关联学生和家长表
        JSONObject parentQuery = new JSONObject();
        parentQuery.put("@column", "occupation, count(*):count");
        parentQuery.put("@group", "occupation");
        parentQuery.put("@having", "count(*) > 0");
        parentQuery.put("@order", "count-");
        
        request.put("Parent[]", new JSONObject().fluentPut("Parent", parentQuery));
        
        JSONObject response = sendRequest("/get", request);
        assertSuccess(response);
        
        JSONArray statistics = response.getJSONArray("Parent[]");
        assertNotNull(statistics, "统计结果不能为空");
        
        System.out.println("家长职业分布统计：");
        for (int i = 0; i < statistics.size(); i++) {
            JSONObject stat = statistics.getJSONObject(i);
            String occupation = stat.getString("occupation");
            int count = stat.getIntValue("count");
            System.out.println("  " + occupation + ": " + count + " 人");
        }
        
        System.out.println("✓ 职业分布统计完成，共 " + statistics.size() + " 种职业");
    }
    
    // =================== 删除测试（清理数据） ===================
    
    @Test
    @Order(90)
    @DisplayName("9.1 删除测试家长")
    void testDeleteParent() throws Exception {
        if (newParentId != null) {
            JSONObject request = new JSONObject();
            request.put("Parent", new JSONObject().fluentPut("id", newParentId));
            
            JSONObject response = sendRequest("/delete", request);
            assertSuccess(response);
            
            System.out.println("✓ 测试家长删除成功");
        }
    }
    
    @Test
    @Order(91)
    @DisplayName("9.2 删除测试学生")
    void testDeleteStudent() throws Exception {
        if (newStudentId != null) {
            JSONObject request = new JSONObject();
            request.put("Student", new JSONObject().fluentPut("id", newStudentId));
            
            JSONObject response = sendRequest("/delete", request);
            assertSuccess(response);
            
            System.out.println("✓ 测试学生删除成功");
        }
    }
}