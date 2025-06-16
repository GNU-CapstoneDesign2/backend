package org.duckdns.petfinderapp.domain.chat.dto.response;

import jakarta.validation.constraints.NotNull;

public record ReadMessageDto(
    @NotNull Long lastReadMessageId,
    @NotNull Long userId
) {
}
