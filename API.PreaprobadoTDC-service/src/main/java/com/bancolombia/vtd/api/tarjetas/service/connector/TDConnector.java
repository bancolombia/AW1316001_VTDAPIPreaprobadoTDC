package com.bancolombia.vtd.api.tarjetas.service.connector;

import com.bancolombia.vtd.api.persistence.business.InformacionTransaccionBusiness;
import com.bancolombia.vtd.api.persistence.business.OfertaDigitalBusiness;
import com.bancolombia.vtd.api.persistence.business.TokenSesionBusiness;
import com.bancolombia.vtd.api.persistence.business.VentasDigitalesBusiness;
import com.bancolombia.vtd.api.preaprobadotdc.dto.GetErrorByCodeRequest;
import com.bancolombia.vtd.api.preaprobadotdc.dto.GetErrorsByIdAplicacionResponse;
import com.bancolombia.vtd.api.preaprobadotdc.dto.JsonApiGetErrorByCodeRequest;
import com.bancolombia.vtd.api.preaprobadotdc.dto.JsonApiGetErrorsResponse;
import com.bancolombia.vtd.api.preaprobadotdc.dto.MensajeError;
import com.bancolombia.vtd.api.preaprobadotdc.dto.MensajeFuncional;
import com.bancolombia.vtd.api.tarjetas.dto.RespuestaServicio;
import com.bancolombia.vtd.api.tarjetas.facade.impl.PropertyService;
import com.bancolombia.vtd.api.tarjetas.service.util.ConstantesTDC;
import com.bcol.vtd.lib.comunes.dto.VentaDigital;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalBD;
import com.bcol.vtd.lib.comunes.exception.ConectorClientException;
import com.bcol.vtd.lib.comunes.exception.ValidacionException;
import com.bcol.vtd.lib.comunes.util.CodigosRespuestaServicios;
import com.bcol.vtd.lib.comunes.util.ConstanteGeneral;
import com.bcol.vtd.lib.comunes.util.Constantes;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

public class TDConnector {

    private static final Logger logger = LogManager.getLogger(TDConnector.class);

    @Inject
    VentasDigitalesBusiness ventasDigitalesBusiness;

    @Inject
    TokenSesionBusiness tokenSesionBusiness;

    @Inject
    OfertaDigitalBusiness ofertaDigitalBusiness;

    @Inject
    PropertyService parameterService;

    @Inject
    InformacionTransaccionBusiness infoTransaccionBussines;

    private Client client = null;

    public TDConnector(Client client, PropertyService parameterService,
                       VentasDigitalesBusiness ventasDigitalesBusiness, TokenSesionBusiness tokenSesionBusiness,
                       OfertaDigitalBusiness ofertaDigitalBusiness, InformacionTransaccionBusiness infoTransaccionBussines) {
        this.client = client;
        this.parameterService = parameterService;
        this.ventasDigitalesBusiness = ventasDigitalesBusiness;
        this.tokenSesionBusiness = tokenSesionBusiness;
        this.ofertaDigitalBusiness = ofertaDigitalBusiness;
        this.infoTransaccionBussines = infoTransaccionBussines;
    }

    public void setVentaDigitalTarjetaBD(VentaDigitalBD ventaDigitalBD) throws ConectorClientException {
        try {
            ventasDigitalesBusiness.setVentaDigitalTarjeta(ventaDigitalBD);
        } catch (ConectorClientException cce) {
            throw cce;
        }
    }

    public GetErrorsByIdAplicacionResponse errorService(GetErrorByCodeRequest errorRequestDTO)
            throws ConectorClientException {
        GetErrorsByIdAplicacionResponse errorResponseDTO = null;
        if (errorRequestDTO != null) {
            try {
                JsonApiGetErrorByCodeRequest request = new JsonApiGetErrorByCodeRequest();
                request.addDataItem(errorRequestDTO);
                String url = parameterService.getProperty(ConstantesTDC.MENSAJES.toString());
                WebResource resource = client.resource(url);
                JsonApiGetErrorsResponse response = resource.type(MediaType.APPLICATION_JSON)
                        .post(JsonApiGetErrorsResponse.class, request);
                errorResponseDTO = response.getData().get(0);
            } catch (Exception e) {
                return defaultErrorResponse(errorRequestDTO.getCodigoInterno());
            }
        }
        return errorResponseDTO;
    }

