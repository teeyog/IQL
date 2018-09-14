package iql.web.system.service;

import iql.web.bean.BaseBean;
import iql.web.system.domain.User;
import com.alibaba.fastjson.JSONObject;
import org.springframework.transaction.annotation.Transactional;

public interface UserService {


    JSONObject findUserList(BaseBean base);

    JSONObject findUserById(User user);

    User findByUsername(User user);

    @Transactional
    Integer addUser(User user, String roles);

    @Transactional
    Integer updateUser(User user, String roles);

    @Transactional
    Integer delUser(User user);

    Integer changePassWord(User user);

    Integer changeFirstLoginAndPassWord(User user);

    Integer changeFirstLoginFlag(User user);

    JSONObject findUserSelect(User user);

    @Transactional
    Integer updateToken(User user, String token);

    User findUserByToken(String token);

}
