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
 * 隐私设置模型类
 */
@MethodAccess
public class Privacy {
    public static final String TAG = "Privacy";
    public static final String TABLE_NAME = "Privacy";
    
    private Long id;
    private Long certified;
    private String phone;
    private Long balance;
    private String _password;
    private String _payPassword;
    
    public Privacy() {
        super();
    }
    
    public Privacy(long id) {
        this();
        setId(id);
    }
    
    public Long getId() {
        return id;
    }
    
    public Privacy setId(Long id) {
        this.id = id;
        return this;
    }
    
    public Long getCertified() {
        return certified;
    }
    
    public Privacy setCertified(Long certified) {
        this.certified = certified;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public Privacy setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public Long getBalance() {
        return balance;
    }
    
    public Privacy setBalance(Long balance) {
        this.balance = balance;
        return this;
    }
    
    public String get_password() {
        return _password;
    }
    
    public Privacy set_password(String _password) {
        this._password = _password;
        return this;
    }
    
    public String get_payPassword() {
        return _payPassword;
    }
    
    public Privacy set_payPassword(String _payPassword) {
        this._payPassword = _payPassword;
        return this;
    }
    
    public Privacy setPassword(String password) {
        this._password = password;
        return this;
    }
    
    public Privacy setPayPassword(String payPassword) {
        this._payPassword = payPassword;
        return this;
    }
}