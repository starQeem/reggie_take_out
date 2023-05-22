package com.starqeem.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starqeem.reggie.common.R;
import com.starqeem.reggie.pojo.Employee;
import com.starqeem.reggie.service.EmployeeService;
import com.sun.deploy.net.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    //员工登录
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {

        //1.将页面提交的密码password进行md5加密
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//getBytes() :存到一个byte数组中
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3.如果没有查询到则返回登录失败结果
        if (emp == null) {
            return R.error("用户名错误,登录失败!");
        }
        //4.密码比对,如果不一样则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return R.error("密码错误,登录失败!");
        }
        //5.查看员工状态,如果为已禁用状态,则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return R.error("该账号已被禁用!");
        }
        //6.登录成功,将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());

        return R.success(emp);
    }

    //员工登录退出
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request) {
        //清理员工登录的session信息
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功!");
    }

    //新增员工
    /*
    1.设置初始密码
    2.创建时间
    3.更新时间
    4.创建人(当前登录用户id)
    * */
    @PostMapping
    public R<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增新员工,员工信息: {}", employee.toString());
        //设置初始密码
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //创建时间
//        employee.setCreateTime(LocalDateTime.now());
//        //更新时间
//        employee.setUpdateTime(LocalDateTime.now());
//        //创建人
//        Long empId = (Long) request.getSession().getAttribute("employee");
//
//        employee.setCreateUser(empId);
//        employee.setUpdateUser(empId);

        employeeService.save(employee);

        return R.success("创建成功!");
    }

    //员工信息分页查询
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        log.info("page = {},pageSize = {},neme = {}", page, pageSize, name);
        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件(模糊查询name)
        queryWrapper.like(StringUtils.isNotEmpty(name), Employee::getName, name);
        //添加排序条件(按照更新时间排序)
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        employeeService.page(pageInfo, queryWrapper);

        return R.success(pageInfo);
    }

    //根据id修改员工信息
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info(employee.toString());

        long id = Thread.currentThread().getId();
        log.info("线程id为:{}",id);

//        Long emId = (Long) request.getSession().getAttribute("employee");
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(emId);
        employeeService.updateById(employee);

        return R.success("员工信息修改成功!");
    }
    //编辑员工信息
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }

}