    private GetErrorsByIdAplicacionResponse defaultErrorResponse(String codigoError) {

        MensajeFuncional mensajeFuncional = new MensajeFuncional();
        mensajeFuncional.setCodigoFuncional(codigoError);

        MensajeError mensajeError = new MensajeError();
        mensajeError.addMensajeFuncionalItem(mensajeFuncional);

        GetErrorsByIdAplicacionResponse errorResponseDTO = new GetErrorsByIdAplicacionResponse();
        errorResponseDTO.addMensajeErrorItem(mensajeError);

        return errorResponseDTO;
    }

    public VentaDigital consultarDatosCliente(String idSesion) throws ConectorClientException, ValidacionException {
        VentaDigitalBD ventaDigitalDB = null;
        VentaDigital ventaDigital = null;
        RespuestaServicio respuestaServicio = null;
        try {
            validarObjectoEntradaConsultarDatosCliente(idSesion);
            ThreadContext.put(ConstanteGeneral.SESSION_ID.getValor(), idSesion);
            ventaDigitalDB = ventasDigitalesBusiness.getClientePorIdSesionPasoFuncional(idSesion, Constantes.pagina2aFPTDC);

            ventaDigital = validarVentaDigital(ventaDigitalDB);
            ventaDigital.setInformacionTransaccion(null);
            ventaDigital.setListaProductosPreaprobados(null);
            ventaDigital.setInformacionDispositivo(null);
            ventaDigital.getDatosPersonales().setAceptoPagare(null);
            ventaDigital.getDatosPersonales().setVerPagare(null);

        } catch (ValidacionException e) {

            respuestaServicio = new RespuestaServicio();
            respuestaServicio.setCodigo(e.getCodigo());
            throw new ValidacionException(e.getCause().getMessage(), null, e.getMessage());

        } catch (ConectorClientException e) {

            respuestaServicio = new RespuestaServicio();
            respuestaServicio.setCodigo(CodigosRespuestaServicios.BD001
                    .getCodigo());
            throw new ConectorClientException(e.getCause().getMessage(), null, e.getMessage());
        } catch (Exception e) {
            respuestaServicio = new RespuestaServicio();
            respuestaServicio.setCodigo(CodigosRespuestaServicios.BD001
                    .getCodigo());
            throw new ConectorClientException(e.getCause().getMessage(), null, e.getMessage());
        }
        return ventaDigital;
    }

    public void validarObjectoEntradaConsultarDatosCliente(
            String idSesion)
            throws ValidacionException {

        if (idSesion == null || idSesion.isEmpty()) {
            throw new ValidacionException("Error validando Objecto EntradaConsultarDatosSolicitud ", null, null);
        }


    }

    public VentaDigital validarVentaDigital(VentaDigitalBD ventaDigitalDB) {

        if (null == ventaDigitalDB || null == ventaDigitalDB.getVentaDigital()) {
            return null;
        }

        return ventaDigitalDB.getVentaDigital();
    }

    private void validarObjectoEntradaConsultarDatosSolicitud(
            String idSesion)
            throws ValidacionException {

        if (idSesion == null || idSesion.isEmpty()) {
            throw new ValidacionException(
                    "Error validando Objecto EntradaConsultarDatosSolicitud ", null, null);
        }
    }

    private void validarObjectoEntradaConsultarInformacionVisita(
            String idSesion)
            throws ValidacionException {

		if (idSesion == null || idSesion.isEmpty()) {
			throw new ValidacionException(
					"Error validando Objecto EntradaConsultarInformacionVisita ", null,null);
		} 
	}

}
