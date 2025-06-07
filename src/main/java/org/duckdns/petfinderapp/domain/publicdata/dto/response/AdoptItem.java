package org.duckdns.petfinderapp.domain.publicdata.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.duckdns.petfinderapp.domain.post.entity.Adopt;
import org.duckdns.petfinderapp.domain.post.entity.Coordinates;
import org.duckdns.petfinderapp.domain.post.entity.Image;
import org.duckdns.petfinderapp.domain.post.enums.NeuterStatus;
import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

/**
 * 구조 동물 상세 정보 DTO (주요 필드만 포함)
 */
@Builder
public record AdoptItem(
	@NotBlank
	String desertionNo,   // 구조번호
	@NotBlank
	String age,           // 나이 (예: 2025(60일미만)(년생))
	@NotBlank
	String colorCd,       // 색상
	@NotBlank
	String sexCd,         // 성별 (M: 수컷, F: 암컷, Q: 미상)
	@NotBlank
	String neuterYn,      // 중성화 여부 (Y: 예, N: 아니오, U: 미상)
	@NotBlank
	String weight,        // 체중 (예: 0.11(Kg))
	@NotBlank
	String noticeSdt,     // 공고시작일(YYYYMMDD)
	@NotBlank
	String noticeEdt,     // 공고종료일(YYYYMMDD)
	@NotBlank
	String careNm,        // 보호소이름
	@NotBlank
	String careTel,       // 보호소전화번호
	@NotBlank
	String careRegNo,     // 보호소번호
	@NotBlank
	String happenDt,      // 접수일(YYYYMMDD)
	@NotBlank
	String happenPlace,   // 발견장소
	@NotNull
	PetType upKindNm,      // 축종명 (예: 고양이)
	@NotBlank
	String specialMark,   // 특징
	String popfile1,      // 이미지1 URL
	String popfile2,      // 이미지2 URL
	String popfile3,      // 이미지3 URL
	String popfile4,      // 이미지4 URL
	String popfile5,      // 이미지5 URL
	String popfile6,      // 이미지6 URL
	String popfile7,      // 이미지7 URL
	String popfile8,      // 이미지8 URL
	//        String upKindCd,      // 축종코드 (개: 417000, 고양이: 422400, 기타: 429900)
	//        String kindFullNm,    // 품종 ([축종] 품종명)
	//        String kindCd,        // 품종코드
	//        String kindNm,        // 품종명 (예: 한국 고양이)
	//        String noticeNo,      // 공고번호
	//        String processState,  // 상태 (예: 보호중)
	//        String careOwnerNm,   // 보호소대표자
	String careAddr,      // 보호장소
	String orgNm         // 관할기관
	//        String updTm          // 수정일(yyyy-mm-dd hh:mm:ss)
) {
	private static LocalDateTime parseToLocalDateTime(String dateStr) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd")).atStartOfDay();
	}

	public Adopt toAdopt(Coordinates coordinates) {
		Adopt adopt = Adopt.builder()
			.animalNum(desertionNo)
			.age(age)
			.color(colorCd)
			.gender(sexCd)
			.neuter(NeuterStatus.fromApiValue(neuterYn))
			.weight(weight != null ? Float.parseFloat(weight.replace("(Kg)", "").trim()) : null)
			.startDate(noticeSdt != null ? parseToLocalDateTime(noticeSdt) : null)
			.endDate(noticeEdt != null ? parseToLocalDateTime(noticeEdt) : null)
			.shelterName(careNm)
			.shelterPhone(careTel)
			.date(happenDt != null ? parseToLocalDateTime(happenDt) : null)
			.address(happenPlace)
			.coordinates(coordinates) // 좌표 정보는 API 응답에 포함되지 않음
			.petType(upKindNm) // 축종명은 PetType으로 변환 필요
			.content(specialMark)
			.state(parseToLocalDateTime(noticeEdt).isAfter(LocalDateTime.now())
				? PostState.NOTICE : PostState.ADOPT) // 상태 정보는 API 응답에 포함되지 않음
			.build();

		getImageUrls().stream()
			.map(Image::of)
			.forEach(image -> image.setCommon(adopt)); // Adopt와 연결

		return adopt;
	}

	public Adopt toAdopt(PostState postState, Coordinates coordinates) {
		Adopt adopt = Adopt.builder()
			.animalNum(desertionNo)
			.age(age)
			.color(colorCd)
			.gender(sexCd)
			.neuter(NeuterStatus.fromApiValue(neuterYn))
			.weight(weight != null ? Float.parseFloat(weight.replace("(Kg)", "").replace(",", ".").trim()) : null)
			.startDate(noticeSdt != null ? parseToLocalDateTime(noticeSdt) : null)
			.endDate(noticeEdt != null ? parseToLocalDateTime(noticeEdt) : null)
			.shelterName(careNm)
			.shelterPhone(careTel)
			.date(happenDt != null ? parseToLocalDateTime(happenDt) : null)
			.address(happenPlace)
			.coordinates(coordinates) // 좌표 정보는 API 응답에 포함되지 않음
			.petType(upKindNm) // 축종명은 PetType으로 변환 필요
			.content(specialMark)
			.state(postState) // 상태 정보는 API 응답에 포함되지 않음
			.build();

		getImageUrls().stream()
			.map(Image::of)
			.forEach(image -> image.setCommon(adopt)); // Adopt와 연결

		return adopt;
	}

	public List<String> getImageUrls() {
		return Stream.of(popfile1, popfile2, popfile3, popfile4,
				popfile5, popfile6, popfile7, popfile8)
			.filter(Objects::nonNull)       // null 제거
			.map(String::trim)              // 앞뒤 공백 제거
			.filter(s -> !s.isEmpty())      // 빈 문자열 제거
			.toList();                      // Java 16+: toList(), 그 이전 버전은 collect(Collectors.toList())
	}

}
