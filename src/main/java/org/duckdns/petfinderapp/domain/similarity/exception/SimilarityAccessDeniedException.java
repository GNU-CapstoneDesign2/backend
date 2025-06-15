package org.duckdns.petfinderapp.domain.similarity.exception;

import org.duckdns.petfinderapp.global.error.exception.AccessDeniedGroupException;

public class SimilarityAccessDeniedException  extends AccessDeniedGroupException {

  protected SimilarityAccessDeniedException(String message) {
    super(message);
  }

  public static SimilarityAccessDeniedException accessDenied() {
    return new SimilarityAccessDeniedException("해당 유사도 정보에 접근할 수 없습니다.");
  }
}
