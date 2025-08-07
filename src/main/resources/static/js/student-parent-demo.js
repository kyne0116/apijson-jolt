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

// =================== 编辑功能（占位符） ===================

function editStudent(studentId) {
    alert(`编辑学生功能 (ID: ${studentId}) 待实现`);
}

function editParent(parentId) {
    alert(`编辑家长功能 (ID: ${parentId}) 待实现`);
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
 * 导出功能
 */
function exportData(type) {
    // 获取当前表格数据并导出
    alert(`导出${type}数据功能待实现`);
}