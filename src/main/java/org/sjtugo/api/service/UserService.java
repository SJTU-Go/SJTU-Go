package org.sjtugo.api.service;

import com.vividsolutions.jts.io.ParseException;
import net.sf.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.sjtugo.api.DAO.UserRepository;
import org.sjtugo.api.entity.User;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.HashMap;
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

        String session_key = res.get("session_key").toString();
        String openid = res.get("openid").toString();

        User user = userRepository.findByOpenid(openid);
        if(user != null){
            String userID = user.getId();
        } else {
            User new_user = new User();
            new_user.setOpenid(openid);
            new_user.setSession_key(session_key);
            userRepository.save(new_user);
            String userID = new_user.getId();
        }

        Map<String, String> userInfo = new HashMap<>();
        userInfo.put("session_key", session_key);
        userInfo.put("userID", userID);
        return ResponseEntity<>(userInfo, HttpStatus.OK);
    }

    private String doGet(String code){
        StringBuilder url = new StringBuilder(UserConstant.WX_LOGIN_URL);
        url.append("appid=");
        url.append(UserConstant.WX_LOGIN_APPID);
        url.append("&secret=");
        url.append(UserConstant.WX_LOGIN_SECRET);
        url.append("&js_code=");
        url.append(code);
        url.append("&grant_type=authorization_code");

        CloseableHttpClient client = HttpClientBuilder.create().build();
        HttpGet get = new HttpGet(url.toString());
        try{
            CloseableHttpResponse response = client.execute(get);
            String resultString = EntityUtils.toString(response.getEntity());
            JSONObject res = JSONObject.fromObject(resultString);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
