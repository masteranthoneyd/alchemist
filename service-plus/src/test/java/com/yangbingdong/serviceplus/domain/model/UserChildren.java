package com.yangbingdong.serviceplus.domain.model;

import com.yangbingdong.serviceplus.domain.entity.Children;
import com.yangbingdong.serviceplus.domain.entity.CommonUser;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.List;

/**
 * @author ybd
 * @date 2019/9/3
 * @contact yangbingdong1994@gmail.com
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class UserChildren extends CommonUser {

    private List<Children> c;
}
