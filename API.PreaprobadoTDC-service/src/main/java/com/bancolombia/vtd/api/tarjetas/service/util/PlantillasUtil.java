package com.bancolombia.vtd.api.tarjetas.service.util;

import com.bcol.vtd.lib.comunes.dto.RespuestaServicioEnvioCorreo;
import com.bcol.vtd.lib.comunes.util.CodigosRespuestaServicios;
import com.bcol.vtd.lib.comunes.util.Parametro;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.ThreadContext;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PlantillasUtil {

	private List<Parametro> listaParametros = new ArrayList<Parametro>();

	private String rutaplantilla = null;
	private String plantilla = null;

	private List<String> imagen_tarjeta = null;

	private VelocityEngine velocityEngine = null;
	private VelocityContext context = null;
	private Template template = null;
	private StringWriter writer = null;

	private String imagenes[] = null;

	private byte[] pdf;

	private RespuestaServicioEnvioCorreo respuestaServicioEnvioCorreo;

	private Map<String, String> properties;

	public PlantillasUtil(List<Parametro> listaParametros, Map<String, String> properties, String idSesion) {
		if (idSesion != null) {
			ThreadContext.put("sessionId", idSesion);
		} else {
			ThreadContext.put("sessionId", "");
		}
		this.properties = properties;
		this.listaParametros = listaParametros;
		respuestaServicioEnvioCorreo = new RespuestaServicioEnvioCorreo();
		respuestaServicioEnvioCorreo.setEstadoRespuesta(true);

	}

	@SuppressWarnings("unchecked")
	public boolean createBodyHtml() {

		writer = new StringWriter();

		try {

			plantilla = properties.get(ConstantesEnvioCorreo.NOMBRE_PLANTILLA_CORREO) != null
					? properties.get(ConstantesEnvioCorreo.NOMBRE_PLANTILLA_CORREO).toString()
					: StringUtils.EMPTY;

			context = new VelocityContext();

			for (Parametro param : listaParametros) {

				context.put(param.getClave(), param.getValor());

				if (param.getObject() != null) {
					context.put(param.getClave(), param.getObject());
				}
				if (param.getClave().equals(ConstantesEnvioCorreo.IMAGENES)) {
					imagenes = param.getValor().split(",");
				}
				if (param.getClave().equals(ConstantesEnvioCorreo.CODIGO_IMAGEN)) {
					imagen_tarjeta = ((ArrayList<String>) param.getObject());
				}
				if (param.getClave().equals(ConstantesEnvioCorreo.RUTA_PLANTILLA_CORREO)) {
					rutaplantilla = properties.get(param.getValor());
					context.put("RUTA_IMG", rutaplantilla);
				}
				if (param.getClave().equals(ConstantesEnvioCorreo.NOMBRE_PLANTILLA_PDF)) {
					plantilla = "pdf_" + plantilla;
				}

			}

			velocityEngine = getVelocityEngine(rutaplantilla);
			velocityEngine.init();

			try {
				template = velocityEngine.getTemplate(plantilla);
			} catch (ResourceNotFoundException | ParseErrorException e) {
				respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_002.getCodigo(),
						CodigosRespuestaServicios.DDOC_002.getDescripcion());
			}

			template.merge(context, writer);

			return true;

		} catch (ResourceNotFoundException | ParseErrorException | MethodInvocationException e) {
			respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_005.getCodigo(),
					CodigosRespuestaServicios.DDOC_005.getDescripcion());
			return false;
		}

	}


	public void createFile() {

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

			String fonts = properties.get(ConstantesEnvioCorreo.FUENTES);
			PdfWriter pdfWriter = new PdfWriter(baos);
			createBodyHtml();

			if (respuestaServicioEnvioCorreo.getEstadoRespuesta()) {
				String content = this.writer.toString();
				ConverterProperties properties = new ConverterProperties();
				FontProvider fontProvider = new DefaultFontProvider();
				fontProvider.addDirectory(fonts);
				properties.setFontProvider(fontProvider);
				HtmlConverter.convertToPdf(content, pdfWriter, properties);
				pdfWriter.flush();
				pdf = baos.toByteArray();
				pdfWriter.close();
			}
		} catch (IOException e) {
			respuestaServicioEnvioCorreo.setError(CodigosRespuestaServicios.DDOC_003.getCodigo(),
					CodigosRespuestaServicios.DDOC_003.getDescripcion());
		}

	}

	public byte[]  getFilePdf() {
		return pdf;
	}

	public RespuestaServicioEnvioCorreo getRespuestaServicioEnvioCorreo() {
		return this.respuestaServicioEnvioCorreo;
	}

	public static VelocityEngine getVelocityEngine(String templatePath){
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("input.encoding", StandardCharsets.UTF_8.name());
		velocityEngine.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
		velocityEngine.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		velocityEngine.setProperty("file.resource.loader.path", templatePath);
		velocityEngine.setProperty("file.resource.loader.cache", "false");
		velocityEngine.setProperty("file.resource.loader.modificationCheckInterval", "2");
		return velocityEngine;
	}
}
