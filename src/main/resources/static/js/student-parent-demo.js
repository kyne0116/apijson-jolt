/**
 * APIJSON学生家长管理系统演示页面脚本
 * 实现与APIJSON API的交互功能
 */

// 全局配置
const API_CONFIG = {
    baseUrl: 'http://localhost:8080',
    endpoints: {
        get: '/get',
        post: '/post', 
        put: '/put',
        delete: '/delete',
        head: '/head',
        gets: '/gets',
        heads: '/heads'
    }
};

// 页面初始化
document.addEventListener('DOMContentLoaded', function() {
    // 设置基础URL显示
    document.getElementById('baseUrl').textContent = API_CONFIG.baseUrl;
    
    // 检查API状态
    checkApiStatus();
    
    // 初始化加载数据
    queryAllStudents();
    queryAllParents();
});

/**
 * 检查API服务状态
 */
async function checkApiStatus() {
    const statusElement = document.getElementById('apiStatus');
    
    try {
        const response = await fetch(`${API_CONFIG.baseUrl}${API_CONFIG.endpoints.get}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({"Access": {}})
        });
        
        if (response.ok) {
            statusElement.className = 'badge bg-success';
            statusElement.textContent = '服务正常';
        } else {
            statusElement.className = 'badge bg-warning';
            statusElement.textContent = '服务异常';
        }
    } catch (error) {
        statusElement.className = 'badge bg-danger';
        statusElement.textContent = '连接失败';
        console.error('API Status Check Failed:', error);
    }
}

/**
 * 通用API请求函数
 */
async function makeApiRequest(endpoint, requestData, description = '') {
    showLoading(true);
    
    // 显示请求内容
    displayRequest(requestData, description);
    
    try {
        const response = await fetch(`${API_CONFIG.baseUrl}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(requestData)
        });
        
        const responseData = await response.json();
        
        // 显示响应内容
        displayResponse(responseData);
        
        showLoading(false);
        return responseData;
        
    } catch (error) {
        console.error('API Request Failed:', error);
        displayResponse({
            ok: false,
            code: 500,
            msg: '网络请求失败: ' + error.message,
            time: Date.now()
        });
        
        showLoading(false);
        return null;
    }
}

/**
 * 显示/隐藏加载指示器
 */
function showLoading(show) {
    const loader = document.getElementById('loadingIndicator');
    loader.style.display = show ? 'block' : 'none';
}

/**
 * 显示API请求内容
 */
function displayRequest(requestData, description = '') {
    const element = document.getElementById('requestDisplay');
    const formattedRequest = JSON.stringify(requestData, null, 2);
    
    element.innerHTML = `${description ? '<strong>' + description + '</strong><br>' : ''}${formattedRequest}`;
}

/**
 * 显示API响应内容
 */
function displayResponse(responseData) {
    const element = document.getElementById('responseDisplay');
    const formattedResponse = JSON.stringify(responseData, null, 2);
    
    element.innerHTML = formattedResponse;
}

// =================== 学生管理功能 ===================

/**
 * 查询所有学生
 */
async function queryAllStudents() {
    const requestData = {
        "Student": {}
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '查询所有学生');
    
    if (response && response.ok) {
        displayStudentsTable(response.Student || []);
    }
}

/**
 * 按ID查询学生
 */
