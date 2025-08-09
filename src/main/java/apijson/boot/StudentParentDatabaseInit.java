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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import apijson.Log;

/**
 * 学生家长管理系统数据库初始化工具
 * 创建Student和Parent表并插入测试数据
 * 
 * @author Claude Code
 */
public class StudentParentDatabaseInit {
    private static final String TAG = "StudentParentDatabaseInit";
    
    // 数据库连接配置
    private static final String DB_URL = "jdbc:mysql://localhost:30004/apijson?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Best@2008";
    
    public static void main(String[] args) {
        try {
            Log.d(TAG, "开始初始化学生家长管理系统数据库...");
            
            // 创建表结构
            initSchema();
            
            // 插入测试数据
            initData();
            
            Log.d(TAG, "✓ 数据库初始化完成！");
            Log.d(TAG, "✓ Student表和Parent表创建成功");
            Log.d(TAG, "✓ 测试数据插入完成");
            Log.d(TAG, "✓ 现在可以启动应用程序测试所有功能");
            
        } catch (Exception e) {
            Log.e(TAG, "数据库初始化失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * 创建表结构
     */
    private static void initSchema() throws Exception {
        executeSQL("student-parent-schema.sql", "创建表结构");
    }
    
    /**
     * 插入测试数据
     */
    private static void initData() throws Exception {
        executeSQL("student-parent-data.sql", "插入测试数据");
    }
    
    /**
     * 执行SQL文件
     */
    private static void executeSQL(String fileName, String description) throws Exception {
        Log.d(TAG, "执行 " + description + ": " + fileName);
        
        // 读取SQL文件
        String sql = readSQLFile(fileName);
        if (sql == null || sql.trim().isEmpty()) {
            throw new Exception("SQL文件为空或不存在: " + fileName);
        }
        
        // 建立数据库连接
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement stmt = conn.createStatement()) {
            
            // 分割SQL语句（以分号分隔）
            String[] sqlStatements = sql.split(";");
            
            int executedCount = 0;
            for (String sqlStatement : sqlStatements) {
                sqlStatement = sqlStatement.trim();
                if (!sqlStatement.isEmpty() && !sqlStatement.startsWith("--")) {
                    try {
                        stmt.execute(sqlStatement);
                        executedCount++;
                        Log.d(TAG, "  ✓ 执行SQL语句: " + sqlStatement.substring(0, Math.min(50, sqlStatement.length())) + "...");
                    } catch (Exception e) {
                        Log.w(TAG, "  ⚠ SQL语句执行警告: " + e.getMessage());
                        Log.w(TAG, "  SQL: " + sqlStatement.substring(0, Math.min(100, sqlStatement.length())));
                    }
                }
            }
            
            Log.d(TAG, "✓ " + description + " 完成，执行了 " + executedCount + " 条SQL语句");
        }
    }
    
    /**
     * 读取SQL文件内容
     */
    private static String readSQLFile(String fileName) {
        try {
            InputStream inputStream = StudentParentDatabaseInit.class.getClassLoader()
                    .getResourceAsStream(fileName);
            
            if (inputStream == null) {
                Log.e(TAG, "找不到SQL文件: " + fileName);
                return null;
            }
            
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    content.append(line).append("\n");
                }
            }
            
            return content.toString();
            
        } catch (Exception e) {
            Log.e(TAG, "读取SQL文件失败: " + fileName + ", 错误: " + e.getMessage());
            return null;
        }
    }
}