package com.bancolombia.vtd.api.tarjetas.service;

import com.bancolombia.vd.api.tarjetas.exception.TDCException;
import com.bancolombia.vtd.api.tarjetas.facade.impl.PropertyService;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.DescargaDocumento;
import com.bancolombia.vtd.api.tarjetas.service.connector.TDConnector;
import com.bancolombia.vtd.api.tarjetas.service.util.ConstantesTDC;
import com.bancolombia.vtd.api.tarjetas.service.util.PlantillasUtil;
import com.bancolombia.vtd.api.tarjetas.service.util.TarjetaServiceUtil;
import com.bcol.vtd.lib.comunes.dto.InformacionTarjeta;
import com.bcol.vtd.lib.comunes.dto.VentaDigitalTarjeta;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class DocumentoInfoTarjetaDelegate {

    private VentaDigitalTarjeta ventaDigitalTarjeta;

    private InformacionTarjeta informacionTarjeta;

    private final String path;

    private final String pathImages;

    private final String calendarShoppingDays;

    private final List<String> images;

    private final PropertyService propertyService;

    private TDConnector connector;

    @Inject
    public DocumentoInfoTarjetaDelegate(PropertyService propertyService, TDConnector connector){
        this.propertyService = propertyService;
        this.path = propertyService.getProperty(ConstantesTDC.ENVIAR_CORREO.toString()) + File.separator;
        this.pathImages = this.path + propertyService.getProperty(ConstantesTDC.USO_INMEDIATO.toString()) + File.separator;
        this.calendarShoppingDays = propertyService.getProperty(ConstantesTDC.COMPRAS_INTERNET.toString());
        this.images = buildHTMLImages();
        this.connector = connector;
    }

    public DescargaDocumento createPdf(VentaDigitalTarjeta ventaDigitalTarjeta, String sessionId) {
        String templatePath = propertyService.getProperty(ConstantesTDC.DESCARGAR_PDF.toString());
        this.ventaDigitalTarjeta = ventaDigitalTarjeta;
        this.informacionTarjeta = !this.ventaDigitalTarjeta.getListaInformacionTarjeta().isEmpty() ?
                this.ventaDigitalTarjeta.getListaInformacionTarjeta().get(0) : new InformacionTarjeta();
        String requestedMoney = "$" + TarjetaServiceUtil.setThousandsSeparator(this.informacionTarjeta.getCupoSolicitado());
        VelocityEngine velocityEngine = PlantillasUtil.getVelocityEngine(this.path);
        velocityEngine.init();
        Template t = velocityEngine.getTemplate(templatePath);
        VelocityContext context = new VelocityContext();
        context.put("numeroTarjeta", this.ventaDigitalTarjeta.getInformacionTarjeta().getNumeroTarjeta());
        context.put("fechaVencimiento", this.ventaDigitalTarjeta.getInformacionTarjeta().getFechaVencimientoTarjeta());
        context.put("tipoTarjeta", this.informacionTarjeta.getNombreSubproducto());
        context.put("cupoDisponible", requestedMoney);
        context.put("dias", this.calendarShoppingDays);
        context.put("IMAGES", this.images);
        StringWriter writer = new StringWriter();
        t.merge(context, writer);
        DescargaDocumento descargaDocumento = new DescargaDocumento();

        try(ByteArrayOutputStream pdfByteArray = generatePdf(writer.toString())) {
            descargaDocumento.setFile(Base64.getEncoder().encodeToString(pdfByteArray.toByteArray()));
        } catch (IOException | DocumentException e) {
            TDCException tdcException = new TDCException(
                    ConstantesTDC.FUNCIONAL_DESCARGA.toString(),
                    ConstantesTDC.VALIDACION_GENERAL.toString(),
                    sessionId,
                    connector
            );
            descargaDocumento.setErrorCode(tdcException.getRespuestaServicio().getCodigo());
            descargaDocumento.setErrorDescription(tdcException.getRespuestaServicio().getDescripcion());
        }
        return descargaDocumento;
    }

    private ByteArrayOutputStream generatePdf(String html) throws IOException, DocumentException {
        PdfWriter pdfWriter;
        byte[] clientDocumentPdfPass = this.ventaDigitalTarjeta.getDatosPersonales().getNumeroDocumento() != null ?
                this.ventaDigitalTarjeta.getDatosPersonales().getNumeroDocumento().getBytes() : null;
        String cardSubProduct = this.informacionTarjeta.getNombreSubproducto();
        Document document = new Document();
        document.addAuthor("Bancolombia");
        document.addCreationDate();
        document.addProducer();
        document.addCreator("Grupo Bancolombia");
        document.addTitle("Tarjeta de Crédito " + cardSubProduct);
        document.setPageSize(PageSize.LETTER);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        pdfWriter = PdfWriter.getInstance(document, baos);
        pdfWriter.setEncryption(clientDocumentPdfPass, clientDocumentPdfPass, PdfWriter.ALLOW_COPY, PdfWriter.STANDARD_ENCRYPTION_40);
        pdfWriter.createXmpMetadata();

        document.open();

        XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();
        xmlWorkerHelper.getDefaultCssResolver(true);
        xmlWorkerHelper.parseXHtml(pdfWriter, document, new StringReader(html));

        document.close();
        return baos;
    }

    private List<String> buildHTMLImages(){
        return Arrays.asList(
            this.pathImages + "logo_Header.jpg",
            this.pathImages + "banner.jpg",
            this.pathImages + "icon_01.png",
            this.pathImages + "icon_02.png",
            this.pathImages + "icon_03.png",
            this.pathImages + "icon_04.png",
            this.pathImages + "icon_05.png",
            this.pathImages + "legal.png",
            this.pathImages + "logo.jpg",
            this.pathImages + "fb.jpg",
            this.pathImages + "tw.jpg",
            this.pathImages + "link.jpg",
            this.pathImages + "instragram.jpg",
            this.pathImages + "youtube.jpg"
        );
    }
}
