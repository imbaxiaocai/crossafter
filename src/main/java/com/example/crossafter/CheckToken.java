package com.example.crossafter;

import com.example.crossafter.pub.bean.RespEntity;
import com.example.crossafter.pub.bean.Token;
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
    @Pointcut("execution(* ckt_*(..))")
    public void anyRequest(){
    }
    @Around("anyRequest()")
    public void tokenCheck(ProceedingJoinPoint pjp) throws Throwable{
        //HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();
        Object[] o = pjp.getArgs();
        RespEntity respEntity = new RespEntity();//resp
        ObjectMapper mapper = new ObjectMapper();
        int code;//响应码
        String msg;//响应信息
        String tknString = request.getParameter("head.token");
        System.out.println(JSONObject.fromObject(o[0]));
        JSONObject jsonObject = JSONObject.fromObject(o[0]);
        if(jsonObject.containsKey("token")){
            Token utkn = (Token) JSONObject.toBean(jsonObject.getJSONObject("token"),Token.class);
        }
        //token在不在
        if(tknString==null||"".equals(tknString)){
            code = -1;
            msg = "无token信息";
            respEntity.setCode(code);
            respEntity.setMsg(msg);
            response.getWriter().write(mapper.writeValueAsString(respEntity));
            response.getWriter().close();
        }
        //token对不对
        else{

        }
        response.getWriter().write("");
        response.getWriter().close();

    }
}
