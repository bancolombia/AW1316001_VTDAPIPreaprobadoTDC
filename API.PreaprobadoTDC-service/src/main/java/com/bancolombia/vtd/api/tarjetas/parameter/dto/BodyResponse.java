package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Objects;

public class BodyResponse   {
  private ParameterList parameters = null;

  public void setParameters(ParameterList parameters) {
    this.parameters = parameters;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BodyResponse bodyResponse = (BodyResponse) o;
    return Objects.equals(this.parameters, bodyResponse.parameters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(parameters);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BodyResponse {\n");
    
    sb.append("    parameters: ").append(toIndentedString(parameters)).append("\n");
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

