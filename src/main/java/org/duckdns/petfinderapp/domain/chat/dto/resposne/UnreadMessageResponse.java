package org.duckdns.petfinderapp.domain.chat.dto.resposne;

public record UnreadMessageResponse(
    Integer unreadCount
) {

  public static UnreadMessageResponse from(int unreadCount) {
    return new UnreadMessageResponse(unreadCount);
  }
}
