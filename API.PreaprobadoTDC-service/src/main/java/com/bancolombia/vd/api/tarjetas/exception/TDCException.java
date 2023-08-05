/**
 * 
 */
package com.bancolombia.vd.api.tarjetas.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bancolombia.vtd.api.preaprobadotdc.dto.GetErrorByCodeRequest;
import com.bancolombia.vtd.api.preaprobadotdc.dto.GetErrorsByIdAplicacionResponse;
import com.bancolombia.vtd.api.preaprobadotdc.dto.Header;
import com.bancolombia.vtd.api.preaprobadotdc.dto.MensajeError;
import com.bancolombia.vtd.api.tarjetas.service.connector.TDConnector;
import com.bancolombia.vtd.api.tarjetas.service.util.ConstantesTDC;
import com.bcol.vtd.lib.comunes.bancolombia.dto.ExcepcionServicio;
import com.bancolombia.vtd.api.tarjetas.dto.RespuestaServicio;
import com.bcol.vtd.lib.comunes.exception.ConectorClientException;

public class TDCException extends Exception {

	private static final long serialVersionUID = 1685718626516818897L;
	
	private static final Logger logger = LogManager.getLogger();
	
	private String errorCode;
	private GetErrorsByIdAplicacionResponse errorMessage;
	private RespuestaServicio respuestaServicio;
	private ExcepcionServicio excepcionServicio;
	
	public TDCException(String errorCode, String serviceCode, String sessionId, TDConnector connector) {
		super();
		this.errorCode = errorCode;
		
		String internalCode = new StringBuilder(ConstantesTDC.COD_PRODUCTO.toString())
				.append("-")
				.append(serviceCode) 
				.append(errorCode)
				.toString();
		getErrorMessage(sessionId, internalCode, ConstantesTDC.COD_PRODUCTO.toString(), connector);
		
		respuestaServicio = new RespuestaServicio();

		if (errorMessage != null) {
			MensajeError msgError0 = errorMessage.getMensajeError().get(0);
			respuestaServicio.setCodigo(msgError0.getMensajeFuncional().get(0).getCodigoFuncional());
			respuestaServicio.setDescripcion(msgError0.getMensajeFuncional().get(0).getDescripcionFuncional());
			
			excepcionServicio = new ExcepcionServicio(msgError0.getCodigoError(), msgError0.getDescripcionTecnica());
			excepcionServicio.setTipoExcepcion(msgError0.getTipoExcepcion());
			excepcionServicio.setServicio(msgError0.getServicio());
			excepcionServicio.setOperacionServicio(msgError0.getOperacion());
			excepcionServicio.setCodigoFuncional(msgError0.getCodigoInterno());
			excepcionServicio.setDetalleFuncional(msgError0.getMensajeFuncional().get(0).getDescripcionFuncional());
		}
	}
	
	private void getErrorMessage(String idSesion, String codigoInterno, String idAplicacion, TDConnector connector) {

		Header header = new Header();
		header.setId(idSesion);
		header.setType(ConstantesTDC.API_TDC.toString());

		GetErrorByCodeRequest errorRequestDTO = new GetErrorByCodeRequest();

		errorRequestDTO.setHeader(header);
		errorRequestDTO.setCodigoInterno(codigoInterno);
		errorRequestDTO.setIdAplicacion(idAplicacion);

		try {
			errorMessage = connector.errorService(errorRequestDTO);
		} catch (ConectorClientException e) {
			logger.error("Error consultando el mensaje de error para: {}", codigoInterno, e);
		}
	}

	public RespuestaServicio getRespuestaServicio() {
		return respuestaServicio;
	}
}
