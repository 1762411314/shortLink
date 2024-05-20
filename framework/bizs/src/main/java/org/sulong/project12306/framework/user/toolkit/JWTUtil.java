package org.sulong.project12306.framework.user.toolkit;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.StringUtils;
import org.sulong.project12306.framework.user.core.UserInfoDTO;
import io.jsonwebtoken.Jwts;
import org.sulong.project12306.framework.bases.constant.UserConstant;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import java.util.Date;
import java.util.HashMap;
@Slf4j
public final class JWTUtil {

    private static final Long EXPIRATION=86400L;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String ISS = "project12306";
    public static final String SECRET = "SecretKey039245678901232039487623456783092349288901402967890140939827";

    public static String generateAccessToken(UserInfoDTO userInfoDTO){
        HashMap<String,String> customerUserMap=new HashMap<>();
        customerUserMap.put(UserConstant.USER_ID_KEY,userInfoDTO.getUserId());
        customerUserMap.put(UserConstant.USER_NAME_KEY,userInfoDTO.getUsername());
        customerUserMap.put(UserConstant.REAL_NAME_KEY,userInfoDTO.getRealName());
        String jwtToken=Jwts.builder()
                .setIssuer(ISS)
                .setSubject(customerUserMap.toString())
                .signWith(SignatureAlgorithm.HS512,SECRET)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION * 1000))
                .compact();
        return TOKEN_PREFIX+jwtToken;

    }

    public static UserInfoDTO parseJwtToken(String jwtToken){
        if (StringUtils.hasText(jwtToken)){
            String actualJwtToken=jwtToken.replace(TOKEN_PREFIX,"");
            try {
                Claims claims=Jwts.parser().setSigningKey(SECRET).parseClaimsJwt(actualJwtToken).getBody();
                if (claims.getExpiration().after(new Date())){
                    return JSON.parseObject(claims.getSubject(),UserInfoDTO.class);
                }
            }catch (ExpiredJwtException ignored) {
            } catch (Exception ex) {
                log.error("JWT Token解析失败，请检查", ex);
            }
        }
        return null;
    }

}
