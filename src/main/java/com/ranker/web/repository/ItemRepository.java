package com.ranker.web.repository;

import com.ranker.web.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {  // Long is the foreign key type

    // Custom Queries
    Optional<Item> findByPosition(int position);

    Optional<Item> findByFavoritesListIdAndPosition(Long id, int i);

    int countByFavoritesListId(Long listId);

    List<Item> findByFavoritesListIdOrderByPositionAsc(Long listId);

    @Modifying
    @Query("UPDATE Item i SET i.position = i.position - 1 WHERE i.favoritesList.id = :listId AND i.position > :deletedPosition")
    void decrementPositionsAfter(@Param("listId") Long listId, @Param("deletedPosition") int deletedPosition);

}
