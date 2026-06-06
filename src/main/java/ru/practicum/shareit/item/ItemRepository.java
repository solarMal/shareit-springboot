package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    @Query("""
        select i
        from Item i
        where i.user.id = :userId
    """)
    List<Item> findAllByUserId(@Param("userId") Long userId);


    @Query("""
        select i
        from Item i
        where i.available = true
            and(
                lower(i.name) like lower(concat('%', :text, '%'))
            or lower(i.description) like lower(concat('%', :text, '%')))
        """)
    List<Item> searchByText(@Param("text") String text);
}
