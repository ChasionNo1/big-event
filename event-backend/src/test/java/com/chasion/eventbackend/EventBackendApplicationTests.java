package com.chasion.eventbackend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

@SpringBootTest
class EventBackendApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void testGen() {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("id", 1);
        claims.put("username", "张三");
        String token = JWT.create()
//                添加载荷
                .withClaim("user", claims)
//                添加过期时间
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*60*12))
//                配置签名，
                .sign(Algorithm.HMAC256("chasion"));
        System.out.println(token);
    }

    @Test
    public void testDecode() {
        // 解析jwt
        String jwt = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7ImlkIjoxLCJ1c2VybmFtZSI6IuW8oOS4iSJ9LCJleHAiOjE3NDYzODY2MDl9.GC_Q0JWwstrVndB_Dlm30ExNm9kxLsqsQdPU5m8Wfto";
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256("chasion")).build();
        DecodedJWT verify = jwtVerifier.verify(jwt);
        Claim user = verify.getClaim("user");
        System.out.println(user);
    }
}
