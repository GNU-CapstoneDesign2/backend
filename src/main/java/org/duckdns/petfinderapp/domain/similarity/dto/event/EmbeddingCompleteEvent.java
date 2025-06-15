package org.duckdns.petfinderapp.domain.similarity.dto.event;

import org.duckdns.petfinderapp.domain.post.enums.PostState;

public record EmbeddingCompleteEvent (
	Long postId,
	PostState postState
){
}
