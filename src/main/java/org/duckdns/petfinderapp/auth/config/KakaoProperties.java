package org.duckdns.petfinderapp.auth.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "kakao.oauth")
public class KakaoProperties {
	private String tokenUrl;
	private String userInfoUri;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
}
