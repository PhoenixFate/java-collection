package com.phoenix.oauth2.sso.controller;

import com.phoenix.oauth2.base.result.RequestResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author phoenix
 * @Date 10/7/22 14:46
 * @Version 1.0
 */
@Controller
@AllArgsConstructor
@Slf4j
public class MainController {

    private final OAuth2RestTemplate oAuth2RestTemplate;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/member")
    public String member() {
        ResponseEntity<RequestResult> response = oAuth2RestTemplate.getForEntity("http://localhost:7001/product/product/list", RequestResult.class);
        RequestResult body = response.getBody();
        assert body != null;
        log.info("body: " + body.toJsonString());

        RequestResult result = oAuth2RestTemplate.getForObject("http://localhost:7001/product/product/list", RequestResult.class);
        assert result != null;
        log.info("result: " + result.toJsonString());

        return "member";
    }
}
