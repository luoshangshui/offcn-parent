package com.offcn.scworder.service.impl;

import com.offcn.dycommons.enums.OrderStatusEnume;
import com.offcn.dycommons.response.AppResponse;
import com.offcn.scworder.mapper.TOrderMapper;
import com.offcn.scworder.pojo.TOrder;
import com.offcn.scworder.service.OrderService;
import com.offcn.scworder.service.ProjectServiceFeign;
import com.offcn.scworder.vo.req.OrderInfoSubmitVo;
import com.offcn.scworder.vo.resp.TReturn;
import com.offcn.utils.AppDateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private TOrderMapper orderMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProjectServiceFeign projectServiceFeign;

    @Override
    public TOrder saveOrder(OrderInfoSubmitVo vo) {
        //
        TOrder tOrder = new TOrder();
        BeanUtils.copyProperties(vo, tOrder);
        //设置memberId
        String s = redisTemplate.opsForValue().get(vo.getAccessToken());
        Integer memberId = Integer.parseInt(s);
        tOrder.setMemberid(memberId);
        //创建日期
        tOrder.setCreatedate(AppDateUtils.getFormatTime());
        //订单号
        String orderNum = UUID.randomUUID().toString().replace("-", "");
        tOrder.setOrdernum(orderNum);
        //设置状态
        tOrder.setStatus(OrderStatusEnume.UNPAY.getCode() + "");
        //设置发票状态
        tOrder.setInvoice(vo.getInvoice()+"");
        //设置订单金额
        AppResponse<List<TReturn>> data = projectServiceFeign.getReturnlist(vo.getProjectid());
        List<TReturn> data1 = data.getData();
        TReturn userReturn = null;
        for (TReturn tReturn : data1) {
            if(tReturn.getId().intValue() == vo.getReturnid().intValue()){
                userReturn = tReturn;
            }
        }
        Integer money = tOrder.getRtncount()* userReturn.getSupportmoney()+userReturn.getFreight();
        tOrder.setMoney(money);
        orderMapper.insert(tOrder);
        return tOrder;
    }
}
