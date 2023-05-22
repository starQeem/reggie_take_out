package com.starqeem.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starqeem.reggie.mapper.EmployeeMapper;
import com.starqeem.reggie.pojo.Employee;
import com.starqeem.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
