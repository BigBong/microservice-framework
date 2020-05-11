package com.frodo.bigbong.micro.framework.interceptor;

import com.frodo.bigbong.micro.framework.common.RequestContext;
import com.frodo.bigbong.micro.framework.exception.ArgumentException;
import com.frodo.bigbong.micro.framework.exception.BizException;
import com.frodo.bigbong.micro.framework.response.CommonPageResponse;
import com.frodo.bigbong.micro.framework.response.CommonResponse;
import com.frodo.bigbong.micro.framework.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static com.frodo.bigbong.micro.framework.response.CommonResponse.ERROR_ARGUMENTS;

/**
 * @author frodoking on 2019/10/18.
 */
@Slf4j
public class MicroServiceAroundAdvice {

    public static Object around(ProceedingJoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        String requestId = RequestContext.getRequestId();
        if (StringUtils.isBlank(requestId)) {
            requestId = "reqIdNA";
        }
        MDC.put("reqId", requestId);
        String remoteIP = RequestContext.getRemoteAddress();
        if (StringUtils.isBlank(remoteIP)) {
            remoteIP = "remIpNA";
        }

        Object target = joinPoint.getTarget();
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object object = null;
        try {
            object = joinPoint.proceed();
            return object;
        } catch (Throwable throwable) {
            object = CommonResponse.error(throwable.getMessage());
            if (throwable instanceof ArgumentException) {
                ArgumentException argumentException = (ArgumentException) throwable;
                log.warn("服务参数异常 {}", argumentException.getMessage());
                Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                if (returnType == CommonResponse.class) {
                    object = CommonResponse.error(ERROR_ARGUMENTS, argumentException.getMessage());
                } else if (returnType == CommonPageResponse.class) {
                    object = CommonPageResponse.error(ERROR_ARGUMENTS, argumentException.getMessage());
                }
            } else if (throwable instanceof BizException) {
                BizException bizException = (BizException) throwable;
                log.warn("服务业务异常 {} : {}", bizException.getErrorCode(), bizException.getMessage());
                Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                if (returnType == CommonResponse.class) {
                    object = CommonResponse.error(bizException.getErrorCode(), bizException.getMessage());
                } else if (returnType == CommonPageResponse.class) {
                    object = CommonPageResponse.error(bizException.getErrorCode(), bizException.getMessage());
                }
            } else {
                Class returnType = ((MethodSignature) joinPoint.getSignature()).getReturnType();
                log.error("服务异常 ", throwable);
                if (returnType == CommonResponse.class) {
                    object = CommonResponse.error(throwable.getMessage());
                } else if (returnType == CommonPageResponse.class) {
                    object = CommonPageResponse.error(throwable.getMessage());
                }
            }

            return object;
        } finally {
            long takeTime = System.currentTimeMillis() - startTime;
            log.info("[ requestId {} ], [ remoteIp {} ], [ {}.{}, args {}, output {} ], [ take time {} ms ]",
                    requestId, remoteIP, target.getClass().getName(), signature.getName(), GsonUtils.toJson(args),
                    GsonUtils.toJson(object), takeTime);
            MDC.clear();
        }
    }

    public static Object aroundLocalService(ProceedingJoinPoint joinPoint, Boolean needPrintArgs) {
        long startTime = System.currentTimeMillis();
        String requestId = RequestContext.getRequestId();
        if (StringUtils.isBlank(requestId)) {
            requestId = "reqIdNA";
        }
        MDC.put("reqId", requestId);
        Object target = joinPoint.getTarget();
        Signature signature = joinPoint.getSignature();
        Object[] args = joinPoint.getArgs();
        Object object = null;
        try {
            object = joinPoint.proceed();
            return object;
        } catch (Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable);
            }
        } finally {
            long takeTime = System.currentTimeMillis() - startTime;
            if (needPrintArgs) {
                log.info("[ requestId {} ], [ {}.{}, args {}, output {} ], [ take time {} ms ]",
                        requestId, target.getClass().getName(), signature.getName(), GsonUtils.toJson(args),
                        GsonUtils.toJson(object), takeTime);
            } else {
                log.info("[ requestId {} ], [ {}.{} ], [ take time {} ms ]",
                        requestId, target.getClass().getName(), signature.getName(), takeTime);
            }
        }
    }

    public static <T extends Annotation> T parseAopAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> userClass = ClassUtils.getUserClass(joinPoint.getTarget());
        Method specificMethod = ClassUtils.getMostSpecificMethod(method, userClass);
        return specificMethod.getAnnotation(annotationClass);
    }
}
