package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import java.util.List;

public record SgisGeocodingResult(
	String totalcount,
	List<SgisGeocodingItem> resultdata,
	String matching,
	String pagenum,
	String returncount
) {
}
