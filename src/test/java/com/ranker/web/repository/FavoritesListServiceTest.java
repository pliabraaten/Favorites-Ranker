package com.ranker.web.services;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.models.FavoritesList;
import com.ranker.web.models.UserEntity;
import com.ranker.web.repository.FavoritesListRepository;
import com.ranker.web.repository.UserRepository;
import com.ranker.web.services.impl.FavoritesListServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void shouldFindUserLists() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

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

    @Test
    void shouldSaveList() {
        // Given
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(userRepository.findByUsername("testuser")).thenReturn(testUser);

        FavoritesListDTO listDTO = new FavoritesListDTO();
        listDTO.setListName("New List");

        FavoritesList savedList = createList(1L, "New List");
        when(favoritesListRepository.save(any(FavoritesList.class))).thenReturn(savedList);

        // When
        Long result = favoritesListService.saveList(listDTO);

        // Then
        assertThat(result).isEqualTo(1L);
        verify(favoritesListRepository).save(any(FavoritesList.class));
    }

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
    void shouldDeleteList() {
        // When
        favoritesListService.delete(1L);

        // Then
        verify(favoritesListRepository).deleteById(1L);
    }

    // Helper method
    private FavoritesList createList(Long id, String name) {
        FavoritesList list = new FavoritesList();
        list.setId(id);
        list.setListName(name);
        list.setUser(testUser);
        list.setSortedCount(0);
        return list;
    }
}
