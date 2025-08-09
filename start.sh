#!/bin/bash

# APIJSON学生家长管理系统一键启动脚本
# 作者: Claude Code AI Assistant

echo "=================================================="
echo "🚀 APIJSON学生家长管理系统启动脚本"
echo "=================================================="

# 颜色定义
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 检查Java版本
echo -e "${BLUE}📋 检查Java环境...${NC}"
if command -v java &> /dev/null; then
    JAVA_VERSION=$(java -version 2>&1 | grep "version" | awk -F'"' '{print $2}' | awk -F'.' '{print $1}')
    if [ "$JAVA_VERSION" -ge 17 ]; then
        echo -e "${GREEN}✅ Java版本检查通过: $(java -version 2>&1 | head -n 1)${NC}"
    else
        echo -e "${RED}❌ Java版本过低，需要JDK 17+${NC}"
        exit 1
    fi
else
    echo -e "${RED}❌ 未找到Java，请先安装JDK 17+${NC}"
    exit 1
fi

# 检查Maven
echo -e "${BLUE}📋 检查Maven环境...${NC}"
if command -v mvn &> /dev/null; then
    echo -e "${GREEN}✅ Maven检查通过: $(mvn -version | head -n 1)${NC}"
else
    echo -e "${RED}❌ 未找到Maven，请先安装Maven 3.6+${NC}"
    exit 1
fi

# 检查MySQL连接
echo -e "${BLUE}📋 检查MySQL连接...${NC}"
if command -v mysql &> /dev/null; then
    # 尝试连接MySQL
    mysql -h localhost -P 30004 -u root -pBest@2008 -e "SELECT 1;" 2>/dev/null
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✅ MySQL连接成功${NC}"
    else
        echo -e "${YELLOW}⚠️ MySQL连接失败，请确保MySQL服务运行在localhost:30004${NC}"
        echo -e "${YELLOW}   用户名: root, 密码: Best@2008${NC}"
        read -p "是否继续启动应用？(y/n): " -r
        if [[ ! $REPLY =~ ^[Yy]$ ]]; then
            exit 1
        fi
    fi
else
    echo -e "${YELLOW}⚠️ 未找到mysql命令，跳过数据库连接检查${NC}"
fi

# 创建数据库（如果不存在）
echo -e "${BLUE}📋 准备数据库...${NC}"
mysql -h localhost -P 30004 -u root -pBest@2008 -e "CREATE DATABASE IF NOT EXISTS apijson CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据库准备完成${NC}"
fi

# 编译项目
echo -e "${BLUE}📋 编译项目...${NC}"
mvn compile -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 项目编译成功${NC}"
else
    echo -e "${RED}❌ 项目编译失败${NC}"
    exit 1
fi

# 初始化数据库
echo -e "${BLUE}📋 初始化数据库表和数据...${NC}"
mvn compile exec:java -Dexec.mainClass="apijson.boot.StudentParentDatabaseInit" -q
if [ $? -eq 0 ]; then
    echo -e "${GREEN}✅ 数据库初始化完成${NC}"
else
    echo -e "${YELLOW}⚠️ 数据库初始化可能存在问题，但继续启动应用${NC}"
fi

# 启动应用
echo -e "${BLUE}📋 启动Spring Boot应用...${NC}"
echo -e "${YELLOW}请等待应用启动完成...${NC}"
echo ""

# 在后台启动应用并保存PID
mvn spring-boot:run &
APP_PID=$!

# 等待应用启动
echo -e "${BLUE}等待应用启动...${NC}"
for i in {1..30}; do
    if curl -s http://localhost:8080 >/dev/null 2>&1; then
        echo -e "${GREEN}✅ 应用启动成功！${NC}"
        break
    fi
    echo -n "."
    sleep 2
done

echo ""
echo "=================================================="
echo -e "${GREEN}🎉 APIJSON学生家长管理系统启动完成！${NC}"
echo "=================================================="
echo ""
echo -e "${BLUE}📍 访问地址:${NC}"
echo -e "   🌐 API服务: ${GREEN}http://localhost:8080${NC}"
echo -e "   📱 演示页面: ${GREEN}http://localhost:8080/student-parent-demo.html${NC}"
echo ""
echo -e "${BLUE}🔧 系统功能:${NC}"
echo -e "   ✅ 学生单表CRUD操作"
echo -e "   ✅ 学生聚合复杂查询"
echo -e "   ✅ 家长管理功能"
echo -e "   ✅ 多表关联查询"
echo -e "   ✅ 数据统计分析"
echo -e "   ✅ 实时API演示"
echo ""
echo -e "${BLUE}🧪 运行测试:${NC}"
echo -e "   mvn test -Dtest=StudentParentApiTest"
echo ""
echo -e "${BLUE}📖 查看文档:${NC}"
echo -e "   cat 运行指南.md"
echo ""
echo -e "${YELLOW}💡 提示: 按 Ctrl+C 停止应用${NC}"
echo "=================================================="

# 等待用户中断
wait $APP_PID