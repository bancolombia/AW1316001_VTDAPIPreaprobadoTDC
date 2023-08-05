package com.bcol.vtd.api.tarjetas.web.rest;

import com.bancolombia.vtd.api.tarjetas.facade.impl.PropertyService;
import com.bancolombia.vtd.api.tarjetas.parameter.ParameterUtil;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.GetParametersRequest;
import com.bancolombia.vtd.api.tarjetas.parameter.dto.GetParametersResponse;
import com.bcol.vtd.lib.comunes.dto.Errors;
import com.google.gson.Gson;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.FileNotFoundException;

@Path("parameters")
@RequestScoped
public class ParametersService {
	
	private static final String ENCODING = "UTF-8";
	
	@Inject
	PropertyService paramsService;
	
	@POST
	@Path("getParameterOffer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getParameterOffer(String json) throws FileNotFoundException {

		Response response = null;

		if (ParameterUtil.validateJsonOffer(json)) {

			Gson gson = new Gson();
			GetParametersRequest getParametrizationOffer = gson.fromJson(json, GetParametersRequest.class);

			GetParametersResponse getParametersResponse = null;

			getParametersResponse = paramsService.getParameterOffer(getParametrizationOffer.getData().get(0));

			response = Response.status(Status.OK).entity(getParametersResponse).build();

		} else {

			Errors error = ParameterUtil.getError(String.valueOf(Status.BAD_REQUEST.getStatusCode()), Status.BAD_REQUEST.getReasonPhrase(),
					Status.BAD_REQUEST.getReasonPhrase());
			response = Response.status(Status.BAD_REQUEST).entity(error).build();

		}

		return response;
	}

}
