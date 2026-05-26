package com.czh.leave.aspect;

import com.czh.leave.annotation.LogOperation;
import com.czh.leave.entity.OperationLog;
import com.czh.leave.mapper.OperationLogMapper;
import com.czh.leave.mapper.UserMapper;
import com.czh.leave.entity.User;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class LogAspect {

    @Autowired
    private OperationLogMapper operationLogMapper;
    
    @Autowired
    private UserMapper userMapper;

    @Pointcut("@annotation(com.czh.leave.annotation.LogOperation)")
    public void logPointCut() {}

    @AfterReturning(value = "logPointCut()", returning = "result")
    public void saveLog(JoinPoint joinPoint, Object result) {
        try {
            OperationLog log = new OperationLog();

            // 1. Get Operation Description
            String action = "";
            try {
                String methodName = joinPoint.getSignature().getName();
                Class<?> targetClass = joinPoint.getTarget().getClass();
                Method[] methods = targetClass.getMethods();
                for (Method method : methods) {
                    if (method.getName().equals(methodName)) {
                        LogOperation annotation = method.getAnnotation(LogOperation.class);
                        if (annotation != null) {
                            action = annotation.value();
                        }
                        break;
                    }
                }
            } catch (Exception e) {
                action = "Unknown Operation";
            }
            log.setAction(action);

            // 2. Get IP Address
            try {
                HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                log.setIpAddress(request.getRemoteAddr());
            } catch (Exception e) {
                log.setIpAddress("Unknown IP");
            }

            // 3. Get User ID
            Long userId = null;
            Object[] args = joinPoint.getArgs();
            
            try {
                for (Object arg : args) {
                    if (arg == null) continue;
                    String className = arg.getClass().getSimpleName();
                    
                    if (className.equals("LeaveApplyRequest")) {
                        try { userId = (Long) arg.getClass().getMethod("getApplicantId").invoke(arg); } catch(Exception e){}
                    } else if (className.equals("ApprovalActionRequest")) {
                        try { userId = (Long) arg.getClass().getMethod("getApproverId").invoke(arg); } catch(Exception e){}
                    } else if (className.equals("LoginRequest")) {
                        try { 
                            String username = (String) arg.getClass().getMethod("getUsername").invoke(arg);
                            User u = userMapper.findByUsername(username);
                            if (u != null) userId = u.getId();
                        } catch(Exception e){}
                    } else if (className.equals("UpdateProfileRequest") || className.equals("UpdatePasswordRequest")) {
                        try { userId = (Long) arg.getClass().getMethod("getUserId").invoke(arg); } catch(Exception e){}
                    }
                }
            } catch (Exception e) {
                // Ignore extraction errors
            }

            log.setUserId(userId != null ? userId : 0L);

            operationLogMapper.insert(log);
        } catch (Exception e) {
            // CRITICAL: Swallow all exceptions in Aspect to prevent blocking main business logic
            System.err.println("Failed to record operation log: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
