package com.bancolombia.vtd.api.tarjetas.service.util;

public enum ConstantesEnvioCorreo {

	PARA("PARA"),
	NOMBRE_CLIENTE("NOMBRE_CLIENTE"),
	DOCUMENTO("DOCUMENTO"),
	CUPO_SOLICITADO("CUPO"),
	NOMBRE_PRODUCTO("PRODUCTO"),
	BENEFICIO("BENEFICIO"),
	GEOLOCALIZACIONES("GEOLOCALIZACIONES"),
	DIRECCION("DIRECCION"),
	SUCURSAL("SUCURSAL"),
	CUOTA("CUOTA"),
	DEBITO("DEBITO"),
	TIPOS_CUENTAS("TIPOS_CUENTAS"),
	NUMEROS_CUENTAS("NUMERO_CUENTAS"),
	INMEDIATO("INMEDIATO"),
	VALIDACION_PUNTOS("VALIDACION_PUNTOS"),
	PUNTO("PUNTO"),
	LIFEMILES("32422342"),
	LIFEMILES_MICROPYME("32342442"),
	AMEX_LIBRE("998"),
	MASTER_IDEAL("IDEAL"),

	AHORROS("31"),
	CUENTA_CORRIENTE("115")

	;
	
		public static final String IMAGENES = "IMAGENES_CORREO";
		public static final String ASUNTO_CORREO = "ASUENTO_CORREO";
		public static final String ASUNTO_TARJETA_CREDITO = "ASUNTO_CORREO_TDC";
		public static final String RUTA_PLANTILLA_CORREO = "PLANTILLA_ENVIADA";
		public static final String RUTA_PLANTILLA_CORREO_TDC = "PLANTILLA_ENVIADA_TDC";
		public static final String ARCHIVO1 = "ARCHIVO1_CORREO";
		public static final String enviarCorreo = "CORREO_EVIADO";
		public static final String NOMBRE_PLANTILLA_CORREO = "PLANTILLA_CORREO_NOMBRE";
		public static final String NOMBRE_PLANTILLA_PDF = "PLANTILLA_DESCARGADA";
		public static final String ENDPOINT_ENVIO_CORREO = "CORREO_ENVIADO_ENDPOINT";
		public static final String CODIGO_IMAGEN = "IMAGEN_CODIGO";
		public static final String FUENTES = "FUENTES";
	
	private final String value;
	
	private ConstantesEnvioCorreo(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
