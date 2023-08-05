package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Objects;
import com.bcol.vtd.lib.comunes.dto.Errors;

public class DataResponse   {
  private String id = null;

  private String channel = null;

  private Errors status = null;

  private BodyResponse response = null;

  public void setId(String id) {
    this.id = id;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }


  public void setStatus(Errors status) {
    this.status = status;
  }

  public void setResponse(BodyResponse response) {
    this.response = response;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DataResponse dataResponse = (DataResponse) o;
    return Objects.equals(this.id, dataResponse.id) &&
        Objects.equals(this.channel, dataResponse.channel) &&
        Objects.equals(this.status, dataResponse.status) &&
        Objects.equals(this.response, dataResponse.response);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, channel, status, response);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DataResponse {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    response: ").append(toIndentedString(response)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

