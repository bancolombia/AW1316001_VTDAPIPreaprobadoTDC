package com.bancolombia.vtd.api.tarjetas.service.util;

import com.bancolombia.vtd.api.tarjetas.service.connector.TDConnector;
import com.bcol.vtd.lib.comunes.dto.DatosPersonales;
import com.bcol.vtd.lib.comunes.dto.InformacionTarjeta;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalBD;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalTarjeta;
import com.bcol.vtd.lib.comunes.dto.servicioOfertaDigital.Categoria;
import com.bcol.vtd.lib.comunes.dto.servicioOfertaDigital.Documentos;
import com.bcol.vtd.lib.comunes.dto.servicioOfertaDigital.Subproducto;
import com.bcol.vtd.lib.comunes.exception.ConectorClientException;
import com.bcol.vtd.lib.comunes.util.ConstanteGeneral;
import com.bcol.vtd.lib.comunes.util.Parametro;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;

public class TarjetaServiceUtil {

	private static final Logger logger = LogManager.getLogger();
	
	private static final Pattern X_FORWARDED_PATTERN = Pattern.compile("^[\\dA-F\\.:,\\h]+$", Pattern.CASE_INSENSITIVE);

	private static final String CAMPO_VALIDADO_CORRECTAMENTE = "VALIDACION";
	private static final  String ERROR_EN_VALIDACION = "ERROR VALIDACION";
	private static final String CAMPO_SIN_VALOR = "CAMPO SIN VALOR";

	private TarjetaServiceUtil() {
	}

	public static String getClientIP(HttpServletRequest requestContext) {
		String ipCliente = requestContext.getHeader(ConstanteGeneral.HEADER_X_FORWARDED_FOR.getValor());
		if (ipCliente == null || ConstanteGeneral.CADENA_VACIA.getValor().equals(ipCliente)) {
			ipCliente = requestContext.getRemoteAddr();
		}
		if (ipCliente != null && !X_FORWARDED_PATTERN.matcher(ipCliente).matches()) {
			ipCliente = "";
		}
		return (ipCliente != null) ? ipCliente.split(",")[0] : "";
	}

	public static void saveTransactionInformation(VentaDigitalTarjeta ventaDigitalTarjeta, String idSesion, String tokenApp, String ipcliente,
												  String pasoFuncional, DatosPersonales newDatosPersonales, TDConnector connector) throws ConectorClientException {
		VentaDigitalBD ventaDigitalBD;
		try {
			if (newDatosPersonales != null) {
				if (ventaDigitalTarjeta.getDatosPersonales() == null) {
					ventaDigitalTarjeta.setDatosPersonales(new DatosPersonales());
				}
				DatosPersonales datosPersonales = ventaDigitalTarjeta.getDatosPersonales();
				datosPersonales.setNumeroDocumento(newDatosPersonales.getNumeroDocumento());
				datosPersonales.setCorreoElectronico(newDatosPersonales.getCorreoElectronico());
				datosPersonales.setCorreoElectronicoAutenticacion(newDatosPersonales.getCorreoElectronicoAutenticacion());
				datosPersonales.setTelefonoCelular(newDatosPersonales.getTelefonoCelular());
				datosPersonales.setTelefonoCelularAutenticacion(newDatosPersonales.getTelefonoCelularAutenticacion());
			}
			ventaDigitalBD = new VentaDigitalBD();
			ventaDigitalBD.setVentaDigital(ventaDigitalTarjeta);
			ventaDigitalBD.setListaInformacionTarjeta(ventaDigitalTarjeta.getListaInformacionTarjeta());
			ventaDigitalBD.setInformacionTarjeta(ventaDigitalTarjeta.getInformacionTarjeta());
			connector.setVentaDigitalTarjetaBD(ventaDigitalBD);
		} finally {
			ventaDigitalBD = null;
		}
	}

	public static byte[] transformStringB64ToByte(String encodeStringB64) {
		byte[] byteArrayFile = Base64.getDecoder().decode(encodeStringB64);
		return byteArrayFile;
	}

