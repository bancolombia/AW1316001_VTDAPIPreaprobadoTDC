package com.bancolombia.vtd.api.tarjetas.facade.impl;


import com.bancolombia.vtd.api.tarjetas.parameter.ParameterUtil;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.BodyResponse;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.Data;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.DataListResponse;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.DataResponse;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.GetParametersResponse;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.OfferJson;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.Parameter;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.ParameterList;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.PropertiesFile;
import com.bancolombia.vtd.api.tarjetas.service.util.ConstantesTDC;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.ws.rs.core.Response.Status;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class PropertyService {
	
	private static AtomicBoolean automaticUpdate = new AtomicBoolean(true);
	private static AtomicBoolean watcherStarted = new AtomicBoolean(false);
	
	private static final Logger LOGGER = LogManager.getLogger();
	
	private final static String PATH_PROPERTIES_DEFAULT = StringUtils.EMPTY;
	private final static String PATH_SECURITY_PROPERTY_DEFAULT = StringUtils.EMPTY;
	
	private final static String PATH_PROPERTIES = StringUtils.EMPTY;
	private final static String PATH_SECURITY_PROPERTY = StringUtils.EMPTY;

	private static PropertiesFile properties;
	private static PropertiesFile securityProperties;
	private static List<PropertiesFile> propsFiles = new ArrayList<>();
	
	static {
		String propsFileName = System.getProperty(PATH_PROPERTIES) != null 
				? System.getProperty(PATH_PROPERTIES)
				: PATH_PROPERTIES_DEFAULT;
		String secPropsFileName = System.getProperty(PATH_SECURITY_PROPERTY) != null
				? System.getProperty(PATH_SECURITY_PROPERTY)
				: PATH_SECURITY_PROPERTY_DEFAULT;

		try {
			properties = new PropertiesFile(propsFileName, readJsonFile(propsFileName));
			securityProperties = new PropertiesFile(secPropsFileName, readJsonFile(secPropsFileName));
		} catch (IOException e) {
			LOGGER.error(e.getMessage());
		}

		propsFiles.add(properties);
		propsFiles.add(securityProperties);
	}
	
	public PropertyService() {
		if (!watcherStarted.get() && automaticUpdate.get()) {
			Runnable loader = new WatchPropertiesPaths();
			ExecutorService pool = Executors.newSingleThreadExecutor();
			pool.submit(loader);
		}
	}

	private class WatchPropertiesPaths implements Runnable {

		private Set<Path> watchDirs = new HashSet<>();

		@Override
		public void run() {
			for (PropertiesFile propertiesFile : propsFiles) {
				String filePath = propertiesFile.getFilePath();
				final Path watchDir = FileSystems.getDefault().getPath(filePath).getParent();
				if (!watchDirs.contains(watchDir)) {
					watchDirs.add(watchDir);
					try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
						watchDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);
						watcherStarted.set(true);
						while (automaticUpdate.get()) {
							final WatchKey wk = watchService.take();
							for (WatchEvent<?> event : wk.pollEvents()) {
								String modifiedFile = watchDir.resolve((Path) event.context()).toString();
								checkAndUpdateProperties(modifiedFile);
							}
							wk.reset();
						}
					} catch (Exception e) {
						LOGGER.error(e.getMessage());
					}
				}
			}
		}
	}
	
	private void checkAndUpdateProperties(String modifiedFile) throws IOException {
		Optional<PropertiesFile> propsFile = propsFiles.stream().filter(p -> p.getFilePath().equals(modifiedFile)).findAny();
		if (propsFile.isPresent()) {
			propsFile.get().setProperties(readJsonFile(modifiedFile));
		}
	}

	public String getProperty(String key) {
		String toReturn = null;
		if (properties.getProperties().containsKey(key)) {
			if(key.equals(ConstantesTDC.ACTIVACION_USO.toString())) {
				if (!isImmediateUseActive()) {
					return "false";
				}
			}
			return properties.getProperties().get(key);
		}
		return toReturn;
	}
	
	public Map<String, String> getProperties(){
		return properties.getProperties();
	}
	
	public Map<String, String> getSecurityProperties(){
		return securityProperties.getProperties();
	}


	public GetParametersResponse getParameterOffer(Data data) throws FileNotFoundException {

		ParameterList parameters = new ParameterList();
		GetParametersResponse getParametersResponse = new GetParametersResponse();
		DataResponse dataResponse = new DataResponse();
		BodyResponse bodyResponse = new BodyResponse();
		DataListResponse dataListResponse = new DataListResponse();
		OfferJson offerJson = new OfferJson();

		BufferedReader br = new BufferedReader(new FileReader(getProperty(ConstantesTDC.URL_JSON.toString())));
		offerJson = new Gson().fromJson(br, OfferJson.class);

		for (int i = 0; i < offerJson.getParameterOffers().size(); i++) {

			Parameter parameter = new Parameter();
			parameter.setCodigoImagen(offerJson.getParameterOffers().get(i).getCodigoImagen());
			parameter.setFranquicia(offerJson.getParameterOffers().get(i).getFranquicia());
			parameter.setMontoInferior(offerJson.getParameterOffers().get(i).getMontoInferior());
			parameter.setMontoSuperior(offerJson.getParameterOffers().get(i).getMontoSuperior());

			parameters.add(i, parameter);
		}

		dataResponse = ParameterUtil.getHeaderResponse(data, String.valueOf(Status.OK.getStatusCode()), Status.OK.getReasonPhrase(),
				Status.OK.getReasonPhrase());

		bodyResponse.setParameters(parameters);
		dataResponse.setResponse(bodyResponse);
		dataListResponse.add(0, dataResponse);

		getParametersResponse.setData(dataListResponse);

		return getParametersResponse;
	}
	
	private static Map<String, String> readJsonFile(String path) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, String> jsonMap = null;
		jsonMap = mapper.readValue(new File(path), new TypeReference<Map<String, String>>(){});

		return jsonMap;
	}

	public Boolean isImmediateUseActive(){

		LocalTime currentTime = LocalTime.now();
		LocalTime inactiveTime = LocalTime.parse(getProperty(ConstantesTDC.TIEMPO_INACTIVACION.toString()));
		LocalTime activeTime = LocalTime.parse(getProperty(ConstantesTDC.TIEMPO_ACTIVACION.toString()));

		return currentTime.compareTo(activeTime)>=0 && currentTime.compareTo(inactiveTime)<=0;
	}
}
