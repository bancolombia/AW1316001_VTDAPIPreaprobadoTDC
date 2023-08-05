package com.bancolombia.vtd.api.tarjetas.service;


import com.bancolombia.vtd.api.tarjetas.facade.impl.PropertyService;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.DescargaDocumento;
import com.bancolombia.vtd.api.tarjetas.service.connector.TDConnector;
import com.bancolombia.vtd.api.tarjetas.service.util.ConstantesTDC;
import com.bancolombia.vtd.api.tarjetas.service.util.HashUtil;
import com.bancolombia.vtd.api.tarjetas.service.util.PlantillasUtil;
import com.bancolombia.vtd.api.tarjetas.service.util.TarjetaServiceUtil;
import com.bcol.vtd.lib.comunes.dto.DatosPersonales;
import com.bcol.vtd.lib.comunes.dto.InformacionTransaccion;
import com.bcol.vtd.lib.comunes.dto.RespuestaServicioEnvioCorreo;
import com.bcol.vtd.lib.comunes.dto.VentaDigital;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalTarjeta;
import com.bcol.vtd.lib.comunes.exception.ConectorClientException;
import com.bcol.vtd.lib.comunes.exception.ValidacionException;
import com.bcol.vtd.lib.comunes.util.Constantes;
import com.bcol.vtd.lib.comunes.util.Parametro;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.io.ByteArrayOutputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class DescargaDocumentoDelegado {

	private static final Logger logger = LogManager.getLogger(DescargaDocumentoDelegado.class);
	
	private PropertyService parameterService;
	
	
	private TDConnector connector;
	
	private HashUtil hashUtil;
	

	public Response obtenerCartaBienvenida(HttpServletRequest requestContext, VentaDigitalTarjeta ventaDigitalTarjeta, String idSesion,
			String tokenApp) throws ConectorClientException, ValidacionException {
		String pasoFuncional = ConstantesTDC.OBTENER_DOCUMENTOS.toString();

		String solicitudCodDec = null;

		String ipCliente = TarjetaServiceUtil.getClientIP(requestContext);
		ByteArrayOutputStream archivoFinal = null;

		Response res;
		
		try {
			setInformacionTransaccion(ventaDigitalTarjeta, idSesion, tokenApp, ipCliente, pasoFuncional);
			solicitudCodDec = hashUtil.decode(ventaDigitalTarjeta.getSolicitud().getNumeroSolicitudVirtual());
			VentaDigital datosCliente = consultarDatosCliente(idSesion);
			ventaDigitalTarjeta.setDatosPersonales(datosCliente.getDatosPersonales());
			
			VentaDigitalTarjeta ventaDigitalClone= SerializationUtils.clone(ventaDigitalTarjeta);
			hashUtil.desEncriptarInformacionSensible(ventaDigitalClone);

			List<Parametro> listaParametros = TarjetaServiceUtil.getDocumentsParams(ventaDigitalClone); 
			listaParametros.add(new Parametro(ConstantesTDC.CORREO_RUTA.toString(), ConstantesTDC.ENVIAR_CORREO.toString()));
			listaParametros.add(new Parametro(ConstantesTDC.NOMBRE_PLANTILLA.toString(), ConstantesTDC.NOMBRE_PLANTILLA.toString()));

			byte[] cartaBienvenida = getCartaBienvenida(listaParametros, idSesion);
			
			String documento = "";
			documento = ventaDigitalTarjeta.getSolicitud().getOfertaDigital().getDocumentos().get(0).getDocumento();

			List<byte[]> listFiles = new ArrayList<>();
			listFiles.add(cartaBienvenida);
			listFiles.add(TarjetaServiceUtil.transformStringB64ToByte(documento));
			archivoFinal = TarjetaServiceUtil.mergePDFFiles(listFiles);
			byte[] fileContent = archivoFinal.toByteArray();

			DescargaDocumento descarga = new DescargaDocumento();
			descarga.setFile(Base64.getEncoder().encodeToString(fileContent));
			
			ResponseBuilder response = Response.ok(descarga);
			
			res= response.build();
			
		} catch ( IllegalBlockSizeException | BadPaddingException e) {
			res = Response.status(Response.Status.FORBIDDEN).build();
		}

			ventaDigitalTarjeta.getSolicitud().setNumeroSolicitudVirtual(solicitudCodDec);
			ventaDigitalTarjeta.getSolicitud().setEstadoId(Constantes.ESTADO_SOLICITUD_EN_PROCESO_ENTREGA);
			saveTransactionInformation(ventaDigitalTarjeta, idSesion, tokenApp, ipCliente, pasoFuncional, null);

		return res;
		
	}

	private byte[] getCartaBienvenida(List<Parametro> datosCartaBienvenida, String idSesion) {

		byte[] cartaBienvenida = null;
		RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = getDocumento(datosCartaBienvenida, idSesion);

		if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {

			Response.ResponseBuilder response = Response.ok((Object) respuestaServicioEnvioCorreo.getObjetoRespuesta());

			response.header("Content-Disposition", "attachment; filename=\"carta_bienvenida_pdf.pdf\"");

			cartaBienvenida = (byte[]) respuestaServicioEnvioCorreo.getObjetoRespuesta();

		} 
		
		return cartaBienvenida;
	}
	
	private RespuestaServicioEnvioCorreo getDocumento(List<Parametro> listaParametros, String idSesion) {

		RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo = null;

		PlantillasUtil plantillasUtil = new PlantillasUtil(listaParametros, parameterService.getProperties() , idSesion);

		plantillasUtil.createFile();

		respuestaServicioEnvioCorreo =  plantillasUtil.getRespuestaServicioEnvioCorreo();

		if (respuestaServicioEnvioCorreo.getEstadoRespuesta()){

			respuestaServicioEnvioCorreo.setObjetoRespuesta(plantillasUtil.getFilePdf());

		}

		return respuestaServicioEnvioCorreo;
	}
	
	private void setInformacionTransaccion(VentaDigitalTarjeta ventaDigitalTarjeta, String idSesion, String tokenApp, String ipCliente, String pasoFuncional) {
        if (null == ventaDigitalTarjeta.getInformacionTransaccion()) {
            ventaDigitalTarjeta.setInformacionTransaccion(new InformacionTransaccion());
        }
 
        ventaDigitalTarjeta.getInformacionTransaccion().setIdSesion(idSesion);
        ventaDigitalTarjeta.getInformacionTransaccion().setTokenApp(tokenApp);
        ventaDigitalTarjeta.getInformacionTransaccion().setIpCliente(ipCliente);
        ventaDigitalTarjeta.getInformacionTransaccion().setPasoFuncional(pasoFuncional);
        ventaDigitalTarjeta.getInformacionTransaccion().setIdAplicacion(Constantes.ID_APLICACION_PREAPROBADO);
        ventaDigitalTarjeta.getInformacionTransaccion().setCanal(parameterService.getProperty(ConstantesTDC.CANAL.toString()));
    }
	
	private void saveTransactionInformation(VentaDigitalTarjeta ventaDigitalTarjeta, String idSesion, String tokenApp,
			String ipCliente, String pasoFuncional, DatosPersonales datosPersonalesBackup)
			throws ConectorClientException {
		
		TarjetaServiceUtil.saveTransactionInformation(ventaDigitalTarjeta, idSesion, tokenApp, ipCliente, pasoFuncional,
				datosPersonalesBackup, connector);
	}
	
	private VentaDigital consultarDatosCliente(String idSesion) throws ConectorClientException, ValidacionException {
		LocalTime ltBegin = LocalTime.now(); 
		VentaDigital toReturn = connector.consultarDatosCliente(idSesion);
		return toReturn;
	}
	
	
}
