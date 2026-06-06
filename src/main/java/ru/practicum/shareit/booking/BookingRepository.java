package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, QuerydslPredicateExecutor<Booking> {

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        join fetch i.user u
        where u.id = :id
        order by b.start desc
    """)
    List<Booking> findAllByItemOwnerId(@Param("id") Long id);

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        where i.user.id = :ownerId
            and b.status = :status
        order by b.start desc
""")
    List<Booking> findAllByOwnerIdAndStatus(@Param("ownerId") Long ownerId,
                                            @Param("status") BookingStatus status);

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        where i.user.id = :ownerId
            and b.start <= :now
            and b.end >= :now
        order by b.start desc
""")
    List<Booking> findCurrentBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                             @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        where i.user.id = :ownerId
            and b.end < :now
        order by b.start desc
""")
    List<Booking> findPastBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                            @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        where i.user.id = :ownerId
            and b.start > :now
        order by b.start desc
""")
    List<Booking> findFutureBookingsByOwnerId(@Param("ownerId") Long ownerId,
                                              @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.booker
        join fetch b.item i
        join fetch i.user u
        where b.id = :bookerId and b.status = :status
        order by b.start desc
    """)
    List<Booking> findAllByBookerIdAndStatus(@Param("bookerId") Long bookerId, @Param("status") BookingStatus status);

    @Query("""
        select b
        from Booking b
        join fetch b.item
        where b.booker.id = :userId
            and b.start <= :now
            and b.end >= :now
        order by b.start desc
""")
    List<Booking> findCurrentBookerByBookerId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.item
        where b.booker.id = :userId
            and b.end <= :now
        order by b.start desc
""")
    List<Booking> findPastBookerByBookerId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.item
        where b.booker.id = :userId
            and b.start >= :now
        order by b.start desc
""")
    List<Booking> findFutureBookerByBookerId(@Param("userId") Long userId, @Param("now") LocalDateTime now);

    @Query("""
        select b
        from Booking b
        join fetch b.booker u
        join fetch b.item i
        where b.booker.id = :id
        order by b.start desc
""")
    List<Booking> findAllByBookerId(@Param("id") Long id);


    @Query("""
        select b
        from Booking b
        join fetch b.item i
        where i.id = :itemId
""")
    List<Booking> findAllByItemId(@Param("itemId") Long itemId);

    Booking findFirstByItemIdAndStartBeforeOrderByStartDesc(
            Long itemId,
            LocalDateTime now
    );

    Booking findFirstByItemIdAndStartAfterOrderByStartAsc(
            Long itemId,
            LocalDateTime now
    );
}
