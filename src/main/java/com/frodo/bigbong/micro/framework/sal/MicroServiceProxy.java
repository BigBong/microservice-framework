package com.frodo.bigbong.micro.framework.sal;

import com.frodo.bigbong.micro.framework.common.RpcContext;
import com.frodo.bigbong.micro.framework.exception.BizException;
import com.frodo.bigbong.micro.framework.common.RpcPageResponse;
import com.frodo.bigbong.micro.framework.common.RpcResponse;
import com.frodo.bigbong.micro.framework.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * @author: frodoking
 * @date: 2020/02/14
 * @description:
 */
@Slf4j
public class MicroServiceProxy extends AbstractServiceProxy {

    public MicroServiceProxy(String serviceName, String host, RestTemplate restTemplate) {
        super(serviceName, host, restTemplate);
    }

    public <Resp> Resp doMicroGetRequest(String relativePath, Map<String, Object> request,
                                         ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        return doMicroGetRequest(relativePath, defaultHeaders(), request, responseType);
    }

    public <Resp> Resp doMicroGetRequest(String relativePath, HttpHeaders headers, Map<String, Object> request,
                                         ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        return doMicroRequest(relativePath, HttpMethod.GET, headers, request, responseType);
    }

    public <Resp> Resp doMicroPostRequest(String relativePath, Map<String, Object> request,
                                          ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        return doMicroPostRequest(relativePath, defaultHeaders(), request, responseType);
    }

    public <Resp> Resp doMicroPostRequest(String relativePath, HttpHeaders headers, Map<String, Object> request,
                                          ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        return doMicroRequest(relativePath, HttpMethod.POST, headers, request, responseType);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(RpcContext.SESSION_ORG_ID, String.valueOf(RpcContext.getOrgId()));
        headers.add(RpcContext.SESSION_ORG_TYPE, String.valueOf(RpcContext.getOrgType()));

        if (RpcContext.getCurrentUserId() != null) {
            headers.add(RpcContext.SESSION_USER_ID, String.valueOf(RpcContext.getCurrentUserId()));
        }
        try {
            headers.add(RpcContext.SESSION_USER_NAME, URLEncoder.encode(RpcContext.getCurrentUserName(), "UTF-8"));
        } catch (Exception e) {
            log.warn("SESSION_USER_NAME ", e);
        }
        if (RpcContext.getRequestId() != null) {
            headers.add(RpcContext.REQUEST_ID, RpcContext.getRequestId());
        }
        return headers;
    }


    public <Resp> Resp doMicroRequest(String relativePath, HttpMethod method, HttpHeaders headers, Map<String, Object> request,
                                      ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        if (method == HttpMethod.GET) {
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(getHost() + relativePath);
            //如果存在參數
            if (!request.isEmpty()) {
                for (Map.Entry<String, Object> e : request.entrySet()) {
                    builder.queryParam(e.getKey(), e.getValue());
                }
            }
            String reallyUrl = builder.build().toString();
            return doMicroRequest(reallyUrl, method, httpEntity, responseType);
        } else if (method == HttpMethod.POST) {
            HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(request, headers);
            return doMicroRequest(getHost() + relativePath, method, formEntity, responseType);
        }
        throw new RuntimeException("http method [" + method + "]supported.");
    }

    public <Resp> Resp doMicroRequest(String url, HttpMethod method, HttpEntity formEntity,
                                      ParameterizedTypeReference<RpcResponse<Resp>> responseType) {
        try {
            long start = System.currentTimeMillis();

            log.info("{} url: [{}], params: [{}]", method, url, formEntity.toString());

            ResponseEntity<RpcResponse<Resp>> responseEntity = getRestTemplate().exchange(url, method,
                    formEntity, responseType);

            start = System.currentTimeMillis() - start;
            log.info("{} url: [{}] Response: [{}], take time {}ms", method, url, GsonUtils.toJson(responseEntity), start);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                RpcResponse<Resp> remoteResponse = responseEntity.getBody();
                return unWarpResponse(remoteResponse);
            } else {
                throw new RuntimeException(getServiceName() + "异常 " + responseEntity.getStatusCode());
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw e;
            } else {
                throw new RuntimeException(getServiceName() + "超时异常");
            }
        }
    }

    public <Resp> List<Resp> doMicroRequestWithList(String relativePath, Map<String, Object> request,
                                                    ParameterizedTypeReference<RpcPageResponse<Resp>> responseType) {
        return doMicroRequestWithList(relativePath, defaultHeaders(), request, responseType);
    }

    public <Resp> List<Resp> doMicroRequestWithList(String relativePath, HttpHeaders headers, Map<String, Object> request,
                                                    ParameterizedTypeReference<RpcPageResponse<Resp>> responseType) {
        HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(request, headers);
        try {
            long start = System.currentTimeMillis();
            log.info("Post/Get url: [{}], params: [{}]", getHost() + relativePath, GsonUtils.toJson(request));

            ResponseEntity<RpcPageResponse<Resp>> responseEntity = getRestTemplate().exchange(getHost() + relativePath, HttpMethod.POST,
                    formEntity, responseType);

            start = System.currentTimeMillis() - start;
            log.info("Post/Get url: [{}] Response: [{}], take time {}ms", getHost() + relativePath, GsonUtils.toJson(responseEntity), start);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                RpcPageResponse<Resp> remoteResponse = responseEntity.getBody();
                return unWarpResponse(remoteResponse);
            } else {
                throw new RuntimeException(getServiceName() + "异常 " + responseEntity.getStatusCode());
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw e;
            } else {
                throw new RuntimeException(getServiceName() + "超时异常");
            }
        }
    }

    public <Resp> Resp unWarpResponse(RpcResponse<Resp> remoteResponse) {
        if (remoteResponse == null) {
            throw new RuntimeException(this.getServiceName() + "服务内部异常");
        } else if (!remoteResponse.isOk()) {
            log.info(this.getServiceName() + "内部异常 msg [" + remoteResponse.getMessage() + "]");
            throw new BizException(remoteResponse.getCode(), remoteResponse.getMessage());
        } else {
            return remoteResponse.getData();
        }
    }

    public <Resp> List<Resp> unWarpResponse(RpcPageResponse<Resp> remoteResponse) {
        if (remoteResponse == null) {
            throw new RuntimeException(this.getServiceName() + "服务内部异常");
        } else if (!remoteResponse.isOk()) {
            log.info(this.getServiceName() + "内部异常 msg [" + remoteResponse.getMessage() + "]");
            throw new BizException(remoteResponse.getCode(), remoteResponse.getMessage());
        } else {
            return remoteResponse.getData();
        }
    }
}
