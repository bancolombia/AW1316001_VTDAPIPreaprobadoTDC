package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import java.util.Map;

import com.bcol.vtd.lib.comunes.dto.DatosPersonales;

public class ParameterGenOtp {

	String sessionId;
	Map<String, String> propiedades;
	DatosPersonales datosAutenticacionFuerte;

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setPropiedades(Map<String, String> propiedades) {
		this.propiedades = propiedades;
	}

	public void setDatosAutenticacionFuerte(DatosPersonales datosAutenticacionFuerte) {
		this.datosAutenticacionFuerte = datosAutenticacionFuerte;
	}
}
