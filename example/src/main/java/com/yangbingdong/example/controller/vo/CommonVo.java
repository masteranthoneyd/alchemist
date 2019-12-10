package com.yangbingdong.example.controller.vo;

import com.yangbingdong.example.controller.vo.enums.Gender;
import lombok.Data;

/**
 * @author ybd
 * @date 2019/12/9
 * @contact yangbingdong1994@gmail.com
 */
@Data
public class CommonVo {
    private Long id;

    private Gender gender;
}
