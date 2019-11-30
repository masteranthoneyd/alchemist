package com.yangbingdong.redisoperv2.core.redisoper;

import com.yangbingdong.redisoperv2.core.Redisoper;
import com.yangbingdong.redisoperv2.core.RedisoperAware;
import com.yangbingdong.redisoperv2.core.redisoper.entity.TestUser;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author ybd
 * @date 19-3-21
 * @contact yangbingdong1994@gmail.com
 */
@Service
public class UserService implements RedisoperAware<TestUser> {

    public static final String EMAIL = "EMAIL";
    public static final String NAME = "NAME";
    private TestUserRepository testUserRepository = new TestUserRepository();

	@Getter
	private Redisoper<TestUser> redisoper;

	@Override
	public void setRedisoper(Redisoper<TestUser> redisoper) {
		this.redisoper = redisoper;
	}

	public TestUser getById(Long id) {
		return redisoper.getByKey(() -> testUserRepository.getById(id), id);
	}


	public TestUser save(TestUser testUser) {
		saveOrUpdateInnerDB(testUser);
		redisoper.del(testUser);
		return testUser;
	}

	private void saveOrUpdateInnerDB(TestUser testUser) {
		if (testUser.getId() == null) {
			testUser.setId(6666666666666666666L);
		}
	}

	class TestUserRepository {
		TestUser getById(Long id) {
            System.out.println("######## 从数据库获取");
			return TestUser.buildUserById(id).setEmail(EMAIL).setName(NAME);
		}

		TestUser getByEmail(String email) {
			return TestUser.buildUserByName(NAME).setEmail(EMAIL);
		}

		public List<TestUser> getByName(String name) {
			return Collections.singletonList(TestUser.buildUserByName(NAME).setEmail(EMAIL));
		}
	}
}
