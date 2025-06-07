package org.duckdns.petfinderapp.domain.publicdata.dto.request;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AdoptApiRequest(
	@NotBlank
	String serviceKey,
	String state,
	String updateDate,
	Integer numberOfRows
) {

	public static AdoptApiRequest forLatest(String serviceKey, Integer numberOfRows) {
		return AdoptApiRequest.builder()
			.serviceKey(serviceKey)
			.numberOfRows(numberOfRows)
			.build();
	}

	public static AdoptApiRequest forState(String serviceKey, String state, Integer numberOfRows) {
		return AdoptApiRequest.builder()
			.serviceKey(serviceKey)
			.state(state)
			.updateDate(
				LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd")))
			.numberOfRows(numberOfRows)
			.build();
	}
}
