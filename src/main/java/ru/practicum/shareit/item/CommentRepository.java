package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, QuerydslPredicateExecutor<Comment> {

    @Query("""
        select c
        from Comment c
        join fetch c.item i
        join fetch c.author a
        where i.id = :itemId
""")
    List<Comment> findAllCommentsByItemId(Long itemId);
}
