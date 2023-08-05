package com.bancolombia.vtd.api.tarjetas.service.util;

import org.apache.commons.lang3.StringUtils;

public enum ConstantesTDC {

    COD_PRODUCTO("210012"),
    API_TDC("TDC"),

    URL_JSON(StringUtils.EMPTY),

    TARJETAS_CREDITO("TARJETADECREDITO"),

    CORREO_RUTA("CORREO_RUTA"),
    ENVIAR_CORREO("ENVIAR_CORREO"),
    NOMBRE_PLANTILLA("NOMBRE_PLANTILLA"),
    DESCARGAR_PDF("DESCARGAR_PDF"),
    USO_INMEDIATO("USO_INMEDIATO"),
	COMPRAS_INTERNET("COMPRAS_INTERNET"),

	OBTENER_DOCUMENTOS("obtenerDocumentos"),

	ERROR_GENERAR("EGE"),
	PREFIJO_ERROR("PEGE"),
	VALIDACION_GENERAL("VAL"),
	FUNCIONAL_DESCARGA("10000211"),

    MECANISMO("Mecanismo"),

    CANAL("Canal"),
    MENSAJES("Mensajes"),
    GENERAR("Generar"),
    ACTIVACION_USO("Activación Uso"),
    TIEMPO_INACTIVACION("Tiempo Inactivación"),
    TIEMPO_ACTIVACION("Tiempo Activación"),
	INMEDIATE("Inmediate");


    private final String codigo;

    private ConstantesTDC(String codigo) {
        this.codigo = codigo;
    }

    public String toString() {
        return codigo;
    }
}
