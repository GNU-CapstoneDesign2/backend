package org.duckdns.petfinderapp.domain.post.exception;

import org.duckdns.petfinderapp.global.error.exception.NotFoundGroupException;

public class PostNotFoundException extends NotFoundGroupException {

  protected PostNotFoundException(String message) {
    super(message);
  }

  public static PostNotFoundException missingPostCommon(Long postId) {
    return new PostNotFoundException(postId + " 게시글이 존재하지 않습니다.");
  }

  public static PostNotFoundException missingPostCommon() {
    return new PostNotFoundException("해당 게시글이 존재하지 않습니다.");
  }
}

