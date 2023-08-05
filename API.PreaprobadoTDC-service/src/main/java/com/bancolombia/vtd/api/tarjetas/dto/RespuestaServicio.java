package com.bancolombia.vtd.api.tarjetas.dto;

import java.io.Serializable;

public class RespuestaServicio implements Serializable {

	private static final long serialVersionUID = -4846493914781589743L;
	
	private String codigo;
	
	private String descripcion;
	
	private String redirectUrl;
	
	private boolean redirectGliaScreen = true;

	
	public RespuestaServicio() {
		super();
	}

	public RespuestaServicio(String codigo) {
		super();
		this.codigo = codigo;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setRedirectGliaScreen(boolean redirectGliaScreen) {
		this.redirectGliaScreen = redirectGliaScreen;
	}
}
