
package com.tenco.bank.dto;

import java.util.LinkedHashMap;
import java.util.Map;
public class Profile {

	private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
    private Boolean isDefaultImage;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
