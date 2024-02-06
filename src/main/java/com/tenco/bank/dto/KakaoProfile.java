
package com.tenco.bank.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.processing.Generated;

import lombok.Data;

@Data
public class KakaoProfile {

	private Long id;
	private String connectedAt;
	private Properties properties;
	private KakaoAccount kakaoAccount;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