	public static ByteArrayOutputStream mergePDFFiles(List<byte[]> listFiles) {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			PDFMergerUtility PDFmerger = new PDFMergerUtility();
			PDFmerger.setDestinationStream(baos);
			for (byte[] file : listFiles) {
				ByteArrayInputStream bais = new ByteArrayInputStream(file);
				PDFmerger.addSource(bais);
			}
			PDFmerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
			return baos;
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public static List<Parametro> getDocumentsParams(VentaDigitalTarjeta ventaDigitalTarjeta) {
		List<Parametro> listaParametros = new ArrayList<>();
		List<List<String>> listBeneficios = new ArrayList<>();
		List<String> codigosImagen = new ArrayList<>();
		List<String> nombresTarjetas = new ArrayList<>();
		List<String> cuposTarjetas = new ArrayList<>();
		DecimalFormat formatNum = new DecimalFormat("##");
		try {
			
			String correo = null;
			DatosPersonales datosPersonales = ventaDigitalTarjeta.getDatosPersonales();
			if (datosPersonales.getCorreoElectronicoAutenticacion() != null) {

				if (!datosPersonales.getCorreoElectronicoAutenticacion().isEmpty()) {
					correo = datosPersonales.getCorreoElectronicoAutenticacion();
				} else {
					correo = datosPersonales.getCorreoElectronico();
				}
			} else {
				correo = datosPersonales.getCorreoElectronico();
			}

			String nombre = datosPersonales.getNombreLargoCliente().trim().split(" ")[0];
			String idCodImg = "";
			int puntosLifeMiles = 10;
			ArrayList<com.bcol.vtd.lib.comunes.dto.servicioOfertaDigital.Producto> productList = ventaDigitalTarjeta
					.getSolicitud().getOfertaDigital().getOferta().getProducto();

			if (null != productList && !productList.isEmpty()
					&& null != ventaDigitalTarjeta.getListaInformacionTarjeta()) {

				for (Categoria category : productList.get(0).getCategoria()) {

					for (Subproducto subProducto : category.getSubproducto()) {

						for (InformacionTarjeta infoTarjeta : ventaDigitalTarjeta.getListaInformacionTarjeta()) {

							if (subProducto.getIdSubproducto().equals(infoTarjeta.getIdSubProducto())) {
								listBeneficios.add(subProducto.getListaCondiciones());
								codigosImagen.add(infoTarjeta.getCodigoImagen());
								nombresTarjetas.add(infoTarjeta.getNombreSubproducto());
								cuposTarjetas
									    .add(formatNum.format(Integer.parseInt(infoTarjeta.getCupoSolicitado())));
								idCodImg = infoTarjeta.getCodigoImagen();
							}
						}
					}
				}
			}
			int usoInmediato = 2;
			String tipoGrabacionTarjeta = ventaDigitalTarjeta.getInformacionTarjeta().getTipoGrabacion();
			if(tipoGrabacionTarjeta != null && tipoGrabacionTarjeta.equals(ConstantesTDC.INMEDIATE.toString())){
				usoInmediato = 10;
			}

			List<Documentos> docs = ventaDigitalTarjeta.getSolicitud().getOfertaDigital().getDocumentos();
			if (docs != null && !docs.isEmpty()) {
				Object documento = docs.get(0).getDocumento();
				listaParametros.add(new Parametro(ConstantesEnvioCorreo.ARCHIVO1, documento));
			}
			int geolocalizacion = 3;
			String direccionSucursal = "";
			String nombreSucursal = "";
			if (geolocalizacion == 8){
				direccionSucursal = "";
				nombreSucursal = "";
			}

			int debitoAutomatico = 1;
			String tipoCuenta = "";
			String numeroCuenta = "";
			InformacionTarjeta informacionTarjetaCuenta = ventaDigitalTarjeta.getListaInformacionTarjeta().get(0);
			if (!informacionTarjetaCuenta.getTipoCuentaDebitar().equals("0")){
				debitoAutomatico = 5;
				tipoCuenta = informacionTarjetaCuenta.getTipoCuentaDebitar();
				tipoCuenta = tipoCuenta.equals("S")?ConstantesEnvioCorreo.AHORROS.getValue()
									:ConstantesEnvioCorreo.CUENTA_CORRIENTE.getValue();
				numeroCuenta = informacionTarjetaCuenta.getNumeroCuenta();
				numeroCuenta = numeroCuenta.replaceAll(".(?=[\\w]{8})", "*");
			}

			List<String> listCondiciones = listBeneficios.get(0);
			String cuotaManejo = listCondiciones.get(listCondiciones.size()-1);
			String puntos = listCondiciones.get(listCondiciones.size()-2);;
			listCondiciones.remove(listCondiciones.size()-1);
			listCondiciones.remove(listCondiciones.size()-1);

			listBeneficios.remove(0);
			listBeneficios.add(listCondiciones);

			if (idCodImg.equals(ConstantesEnvioCorreo.LIFEMILES.getValue()) ||
					idCodImg.equals(ConstantesEnvioCorreo.LIFEMILES_MICROPYME.getValue()) ||
					idCodImg.equals(ConstantesEnvioCorreo.AMEX_LIBRE.getValue()) ||
					idCodImg.equals(ConstantesEnvioCorreo.MASTER_IDEAL.getValue()) ){
				puntosLifeMiles = 1;
			}

			listaParametros.add(new Parametro(ConstantesEnvioCorreo.PARA.getValue(), (Object)correo));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.NOMBRE_CLIENTE.getValue(), nombre));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.DOCUMENTO.getValue(), (Object)datosPersonales.getNumeroDocumento()));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.INMEDIATO.getValue(), usoInmediato));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.CUPO_SOLICITADO.getValue(), cuposTarjetas));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.NOMBRE_PRODUCTO.getValue(), nombresTarjetas));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.BENEFICIO.getValue(), listBeneficios));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.ASUNTO_CORREO, ConstantesEnvioCorreo.ASUNTO_TARJETA_CREDITO));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.RUTA_PLANTILLA_CORREO, ConstantesEnvioCorreo.RUTA_PLANTILLA_CORREO_TDC));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.GEOLOCALIZACIONES.getValue(), geolocalizacion));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.DIRECCION.getValue(), direccionSucursal));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.SUCURSAL.getValue(), nombreSucursal));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.CUOTA.getValue(), cuotaManejo));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.DEBITO.getValue(), debitoAutomatico));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.TIPOS_CUENTAS.getValue(), tipoCuenta));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.NUMEROS_CUENTAS.getValue(), numeroCuenta));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.INMEDIATO.getValue(), usoInmediato));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.VALIDACION_PUNTOS.getValue(), puntos));
			listaParametros.add(new Parametro(ConstantesEnvioCorreo.PUNTO.getValue(), puntosLifeMiles));
		} catch (Exception e) {
			logger.error(e);
		}
		return listaParametros;
	}


	public static String setThousandsSeparator(String number){
		DecimalFormat decimalFormatter = new DecimalFormat("##");
		Integer numberInteger = Integer.parseInt(number);
		return decimalFormatter.format(numberInteger);
	}

}
