package com.ranker.web.repository;

import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ItemRepository extends JpaRepository<Item, Long> {

    // Custom Queries
    Optional<FavoritesList> findByPosition(int position);

}
