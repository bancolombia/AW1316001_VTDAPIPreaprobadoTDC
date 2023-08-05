package com.bancolombia.vtd.api.tarjetas.parameter;

import com.bancolombia.vtd.api.tarjetas.parameter.dto.Data;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.DataResponse;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.GetParametersRequest;
import com.bcol.vtd.lib.comunes.dto.Error;
import com.bcol.vtd.lib.comunes.dto.Errors;
import com.bcol.vtd.lib.comunes.dto.ErrorsList;
import com.bcol.vtd.lib.comunes.exception.ValidacionException;
import com.bcol.vtd.lib.comunes.util.CodigosRespuestaServicios;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;

public class ParameterUtil {
	

	public static DataResponse getHeaderResponse(Data data, String code, String description, String title) {

		DataResponse dataResponse = new DataResponse();

		dataResponse.setChannel(data.getChannel());
		dataResponse.setId(data.getId());
		dataResponse.setStatus(getError(code, description, title));

		return dataResponse;
	}

	public static Errors getError(String code, String description, String title) {

		Errors errors = new Errors();
		ErrorsList errorList = new ErrorsList();
		Error error = new Error();
		error.setDetail(description);
		error.setStatus(code);
		error.setTitle(title);
		errorList.add(error);
		errors.setErrors(errorList);

		return errors;
	}

	public static boolean validateJsonOffer(String json) {

		boolean response = false;

		if (json != null) {
			try {
				ObjectMapper mapper = new ObjectMapper();
				GetParametersRequest getParametersRequest = mapper.readValue(json, GetParametersRequest.class);

				validate(getParametersRequest);
				validateList(getParametersRequest.getData());

				response = true;
			} catch (ValidacionException | JsonProcessingException e) {
				response = false;
			}

		}

		return response;
	}


	public static void validate(Object objeto) throws ValidacionException {

		if (null == objeto) {
			return;
		}

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Object>> violations = validator.validate(objeto);

		if (null != violations && !violations.isEmpty()) {
			String errorMsj = StringUtils.EMPTY;
			for (ConstraintViolation<Object> violation : violations) {
				errorMsj += violation.getMessage() + ", ";
			}
			throw new ValidacionException(CodigosRespuestaServicios.VAL001.getCodigo(), null, CodigosRespuestaServicios.VAL001.getDescripcion());
		}

	}

	public static void validateList(List<?> objetos) throws ValidacionException {

		if (null == objetos || objetos.isEmpty()) {
			return;
		}

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();

		Set<ConstraintViolation<Object>> violations = null;

		for (Object object : objetos) {

			violations = validator.validate(object);

			if (null != violations && !violations.isEmpty()) {
				String errorMsj = StringUtils.EMPTY;
				for (ConstraintViolation<Object> violation : violations) {
					errorMsj += violation.getMessage() + ", ";
				}
				throw new ValidacionException(CodigosRespuestaServicios.VAL001.getCodigo(), null, CodigosRespuestaServicios.VAL001.getDescripcion());

			}
		}

	}
}
