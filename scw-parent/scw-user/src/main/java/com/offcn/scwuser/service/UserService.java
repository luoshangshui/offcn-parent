package com.offcn.scwuser.service;

import com.offcn.scwuser.pojo.TMember;
import com.offcn.scwuser.pojo.TMemberAddress;

import java.util.List;

public interface UserService {
    public void registerUser(TMember tMember);
    public TMember login(String username,String pwd);
    //查询地址
    public List<TMemberAddress> findAddress(Integer memberId);
}
