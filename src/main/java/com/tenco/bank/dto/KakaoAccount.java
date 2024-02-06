
package com.tenco.bank.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import lombok.Data;

@Data
public class KakaoAccount {

    private Boolean profileNicknameNeedsAgreement;
    private Boolean profileImageNeedsAgreement; 
    private Profile profile;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
