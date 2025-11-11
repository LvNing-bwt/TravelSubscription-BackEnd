package me.hearta.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.hearta.entity.CompanyAccount;
import me.hearta.mapper.CompanyAccountMapper;
import org.springframework.stereotype.Service;

@Service
public class CompanyAccountService extends ServiceImpl<CompanyAccountMapper, CompanyAccount> {
    public CompanyAccount findByUsername(String username){
        return this.lambdaQuery()  // 1. 创建Lambda查询构造器
                .eq(CompanyAccount::getUsername,username) // 2. 设置相等条件
                .one(); // 3. 执行查询并返回单个结果
    }
}
