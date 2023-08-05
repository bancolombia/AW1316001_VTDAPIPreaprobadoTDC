
package com.bancolombia.vtd.api.preaprobadotdc.dto;

import java.util.Objects;


@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2019-02-12T21:07:33.062Z[GMT]")
public class Header {

	private String id = null;

	private String type = null;

	public Header id(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Header type(String type) {
		this.type = type;
		return this;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean equals(java.lang.Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Header header = (Header) o;
		return Objects.equals(this.id, header.id) && Objects.equals(this.type, header.type);
	}

	@Override
	public int hashCode() {
		return java.util.Objects.hash(id, type);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("class Header {\n");

		sb.append("    id: ").append(toIndentedString(id)).append("\n");
		sb.append("    type: ").append(toIndentedString(type)).append("\n");
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
