package com.bvb.agroGenius.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({"success", "challenge_ts", "hostname", "error-codes"})
public class GoogleRecaptchaResponse {

  @JsonProperty("success")
  private boolean success;

  @JsonProperty("challenge_ts")
  private String challengeTs;

  @JsonProperty("hostname")
  private String hostname;

  @JsonProperty("error-codes")
  private String errorCodes;

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getChallengeTs() {
    return challengeTs;
  }

  public void setChallengeTs(String challengeTs) {
    this.challengeTs = challengeTs;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  public String getErrorCodes() {
    return errorCodes;
  }

  public void setErrorCodes(String errorCodes) {
	  this.errorCodes = errorCodes;
  }
}