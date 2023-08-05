
package com.bancolombia.vtd.api.preaprobadotdc.dto;

import java.util.Objects;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-02-12T21:07:33.062Z[GMT]")
public class MensajeFuncional {

	private String codigoFuncional = null;

	private String descripcionFuncional = null;

	public MensajeFuncional codigoFuncional(String codigoFuncional) {
		this.codigoFuncional = codigoFuncional;
		return this;
	}

	public String getCodigoFuncional() {
		return codigoFuncional;
	}

	public void setCodigoFuncional(String codigoFuncional) {
		this.codigoFuncional = codigoFuncional;
	}

	public MensajeFuncional descripcionFuncional(String descripcionFuncional) {
		this.descripcionFuncional = descripcionFuncional;
		return this;
	}

	public String getDescripcionFuncional() {
		return descripcionFuncional;
	}

	public void setDescripcionFuncional(String descripcionFuncional) {
		this.descripcionFuncional = descripcionFuncional;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MensajeFuncional mensajeFuncional = (MensajeFuncional) o;
		return Objects.equals(this.codigoFuncional, mensajeFuncional.codigoFuncional)
				&& Objects.equals(this.descripcionFuncional, mensajeFuncional.descripcionFuncional);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(codigoFuncional, descripcionFuncional);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MensajeFuncional {\n");

		sb.append("    codigoFuncional: ").append(toIndentedString(codigoFuncional)).append("\n");
		sb.append("    descripcionFuncional: ").append(toIndentedString(descripcionFuncional)).append("\n");
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
