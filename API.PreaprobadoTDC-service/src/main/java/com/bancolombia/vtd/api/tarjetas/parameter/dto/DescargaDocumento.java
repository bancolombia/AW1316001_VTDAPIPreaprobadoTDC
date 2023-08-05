package com.bancolombia.vtd.api.tarjetas.parameter.dto;

import lombok.Data;

@Data
public class DescargaDocumento {

	private String file;

	private String errorCode;

	private String errorDescription;
}
