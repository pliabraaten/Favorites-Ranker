package com.ranker.web.repository;


import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface FavoritesListRepository extends JpaRepository<FavoritesList, Long> {

    // Custom Queries
    Optional<FavoritesList> findByListName(String listName);
    Optional<FavoritesList> findByUser(User user);

}
