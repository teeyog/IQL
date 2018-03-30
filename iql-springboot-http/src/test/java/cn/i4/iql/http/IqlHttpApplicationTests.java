package cn.i4.iql.http;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MockServletContext.class)
@WebAppConfiguration
public class IqlHttpApplicationTests {

//	@Autowired
//	private ActorSystem actorSystem;
//
//	@Test
//	public void contextLoads() {
//		System.out.println(actorSystem);
//	}

//	@Autowired
//	private IqlExcutionRepository iqlExcutionRepository;

//	@Autowired
//	private UserRepository userRepository;
//
//	@Test
//	public void test() throws Exception {
//
//		// 创建10条记录
////		iqlExcutionRepository.save(new IqlExcution(new Timestamp(System.currentTimeMillis()),5L,true,"hdfs"));
//
//		userRepository.save(new User("HHH", 80));
//
//	}

}
