package org.duckdns.petfinderapp.domain.chat.dto.response;

public record UnreadMessageResponse(
    Integer unreadCount
) {

  public static UnreadMessageResponse from(int unreadCount) {
    return new UnreadMessageResponse(unreadCount);
  }
}
