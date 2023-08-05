package com.bancolombia.vtd.api.tarjetas.service.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.bcol.vtd.lib.comunes.dto.VentaDigital;
import com.bcol.vtd.lib.comunes.util.ConstanteGeneral;
import com.bcol.vtd.lib.comunes.util.ConstanteNumerico;
import com.bcol.vtd.lib.comunes.util.Constantes;

public class HashUtil {
	
	private static Cipher cipher;
	private static Cipher descipher;
	private static Base64.Encoder encoder; 
	private static Base64.Decoder decoder;
	
	public HashUtil(Map<String, String> properties) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		if (cipher == null || descipher == null || encoder == null || decoder == null) {
			String phrase = properties.get(Constantes.FWK_FRASE_SEGURIDAD);
			encoder = Base64.getEncoder();
			decoder = Base64.getDecoder();
			
			Key aesKey = new SecretKeySpec(phrase.getBytes(), ConstanteGeneral.METODO_ENCRIPTACION.getValor());
			cipher = Cipher.getInstance(ConstanteGeneral.METODO_ENCRIPTACION.getValor());
			cipher.init(Cipher.ENCRYPT_MODE, aesKey);
			
			descipher = Cipher.getInstance(ConstanteGeneral.METODO_ENCRIPTACION.getValor());
			descipher.init(Cipher.DECRYPT_MODE, aesKey);
		}
	}
	
	public VentaDigital desEncriptarInformacionSensible(VentaDigital ventaDigital)
			throws IllegalBlockSizeException, BadPaddingException {
		return desEncriptarInformacionSensible(ventaDigital, false);
	}
	
	public VentaDigital desEncriptarInformacionSensible(VentaDigital ventaDigital, boolean enmascarar)
			throws IllegalBlockSizeException, BadPaddingException {

		if (ventaDigital.getDatosPersonales().getCorreoElectronico() != null) {
			String dato = decode(ventaDigital.getDatosPersonales().getCorreoElectronico());
			if (enmascarar) {
				dato = enmascarar(dato);
			}
			ventaDigital.getDatosPersonales().setCorreoElectronico(dato);
		}

		if (ventaDigital.getDatosPersonales().getCorreoElectronicoAutenticacion() != null) {
			String dato = decode(ventaDigital.getDatosPersonales().getCorreoElectronicoAutenticacion());
			if (enmascarar) {
				dato = enmascarar(dato);
			}
			ventaDigital.getDatosPersonales().setCorreoElectronicoAutenticacion(dato);
		}

		if (ventaDigital.getDatosPersonales().getTelefonoCelular() != null) {
			String dato = decode(ventaDigital.getDatosPersonales().getTelefonoCelular());
			if (enmascarar) {
				dato = enmascarar(dato);
			}
			ventaDigital.getDatosPersonales().setTelefonoCelular(dato);
		}

		if (ventaDigital.getDatosPersonales().getTelefonoCelularAutenticacion() != null) {
			String dato = decode(ventaDigital.getDatosPersonales().getTelefonoCelularAutenticacion());
			if (enmascarar) {
				dato = enmascarar(dato);
			}
			ventaDigital.getDatosPersonales().setTelefonoCelularAutenticacion(dato);
		}

		return ventaDigital;
	}

	public String decode(String encryptedString) throws IllegalBlockSizeException,
			BadPaddingException {
		byte [] decoded;
		synchronized (descipher) {
			decoded = decoder.decode(encryptedString);
		}
		String decrypted = new String(descipher.doFinal(decoded));
		return decrypted;
	}
	
	private static String enmascarar(String texto){ 

		String textoAux = texto;
		
		if (texto.contains(ConstanteGeneral.ARROBA.getValor())) {
			texto = texto.split(ConstanteGeneral.ARROBA.getValor())[ConstanteNumerico.CERO.getValor()];
		}
		
		int mitad = texto.length() / ConstanteNumerico.DOS.getValor();
		double porcentage = texto.length() * 0.3;
		double mitadSubcadena = porcentage / ConstanteNumerico.DOS.getValor();
		
		int rangoInicio = (int) (mitad - mitadSubcadena);
		int rangoFinal = (int) (mitad + mitadSubcadena);
		
		String subText = texto.substring(rangoInicio, rangoFinal);
		String cadenaEnmascaramiento = ConstanteGeneral.CADENA_VACIA.getValor();
		
		for (int i = 0; i < subText.length(); i++) {
			
			cadenaEnmascaramiento += ConstanteGeneral.ASTERISCO.getValor();
		}
		
		String textoFinal = ConstanteGeneral.CADENA_VACIA.getValor();
		
		if ((textoAux.contains(ConstanteGeneral.ARROBA.getValor()))) {
			
			textoFinal = textoAux.replace(subText, cadenaEnmascaramiento);
		} else {
			textoFinal = texto.replace(subText, cadenaEnmascaramiento);
		}
		
		return textoFinal;
	}
}
