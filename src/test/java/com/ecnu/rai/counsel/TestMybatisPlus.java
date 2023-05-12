package com.ecnu.rai.counsel;

import com.ecnu.rai.counsel.entity.User;
import com.ecnu.rai.counsel.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMybatisPlus {
	@Autowired
	private UserMapper userMapper;

	@Test
	public void testSelect() {
		User user = new User();
		user.setUsername("Jack");
		user.setPassword("123456");
		user.setType("visitor");
		int result = this.userMapper.insert(user);
		System.out.println("result: " + result);
		System.out.println(user.getId());
	}
}
