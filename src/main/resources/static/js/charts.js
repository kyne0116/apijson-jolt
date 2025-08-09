/**
 * APIJSON-JOLT ECharts 图表系统
 * 功能：数据可视化、JOLT转换、图表展示
 * 作者：Claude Code AI Assistant
 */

// 全局配置
const ChartsConfig = {
    baseUrl: 'http://localhost:8080',
    joltTransformUrl: '/jolt/transform',
    apiGetUrl: '/get',  // 回到原来的APIJSON标准端点
    colors: {
        primary: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de', '#3ba272', '#fc8452', '#9a60b4'],
        gradient: {
            blue: ['#667eea', '#764ba2'],
            green: ['#f093fb', '#f5576c'],
            orange: ['#4facfe', '#00f2fe']
        }
    }
};

// 图表实例
let gradeChart = null;
let genderChart = null;
let ageChart = null;

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    console.log('📊 ECharts图表系统初始化开始...');
    
    // 检查API连接状态
    checkApiConnection();
    
    // 初始化所有图表
    initializeCharts();
    
    // 加载数据并渲染图表
    loadAllChartsData();
    
    console.log('✅ ECharts图表系统初始化完成');
});

/**
 * 检查API连接状态
 */
async function checkApiConnection() {
    try {
        const response = await fetch(`${ChartsConfig.baseUrl}${ChartsConfig.apiGetUrl}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                "Student": {
                    "@count": 1
                }
            })
        });
        
        if (response.ok) {
            document.getElementById('connectionStatus').className = 'badge bg-success';
            document.getElementById('connectionStatus').textContent = '连接正常';
            document.getElementById('apiBaseUrl').textContent = ChartsConfig.baseUrl;
        } else {
            throw new Error('API连接失败');
        }
    } catch (error) {
        console.error('❌ API连接检查失败:', error);
        document.getElementById('connectionStatus').className = 'badge bg-danger';
        document.getElementById('connectionStatus').textContent = '连接失败';
    }
}

/**
 * 初始化所有图表
 */
function initializeCharts() {
    // 初始化年级分布柱状图
    gradeChart = echarts.init(document.getElementById('gradeChart'));
    
    // 初始化性别分布饼图
    genderChart = echarts.init(document.getElementById('genderChart'));
    
    // 初始化年龄分布折线图
    ageChart = echarts.init(document.getElementById('ageChart'));
    
    // 响应式调整
    window.addEventListener('resize', function() {
        gradeChart.resize();
        genderChart.resize();
        ageChart.resize();
    });
}

/**
 * 加载所有图表数据
 */
async function loadAllChartsData() {
    console.log('📈 开始加载所有图表数据...');
    
    // 并行加载所有数据
    await Promise.all([
        loadGradeDistribution(),
        loadGenderDistribution(),
        loadAgeDistribution(),
        loadStatsOverview()
    ]);
    
    console.log('✅ 所有图表数据加载完成');
}

/**
 * 加载年级分布数据并渲染柱状图
 */
async function loadGradeDistribution() {
    showLoading('gradeLoading');
    
    try {
        // APIJSON查询年级分布数据
        const apiData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "grade, count(*):count",
                    "@group": "grade",
                    "@order": "grade+"
                }
            }
        });
        
        console.log('📊 年级分布原始数据:', apiData);
        
        // JOLT转换为ECharts格式
        const chartData = await transformDataWithJolt(apiData, 'grade-distribution');
        
        console.log('🔄 年级分布转换后数据:', chartData);
        
        // 渲染柱状图
        renderGradeChart(chartData);
        
    } catch (error) {
        console.error('❌ 年级分布数据加载失败:', error);
        showErrorInChart(gradeChart, '年级分布数据加载失败');
    } finally {
        hideLoading('gradeLoading');
    }
}

/**
 * 加载性别分布数据并渲染饼图
 */
async function loadGenderDistribution() {
    showLoading('genderLoading');
    
    try {
        // APIJSON查询性别分布数据
        const apiData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "gender, count(*):count",
                    "@group": "gender",
                    "@order": "gender+"
                }
            }
        });
        
        console.log('📊 性别分布原始数据:', apiData);
        
        // JOLT转换为ECharts格式
        const chartData = await transformDataWithJolt(apiData, 'gender-distribution');
        
        console.log('🔄 性别分布转换后数据:', chartData);
        
        // 渲染饼图
        renderGenderChart(chartData);
        
    } catch (error) {
        console.error('❌ 性别分布数据加载失败:', error);
        showErrorInChart(genderChart, '性别分布数据加载失败');
    } finally {
        hideLoading('genderLoading');
    }
}

/**
 * 加载年龄分布数据并渲染折线图
 */
async function loadAgeDistribution() {
    showLoading('ageLoading');
    
    try {
        // APIJSON查询年龄分布数据
        const apiData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "age, count(*):count",
                    "@group": "age",
                    "@order": "age+"
                }
            }
        });
        
        console.log('📊 年龄分布原始数据:', apiData);
        
        // JOLT转换为ECharts格式
        const chartData = await transformDataWithJolt(apiData, 'age-distribution');
        
        console.log('🔄 年龄分布转换后数据:', chartData);
        
        // 渲染折线图
        renderAgeChart(chartData);
        
    } catch (error) {
        console.error('❌ 年龄分布数据加载失败:', error);
        showErrorInChart(ageChart, '年龄分布数据加载失败');
    } finally {
        hideLoading('ageLoading');
    }
}

/**
 * 加载统计概览数据
 */
async function loadStatsOverview() {
    try {
        // 获取学生总数
        const totalData = await fetchApiData({
            "Student": {
                "@count": 1
            }
        });
        
        // 获取年级数量
        const gradeData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "count(distinct grade):grade_count"
                }
            }
        });
        
        // 获取平均年龄
        const avgAgeData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "avg(age):avg_age"
                }
            }
        });
        
        // 获取男生比例
        const maleData = await fetchApiData({
            "Student[]": {
                "Student": {
                    "@column": "sum(case when gender=0 then 1 else 0 end)*100.0/count(*):male_ratio"
                }
            }
        });
        
        // 更新统计数据显示
        updateStatsDisplay({
            total: totalData.Student.count || 0,
            grades: gradeData['Student[]']?.[0]?.grade_count || 0,
            avgAge: Math.round(avgAgeData['Student[]']?.[0]?.avg_age || 0),
            maleRatio: Math.round(maleData['Student[]']?.[0]?.male_ratio || 0)
        });
        
    } catch (error) {
        console.error('❌ 统计概览数据加载失败:', error);
    }
}

/**
 * 从APIJSON获取数据
 */
async function fetchApiData(requestBody) {
    const response = await fetch(`${ChartsConfig.baseUrl}${ChartsConfig.apiGetUrl}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(requestBody)
    });
    
    if (!response.ok) {
        throw new Error(`API请求失败: ${response.status}`);
    }
    
    return await response.json();
}

