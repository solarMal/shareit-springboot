package ru.practicum.shareit.item;

public class CommentMapper {

    public static Comment toCommentFromRequestDto(CommentRequestDto dto) {
        Comment comment = new Comment();

        comment.setText(dto.getText());

        return comment;
    }

    public static CommentResponseDto toCommentResponseDto(Comment comment) {
        CommentResponseDto dto = new CommentResponseDto();

        dto.setId(comment.getId());
        dto.setText(comment.getText());
        dto.setAuthorName(comment.getAuthor().getName());
        dto.setCreated(comment.getCreated());

        return dto;
    }
}
