package com.ranker.web.repository;

import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {  // Long is the foreign key type

    // Custom Queries
    Optional<Item> findByPosition(int position);

    Optional<Item> findByFavoritesListIdAndPosition(Long id, int i);

    int countByFavoritesListId(Long listId);

    List<Item> findByFavoritesListIdOrderByPositionAsc(Long listId);
}
