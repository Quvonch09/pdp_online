package com.example.pdponline.service;


import com.example.pdponline.entity.Task;
import com.example.pdponline.exception.RestException;
import com.example.pdponline.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.pdponline.entity.Chat;
import com.example.pdponline.entity.User;
import com.example.pdponline.entity.enums.Role;
import com.example.pdponline.exception.BadRequestException;
import com.example.pdponline.exception.NotFoundException;
import com.example.pdponline.payload.*;
import com.example.pdponline.repository.ChatRepository;
import com.example.pdponline.repository.UserRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final UserService userService;
    private final FileService fileService;
    private final TaskRepository taskRepository;


    public ApiResponse<?> onlineOffline(boolean isActive, User user) {
        userService.onlineOffline(user, isActive);
        return ApiResponse.successResponse("Success");
    }


    public List<ChatReadDto> isReadMessage(ChatIds chatIds) {
        List<ChatReadDto> list = new ArrayList<>();
        List<Long> ids = chatIds.getIds();
        if (ids == null || ids.isEmpty()) {
            return list;
        }

        ids.forEach(id -> {
            Chat chat = getChat(id);
            chat.setIsRead((byte) 1);
            Chat save = chatRepository.save(chat);

            ChatReadDto chatReadDto = new ChatReadDto();
            chatReadDto.setChatId(chat.getId());
            chatReadDto.setRead(true);
            chatReadDto.setSenderId(save.getSender());
            chatReadDto.setReceiverId(save.getReceiver());
            list.add(chatReadDto);
        });

        return list;
    }


    public List<ChatUser> getChatUsers(User user)
    {
        List<ChatUserSearch> userList = chatRepository.searchChatUser(user.getId());

        if (userList == null || userList.isEmpty()) {
            throw new NotFoundException("Data not found");
        }

        return getChatUserList(userList, user);
    }


    public List<SearchChatUser> searchNachatChat(String fullName, String phone, String status) {
        if ((fullName == null && phone == null) || status == null) {
            throw new BadRequestException("Invalid input parameters");
        }

        Role role;
        if (status.equalsIgnoreCase("TEACHER")) {
            role = Role.ROLE_TEACHER;
        } else if (status.equalsIgnoreCase("STUDENT")) {
            role = Role.ROLE_TEACHER;
        } else {
            throw new NotFoundException(status + " status not found");
        }

        List<User> users = userService.searchForChat(fullName, phone, role.name());
        if (users.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return builderNachtChatForChatUser(users);
    }


    public List<SearchChatUser> builderNachtChatForChatUser(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        return users.stream()
                .map(u -> new SearchChatUser(u.getId(), u.getFirstName(), u.getPhoneNumber()))
                .toList();
    }


    public ApiResponse<?> sendNachatChat(SendNachatChat sendNachatChat, User user)
    {
        User one = userService.getUser(sendNachatChat.userId());
        if (one != null) {
            return saveNachatChatMessage(user.getId(), one.getId(), sendNachatChat.message(), sendNachatChat.taskId());
        }
        throw new NotFoundException("User not found");
    }


    public ApiResponse<?> saveNachatChatMessage(Long sender, Long receiver, String message, Long taskId) {
        if (sender == null || receiver == null || message == null) {
            throw new BadRequestException("Invalid input parameters");
        }

        Task task = taskRepository.findById(taskId).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Task"))
        );

//        Chat existingChat = chatRepository.checkChat(sender, receiver);
//        if (existingChat == null) {
//            existingChat = chatRepository.checkChat(receiver, sender);
//        }
//
//        if (existingChat != null) {
//            saveChat(existingChat.getSender(), existingChat.getReceiver(), new Chat(), message);
//        } else {
            saveChat(sender, receiver, new Chat(), message, task);
//        }

        return ApiResponse.successResponse("Success");
    }

    public void saveChat(Long sender, Long receiver, Chat chat, String message, Task task) {
        chat.setSender(sender);
        chat.setReceiver(receiver);
        chat.setContent(message);
        chat.setTask(task);
        chatRepository.save(chat);
    }


    public ApiResponse<?> makeAllChatMessagesRead(User user)
    {
        chatRepository.readForSender(user.getId());
        chatRepository.readForReceiver(user.getId());
        return ApiResponse.successResponse("Success");
    }


    public List<SearchChatUser> search(User user, String name) {
        List<ChatUserSearch> chats = chatRepository.searchByUser(user.getId(), name.toUpperCase());
        if (chats.isEmpty()) {
            throw new NotFoundException("Chat not found");
        }

        return chats.stream()
                .filter(chat -> !chat.getId().equals(user.getId()))
                .map(this::getSearchBuilderData)
                .toList();
    }


    public SearchChatUser getSearchBuilderData(ChatUserSearch chat)
    {
        return new SearchChatUser(chat.getId(), chat.getName(), chat.getPhone());
    }


    public List<ChatUser> getChatUserList(List<ChatUserSearch> userList, User user)
    {
        if (!userList.isEmpty()) {
            return userList.stream()
                    .filter(chat -> !chat.getId().equals(user.getId()))
                    .map(chat -> getChatUser(chat, user))
                    .toList();
        }
        return List.of();
    }


    public List<ChatDto> findChatMessages(Long sender, Long receiver)
    {
        List<Chat> chatMessageList = chatRepository.getFindALlChat(sender, receiver);

        if (chatMessageList == null || chatMessageList.isEmpty()) {
            throw new NotFoundException("Chat not found");
        }

        ChatForNameAndImg nameAndImgForChat = getNameAndImgForChat(sender, receiver);

        return chatMessageList.stream()
                .map(chat -> builderChatDto(chat, nameAndImgForChat))
                .toList();
    }


    public ChatDto replay(ChatMessageEditOrReplay editOrReplay)
    {
        ChatDto chatDto = editOrReplay.chatDto();
//        Chat existingChat = chatRepository.checkChat(chatDto.getSender(), chatDto.getReceiver());
//
//        if (existingChat == null) {
//            existingChat = chatRepository.checkChat(chatDto.getReceiver(), chatDto.getSender());
//        }

//        if (existingChat != null) {
//            chatDto.setSender(existingChat.getSender());
//            chatDto.setReceiver(existingChat.getReceiver());
//        }

        Chat replayChat = getChat(editOrReplay.messageId());
        return saveChatMessage(chatDto, replayChat, new Chat());
    }


    public ChatDto saveChatMessage(ChatDto chatDto, Chat replayChatId, Chat chat)
    {
        chat.setReplayChat(replayChatId);
        builderChatBody(chatDto, chat, chatDto.getContent());
        Chat save = chatRepository.save(chat);
        ChatForNameAndImg nameAndImgForChat = getNameAndImgForChat(save.getSender(), save.getReceiver());
        return builderChatDto(save, nameAndImgForChat);
    }


    public ChatDto saveMessage(ChatDto dto)
    {
        log.info("service ga ham keldi");
//        Chat existingChat = chatRepository.checkChat(dto.getSender(), dto.getReceiver());
//        if (existingChat == null) {
//            existingChat = chatRepository.checkChat(dto.getReceiver(), dto.getSender());
//        }
//
//        if (existingChat != null) {
//            dto.setReceiver(existingChat.getReceiver());
//            dto.setSender(existingChat.getSender());
//        }

        return saveMessageBody(dto, new Chat());
    }



    public ChatDto saveMessageBody(ChatDto dto, Chat chat)
    {

        Task task = taskRepository.findById(dto.getTaskId()).orElseThrow(
                () -> RestException.restThrow(ResponseError.NOTFOUND("Task"))
        );
        chat.setReceiver(dto.getReceiver());
        chat.setSender(dto.getSender());
        chat.setContent(dto.getContent());
        chat.setIsRead((byte) 0);
        chat.setTask(task);
        chat.setIsEdited((byte) 0);
        if(dto.getAttachmentIds() != null)
            chat.setAttachmentIds(attachmentList(dto.getAttachmentIds()));
        Chat save = chatRepository.save(chat);

        ChatForNameAndImg nameAndImgForChat = getNameAndImgForChat(save.getSender(), save.getReceiver());
        return builderChatDto(save, nameAndImgForChat);
    }


    public ChatDto editMessage(ChatMessageEditOrReplay messageEdit)
    {
        Chat chat = getChat(messageEdit.messageId());
        messageEdit.chatDto().setSender(chat.getSender());
        messageEdit.chatDto().setReceiver(chat.getReceiver());
        chat.setSender(messageEdit.chatDto().getSender());
        chat.setIsEdited((byte) 1);
        builderChatBody(messageEdit.chatDto(), chat, "");
        Chat save = chatRepository.save(chat);

        ChatForNameAndImg nameAndImgForChat = getNameAndImgForChat(
                messageEdit.chatDto().getSender(), messageEdit.chatDto().getReceiver()
        );
        return builderChatDto(save, nameAndImgForChat);
    }


    public void builderChatBody(ChatDto chatDto, Chat chat, String content)
    {
        Set<Long> attachments = chat.getAttachmentIds();
        if (attachments == null) {
            attachments = new HashSet<>(attachmentList(chatDto.getAttachmentIds()));
        } else {
            attachments.addAll(attachmentList(chatDto.getAttachmentIds()));
        }

        chat.setAttachmentIds(attachments);
        chat.setSender(chatDto.getSender());
        chat.setReceiver(chatDto.getReceiver());
        chat.setContent(content.isEmpty() ? chatDto.getContent() : content);
    }


    public Set<Long> attachmentList(Set<Long> attachmentIds) {
        if (attachmentIds != null && !attachmentIds.isEmpty()) {
            return attachmentIds.stream().collect(Collectors.toUnmodifiableSet());
        }
        return Set.of();
    }


    public ApiResponse<?> deleteUser(List<Long> userIds, User user)
    {
        if (userIds == null || userIds.isEmpty()) {
            throw new NotFoundException("User not found");
        }

        userIds.forEach(id -> chatRepository.deleteChat(user.getId(), id));
        return ApiResponse.successResponse("Success");
    }


    public void deleteMessageList(ChatIds chatIds) {
        chatIds.getIds().forEach(this::deleteMessage);
    }


    public void deleteMessage(Long chatId)
    {
        if (chatId == null) {
            return;
        }

        chatRepository.findById(chatId)
                .ifPresent(chat -> {
                    if (!chat.getAttachmentIds().isEmpty()) {
                        chat.getAttachmentIds().forEach(fileService::deleteFile);
                    }
                    chatRepository.delete(chat);
                });
    }



    public ChatUser getChatUser(ChatUserSearch user, User currentUser)
    {
        Chat chat = lastChatMessage(user.getId());
        String userFirstName = checkAdmin(user.getRoleName(), user.getName());

        ChatForNameAndImg chatFrom = (chat != null) ? getNameAndImgForChat(chat.getSender(), chat.getReceiver()) : null;
        return ChatUser.builder()
                .userId(user.getId())
                .name(userFirstName)
                .phone(user.getPhone())
                .status(user.getStatus())
                .attachmentId(user.getPhoto())
                .newMessageCount(newMessageCount(user.getId(), currentUser))
                .chatDto(chat != null ? builderChatDto(chat, chatFrom) : null)
                .build();
    }


    public ChatForNameAndImg getNameAndImgForChat(Long sender, Long receiver)
    {
        User senderUser = userService.getUser(sender);
        User receiverUser = userService.getUser(receiver);

        String senderName = getUserName(senderUser);
        String receiverName = getUserName(receiverUser);

        String senderImg = getUserImg(senderUser);
        String receiverImg = getUserImg(receiverUser);

        return new ChatForNameAndImg(senderName, receiverName, senderImg, receiverImg);
    }


    private String getUserName(User user) {
        return (user != null) ? user.getFirstName() : "";
    }


    private String getUserImg(User user) {
        return (user != null && user.getImgUrl() != null) ? user.getImgUrl() : null;
    }


    public String checkAdmin(String name, String firstName) {
        if (name.equals("ROLE_SUPER_ADMIN")) return "Support service";
        return firstName;
    }


    public Chat lastChatMessage(Long senderOrReceiver)
    {
        return chatRepository.lastChat(senderOrReceiver);
    }


    public ChatDto builderChatDto(Chat chat, ChatForNameAndImg chatFrom)
    {
        return ChatDto.builder()
                .id(chat.getId())
                .sender(chat.getSender())
                .receiver(chat.getReceiver())
                .content(chat.getContent())
                .isRead(chat.getIsRead() == 1)
                .isEdited(chat.getIsEdited() == 1)
                .senderName(chatFrom.senderName())
                .receiverName(chatFrom.receiverName())
                .senderImg(chatFrom.senderImg())
                .receiverImg(chatFrom.receiverImg())
                .createdAt(String.valueOf(chat.getCreatedAt()))
                .attachmentIds(chat.getAttachmentIds() == null
                        ? null
                        : new HashSet<>(chat.getAttachmentIds()))
                .replayDto(
                        chat.getReplayChat() != null
                                ? builderChatDto(chat.getReplayChat(), chatFrom)
                                : null)
                .build();
    }


    public Chat getChat(Long chatId)
    {
        return chatRepository.findById(chatId)
                .orElseThrow(() -> new NotFoundException("Chat"));
    }


    public int newMessageCount(Long sender, User currentUser)
    {
        Integer i = chatRepository.numberOfUnreadMessages(sender, currentUser.getId());
        return i == null || i == 0 ? 0 : i;
    }
}
