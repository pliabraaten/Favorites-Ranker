package com.ranker.web.repository;


import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


// Queries the database
public interface FavoritesListRepository extends JpaRepository<FavoritesList, Long> {

    // Custom Queries
    Optional<FavoritesList> findByListName(String listName);
    @Query("SELECT f FROM FavoritesList f WHERE f.listName LIKE CONCAT('%', :query, '%')")
    List<FavoritesList> searchLists(String query);

    List<FavoritesList> findByUserId(Long userId);
}