async function queryStudentById() {
    const studentId = document.getElementById('studentIdInput').value;
    
    if (!studentId) {
        alert('请输入学生ID');
        return;
    }
    
    const requestData = {
        "Student": {
            "id": parseInt(studentId)
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询ID为${studentId}的学生`);
    
    if (response && response.ok) {
        displayStudentsTable(response.Student ? [response.Student] : []);
    }
}

/**
 * 按年级查询学生
 */
async function queryStudentsByGrade() {
    const grade = document.getElementById('gradeSelect').value;
    
    if (!grade) {
        alert('请选择年级');
        return;
    }
    
    const requestData = {
        "Student[]": {
            "Student": {
                "grade": grade
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询${grade}的学生`);
    
    if (response && response.ok) {
        displayStudentsTable(response['Student[]'] || []);
    }
}

/**
 * 统计学生数量
 */
async function countStudents() {
    const requestData = {
        "Student": {
            "@column": "count(*):total"
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '统计学生总数');
    
    if (response && response.ok && response.Student) {
        alert(`学生总数: ${response.Student.total} 人`);
    }
}

/**
 * 显示学生表格
 */
function displayStudentsTable(students) {
    const tbody = document.querySelector('#studentTable tbody');
    tbody.innerHTML = '';
    
    if (!Array.isArray(students)) {
        students = students ? [students] : [];
    }
    
    students.forEach(student => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${student.id || ''}</td>
            <td>${student.student_no || ''}</td>
            <td>${student.name || ''}</td>
            <td>${student.gender === 0 ? '男' : student.gender === 1 ? '女' : ''}</td>
            <td>${student.age || ''}</td>
            <td>${student.grade || ''}</td>
            <td>${student.class_name || ''}</td>
            <td>
                <button class="btn btn-sm btn-warning" onclick="editStudent(${student.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="confirmDeleteStudent(${student.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
    });
}

/**
 * 显示添加学生表单
 */
function showAddStudentForm() {
    const modal = new bootstrap.Modal(document.getElementById('addStudentModal'));
    // 重置表单
    document.getElementById('addStudentForm').reset();
    modal.show();
}

/**
 * 添加学生
 */
async function addStudent() {
    const form = document.getElementById('addStudentForm');
    const formData = new FormData(form);
    
    const studentData = {
        student_no: formData.get('student_no'),
        name: formData.get('name'),
        gender: parseInt(formData.get('gender')),
        age: parseInt(formData.get('age')),
        grade: formData.get('grade'),
        class_name: formData.get('class_name'),
        phone: formData.get('phone'),
        email: formData.get('email'),
        address: formData.get('address')
    };
    
    // 移除空值
    Object.keys(studentData).forEach(key => {
        if (studentData[key] === '' || studentData[key] === null) {
            delete studentData[key];
        }
    });
    
    const requestData = {
        "Student": studentData
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.post, requestData, '添加新学生');
    
    if (response && response.ok) {
        // 关闭模态框
        const modal = bootstrap.Modal.getInstance(document.getElementById('addStudentModal'));
        modal.hide();
        
        // 刷新学生列表
        queryAllStudents();
        
        alert('学生添加成功！');
    } else {
        alert('添加失败: ' + (response?.msg || '未知错误'));
    }
}

/**
 * 确认删除学生
 */
function confirmDeleteStudent(studentId) {
    if (confirm(`确定要删除ID为${studentId}的学生吗？`)) {
        deleteStudentById(studentId);
    }
}

/**
 * 删除学生
 */
async function deleteStudentById(studentId) {
    const requestData = {
        "Student": {
            "id": studentId
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.delete, requestData, `删除ID为${studentId}的学生`);
    
    if (response && response.ok) {
        queryAllStudents();
        alert('学生删除成功！');
    } else {
        alert('删除失败: ' + (response?.msg || '未知错误'));
    }
}

/**
 * 通用删除学生函数
 */
async function deleteStudent() {
    const studentId = prompt('请输入要删除的学生ID:');
    
    if (studentId && !isNaN(studentId)) {
        confirmDeleteStudent(parseInt(studentId));
    } else if (studentId !== null) {
        alert('请输入有效的学生ID');
    }
}

// =================== 家长管理功能 ===================

/**
 * 查询所有家长
 */
async function queryAllParents() {
    const requestData = {
        "Parent[]": {
            "Parent": {}
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '查询所有家长');
    
    if (response && response.ok) {
        displayParentsTable(response['Parent[]'] || []);
    }
}

/**
 * 按学生ID查询家长
 */
async function queryParentsByStudent() {
    const studentId = document.getElementById('parentStudentIdInput').value;
    
    if (!studentId) {
        alert('请输入学生ID');
        return;
    }
    
    const requestData = {
        "Parent[]": {
            "Parent": {
                "student_id": parseInt(studentId)
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询学生ID为${studentId}的家长`);
    
    if (response && response.ok) {
        displayParentsTable(response['Parent[]'] || []);
    }
}

/**
 * 查询紧急联系人
 */
async function queryEmergencyContacts() {
    const requestData = {
        "Parent[]": {
            "Parent": {
                "is_emergency_contact": 1
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '查询所有紧急联系人');
    
    if (response && response.ok) {
        displayParentsTable(response['Parent[]'] || []);
    }
}

/**
 * 显示家长表格
 */
function displayParentsTable(parents) {
    const tbody = document.querySelector('#parentTable tbody');
    tbody.innerHTML = '';
    
    if (!Array.isArray(parents)) {
        parents = parents ? [parents] : [];
    }
    
    parents.forEach(parent => {
        const row = tbody.insertRow();
        row.innerHTML = `
            <td>${parent.id || ''}</td>
            <td>${parent.student_id || ''}</td>
            <td>${parent.name || ''}</td>
            <td>${parent.relationship || ''}</td>
            <td>${parent.phone || ''}</td>
            <td>${parent.occupation || ''}</td>
            <td>${parent.is_emergency_contact ? '是' : '否'}</td>
            <td>
                <button class="btn btn-sm btn-warning" onclick="editParent(${parent.id})">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="btn btn-sm btn-danger" onclick="confirmDeleteParent(${parent.id})">
                    <i class="fas fa-trash"></i>
                </button>
            </td>
        `;
    });
}

/**
 * 显示添加家长表单
 */
function showAddParentForm() {
    const modal = new bootstrap.Modal(document.getElementById('addParentModal'));
    // 重置表单
    document.getElementById('addParentForm').reset();
    modal.show();
}

/**
 * 添加家长
 */
async function addParent() {
    const form = document.getElementById('addParentForm');
    const formData = new FormData(form);
    
    const parentData = {
        student_id: parseInt(formData.get('student_id')),
        name: formData.get('name'),
        relationship: formData.get('relationship'),
        gender: parseInt(formData.get('gender')),
        age: parseInt(formData.get('age')) || undefined,
        phone: formData.get('phone'),
        email: formData.get('email'),
        occupation: formData.get('occupation'),
        work_address: formData.get('work_address'),
        is_emergency_contact: formData.get('is_emergency_contact') ? 1 : 0
    };
    
    // 移除空值
    Object.keys(parentData).forEach(key => {
        if (parentData[key] === '' || parentData[key] === null || parentData[key] === undefined) {
            delete parentData[key];
        }
    });
    
    const requestData = {
        "Parent": parentData
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.post, requestData, '添加新家长');
    
    if (response && response.ok) {
        // 关闭模态框
        const modal = bootstrap.Modal.getInstance(document.getElementById('addParentModal'));
        modal.hide();
        
        // 刷新家长列表
        queryAllParents();
        
        alert('家长添加成功！');
    } else {
        alert('添加失败: ' + (response?.msg || '未知错误'));
    }
}

/**
 * 确认删除家长
 */
function confirmDeleteParent(parentId) {
    if (confirm(`确定要删除ID为${parentId}的家长吗？`)) {
        deleteParentById(parentId);
    }
}

/**
 * 删除家长
 */
async function deleteParentById(parentId) {
    const requestData = {
        "Parent": {
            "id": parentId
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.delete, requestData, `删除ID为${parentId}的家长`);
    
    if (response && response.ok) {
        queryAllParents();
        alert('家长删除成功！');
    } else {
        alert('删除失败: ' + (response?.msg || '未知错误'));
    }
}

// =================== 联表查询功能 ===================

/**
 * 查询学生及其家长信息
 */
async function queryStudentWithParents() {
    const studentId = document.getElementById('relationStudentId').value || 1;
    
    const requestData = {
        "Student": {
            "id": parseInt(studentId)
        },
        "Parent[]": {
            "Parent": {
                "student_id": parseInt(studentId)
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询学生(ID:${studentId})及其家长信息的联表查询`);
    
    if (response && response.ok) {
        // 显示学生信息
        if (response.Student) {
            displayStudentsTable([response.Student]);
        }
        
        // 显示家长信息
        if (response['Parent[]']) {
            displayParentsTable(response['Parent[]']);
        }
    }
}

/**
 * 复杂关联查询
 */
async function queryComplexRelation() {
    const requestData = {
        "Student[]": {
            "Student": {
                "grade": "九年级"
            }
        },
        "Parent[]": {
            "Parent": {
                "is_emergency_contact": 1,
                "@column": "id,name,phone,relationship,student_id"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '复杂关联查询：九年级学生 + 所有紧急联系人');
    
    if (response && response.ok) {
        if (response['Student[]']) {
            displayStudentsTable(response['Student[]']);
        }
        
        if (response['Parent[]']) {
            displayParentsTable(response['Parent[]']);
        }
    }
}

/**
 * 条件关联查询
 */
async function queryWithConditions() {
    const requestData = {
        "Student": {
            "grade": "九年级",
            "@column": "id,name,age,student_no",
            "@order": "age-"
        },
        "Parent[]": {
            "Parent": {
                "relationship": "父亲",
                "@column": "name,phone,occupation",
                "@order": "id+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '条件关联查询：九年级学生按年龄降序 + 父亲信息按ID升序');
    
    if (response && response.ok) {
        if (response.Student) {
            displayStudentsTable([response.Student]);
        }
        
        if (response['Parent[]']) {
            displayParentsTable(response['Parent[]']);
        }
    }
}

// =================== 学生聚合复杂查询功能 ===================

/**
 * 按年级分组统计学生数量
 */
async function groupByGrade() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "grade, count(*):count",
                "@group": "grade",
                "@order": "count-, grade+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '按年级分组统计学生数量');
    
    if (response && response.ok) {
        const statistics = response['Student[]'] || [];
        displayStatisticsTable(
            ['年级', '学生数量'], 
            statistics.map(item => [item.grade, item.count]),
            '年级分组统计'
        );
    }
}

/**
 * 查询各年级平均年龄
 */
async function averageAgeByGrade() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "grade, avg(age):average_age, count(*):student_count, min(age):min_age, max(age):max_age",
                "@group": "grade",
                "@order": "average_age-"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '查询各年级平均年龄');
    
    if (response && response.ok) {
        const statistics = response['Student[]'] || [];
        displayStatisticsTable(
            ['年级', '平均年龄', '学生数', '最小年龄', '最大年龄'], 
            statistics.map(item => [
                item.grade, 
                parseFloat(item.average_age).toFixed(1),
                item.student_count,
                item.min_age,
                item.max_age
            ]),
            '各年级年龄统计'
        );
    }
}

/**
 * 分页查询学生
 */
async function paginationQuery() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "id,student_no,name,age,grade,class_name",
                "@order": "age-, id+",
                "@count": 5,
                "@page": 0
            }
        },
        "total@": "/Student[]/total"
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '分页查询学生（每页5条，按年龄降序）');
    
    if (response && response.ok) {
        displayStudentsTable(response['Student[]'] || []);
        
        // 显示总数信息
        if (response['total@']) {
            alert(`分页查询完成，当前页5条记录，总共${response['total@']}条记录`);
        }
    }
}

/**
 * 年龄范围查询
 */
async function ageRangeQuery() {
    const minAge = document.getElementById('minAge').value || 14;
    const maxAge = document.getElementById('maxAge').value || 16;
    
    const requestData = {
        "Student[]": {
            "Student": {
                "age{}": `[${minAge},${maxAge}]`,
                "@column": "id,name,age,grade,student_no",
                "@order": "age+, name+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询年龄在${minAge}-${maxAge}岁之间的学生`);
    
    if (response && response.ok) {
        displayStudentsTable(response['Student[]'] || []);
        
        const count = response['Student[]'] ? response['Student[]'].length : 0;
        alert(`找到${count}个年龄在${minAge}-${maxAge}岁之间的学生`);
    }
}

// =================== 复杂关联查询功能 ===================

/**
 * 查询所有父亲及学生信息
 */
async function queryFathersWithStudents() {
    const requestData = {
        "Parent[]": {
            "Parent": {
                "relationship": "父亲",
                "@column": "id,name,phone,student_id,occupation,age"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '查询所有父亲信息');
    
    if (response && response.ok) {
        displayParentsTable(response['Parent[]'] || []);
        
        const fatherCount = response['Parent[]'] ? response['Parent[]'].length : 0;
        alert(`查询到${fatherCount}位父亲的信息`);
    }
}

/**
 * 查询九年级学生的紧急联系人
 */
async function queryGradeEmergencyContacts() {
    // 先查询九年级学生ID
    const studentRequest = {
        "Student[]": {
            "Student": {
                "grade": "九年级",
                "@column": "id,name"
            }
        }
    };
    
    const studentResponse = await makeApiRequest(API_CONFIG.endpoints.get, studentRequest, '查询九年级学生');
    
    if (studentResponse && studentResponse.ok) {
        const students = studentResponse['Student[]'] || [];
        const studentIds = students.map(s => s.id);
        
        if (studentIds.length > 0) {
            const parentRequest = {
                "Parent[]": {
                    "Parent": {
                        "student_id{}": studentIds,
                        "is_emergency_contact": 1,
                        "@column": "id,name,relationship,phone,student_id"
                    }
                }
            };
            
            const parentResponse = await makeApiRequest(API_CONFIG.endpoints.get, parentRequest, '查询九年级学生的紧急联系人');
            
            if (parentResponse && parentResponse.ok) {
                displayParentsTable(parentResponse['Parent[]'] || []);
                
                const contactCount = parentResponse['Parent[]'] ? parentResponse['Parent[]'].length : 0;
                alert(`九年级共${students.length}名学生，找到${contactCount}个紧急联系人`);
            }
        }
    }
}

/**
 * 家长职业分布统计
 */
async function queryOccupationStatistics() {
    const requestData = {
        "Parent[]": {
            "Parent": {
                "@column": "occupation, count(*):count, avg(age):avg_age",
                "@group": "occupation",
                "@having": "count(*) > 0",
                "@order": "count-, occupation+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '家长职业分布统计');
    
    if (response && response.ok) {
        const statistics = response['Parent[]'] || [];
        displayStatisticsTable(
            ['职业', '人数', '平均年龄'], 
            statistics.map(item => [
                item.occupation || '未填写', 
                item.count,
                item.avg_age ? parseFloat(item.avg_age).toFixed(1) : 'N/A'
            ]),
            '家长职业分布统计'
        );
    }
}

/**
 * 综合关联演示查询
 */
async function queryComplexJoinDemo() {
    const requestData = {
        "Student": {
            "grade": "九年级",
            "@column": "id,name,age,student_no",
            "@order": "age-"
        },
        "Parent[]": {
            "Parent": {
                "relationship": "父亲",
                "@column": "name,phone,occupation",
                "@order": "id+"
            }
        },
        "EmergencyContact[]": {
            "Parent": {
                "is_emergency_contact": 1,
                "@column": "name as contact_name,phone as contact_phone,relationship",
                "@order": "student_id+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '综合关联演示：九年级学生+所有父亲+所有紧急联系人');
    
    if (response && response.ok) {
        // 显示学生信息
        if (response.Student) {
            displayStudentsTable([response.Student]);
        }
        
        // 显示父亲信息
        if (response['Parent[]']) {
            displayParentsTable(response['Parent[]']);
        }
        
        // 显示紧急联系人统计
        if (response['EmergencyContact[]']) {
            const contacts = response['EmergencyContact[]'];
            displayStatisticsTable(
                ['联系人姓名', '电话', '关系'], 
                contacts.map(item => [item.contact_name, item.contact_phone, item.relationship]),
                '紧急联系人列表'
            );
        }
    }
}

// =================== 数据统计分析功能 ===================

/**
 * 显示性别分布
 */
async function showGenderDistribution() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "gender, count(*):count",
                "@group": "gender"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '学生性别分布统计');
    
    if (response && response.ok) {
        const statistics = response['Student[]'] || [];
        displayStatisticsTable(
            ['性别', '人数'], 
            statistics.map(item => [item.gender === 0 ? '男' : '女', item.count]),
            '学生性别分布'
        );
    }
}

/**
 * 显示年级统计
 */
async function showGradeStatistics() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "grade, count(*):student_count, avg(age):avg_age, count(case when gender=0 then 1 end):male_count, count(case when gender=1 then 1 end):female_count",
                "@group": "grade",
                "@order": "grade+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '年级详细统计');
    
    if (response && response.ok) {
        const statistics = response['Student[]'] || [];
        displayStatisticsTable(
            ['年级', '总人数', '平均年龄', '男生人数', '女生人数'], 
            statistics.map(item => [
                item.grade, 
                item.student_count,
                item.avg_age ? parseFloat(item.avg_age).toFixed(1) : 'N/A',
                item.male_count || 0,
                item.female_count || 0
            ]),
            '年级详细统计'
        );
    }
}

/**
 * 显示年龄分布
 */
async function showAgeDistribution() {
    const requestData = {
        "Student[]": {
            "Student": {
                "@column": "age, count(*):count",
                "@group": "age",
                "@order": "age+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '学生年龄分布统计');
    
    if (response && response.ok) {
        const statistics = response['Student[]'] || [];
        displayStatisticsTable(
            ['年龄', '人数'], 
            statistics.map(item => [item.age + '岁', item.count]),
            '年龄分布统计'
        );
    }
}

/**
 * 显示家长关系统计
 */
async function showParentRelationshipStats() {
    const requestData = {
        "Parent[]": {
            "Parent": {
                "@column": "relationship, count(*):count, count(case when is_emergency_contact=1 then 1 end):emergency_count",
                "@group": "relationship",
                "@order": "count-, relationship+"
            }
        }
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.get, requestData, '家长关系统计');
    
    if (response && response.ok) {
        const statistics = response['Parent[]'] || [];
        displayStatisticsTable(
            ['关系', '总人数', '紧急联系人数'], 
            statistics.map(item => [item.relationship, item.count, item.emergency_count || 0]),
            '家长关系统计'
        );
    }
}

/**
 * 显示统计表格
 */
function displayStatisticsTable(headers, data, title) {
    const statisticsDisplay = document.getElementById('statisticsDisplay');
    const tableHeader = document.getElementById('statisticsTableHeader');
    const tableBody = document.getElementById('statisticsTableBody');
    
    // 显示统计区域
    statisticsDisplay.style.display = 'block';
    
    // 设置表头
    tableHeader.innerHTML = headers.map(header => `<th>${header}</th>`).join('');
    
    // 设置表体
    tableBody.innerHTML = data.map(row => 
        `<tr>${row.map(cell => `<td>${cell}</td>`).join('')}</tr>`
    ).join('');
    
    // 滚动到统计区域
    statisticsDisplay.scrollIntoView({ behavior: 'smooth' });
    
    console.log(`${title}统计完成，共${data.length}条记录`);
}

// =================== 编辑功能 ===================

/**
 * 编辑学生信息
 */
function editStudent(studentId) {
    // 先查询学生信息
    const requestData = {
        "Student": {
            "id": studentId
        }
    };
    
    makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询学生ID=${studentId}的信息`)
        .then(response => {
            if (response && response.ok && response.Student) {
                const student = response.Student;
                
                // 填充编辑表单
                const form = document.getElementById('addStudentForm');
                form.querySelector('[name="student_no"]').value = student.student_no || '';
                form.querySelector('[name="name"]').value = student.name || '';
                form.querySelector('[name="gender"]').value = student.gender || 0;
                form.querySelector('[name="age"]').value = student.age || '';
                form.querySelector('[name="grade"]').value = student.grade || '';
                form.querySelector('[name="class_name"]').value = student.class_name || '';
                form.querySelector('[name="phone"]').value = student.phone || '';
                form.querySelector('[name="email"]').value = student.email || '';
                form.querySelector('[name="address"]').value = student.address || '';
                
                // 修改模态框标题和按钮
                document.querySelector('#addStudentModal .modal-title').textContent = '编辑学生信息';
                document.querySelector('#addStudentModal .btn-success').textContent = '更新';
                document.querySelector('#addStudentModal .btn-success').onclick = () => updateStudent(studentId);
                
                // 显示模态框
                const modal = new bootstrap.Modal(document.getElementById('addStudentModal'));
                modal.show();
            }
        });
}

/**
 * 更新学生信息
 */
async function updateStudent(studentId) {
    const form = document.getElementById('addStudentForm');
    const formData = new FormData(form);
    
    const studentData = {
        id: studentId,
        student_no: formData.get('student_no'),
        name: formData.get('name'),
        gender: parseInt(formData.get('gender')),
        age: parseInt(formData.get('age')),
        grade: formData.get('grade'),
        class_name: formData.get('class_name'),
        phone: formData.get('phone'),
        email: formData.get('email'),
        address: formData.get('address')
    };
    
    // 移除空值
    Object.keys(studentData).forEach(key => {
        if (studentData[key] === '' || studentData[key] === null) {
            delete studentData[key];
        }
    });
    
    const requestData = {
        "Student": studentData
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.put, requestData, `更新学生ID=${studentId}的信息`);
    
    if (response && response.ok) {
        // 关闭模态框
        const modal = bootstrap.Modal.getInstance(document.getElementById('addStudentModal'));
        modal.hide();
        
        // 重置表单和按钮
        resetStudentForm();
        
        // 刷新学生列表
        queryAllStudents();
        
        alert('学生信息更新成功！');
    } else {
        alert('更新失败: ' + (response?.msg || '未知错误'));
    }
}

/**
 * 编辑家长信息
 */
function editParent(parentId) {
    // 先查询家长信息
    const requestData = {
        "Parent": {
            "id": parentId
        }
    };
    
    makeApiRequest(API_CONFIG.endpoints.get, requestData, `查询家长ID=${parentId}的信息`)
        .then(response => {
            if (response && response.ok && response.Parent) {
                const parent = response.Parent;
                
                // 填充编辑表单
                const form = document.getElementById('addParentForm');
                form.querySelector('[name="student_id"]').value = parent.student_id || '';
                form.querySelector('[name="name"]').value = parent.name || '';
                form.querySelector('[name="relationship"]').value = parent.relationship || '';
                form.querySelector('[name="gender"]').value = parent.gender || 0;
                form.querySelector('[name="age"]').value = parent.age || '';
                form.querySelector('[name="phone"]').value = parent.phone || '';
                form.querySelector('[name="email"]').value = parent.email || '';
                form.querySelector('[name="occupation"]').value = parent.occupation || '';
                form.querySelector('[name="work_address"]').value = parent.work_address || '';
                form.querySelector('[name="is_emergency_contact"]').checked = parent.is_emergency_contact === 1;
                
                // 修改模态框标题和按钮
                document.querySelector('#addParentModal .modal-title').textContent = '编辑家长信息';
                document.querySelector('#addParentModal .btn-success').textContent = '更新';
                document.querySelector('#addParentModal .btn-success').onclick = () => updateParent(parentId);
                
                // 显示模态框
                const modal = new bootstrap.Modal(document.getElementById('addParentModal'));
                modal.show();
            }
        });
}

/**
 * 更新家长信息
 */
async function updateParent(parentId) {
    const form = document.getElementById('addParentForm');
    const formData = new FormData(form);
    
    const parentData = {
        id: parentId,
        student_id: parseInt(formData.get('student_id')),
        name: formData.get('name'),
        relationship: formData.get('relationship'),
        gender: parseInt(formData.get('gender')),
        age: parseInt(formData.get('age')) || undefined,
        phone: formData.get('phone'),
        email: formData.get('email'),
        occupation: formData.get('occupation'),
        work_address: formData.get('work_address'),
        is_emergency_contact: formData.get('is_emergency_contact') ? 1 : 0
    };
    
    // 移除空值
    Object.keys(parentData).forEach(key => {
        if (parentData[key] === '' || parentData[key] === null || parentData[key] === undefined) {
            delete parentData[key];
        }
    });
    
    const requestData = {
        "Parent": parentData
    };
    
    const response = await makeApiRequest(API_CONFIG.endpoints.put, requestData, `更新家长ID=${parentId}的信息`);
    
    if (response && response.ok) {
        // 关闭模态框
        const modal = bootstrap.Modal.getInstance(document.getElementById('addParentModal'));
        modal.hide();
        
        // 重置表单和按钮
        resetParentForm();
        
        // 刷新家长列表
        queryAllParents();
        
        alert('家长信息更新成功！');
    } else {
        alert('更新失败: ' + (response?.msg || '未知错误'));
    }
}

/**
 * 重置学生表单
 */
function resetStudentForm() {
    document.getElementById('addStudentForm').reset();
    document.querySelector('#addStudentModal .modal-title').textContent = '添加学生';
    document.querySelector('#addStudentModal .btn-success').textContent = '添加';
    document.querySelector('#addStudentModal .btn-success').onclick = addStudent;
}

/**
 * 重置家长表单
 */
function resetParentForm() {
    document.getElementById('addParentForm').reset();
    document.querySelector('#addParentModal .modal-title').textContent = '添加家长';
    document.querySelector('#addParentModal .btn-success').textContent = '添加';
    document.querySelector('#addParentModal .btn-success').onclick = addParent;
}

// =================== 高级功能 ===================

/**
 * 批量操作功能
 */
function bulkOperations() {
    const operations = [
        '1. 批量导入学生数据',
        '2. 批量导入家长数据',
        '3. 批量更新学生年级',
        '4. 批量设置紧急联系人',
        '5. 数据导出Excel',
        '6. 数据备份'
    ];
    
    alert('批量操作功能：\n' + operations.join('\n') + '\n\n注意：这些功能在实际项目中需要后端支持');
}

/**
 * 高级搜索功能
 */
function advancedSearch() {
    const searchCriteria = prompt(`请输入高级搜索条件（JSON格式），例如：
{
    "Student": {
        "age{}": "[14,16]",
        "grade": "九年级",
        "@order": "age+",
        "@count": 10
    }
}`);
    
    if (searchCriteria) {
        try {
            const requestData = JSON.parse(searchCriteria);
            makeApiRequest(API_CONFIG.endpoints.get, requestData, '高级自定义搜索')
                .then(response => {
                    if (response && response.ok) {
                        // 根据返回的数据类型显示结果
                        if (response.Student) {
                            displayStudentsTable([response.Student]);
                        } else if (response['Student[]']) {
                            displayStudentsTable(response['Student[]']);
                        } else if (response.Parent) {
                            displayParentsTable([response.Parent]);
                        } else if (response['Parent[]']) {
                            displayParentsTable(response['Parent[]']);
                        }
                        
                        alert('高级搜索完成！请查看结果表格。');
                    }
                });
        } catch (e) {
            alert('JSON格式错误：' + e.message);
        }
    }
}

// =================== 工具函数 ===================

/**
 * 格式化日期
 */
function formatDate(dateString) {
    if (!dateString) return '';
    
    const date = new Date(dateString);
    return date.toLocaleDateString('zh-CN') + ' ' + date.toLocaleTimeString('zh-CN');
}

/**
 * 导出数据功能
 */
function exportData(type) {
    // 模拟导出功能
    const timestamp = new Date().toISOString().slice(0, 19).replace(/[:-]/g, '');
    const filename = `${type}_data_${timestamp}.csv`;
    
    alert(`数据导出功能：\n` +
          `文件名: ${filename}\n` +
          `格式: CSV\n` +
          `注意: 实际项目中需要后端提供导出接口`);
}

/**
 * 清空所有表格
 */
function clearAllTables() {
    if (confirm('确定要清空所有显示的数据表格吗？（不会删除数据库中的数据）')) {
        // 清空学生表格
        document.querySelector('#studentTable tbody').innerHTML = '';
        
        // 清空家长表格
        document.querySelector('#parentTable tbody').innerHTML = '';
        
        // 隐藏统计表格
        document.getElementById('statisticsDisplay').style.display = 'none';
        
        // 清空API显示区域
        document.getElementById('requestDisplay').innerHTML = '点击上方任意操作按钮查看API请求';
        document.getElementById('responseDisplay').innerHTML = 'API响应将在这里显示';
        
        alert('所有表格已清空！');
    }
}

/**
 * 显示系统信息
 */
function showSystemInfo() {
    const info = `
APIJSON学生家长管理系统 v1.0

系统特性：
✅ 零代码CRUD操作
✅ 复杂聚合查询
✅ 多表关联查询  
✅ 实时权限控制
✅ 动态字段筛选
✅ 分页和排序
✅ 统计分析功能

技术栈：
• 后端：Spring Boot 3.2.5 + APIJSON 7.1.0
• 数据库：MySQL 8.4
• 前端：Bootstrap 5.1.3 + Vanilla JavaScript
• 构建工具：Maven 3.9+

API端点：
• GET /get - 查询数据
• POST /post - 创建数据  
• PUT /put - 更新数据
• DELETE /delete - 删除数据
• HEAD /head - 统计数量

开发者：Claude Code AI Assistant
许可协议：Apache License 2.0
    `;
    
    alert(info);
}

/**
 * 快速演示所有功能
 */
async function quickDemo() {
    if (!confirm('是否开始快速演示所有功能？这将依次执行多个查询操作。')) {
        return;
    }
    
    const demos = [
        { func: queryAllStudents, name: '查询所有学生' },
        { func: queryAllParents, name: '查询所有家长' },
        { func: groupByGrade, name: '按年级分组统计' },
        { func: showGenderDistribution, name: '性别分布统计' },
        { func: queryFathersWithStudents, name: '查询所有父亲' },
        { func: queryOccupationStatistics, name: '职业分布统计' }
    ];
    
    for (let i = 0; i < demos.length; i++) {
        const demo = demos[i];
        console.log(`执行演示 ${i + 1}/${demos.length}: ${demo.name}`);
        
        try {
            await demo.func();
            await new Promise(resolve => setTimeout(resolve, 2000)); // 等待2秒
        } catch (error) {
            console.error(`演示 ${demo.name} 失败:`, error);
        }
    }
    
    alert('快速演示完成！请查看各个数据表格和统计结果。');
}