package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.impl.FavoritesListServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoritesListServiceTest {

    @Mock
    private FavoritesListRepository favoritesListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FavoritesListServiceImpl favoritesListService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
    }

    private void mockSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);
    }

    // ==================== findUserLists ====================

    @Test
    void shouldFindUserLists() {
        // Given
        mockSecurityContext();
        FavoritesList list1 = createList(1L, "Movies");
        FavoritesList list2 = createList(2L, "Books");
        when(favoritesListRepository.findByUserId(1L))
                .thenReturn(Arrays.asList(list1, list2));

        // When
        List<FavoritesListDTO> result = favoritesListService.findUserLists();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(FavoritesListDTO::getListName)
                .containsExactly("Movies", "Books");
        verify(favoritesListRepository).findByUserId(1L);
    }

    // ==================== saveList ====================

    @Test
    void shouldSaveList() {
        // Given
        mockSecurityContext();
        FavoritesListDTO listDTO = FavoritesListDTO.builder()
                .listName("New List")
                .build();

        FavoritesList savedList = createList(1L, "New List");
        when(favoritesListRepository.save(any(FavoritesList.class))).thenReturn(savedList);

        // When
        Long result = favoritesListService.saveList(listDTO);

        // Then
        assertThat(result).isEqualTo(1L);
        verify(favoritesListRepository).save(any(FavoritesList.class));
    }

    @Test
    void shouldSaveListWithNullSortedCountDefaultingToZero() {
        // Given
        mockSecurityContext();
        FavoritesListDTO listDTO = FavoritesListDTO.builder()
                .listName("New List")
                .sortedCount(null)
                .build();

        FavoritesList savedList = createList(1L, "New List");
        when(favoritesListRepository.save(any(FavoritesList.class))).thenReturn(savedList);

        // When
        favoritesListService.saveList(listDTO);

        // Then
        verify(favoritesListRepository).save(argThat(list -> list.getSortedCount() == 0 && !list.isRanked()));
    }

    // ==================== findListById ====================

    @Test
    void shouldFindListById() {
        // Given
        FavoritesList list = createList(1L, "Test List");
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        FavoritesListDTO result = favoritesListService.findListById(1L);

        // Then
        assertThat(result.getListName()).isEqualTo("Test List");
        verify(favoritesListRepository).findById(1L);
    }

    @Test
    void shouldThrowWhenListNotFound() {
        // Given
        when(favoritesListRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> favoritesListService.findListById(999L))
                .isInstanceOf(Exception.class);
    }

    // ==================== updateListName ====================

    @Test
    void shouldUpdateListName() {
        // Given
        FavoritesList list = createList(1L, "Old Name");
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.updateListName(1L, "New Name");

        // Then
        assertThat(list.getListName()).isEqualTo("New Name");
    }

    @Test
    void shouldThrowWhenUpdatingNameOfNonExistentList() {
        // Given
        when(favoritesListRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> favoritesListService.updateListName(999L, "Name"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("List not found");
    }

    // ==================== delete ====================

    @Test
    void shouldDeleteList() {
        // When
        favoritesListService.delete(1L);

        // Then
        verify(favoritesListRepository).deleteById(1L);
    }

    // ==================== searchLists ====================

    @Test
    void shouldSearchLists() {
        // Given
        FavoritesList list = createList(1L, "My Movies");
        when(favoritesListRepository.searchLists("Movie")).thenReturn(List.of(list));

        // When
        List<FavoritesListDTO> result = favoritesListService.searchLists("Movie");

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getListName()).isEqualTo("My Movies");
    }

    @Test
    void shouldReturnEmptyListWhenSearchFindsNothing() {
        // Given
        when(favoritesListRepository.searchLists("xyz")).thenReturn(Collections.emptyList());

        // When
        List<FavoritesListDTO> result = favoritesListService.searchLists("xyz");

        // Then
        assertThat(result).isEmpty();
    }

    // ==================== updateSortedCount ====================

    @Test
    void shouldUpdateSortedCount() {
        // Given
        FavoritesList list = createList(1L, "List");
        list.setSortedCount(0);
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.updateSortedCount(1L, 5);

        // Then
        assertThat(list.getSortedCount()).isEqualTo(5);
    }

    @Test
    void shouldThrowWhenUpdatingSortedCountOfNonExistentList() {
        // Given
        when(favoritesListRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> favoritesListService.updateSortedCount(999L, 5))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ==================== decrementSortedCount ====================

    @Test
    void shouldDecrementSortedCount() {
        // Given
        FavoritesList list = createList(1L, "List");
        list.setSortedCount(3);
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.decrementSortedCount(1L);

        // Then
        assertThat(list.getSortedCount()).isEqualTo(2);
    }

    @Test
    void shouldNotDecrementSortedCountBelowZero() {
        // Given
        FavoritesList list = createList(1L, "List");
        list.setSortedCount(0);
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.decrementSortedCount(1L);

        // Then
        assertThat(list.getSortedCount()).isEqualTo(0);
    }

    // ==================== setRankedFlag ====================

    @Test
    void shouldSetRankedFlagToTrue() {
        // Given
        FavoritesList list = createList(1L, "List");
        list.setRanked(false);
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.setRankedFlag(1L, true);

        // Then
        assertThat(list.isRanked()).isTrue();
    }

    @Test
    void shouldSetRankedFlagToFalse() {
        // Given
        FavoritesList list = createList(1L, "List");
        list.setRanked(true);
        when(favoritesListRepository.findById(1L)).thenReturn(Optional.of(list));

        // When
        favoritesListService.setRankedFlag(1L, false);

        // Then
        assertThat(list.isRanked()).isFalse();
    }

    @Test
    void shouldThrowWhenSettingRankedFlagOfNonExistentList() {
        // Given
        when(favoritesListRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> favoritesListService.setRankedFlag(999L, true))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ==================== Helper ====================

    private FavoritesList createList(Long id, String name) {
        FavoritesList list = new FavoritesList();
        list.setId(id);
        list.setListName(name);
        list.setUser(testUser);
        list.setSortedCount(0);
        return list;
    }
}
