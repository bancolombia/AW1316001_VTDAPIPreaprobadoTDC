package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Objects;

public class Data   {
  private String id = null;

  private String channel = null;

  public String getId() {
    return id;
  }

  public String getChannel() {
    return channel;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Data data = (Data) o;
    return Objects.equals(this.id, data.id) &&
        Objects.equals(this.channel, data.channel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, channel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Data {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    channel: ").append(toIndentedString(channel)).append("\n");
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

