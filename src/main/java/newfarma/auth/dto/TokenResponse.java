package newfarma.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse (
    @JsonProperty("access_token")
    String AccessToken,
    @JsonProperty("refresh_token")
    String refreshToken
){}
