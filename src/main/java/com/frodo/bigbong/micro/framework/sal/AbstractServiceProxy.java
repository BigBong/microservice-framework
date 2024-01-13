package com.frodo.bigbong.micro.framework.sal;

import com.frodo.bigbong.micro.framework.common.RpcContext;
import com.frodo.bigbong.micro.framework.exception.BizException;
import com.frodo.bigbong.micro.framework.util.GsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

/**
 * @author: frodoking
 * @date: 2020/02/10
 * @description:
 */
@Slf4j
public class AbstractServiceProxy {

    private final String serviceName;
    private final String host;
    private final RestTemplate restTemplate;

    public AbstractServiceProxy(String serviceName, String host, RestTemplate restTemplate) {
        this.serviceName = serviceName;
        this.host = host;
        this.restTemplate = restTemplate;
    }

    public <Resp> Resp doGetRequest(String relativePath, Map<String, Object> request,
                                    ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        return doGetRequest(relativePath, defaultHeaders(), request, responseType);
    }

    public <Resp> Resp doGetRequest(String relativePath, HttpHeaders headers, Map<String, Object> request,
                                    ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        return doRequest(relativePath, HttpMethod.GET, headers, request, responseType);
    }

    public <Resp> Resp doPostRequest(String relativePath, Map<String, Object> request,
                                     ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        return doPostRequest(relativePath, defaultHeaders(), request, responseType);
    }

    public <Resp> Resp doPostRequest(String relativePath, HttpHeaders headers, Map<String, Object> request,
                                     ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        return doRequest(relativePath, HttpMethod.POST, headers, request, responseType);
    }

    private HttpHeaders defaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add(RpcContext.SESSION_ORG_ID, String.valueOf(RpcContext.getOrgId()));
        headers.add(RpcContext.SESSION_ORG_TYPE, String.valueOf(RpcContext.getOrgType()));
        headers.add(RpcContext.REQUEST_ID, RpcContext.getRequestId());
        return headers;
    }

    public <Resp> Resp doRequest(String relativePath, HttpMethod method, HttpHeaders headers, Map<String, Object> request,
                                 ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        if (method == HttpMethod.GET) {
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host + relativePath);
            //如果存在參數
            if (!request.isEmpty()) {
                for (Map.Entry<String, Object> e : request.entrySet()) {
                    builder.queryParam(e.getKey(), e.getValue());
                }
            }
            String reallyUrl = builder.build().toString();
            return doRequest(reallyUrl, method, httpEntity, responseType);
        } else if (method == HttpMethod.POST) {
            HttpEntity<Map<String, Object>> formEntity = new HttpEntity<>(request, headers);
            return doRequest(host + relativePath, method, formEntity, responseType);
        }
        throw new RuntimeException("http method [" + method + "]supported.");
    }

    public <Resp> Resp doRequest(String url, HttpMethod method, HttpEntity formEntity,
                                 ParameterizedTypeReference<? extends SalResponse<Resp>> responseType) {
        try {
            long start = System.currentTimeMillis();
            log.info("{} url: [{}], params: [{}]", method, url, formEntity.toString());

            ResponseEntity<? extends SalResponse<Resp>> responseEntity = restTemplate.exchange(url, method, formEntity, responseType);

            start = System.currentTimeMillis() - start;
            log.info("{} url: [{}] Response: [{}], take time {}ms", method, url, GsonUtils.toJson(responseEntity), start);
            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                SalResponse<Resp> remoteResponse = responseEntity.getBody();
                if (remoteResponse == null) {
                    throw new RuntimeException(serviceName + "服务内部异常");
                } else if (!remoteResponse.isOk()) {
                    log.info(getServiceName() + "内部异常 msg [" + remoteResponse.getRMessage() + "]");
                    throw new BizException(remoteResponse.getRCode(), remoteResponse.getRMessage());
                }
                return remoteResponse.getRData();
            } else {
                throw new RuntimeException(serviceName + "异常 " + responseEntity.getStatusCode());
            }
        } catch (Throwable e) {
            if (e instanceof RuntimeException) {
                throw e;
            } else {
                throw new RuntimeException(serviceName + "超时异常");
            }
        }
    }

    public String getServiceName() {
        return serviceName;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

    public String getHost() {
        return host;
    }
}
