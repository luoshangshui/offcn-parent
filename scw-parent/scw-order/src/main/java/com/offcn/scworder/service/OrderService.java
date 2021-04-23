package com.offcn.scworder.service;

import com.offcn.scworder.pojo.TOrder;
import com.offcn.scworder.vo.req.OrderInfoSubmitVo;

public interface OrderService {
    //保存订单
    public TOrder saveOrder(OrderInfoSubmitVo vo);
}
