package com.yangbingdong.service.core.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yangbingdong.service.core.ServiceImpl;
import com.yangbingdong.service.core.TestUserService;
import com.yangbingdong.service.mp.entity.TestUser;
import com.yangbingdong.service.mp.mapper.TestUserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author yangbingdong
 * @since 2019-09-09
 */
@Service
public class TestUserServiceImpl extends ServiceImpl<TestUserMapper, TestUser> implements TestUserService {
    /* !!!TUPLE_MARK_START!!! */

    @Override
    public TestUser getById(Long id) {
        return redisoper.getByKey(() -> baseMapper.selectById(id), id);
    }

    @Override
    public TestUser getByTestUk(Long testUk) {
        return redisoper.getByUniqueIndex(() -> getOne(new QueryWrapper<TestUser>().eq(TestUser.TEST_UK, testUk)), TestUser.IDX_UK, testUk);
    }

    @Override
    public List<TestUser> getByName(String Name) {
        return redisoper.getByNormalIndex(() -> list(new QueryWrapper<TestUser>().eq(TestUser.NAME, Name)), TestUser.IDX_NAME, Name);
    }

    @Override
    public List<TestUser> getByAgeAndEmail(Integer age ,String email) {
        return redisoper.getByNormalIndex(() -> list(new QueryWrapper<TestUser>().eq(TestUser.AGE, age).eq(TestUser.EMAIL, email)), TestUser.IDX_UNION, age ,email);
    }

    /* !!!TUPLE_MARK_END!!! */
}
