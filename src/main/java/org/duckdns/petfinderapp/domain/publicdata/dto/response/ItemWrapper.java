package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import java.util.List;

public record ItemWrapper<T>(
	List<T> item
) {
}
