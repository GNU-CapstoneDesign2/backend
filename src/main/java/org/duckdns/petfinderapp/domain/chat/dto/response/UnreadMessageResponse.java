package org.duckdns.petfinderapp.domain.chat.dto.response;

public record UnreadMessageResponse(
    int unreadCount
) {

  public static UnreadMessageResponse from(int unreadCount) {
    return new UnreadMessageResponse(unreadCount);
  }
}
