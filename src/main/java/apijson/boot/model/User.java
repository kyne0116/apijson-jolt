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

import java.util.List;
import apijson.MethodAccess;
import apijson.orm.Visitor;

/**
 * 用户基础模型类
 */
@MethodAccess
public class User implements Visitor<Long> {
    public static final String TAG = "User";
    public static final String TABLE_NAME = "User";
    
    private Long id;
    private String name;
    private String phone;
    private String password;
    private String head;
    private Integer sex;
    private String tag;
    private List<Long> contactIdList;
    private String pictureList;
    private String date;
    
    public User() {
        super();
    }
    
    public User(long id) {
        this();
        setId(id);
    }
    
    public Long getId() {
        return id;
    }
    
    public User setId(Long id) {
        this.id = id;
        return this;
    }
    
    public String getName() {
        return name;
    }
    
    public User setName(String name) {
        this.name = name;
        return this;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public User setPhone(String phone) {
        this.phone = phone;
        return this;
    }
    
    public String getPassword() {
        return password;
    }
    
    public User setPassword(String password) {
        this.password = password;
        return this;
    }
    
    public String getHead() {
        return head;
    }
    
    public User setHead(String head) {
        this.head = head;
        return this;
    }
    
    public Integer getSex() {
        return sex;
    }
    
    public User setSex(Integer sex) {
        this.sex = sex;
        return this;
    }
    
    public String getTag() {
        return tag;
    }
    
    public User setTag(String tag) {
        this.tag = tag;
        return this;
    }
    
    public List<Long> getContactIdList() {
        return contactIdList;
    }
    
    public User setContactIdList(List<Long> contactIdList) {
        this.contactIdList = contactIdList;
        return this;
    }
    
    public String getPictureList() {
        return pictureList;
    }
    
    public User setPictureList(String pictureList) {
        this.pictureList = pictureList;
        return this;
    }
    
    public String getDate() {
        return date;
    }
    
    public User setDate(String date) {
        this.date = date;
        return this;
    }
}