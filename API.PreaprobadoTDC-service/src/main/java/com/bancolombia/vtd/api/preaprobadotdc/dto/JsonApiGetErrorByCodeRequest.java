
package com.bancolombia.vtd.api.preaprobadotdc.dto;

import java.util.Objects;
import java.util.ArrayList;
import java.util.List;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-02-12T21:07:33.062Z[GMT]")
public class JsonApiGetErrorByCodeRequest {

	private List<GetErrorByCodeRequest> data = new ArrayList<GetErrorByCodeRequest>();

	public JsonApiGetErrorByCodeRequest data(List<GetErrorByCodeRequest> data) {
		this.data = data;
		return this;
	}

	public JsonApiGetErrorByCodeRequest addDataItem(GetErrorByCodeRequest dataItem) {
		this.data.add(dataItem);
		return this;
	}

	public List<GetErrorByCodeRequest> getData() {
		return data;
	}

	public void setData(List<GetErrorByCodeRequest> data) {
		this.data = data;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		JsonApiGetErrorByCodeRequest jsonApiGetErrorByCodeRequest = (JsonApiGetErrorByCodeRequest) o;
		return Objects.equals(this.data, jsonApiGetErrorByCodeRequest.data);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(data);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class JsonApiGetErrorByCodeRequest {\n");

		sb.append("    data: ").append(toIndentedString(data)).append("\n");
		sb.append("}");
		return sb.toString();
	}

	private String toIndentedString(java.lang.Object o) {
		if (o == null) {
			return "null";
		}
		return o.toString().replace("\n", "\n    ");
	}

}