/**
 * 使用JOLT转换数据
 */
async function transformDataWithJolt(apiData, transformType) {
    // 优先使用JOLT服务转换
    try {
        const response = await fetch(`${ChartsConfig.baseUrl}/jolt/${transformType}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(apiData)
        });
        
        if (response.ok) {
            const result = await response.json();
            if (result.success) {
                console.log('✅ JOLT转换成功:', result);
                return postProcessJoltData(result.data, transformType);
            } else {
                throw new Error(result.error || 'JOLT转换失败');
            }
        } else {
            throw new Error(`JOLT API请求失败: ${response.status}`);
        }
    } catch (error) {
        console.warn('⚠️ JOLT服务不可用，使用本地转换:', error);
        // 回退到本地转换逻辑
        return performLocalTransform(apiData, transformType);
    }
}

/**
 * 对JOLT转换后的数据进行后处理
 */
function postProcessJoltData(joltData, transformType) {
    switch (transformType) {
        case 'grade-distribution':
            // 年级分布数据已经是正确格式
            return joltData;
            
        case 'gender-distribution':
            // 处理性别数据，将gender码转换为名称
            if (Array.isArray(joltData)) {
                return joltData.map(item => ({
                    name: item.gender === 0 ? '男' : '女',
                    value: item.value
                }));
            }
            return joltData;
            
        case 'age-distribution':
            // 处理年龄数据，添加年龄标签
            if (joltData && joltData.categories && joltData.values) {
                return {
                    categories: joltData.categories.map(age => `${age}岁`),
                    values: joltData.values
                };
            }
            return joltData;
            
        default:
            return joltData;
    }
}

/**
 * 获取JOLT转换规格
 */
function getJoltSpec(transformType) {
    const specs = {
        'grade-distribution': [
            {
                "operation": "shift",
                "spec": {
                    "Student[]": {
                        "*": {
                            "grade": "categories[]",
                            "count": "values[]"
                        }
                    }
                }
            }
        ],
        'gender-distribution': [
            {
                "operation": "shift",
                "spec": {
                    "Student[]": {
                        "*": {
                            "gender": "temp_gender",
                            "count": "temp_count"
                        }
                    }
                }
            },
            {
                "operation": "modify-overwrite-beta",
                "spec": {
                    "temp_gender": ["=toInteger", null]
                }
            },
            {
                "operation": "modify-overwrite-beta",
                "spec": {
                    "name": "=if(@(1,temp_gender)==0,'男','女')",
                    "value": "@(1,temp_count)"
                }
            }
        ],
        'age-distribution': [
            {
                "operation": "shift",
                "spec": {
                    "Student[]": {
                        "*": {
                            "age": "categories[]",
                            "count": "values[]"
                        }
                    }
                }
            }
        ]
    };
    
    return specs[transformType] || [];
}

/**
 * 本地数据转换
 */
function performLocalTransform(apiData, transformType) {
    switch (transformType) {
        case 'grade-distribution':
            return transformGradeData(apiData);
        case 'gender-distribution':
            return transformGenderData(apiData);
        case 'age-distribution':
            return transformAgeData(apiData);
        default:
            return apiData;
    }
}

/**
 * 转换年级分布数据
 */
function transformGradeData(apiData) {
    const students = apiData['Student[]'] || [];
    const categories = students.map(item => item.grade);
    const values = students.map(item => item.count);
    
    return { categories, values };
}

/**
 * 转换性别分布数据
 */
function transformGenderData(apiData) {
    const students = apiData['Student[]'] || [];
    return students.map(item => ({
        name: item.gender === 0 ? '男' : '女',
        value: item.count
    }));
}

/**
 * 转换年龄分布数据
 */
function transformAgeData(apiData) {
    const students = apiData['Student[]'] || [];
    const categories = students.map(item => `${item.age}岁`);
    const values = students.map(item => item.count);
    
    return { categories, values };
}

/**
 * 渲染年级分布柱状图
 */
function renderGradeChart(data) {
    const option = {
        title: {
            text: '学生年级分布',
            subtext: '数据来源: APIJSON + JOLT转换',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            axisPointer: {
                type: 'shadow'
            },
            formatter: function(params) {
                return `${params[0].name}<br/>学生数量: ${params[0].value}人`;
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            data: data.categories || [],
            axisTick: {
                alignWithLabel: true
            }
        },
        yAxis: {
            type: 'value',
            name: '学生数量',
            nameLocation: 'middle',
            nameGap: 40
        },
        series: [{
            name: '学生数量',
            type: 'bar',
            data: data.values || [],
            itemStyle: {
                color: new echarts.graphic.LinearGradient(
                    0, 0, 0, 1,
                    [
                        {offset: 0, color: '#83bff6'},
                        {offset: 0.5, color: '#188df0'},
                        {offset: 1, color: '#188df0'}
                    ]
                )
            },
            emphasis: {
                itemStyle: {
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#2378f7'},
                            {offset: 0.7, color: '#2378f7'},
                            {offset: 1, color: '#83bff6'}
                        ]
                    )
                }
            }
        }]
    };
    
    gradeChart.setOption(option);
    
    // 添加点击事件
    gradeChart.on('click', function(params) {
        console.log('点击年级:', params.name, '数量:', params.value);
        showGradeDetail(params.name);
    });
}

/**
 * 渲染性别分布饼图
 */
function renderGenderChart(data) {
    const option = {
        title: {
            text: '学生性别分布',
            subtext: '数据来源: APIJSON + JOLT转换',
            left: 'center'
        },
        tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}人 ({d}%)'
        },
        legend: {
            bottom: '10%',
            left: 'center'
        },
        series: [{
            name: '性别分布',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
                borderRadius: 10,
                borderColor: '#fff',
                borderWidth: 2
            },
            label: {
                show: false,
                position: 'center'
            },
            emphasis: {
                label: {
                    show: true,
                    fontSize: '30',
                    fontWeight: 'bold'
                }
            },
            labelLine: {
                show: false
            },
            data: data || [],
            color: ['#5470c6', '#91cc75']
        }]
    };
    
    genderChart.setOption(option);
    
    // 添加点击事件
    genderChart.on('click', function(params) {
        console.log('点击性别:', params.name, '数量:', params.value);
        showGenderDetail(params.name);
    });
}

/**
 * 渲染年龄分布折线图
 */
function renderAgeChart(data) {
    const option = {
        title: {
            text: '学生年龄分布',
            subtext: '数据来源: APIJSON + JOLT转换',
            left: 'center'
        },
        tooltip: {
            trigger: 'axis',
            formatter: function(params) {
                return `${params[0].name}<br/>学生数量: ${params[0].value}人`;
            }
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: data.categories || []
        },
        yAxis: {
            type: 'value',
            name: '学生数量',
            nameLocation: 'middle',
            nameGap: 40
        },
        series: [{
            name: '学生数量',
            type: 'line',
            stack: 'Total',
            smooth: true,
            lineStyle: {
                width: 3
            },
            showSymbol: true,
            areaStyle: {
                opacity: 0.8,
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                    {
                        offset: 0,
                        color: 'rgba(255, 158, 68, 0.8)'
                    },
                    {
                        offset: 1,
                        color: 'rgba(255, 70, 131, 0.1)'
                    }
                ])
            },
            emphasis: {
                focus: 'series'
            },
            data: data.values || [],
            color: '#ff9e44'
        }]
    };
    
    ageChart.setOption(option);
    
    // 添加点击事件
    ageChart.on('click', function(params) {
        console.log('点击年龄:', params.name, '数量:', params.value);
        showAgeDetail(params.name);
    });
}

/**
 * 更新统计数据显示
 */
function updateStatsDisplay(stats) {
    document.getElementById('totalStudents').textContent = stats.total;
    document.getElementById('totalGrades').textContent = stats.grades;
    document.getElementById('avgAge').textContent = `${stats.avgAge}岁`;
    document.getElementById('maleRatio').textContent = `${stats.maleRatio}%`;
}

/**
 * 显示加载状态
 */
function showLoading(elementId) {
    const loadingElement = document.getElementById(elementId);
    if (loadingElement) {
        loadingElement.style.display = 'block';
    }
}

/**
 * 隐藏加载状态
 */
function hideLoading(elementId) {
    const loadingElement = document.getElementById(elementId);
    if (loadingElement) {
        loadingElement.style.display = 'none';
    }
}

/**
 * 在图表中显示错误信息
 */
function showErrorInChart(chart, message) {
    const option = {
        title: {
            text: '数据加载失败',
            subtext: message,
            left: 'center',
            top: 'center',
            textStyle: {
                color: '#999'
            }
        }
    };
    chart.setOption(option);
}

/**
 * 刷新所有图表
 */
async function refreshAllCharts() {
    console.log('🔄 刷新所有图表...');
    await loadAllChartsData();
    console.log('✅ 图表刷新完成');
}

/**
 * 导出图表数据
 */
function exportChartsData() {
    const exportData = {
        timestamp: new Date().toISOString(),
        charts: {
            grade: gradeChart.getOption(),
            gender: genderChart.getOption(),
            age: ageChart.getOption()
        }
    };
    
    const dataStr = JSON.stringify(exportData, null, 2);
    const dataBlob = new Blob([dataStr], {type: 'application/json'});
    
    const link = document.createElement('a');
    link.href = URL.createObjectURL(dataBlob);
    link.download = `charts-data-${new Date().getTime()}.json`;
    link.click();
    
    console.log('📁 图表数据已导出');
}

/**
 * 显示JOLT转换演示
 */
function showJoltTransformDemo() {
    // 显示模态框
    const modal = new bootstrap.Modal(document.getElementById('joltDemoModal'));
    modal.show();
    
    // 填充演示数据
    populateJoltDemo();
}

/**
 * 填充JOLT演示数据
 */
function populateJoltDemo() {
    // 原始APIJSON数据示例
    const originalData = {
        "Student[]": [
            {"grade": "七年级", "count": 3},
            {"grade": "八年级", "count": 2},
            {"grade": "九年级", "count": 4},
            {"grade": "十年级", "count": 1}
        ]
    };
    
    // JOLT转换规则
    const joltSpec = [
        {
            "operation": "shift",
            "spec": {
                "Student[]": {
                    "*": {
                        "grade": "categories[]",
                        "count": "values[]"
                    }
                }
            }
        }
    ];
    
    // 转换后的ECharts数据
    const transformedData = {
        "categories": ["七年级", "八年级", "九年级", "十年级"],
        "values": [3, 2, 4, 1]
    };
    
    // 显示数据
    document.getElementById('originalData').textContent = JSON.stringify(originalData, null, 2);
    document.getElementById('joltSpec').textContent = JSON.stringify(joltSpec, null, 2);
    document.getElementById('transformedData').textContent = JSON.stringify(transformedData, null, 2);
}

/**
 * 测试JOLT转换
 */
async function testJoltTransform() {
    console.log('🧪 测试JOLT转换...');
    
    try {
        const testData = {
            "Student[]": [
                {"grade": "测试年级", "count": 10}
            ]
        };
        
        const result = await transformDataWithJolt(testData, 'grade-distribution');
        console.log('✅ JOLT转换测试成功:', result);
        
        alert('JOLT转换测试成功！请查看控制台日志。');
    } catch (error) {
        console.error('❌ JOLT转换测试失败:', error);
        alert(`JOLT转换测试失败: ${error.message}`);
    }
}

/**
 * 显示年级详情
 */
function showGradeDetail(grade) {
    console.log(`📋 显示${grade}详情`);
    // 这里可以实现显示具体年级的学生列表等功能
    alert(`点击了${grade}，可以在这里显示该年级的详细信息`);
}

/**
 * 显示性别详情
 */
function showGenderDetail(gender) {
    console.log(`👥 显示${gender}详情`);
    // 这里可以实现显示具体性别的学生列表等功能
    alert(`点击了${gender}，可以在这里显示该性别的详细信息`);
}

/**
 * 显示年龄详情
 */
function showAgeDetail(age) {
    console.log(`🎂 显示${age}详情`);
    // 这里可以实现显示具体年龄的学生列表等功能
    alert(`点击了${age}，可以在这里显示该年龄的详细信息`);
}

// 导出到全局作用域，供HTML调用
window.ChartsConfig = ChartsConfig;
window.refreshAllCharts = refreshAllCharts;
window.exportChartsData = exportChartsData;
window.showJoltTransformDemo = showJoltTransformDemo;
window.testJoltTransform = testJoltTransform;