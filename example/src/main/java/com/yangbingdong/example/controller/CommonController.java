package com.yangbingdong.example.controller;

import com.yangbingdong.auth.annotated.IgnoreAuth;
import com.yangbingdong.example.controller.vo.CommonVo;
import com.yangbingdong.mvc.annotated.Rest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author ybd
 * @date 2019/12/9
 * @contact yangbingdong1994@gmail.com
 */
@Rest("/common")
public class CommonController {

    @IgnoreAuth
    @PostMapping
    public CommonVo reveiveCommon(@RequestBody CommonVo commonVo) {
        System.out.println(commonVo);
        return commonVo;
    }
}
