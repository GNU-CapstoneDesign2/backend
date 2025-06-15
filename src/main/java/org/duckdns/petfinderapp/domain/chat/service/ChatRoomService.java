package org.duckdns.petfinderapp.domain.chat.service;

import lombok.RequiredArgsConstructor;
import org.duckdns.petfinderapp.domain.chat.dto.ChatRoomDto;
import org.duckdns.petfinderapp.domain.chat.dto.ChatRoomPostDto;
import org.duckdns.petfinderapp.domain.chat.dto.request.ChatRoomCreateResponse;
import org.duckdns.petfinderapp.domain.chat.dto.resposne.ChatRoomCreateRequest;
import org.duckdns.petfinderapp.domain.chat.entity.ChatMessage;
import org.duckdns.petfinderapp.domain.chat.entity.ChatRoom;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomConflictException;
import org.duckdns.petfinderapp.domain.chat.exception.ChatRoomNotFoundException;
import org.duckdns.petfinderapp.domain.chat.repository.ChatRoomRepository;
import org.duckdns.petfinderapp.domain.post.dto.response.PostSummaryResponse;
import org.duckdns.petfinderapp.domain.post.entity.PostCommon;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.post.repository.PostRepository;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.duckdns.petfinderapp.domain.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<ChatRoomDto> getChatRoomList(Long userId) {
        // DB에서 해당 사용자가 참여한 채팅방들을 가져온다
        List<ChatRoom> chatRooms = chatRoomRepository.findChatRoomsByUserIdOrderByLatest(userId);
        List<ChatRoomDto> result = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            // 현재 사용자가 sender면 상대방은 receiver, 반대면 sender
            User otherUser;
            if (chatRoom.getSender().getId().equals(userId)) {
                otherUser = chatRoom.getReceiver();
            } else {
                otherUser = chatRoom.getSender();
            }

            // 마지막 메시지 찾기
            String lastMessage;
            LocalDateTime lastMessageTime;

            if (chatRoom.getMessages().isEmpty()) {
                // 메시지가 하나도 없으면
                lastMessage = "대화를 시작해보세요";
                lastMessageTime = chatRoom.getCreateAt();
            } else {
                ChatMessage lastChatMessage = chatRoom.getMessages().get(chatRoom.getMessages().size() - 1);
                lastMessage = lastChatMessage.getContent();
                lastMessageTime = lastChatMessage.getCreateAt();
            }

            ChatRoomDto response = ChatRoomDto.builder()
                    .roomId(chatRoom.getId())
                    .senderName(otherUser.getName())
                    .senderProfile(otherUser.getImageUrl())
                    .lastMessage(lastMessage)
                    .lastMessageTime(lastMessageTime)
                    .build();

            result.add(response);
        }

        return result;
    }

    public ChatRoomPostDto getChatRoomPost(Long roomId, Long currentUserId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        if (!isParticipant(chatRoom, currentUserId)) {
            throw new IllegalArgumentException("해당 채팅방의 참여자가 아닙니다.");
        }

        PostSummaryResponse postSummary = PostSummaryResponse.of(chatRoom.getPost());
        return ChatRoomPostDto.from(chatRoom, postSummary, currentUserId);
    }

    private boolean isParticipant(ChatRoom chatRoom, Long userId) {
        return chatRoom.getSender().getId().equals(userId) ||
                chatRoom.getReceiver().getId().equals(userId);
    }

    public ChatRoomCreateResponse createChatRoom(User user, ChatRoomCreateRequest chatRoomCreateRequest) {
        Long postId = chatRoomCreateRequest.postId();
        // 채팅방이 이미 존재하는지
        chatRoomRepository.findChatRoomByPostId(postId).ifPresent(chatRoom -> {
            throw ChatRoomConflictException.ofChatRoomAlreadyExists();
        });
        // 존재하는 게시글인지
        PostCommon post = postRepository.findById(postId)
                .orElseThrow(ChatRoomNotFoundException::missingChatRoom);
        // 게시글 상태가 LOST 또는 SIGHT 인지
        if (post.getState() != PostState.LOST && post.getState() != PostState.SIGHT) {
            throw ChatRoomConflictException.ofInvalidPostState();
        }
        // 존재하는 사용자인지
        if (user == null) {
            throw UserNotFoundException.missingUser();
        }
        // 게시글 작성자와 채팅방 생성자가 동일한지
        if (post.getUser().getId().equals(user.getId())) {
            throw ChatRoomConflictException.ofCreatorSameAsPostAuthor();
        }

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.of(post, user, post.getUser());
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomCreateResponse.of(savedChatRoom);
    }
}
