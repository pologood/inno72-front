package com.inno72.controller;
import com.inno72.service.Inno72OrderRefundService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
* Created by CodeGenerator on 2018/12/19.
*/
@RestController
@RequestMapping("/api/refund")
public class Inno72OrderRefundController {
    @Resource
    private Inno72OrderRefundService inno72OrderRefundService;

}
