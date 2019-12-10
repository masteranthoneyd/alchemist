package com.yangbingdong.serviceplus.domain.servive;

import com.yangbingdong.serviceplus.core.ServiceImpl;
import com.yangbingdong.serviceplus.domain.entity.CommonUser;
import com.yangbingdong.serviceplus.domain.mapper.CommonUserMapper;
import org.springframework.stereotype.Service;

/**
 * @author ybd
 * @date 2019/12/10
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class CommonUserServiceImpl extends ServiceImpl<CommonUserMapper, CommonUser> implements CommonUserService {
}
