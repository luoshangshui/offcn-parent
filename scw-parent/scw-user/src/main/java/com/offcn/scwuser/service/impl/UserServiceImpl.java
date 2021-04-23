package com.offcn.scwuser.service.impl;

import com.offcn.scwuser.enums.UserExceptionEnum;
import com.offcn.scwuser.exception.UserException;
import com.offcn.scwuser.mapper.TMemberAddressMapper;
import com.offcn.scwuser.mapper.TMemberMapper;
import com.offcn.scwuser.pojo.TMember;
import com.offcn.scwuser.pojo.TMemberAddress;
import com.offcn.scwuser.pojo.TMemberAddressExample;
import com.offcn.scwuser.pojo.TMemberExample;
import com.offcn.scwuser.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private TMemberMapper tMemberMapper;
    @Autowired
    private TMemberAddressMapper tMemberAddressMapper;

    @Override
    public void registerUser(TMember tMember) {
        //判断账号是否存在
        TMemberExample tMemberExample = new TMemberExample();
        tMemberExample.createCriteria().andLoginacctEqualTo(tMember.getLoginacct());
        long l = tMemberMapper.countByExample(tMemberExample);
        if (l > 0) {
            throw new UserException(UserExceptionEnum.LOGINACCT_EXIST);
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String encode = bCryptPasswordEncoder.encode(tMember.getUserpswd());
        tMember.setUserpswd(encode); //加密
        //账号信息设置
        tMember.setUsername(tMember.getLoginacct());
        tMember.setAuthstatus("0");
        tMember.setUsertype("0");
        tMember.setAccttype("2");
//        tMemberMapper.insert(tMember);
        tMemberMapper.insertSelective(tMember);
    }

    @Override
    public TMember login(String username, String pwd) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        TMemberExample example = new TMemberExample();
        TMemberExample.Criteria criteria = example.createCriteria();
        criteria.andLoginacctEqualTo(username);
        List<TMember> list = tMemberMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            TMember tMember = list.get(0);
            boolean matches = encoder.matches(pwd, tMember.getUserpswd());
            if (matches) {
                return tMember;
            } else {
                return null;
            }
        }
        return null;
    }

    @Override
    public List<TMemberAddress> findAddress(Integer memberId) {
        TMemberAddressExample example = new TMemberAddressExample();
        example.createCriteria().andMemberidEqualTo(memberId);
        return tMemberAddressMapper.selectByExample(example);
    }
}
