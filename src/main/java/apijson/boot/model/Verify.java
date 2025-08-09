/*Copyright ©2016 TommyLemon(https://github.com/TommyLemon/APIJSON)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/

package apijson.boot.model;

import apijson.MethodAccess;

/**
 * 验证码模型类
 */
@MethodAccess
public class Verify {
    public static final String TAG = "Verify";
    public static final String TABLE_NAME = "Verify";
    public static final int TYPE_LOGIN = 0;
    public static final int TYPE_REGISTER = 1;
    public static final int TYPE_PAY = 2;
    public static final int TYPE_RELOAD = 3;
    public static final int TYPE_PASSWORD = 4;
    public static final int TYPE_PAY_PASSWORD = 5;
    
    private Long id;
    private Integer type;
    private String phone;
    private String verify;
    private String date;
    
    public Verify() {
        super();
    }
    
    public Verify(long id) {
        this();
        setId(id);
    }
    
    public Verify(int type, String phone) {
        this();
        setType(type);
        setPhone(phone);
    }
    
    public Long getId() {
        return id;
    }
    
    public Verify setId(Long id) {
        this.id = id;
        return this;
    }
    
    public Integer getType() {
        return type;
    }
    
    public Verify setType(Integer type) {
        this.type = type;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Verify setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public String getVerify() {
        return verify;
    }
    
    public Verify setVerify(String verify) {
        this.verify = verify;
        return this;
    }
    
    public String getDate() {
        return date;
    }
    
    public Verify setDate(String date) {
        this.date = date;
        return this;
    }
}