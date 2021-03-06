package org.activiti.examples.service.impl;

import org.activiti.examples.Config;
import org.activiti.examples.resp.SuccessResp;
import org.activiti.examples.util.UUIDUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author wangkai
 * @since JDK8
 */
@Service
public class LoginService {

    @Resource
    private AuthenticationManager authenticationManager;
    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public Object login(String username, String password) {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //redis存储
        String token = UUIDUtil.generateToken();
        redisTemplate.opsForValue().set(token, username, Config.expire, TimeUnit.MINUTES);
        return new SuccessResp(token);
    }

}
