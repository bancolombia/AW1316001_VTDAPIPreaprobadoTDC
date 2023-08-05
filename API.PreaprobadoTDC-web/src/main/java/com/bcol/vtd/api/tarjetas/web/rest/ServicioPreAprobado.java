package com.bcol.vtd.api.tarjetas.web.rest;

import com.bancolombia.vtd.api.tarjetas.parameter.dto.DescargaDocumento;
import com.bancolombia.vtd.api.tarjetas.service.DescargaDocumentoDelegado;
import com.bancolombia.vtd.api.tarjetas.service.DocumentoInfoTarjetaDelegate;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalTarjeta;
import com.bcol.vtd.lib.comunes.exception.ConectorClientException;
import com.bcol.vtd.lib.comunes.exception.ValidacionException;
import org.apache.commons.lang3.StringUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;


@Path("servicio")
@RequestScoped
public class ServicioPreAprobado extends AbstractController {

    @Context
    private SecurityContext securityContext;

    @Inject
    private DescargaDocumentoDelegado descargaDocumentoDelegado;

	@Inject
	private DocumentoInfoTarjetaDelegate documentoInfoTarjetaDelegate;

	@POST
	@Path("obtenerDocumentos")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
	public Response obtenerDocumentos(@Context HttpServletRequest request,
			VentaDigitalTarjeta ventaDigitalTarjeta) throws ConectorClientException, ValidacionException {
		String tokenApp= StringUtils.EMPTY;
		Response res;
		setPrincipalInfo(ventaDigitalTarjeta, securityContext);
		res = descargaDocumentoDelegado.obtenerCartaBienvenida(request, ventaDigitalTarjeta,
				ventaDigitalTarjeta.getInformacionTransaccion().getIdSesion(), tokenApp);
		return res;
	}

	@POST
	@Path("obtenerPdfInfoTarjeta")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON + ";charset=" + ENCODING)
	public Response obtenerPdfInfoTarjeta(@Context HttpServletRequest request,VentaDigitalTarjeta ventaDigitalTarjeta){
		setPrincipalInfo(ventaDigitalTarjeta, securityContext);
		DescargaDocumento pdf = documentoInfoTarjetaDelegate.createPdf(ventaDigitalTarjeta,
				ventaDigitalTarjeta.getInformacionTransaccion().getIdSesion());
		Response.ResponseBuilder response = Response.ok(pdf);
		return response.build();
	}
}
