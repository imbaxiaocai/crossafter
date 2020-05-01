package com.example.crossafter;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.RespHead;
import com.example.crossafter.pub.bean.Token;
import com.example.crossafter.pub.utils.RedisUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import net.sf.json.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Aspect
@Component
@Configuration
public class CheckToken {
    @Autowired
    RedisUtils redisUtils;
    @Pointcut("execution(* ckt_*(..))")
    public void anyRequest(){
    }
    @Around("anyRequest()")
    public void tokenCheck(ProceedingJoinPoint pjp) throws Throwable{
        //获取拦截方法入参
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        Object[] obs = pjp.getArgs();
        RespEntity respEntity = new RespEntity();//resp
        ObjectMapper mapper = new ObjectMapper();
        //参数是否为空
        if(obs[0]==null){
            respEntity.setHead(RespHead.REQ_ERROR);
        }
        //参数非空
        else{
            //System.out.println(JSONObject.fromObject(obs[0]));
            //获取请求json
            JSONObject jsonObject = JSONObject.fromObject(obs[0]);
            //token存在
            if (jsonObject.containsKey("token")) {
                Token utkn = (Token) JSONObject.toBean(jsonObject.getJSONObject("token"), Token.class);
                String uval = utkn.getValue();
                String sysval = redisUtils.getToken(utkn.getKey());
                //token校验通过
                if (uval != null && !"".equals(uval) && uval.equals(sysval)&&sysval!=null&&sysval!="") {
                    redisUtils.setToken(utkn.getKey(),sysval);
                    pjp.proceed();
                    return;
                }
                //token不对
                else {
                    respEntity.setHead(RespHead.TOKEN_ERROR);
                }
            }
            //token不在
            else {
                respEntity.setHead(RespHead.TOKEN_ERROR);
            }
        }
        response.getWriter().write(mapper.writeValueAsString(respEntity));
        response.getWriter().close();

    }
}
