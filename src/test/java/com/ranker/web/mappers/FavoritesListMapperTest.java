package com.ranker.web.mappers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.models.UserEntity;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FavoritesListMapperTest {

    // ==================== mapToFavoritesListDTO ====================

    @Test
    void shouldMapEntityToDTO() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");

        FavoritesList list = new FavoritesList();
        list.setId(1L);
        list.setListName("My Movies");
        list.setUser(user);
        list.setSortedCount(3);

        Item item1 = new Item();
        item1.setId(1L);
        item1.setName("Star Wars");
        item1.setPosition(1);
        item1.setFavoritesList(list);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Indiana Jones");
        item2.setPosition(2);
        item2.setFavoritesList(list);

        list.setItems(List.of(item1, item2));

        // When
        FavoritesListDTO dto = FavoritesListMapper.mapToFavoritesListDTO(list);

        // Then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getListName()).isEqualTo("My Movies");
        assertThat(dto.getUser()).isEqualTo(user);
        assertThat(dto.getSortedCount()).isEqualTo(3);
        assertThat(dto.getItems()).hasSize(2);
        assertThat(dto.getItems().get(0).getName()).isEqualTo("Star Wars");
        assertThat(dto.getItems().get(1).getName()).isEqualTo("Indiana Jones");
    }

    @Test
    void shouldMapEntityWithEmptyItemsList() {
        // Given
        UserEntity user = new UserEntity();
        user.setId(1L);

        FavoritesList list = new FavoritesList();
        list.setId(1L);
        list.setListName("Empty List");
        list.setUser(user);
        list.setSortedCount(0);
        list.setItems(new ArrayList<>());

        // When
        FavoritesListDTO dto = FavoritesListMapper.mapToFavoritesListDTO(list);

        // Then
        assertThat(dto.getItems()).isEmpty();
    }

    // ==================== mapToListEntity ====================

    @Test
    void shouldMapDTOToEntity() {
        // Given
        FavoritesListDTO dto = FavoritesListDTO.builder()
                .id(1L)
                .listName("My Movies")
                .sortedCount(5)
                .build();

        // When
        FavoritesList entity = FavoritesListMapper.mapToListEntity(dto);

        // Then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getListName()).isEqualTo("My Movies");
        assertThat(entity.getSortedCount()).isEqualTo(5);
    }

    @Test
    void shouldMapDTOWithNullSortedCount() {
        // Given
        FavoritesListDTO dto = FavoritesListDTO.builder()
                .id(1L)
                .listName("List")
                .sortedCount(null)
                .build();

        // When
        FavoritesList entity = FavoritesListMapper.mapToListEntity(dto);

        // Then
        assertThat(entity.getSortedCount()).isNull();
    }
}
