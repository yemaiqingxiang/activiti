package com.jerryl;

import org.activiti.engine.*;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IdentityTest {
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    RepositoryService repositoryService;


    /**
     * 添加用户测试
     */
    @Test
    public void saveUser() {
        User user1=new UserEntity();
        user1.setId("张三");
        user1.setPassword("123");
        user1.setEmail("sdf");
        user1.setLastName("df");
        User user2=new UserEntity();
        user2.setId("李四");
        user2.setPassword("456");
        identityService.saveUser(user1);
//        identityService.saveUser(user2);
    }

    /**
     * 删除用户
     */
    @Test
    public void deleteUser() {
        identityService.deleteUser("李四");
    }

    /**
     * 测试添加组（角色）
     */
    @Test
    public void testSaveGroup(){
        Group group=new GroupEntity(); // 实例化组实体
        group.setId("管理员");
        Group group2=new GroupEntity(); // 实例化组实体
        group2.setId("普通用户");
        identityService.saveGroup(group);
//        identityService.saveGroup(group2);
    }

    /**
     * 测试删除组(角色)
     */
    @Test
    public void testDeleteGroup(){
        identityService.deleteGroup("管理员");
    }

    /**
     * 测试添加用户和组（角色）关联关系
     */
    @Test
    public void testSaveMembership(){
        identityService.createMembership("张三", "普通用户");
    }

    /**
     * 测试删除用户和组（角色）关联关系
     */
    @Test
    public void testDeleteMembership(){
        identityService.deleteMembership("张三", "普通用户");
    }

}