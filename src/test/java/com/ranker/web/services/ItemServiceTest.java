package com.ranker.web.services;

import com.ranker.web.dto.ItemDTO;
import com.ranker.web.dto.ItemPositionDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.Item;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.ItemRepository;
import com.ranker.web.services.impl.ItemServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private FavoritesListRepository listRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    private FavoritesList testList;
    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");

        testList = new FavoritesList();
        testList.setId(1L);
        testList.setListName("Movies");
        testList.setUser(testUser);
        testList.setSortedCount(0);
    }

    // ==================== saveItem ====================

    @Test
    void shouldSaveSingleItem() {
        // Given
        when(listRepository.findById(1L)).thenReturn(Optional.of(testList));
        when(itemRepository.countByFavoritesListId(1L)).thenReturn(0);
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        int count = itemService.saveItem(1L, "The Matrix");

        // Then
        assertThat(count).isEqualTo(1);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void shouldSaveMultipleCommaSeparatedItems() {
        // Given
        when(listRepository.findById(1L)).thenReturn(Optional.of(testList));
        when(itemRepository.countByFavoritesListId(1L)).thenReturn(0);
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        int count = itemService.saveItem(1L, "Star Wars, Indiana Jones, Jaws");

        // Then
        assertThat(count).isEqualTo(3);
        verify(itemRepository, times(3)).save(any(Item.class));
    }

    @Test
    void shouldFilterEmptyNamesFromCommaSeparatedInput() {
        // Given
        when(listRepository.findById(1L)).thenReturn(Optional.of(testList));
        when(itemRepository.countByFavoritesListId(1L)).thenReturn(0);
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        int count = itemService.saveItem(1L, "Star Wars, , , Indiana Jones");

        // Then
        assertThat(count).isEqualTo(2);
        verify(itemRepository, times(2)).save(any(Item.class));
    }

    @Test
    void shouldAssignCorrectPositionsStartingAfterExistingItems() {
        // Given
        when(listRepository.findById(1L)).thenReturn(Optional.of(testList));
        when(itemRepository.countByFavoritesListId(1L)).thenReturn(3); // 3 items already exist
        when(itemRepository.save(any(Item.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        itemService.saveItem(1L, "New Item");

        // Then — position should start at 4 (3 existing + 1)
        verify(itemRepository).save(argThat(item -> item.getPosition() == 4));
    }

    @Test
    void shouldThrowWhenSavingToNonExistentList() {
        // Given
        when(listRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> itemService.saveItem(999L, "Item"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ==================== findItemById ====================

    @Test
    void shouldFindItemById() {
        // Given
        Item item = createItem(1L, "Movie", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        ItemDTO result = itemService.findItemById(1L);

        // Then
        assertThat(result.getName()).isEqualTo("Movie");
        assertThat(result.getPosition()).isEqualTo(1);
    }

    // ==================== deleteItem ====================

    @Test
    void shouldDeleteItemAndDecrementPositions() {
        // Given
        Item item = createItem(1L, "Movie", 2);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        itemService.deleteItem(1L);

        // Then
        verify(itemRepository).deleteById(1L);
        verify(itemRepository).decrementPositionsAfter(1L, 2);
    }

    @Test
    void shouldThrowWhenDeletingNonExistentItem() {
        // Given
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> itemService.deleteItem(999L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ==================== getItemsByListId ====================

    @Test
    void shouldGetItemsByListId() {
        // Given
        Item item1 = createItem(1L, "First", 1);
        Item item2 = createItem(2L, "Second", 2);
        when(itemRepository.findByFavoritesListIdOrderByPositionAsc(1L))
                .thenReturn(Arrays.asList(item1, item2));

        // When
        List<ItemDTO> result = itemService.getItemsByListId(1L);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("First");
        assertThat(result.get(1).getName()).isEqualTo("Second");
    }

    @Test
    void shouldReturnEmptyListWhenNoItems() {
        // Given
        when(itemRepository.findByFavoritesListIdOrderByPositionAsc(1L))
                .thenReturn(Collections.emptyList());

        // When
        List<ItemDTO> result = itemService.getItemsByListId(1L);

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== updatePositions ====================

    @Test
    void shouldUpdatePositions() {
        // Given
        Item item1 = createItem(1L, "First", 1);
        Item item2 = createItem(2L, "Second", 2);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item1));
        when(itemRepository.findById(2L)).thenReturn(Optional.of(item2));

        List<ItemPositionDTO> positions = Arrays.asList(
                new ItemPositionDTO(1L, 2),
                new ItemPositionDTO(2L, 1));

        // When
        itemService.updatePositions(1L, positions);

        // Then
        assertThat(item1.getPosition()).isEqualTo(2);
        assertThat(item2.getPosition()).isEqualTo(1);
    }

    @Test
    void shouldThrowSecurityExceptionWhenItemBelongsToDifferentList() {
        // Given
        FavoritesList otherList = new FavoritesList();
        otherList.setId(99L);

        Item item = new Item();
        item.setId(1L);
        item.setFavoritesList(otherList);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        List<ItemPositionDTO> positions = List.of(new ItemPositionDTO(1L, 1));

        // When & Then
        assertThatThrownBy(() -> itemService.updatePositions(1L, positions))
                .isInstanceOf(SecurityException.class);
    }

    // ==================== updateItemName ====================

    @Test
    void shouldUpdateItemName() {
        // Given
        Item item = createItem(1L, "Old Name", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        itemService.updateItemName(1L, "New Name");

        // Then
        assertThat(item.getName()).isEqualTo("New Name");
    }

    @Test
    void shouldThrowWhenUpdatingNameOfNonExistentItem() {
        // Given
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> itemService.updateItemName(999L, "Name"))
                .isInstanceOf(RuntimeException.class);
    }

    // ==================== getListIdByItemId ====================

    @Test
    void shouldGetListIdByItemId() {
        // Given
        Item item = createItem(1L, "Movie", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));

        // When
        Long listId = itemService.getListIdByItemId(1L);

        // Then
        assertThat(listId).isEqualTo(1L);
    }

    @Test
    void shouldThrowWhenGettingListIdOfNonExistentItem() {
        // Given
        when(itemRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> itemService.getListIdByItemId(999L))
                .isInstanceOf(RuntimeException.class);
    }

    // ==================== repositionItem ====================

    @Test
    void shouldDelegateToMoveItemUpWhenDirectionIsUp() {
        // Given
        Item item = createItem(1L, "Movie", 2);
        Item priorItem = createItem(2L, "Prior", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 1))
                .thenReturn(Optional.of(priorItem));

        // When
        itemService.repositionItem(1L, "Up");

        // Then
        assertThat(item.getPosition()).isEqualTo(1);
        assertThat(priorItem.getPosition()).isEqualTo(2);
    }

    @Test
    void shouldDelegateToMoveItemDownWhenDirectionIsDown() {
        // Given
        Item item = createItem(1L, "Movie", 1);
        Item nextItem = createItem(2L, "Next", 2);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 2))
                .thenReturn(Optional.of(nextItem));

        // When
        itemService.repositionItem(1L, "Down");

        // Then
        assertThat(item.getPosition()).isEqualTo(2);
        assertThat(nextItem.getPosition()).isEqualTo(1);
    }

    // ==================== moveItemUp ====================

    @Test
    void shouldMoveItemUp() {
        // Given
        Item item = createItem(1L, "Movie", 2);
        Item priorItem = createItem(2L, "Prior", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 1))
                .thenReturn(Optional.of(priorItem));

        // When
        itemService.moveItemUp(1L);

        // Then — positions should be swapped
        assertThat(item.getPosition()).isEqualTo(1);
        assertThat(priorItem.getPosition()).isEqualTo(2);
    }

    @Test
    void shouldNotMoveItemUpWhenAlreadyAtTop() {
        // Given
        Item item = createItem(1L, "Movie", 1);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 0))
                .thenReturn(Optional.empty());

        // When
        itemService.moveItemUp(1L);

        // Then — position unchanged
        assertThat(item.getPosition()).isEqualTo(1);
    }

    // ==================== moveItemDown ====================

    @Test
    void shouldMoveItemDown() {
        // Given
        Item item = createItem(1L, "Movie", 1);
        Item nextItem = createItem(2L, "Next", 2);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 2))
                .thenReturn(Optional.of(nextItem));

        // When
        itemService.moveItemDown(1L);

        // Then — positions should be swapped
        assertThat(item.getPosition()).isEqualTo(2);
        assertThat(nextItem.getPosition()).isEqualTo(1);
    }

    @Test
    void shouldNotMoveItemDownWhenAlreadyAtBottom() {
        // Given
        Item item = createItem(1L, "Movie", 3);
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemRepository.findByFavoritesListIdAndPosition(1L, 4))
                .thenReturn(Optional.empty());

        // When
        itemService.moveItemDown(1L);

        // Then — position unchanged
        assertThat(item.getPosition()).isEqualTo(3);
    }

    // ==================== Helper ====================

    private Item createItem(Long id, String name, int position) {
        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setPosition(position);
        item.setFavoritesList(testList);
        return item;
    }
}
