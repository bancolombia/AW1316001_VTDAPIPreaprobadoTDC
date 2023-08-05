package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Objects;

public class Parameter   {
  private String franquicia = null;

  private String montoInferior = null;

  private String montoSuperior = null;

  private String codigoImagen = null;

  public Parameter franquicia(String franquicia) {
    this.franquicia = franquicia;
    return this;
  }

  public String getFranquicia() {
    return franquicia;
  }

  public void setFranquicia(String franquicia) {
    this.franquicia = franquicia;
  }

  public Parameter montoInferior(String montoInferior) {
    this.montoInferior = montoInferior;
    return this;
  }

  public String getMontoInferior() {
    return montoInferior;
  }

  public void setMontoInferior(String montoInferior) {
    this.montoInferior = montoInferior;
  }

  public Parameter montoSuperior(String montoSuperior) {
    this.montoSuperior = montoSuperior;
    return this;
  }

  public String getMontoSuperior() {
    return montoSuperior;
  }

  public void setMontoSuperior(String montoSuperior) {
    this.montoSuperior = montoSuperior;
  }

  public Parameter codigoImagen(String codigoImagen) {
    this.codigoImagen = codigoImagen;
    return this;
  }

  public String getCodigoImagen() {
    return codigoImagen;
  }

  public void setCodigoImagen(String codigoImagen) {
    this.codigoImagen = codigoImagen;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Parameter parameter = (Parameter) o;
    return Objects.equals(this.franquicia, parameter.franquicia) &&
        Objects.equals(this.montoInferior, parameter.montoInferior) &&
        Objects.equals(this.montoSuperior, parameter.montoSuperior) &&
        Objects.equals(this.codigoImagen, parameter.codigoImagen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(franquicia, montoInferior, montoSuperior, codigoImagen);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Parameter {\n");
    
    sb.append("    franquicia: ").append(toIndentedString(franquicia)).append("\n");
    sb.append("    montoInferior: ").append(toIndentedString(montoInferior)).append("\n");
    sb.append("    montoSuperior: ").append(toIndentedString(montoSuperior)).append("\n");
    sb.append("    codigoImagen: ").append(toIndentedString(codigoImagen)).append("\n");
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

