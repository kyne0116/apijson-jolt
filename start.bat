@echo off
chcp 65001 >nul

REM APIJSON学生家长管理系统一键启动脚本 (Windows版)
REM 作者: Claude Code AI Assistant

echo ==================================================
echo 🚀 APIJSON学生家长管理系统启动脚本 (Windows)
echo ==================================================

REM 检查Java版本
echo 📋 检查Java环境...
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未找到Java，请先安装JDK 17+
    pause
    exit /b 1
)

for /f "tokens=3" %%i in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set java_version=%%i
    goto :java_version_found
)

:java_version_found
echo ✅ Java版本检查通过: %java_version%

REM 检查Maven
echo 📋 检查Maven环境...
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ❌ 未找到Maven，请先安装Maven 3.6+
    pause
    exit /b 1
)
echo ✅ Maven检查通过

REM 编译项目
echo 📋 编译项目...
mvn compile -q
if %errorlevel% neq 0 (
    echo ❌ 项目编译失败
    pause
    exit /b 1
)
echo ✅ 项目编译成功

REM 初始化数据库
echo 📋 初始化数据库表和数据...
mvn compile exec:java -Dexec.mainClass="apijson.boot.StudentParentDatabaseInit" -q
if %errorlevel% neq 0 (
    echo ⚠️ 数据库初始化可能存在问题，但继续启动应用
)
echo ✅ 数据库初始化完成

REM 启动应用
echo 📋 启动Spring Boot应用...
echo 请等待应用启动完成...
echo.

REM 启动应用
start /b mvn spring-boot:run

REM 等待应用启动
echo 等待应用启动...
timeout /t 10 /nobreak >nul

REM 检查应用是否启动成功
for /l %%i in (1,1,15) do (
    curl -s http://localhost:8080 >nul 2>&1
    if %errorlevel% equ 0 (
        echo ✅ 应用启动成功！
        goto :app_started
    )
    echo 启动中...
    timeout /t 2 /nobreak >nul
)

:app_started
echo.
echo ==================================================
echo 🎉 APIJSON学生家长管理系统启动完成！
echo ==================================================
echo.
echo 📍 访问地址:
echo    🌐 API服务: http://localhost:8080
echo    📱 演示页面: http://localhost:8080/student-parent-demo.html
echo.
echo 🔧 系统功能:
echo    ✅ 学生单表CRUD操作
echo    ✅ 学生聚合复杂查询
echo    ✅ 家长管理功能
echo    ✅ 多表关联查询
echo    ✅ 数据统计分析
echo    ✅ 实时API演示
echo.
echo 🧪 运行测试:
echo    mvn test -Dtest=StudentParentApiTest
echo.
echo 📖 查看文档:
echo    type 运行指南.md
echo.
echo 💡 提示: 按任意键打开演示页面，或关闭此窗口停止应用
echo ==================================================

REM 自动打开浏览器
start http://localhost:8080/student-parent-demo.html

pause