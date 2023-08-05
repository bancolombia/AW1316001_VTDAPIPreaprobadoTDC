
package com.bancolombia.vtd.api.preaprobadotdc.dto;

import java.util.Objects;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-02-12T21:07:33.062Z[GMT]")
public class GetErrorByCodeRequest {

	private Header header = null;

	private String idAplicacion = null;

	private String codigoInterno = null;

	public GetErrorByCodeRequest header(Header header) {
		this.header = header;
		return this;
	}

	/**
	 * Get header
	 * 
	 * @return header
	 **/
	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public GetErrorByCodeRequest idAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
		return this;
	}


	public String getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public GetErrorByCodeRequest codigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
		return this;
	}


	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GetErrorByCodeRequest getErrorByCodeRequest = (GetErrorByCodeRequest) o;
		return Objects.equals(this.header, getErrorByCodeRequest.header) && Objects.equals(this.idAplicacion, getErrorByCodeRequest.idAplicacion)
				&& Objects.equals(this.codigoInterno, getErrorByCodeRequest.codigoInterno);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(header, idAplicacion, codigoInterno);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class GetErrorByCodeRequest {\n");

		sb.append("    header: ").append(toIndentedString(header)).append("\n");
		sb.append("    idAplicacion: ").append(toIndentedString(idAplicacion)).append("\n");
		sb.append("    codigoInterno: ").append(toIndentedString(codigoInterno)).append("\n");
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
