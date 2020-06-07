package org.sjtugo.api.service;

import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     *
     * @param code:用户临时登陆凭证
     * @return 用户id & 用户openID
     */
    public ResponseEntity<?> userLogin(String code){
        String resultString = doGet(code);
        JSONObject res = JSONObject.fromObject(resultString);
        System.out.println(res);
        //错误code会返回 'errcode': 40029
        String session_key = res.get("session_key").toString();
        String openid = res.get("openid").toString();

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("openid", openid);
        List<User> users = userRepository.findByOpenId(openid);
        if(users.size() != 0){
            User user = users.get(0);
            Integer temp_id = user.getUserID();
            String userID = temp_id.toString();
            userInfo.put("userID", userID);
        } else {
            User new_user = new User();
            new_user.setOpenId(openid);
            new_user.setSessionkey(session_key);
            userRepository.save(new_user);
            Integer temp_id = new_user.getUserID();
            String userID = temp_id.toString();
            userInfo.put("userID", userID);
        }
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    private String doGet(String code){
        StringBuffer url = new StringBuffer(UserConstant.WX_LOGIN_URL);
        url.append("appid=");
        url.append(UserConstant.WX_LOGIN_APPID);
        url.append("&secret=");
        url.append(UserConstant.WX_LOGIN_SECRET);
        url.append("&js_code=");
        url.append(code);
        url.append("&grant_type=authorization_code");

        //String url1 = URLEncoder.encode(url.toString(), StandardCharsets.UTF_8);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet get = new HttpGet(url.toString());

        String resultString = null;
        try{
            CloseableHttpResponse response = client.execute(get);
            resultString = EntityUtils.toString(response.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultString;
    }
}
