
package com.bancolombia.vtd.api.preaprobadotdc.dto;

import java.util.Objects;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-02-12T21:07:33.062Z[GMT]")
public class MensajeError {

	private String idAplicacion = null;

	private String codigoInterno = null;

	private String servicio = null;

	private String operacion = null;

	private String codigoError = null;

	private String tipoExcepcion = null;

	private String descripcionTecnica = null;

	private List<MensajeFuncional> mensajeFuncional = new ArrayList<MensajeFuncional>();

	public MensajeError idAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
		return this;
	}

	public String getIdAplicacion() {
		return idAplicacion;
	}

	public void setIdAplicacion(String idAplicacion) {
		this.idAplicacion = idAplicacion;
	}

	public MensajeError codigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
		return this;
	}

	public String getCodigoInterno() {
		return codigoInterno;
	}

	public void setCodigoInterno(String codigoInterno) {
		this.codigoInterno = codigoInterno;
	}

	public MensajeError servicio(String servicio) {
		this.servicio = servicio;
		return this;
	}


	public String getServicio() {
		return servicio;
	}

	public void setServicio(String servicio) {
		this.servicio = servicio;
	}

	public MensajeError operacion(String operacion) {
		this.operacion = operacion;
		return this;
	}

	public String getOperacion() {
		return operacion;
	}

	public void setOperacion(String operacion) {
		this.operacion = operacion;
	}

	public MensajeError codigoError(String codigoError) {
		this.codigoError = codigoError;
		return this;
	}

	public String getCodigoError() {
		return codigoError;
	}

	public void setCodigoError(String codigoError) {
		this.codigoError = codigoError;
	}

	public MensajeError tipoExcepcion(String tipoExcepcion) {
		this.tipoExcepcion = tipoExcepcion;
		return this;
	}


	public String getTipoExcepcion() {
		return tipoExcepcion;
	}

	public void setTipoExcepcion(String tipoExcepcion) {
		this.tipoExcepcion = tipoExcepcion;
	}

	public MensajeError descripcionTecnica(String descripcionTecnica) {
		this.descripcionTecnica = descripcionTecnica;
		return this;
	}

	public String getDescripcionTecnica() {
		return descripcionTecnica;
	}

	public void setDescripcionTecnica(String descripcionTecnica) {
		this.descripcionTecnica = descripcionTecnica;
	}

	public MensajeError mensajeFuncional(List<MensajeFuncional> mensajeFuncional) {
		this.mensajeFuncional = mensajeFuncional;
		return this;
	}

	public MensajeError addMensajeFuncionalItem(MensajeFuncional mensajeFuncionalItem) {
		this.mensajeFuncional.add(mensajeFuncionalItem);
		return this;
	}


	public List<MensajeFuncional> getMensajeFuncional() {
		return mensajeFuncional;
	}

	public void setMensajeFuncional(List<MensajeFuncional> mensajeFuncional) {
		this.mensajeFuncional = mensajeFuncional;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		MensajeError mensajeError = (MensajeError) o;
		return Objects.equals(this.idAplicacion, mensajeError.idAplicacion) && Objects.equals(this.codigoInterno, mensajeError.codigoInterno)
				&& Objects.equals(this.servicio, mensajeError.servicio) && Objects.equals(this.operacion, mensajeError.operacion)
				&& Objects.equals(this.codigoError, mensajeError.codigoError) && Objects.equals(this.tipoExcepcion, mensajeError.tipoExcepcion)
				&& Objects.equals(this.descripcionTecnica, mensajeError.descripcionTecnica)
				&& Objects.equals(this.mensajeFuncional, mensajeError.mensajeFuncional);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(idAplicacion, codigoInterno, servicio, operacion, codigoError, tipoExcepcion, descripcionTecnica,
				mensajeFuncional);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class MensajeError {\n");

		sb.append("    idAplicacion: ").append(toIndentedString(idAplicacion)).append("\n");
		sb.append("    codigoInterno: ").append(toIndentedString(codigoInterno)).append("\n");
		sb.append("    servicio: ").append(toIndentedString(servicio)).append("\n");
		sb.append("    operacion: ").append(toIndentedString(operacion)).append("\n");
		sb.append("    codigoError: ").append(toIndentedString(codigoError)).append("\n");
		sb.append("    tipoExcepcion: ").append(toIndentedString(tipoExcepcion)).append("\n");
		sb.append("    descripcionTecnica: ").append(toIndentedString(descripcionTecnica)).append("\n");
		sb.append("    mensajeFuncional: ").append(toIndentedString(mensajeFuncional)).append("\n");
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
