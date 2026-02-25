package com.ranker.web.mappers;

import com.ranker.web.dto.ItemDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ItemMapperTest {

    // ==================== mapToItemDTO ====================

    @Test
    void shouldMapEntityToDTO() {
        // Given
        FavoritesList list = new FavoritesList();
        list.setId(5L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Star Wars");
        item.setPosition(3);
        item.setFavoritesList(list);

        // When
        ItemDTO dto = ItemMapper.mapToItemDTO(item);

        // Then
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getName()).isEqualTo("Star Wars");
        assertThat(dto.getPosition()).isEqualTo(3);
        assertThat(dto.getListId()).isEqualTo(5L);
    }

    @Test
    void shouldMapEntityWithNullFavoritesListToDTO() {
        // Given
        Item item = new Item();
        item.setId(1L);
        item.setName("Orphan Item");
        item.setPosition(1);
        item.setFavoritesList(null);

        // When
        ItemDTO dto = ItemMapper.mapToItemDTO(item);

        // Then
        assertThat(dto.getListId()).isNull();
    }

    // ==================== mapToItemEntity ====================

    @Test
    void shouldMapDTOToEntity() {
        // Given
        ItemDTO dto = ItemDTO.builder()
                .id(1L)
                .name("Star Wars")
                .position(3)
                .build();

        // When
        Item entity = ItemMapper.mapToItemEntity(dto);

        // Then
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getName()).isEqualTo("Star Wars");
        assertThat(entity.getPosition()).isEqualTo(3);
    }
}
