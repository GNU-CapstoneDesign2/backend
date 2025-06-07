package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "OpenAPI_ServiceResponse")
public record ErrorResponseWrapper(
	@JacksonXmlProperty(localName = "cmmMsgHeader")
	CmmMsgHeader cmmMsgHeader
) {
	public record CmmMsgHeader(
		@JacksonXmlProperty(localName = "errMsg")
		String errMsg,

		@JacksonXmlProperty(localName = "returnAuthMsg")
		String returnAuthMsg,

		@JacksonXmlProperty(localName = "returnReasonCode")
		int returnReasonCode
	) {
	}
}
