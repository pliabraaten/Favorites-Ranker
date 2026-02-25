package com.ranker.web.controllers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.security.CustomUserDetailsService;
import com.ranker.web.security.SecurityConfig;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import com.ranker.web.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritesListController.class)
@Import(SecurityConfig.class)
class FavoritesListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FavoritesListService favoritesListService;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @WithMockUser(username = "testuser")
    void shouldDisplayListsPage() throws Exception {
        // Given
        FavoritesListDTO list1 = createListDTO(1L, "Movies");
        FavoritesListDTO list2 = createListDTO(2L, "Books");
        List<FavoritesListDTO> lists = Arrays.asList(list1, list2);

        when(favoritesListService.findUserLists()).thenReturn(lists);

        // When & Then
        mockMvc.perform(get("/lists"))
                .andExpect(status().isOk())
                .andExpect(view().name("lists-list"))
                .andExpect(model().attributeExists("lists"))
                .andExpect(model().attribute("lists", lists));

        verify(favoritesListService).findUserLists();
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldDisplayListDetailsPage() throws Exception {
        // Given
        FavoritesListDTO listDTO = createListDTO(1L, "My Movies");
        when(favoritesListService.findListById(1L)).thenReturn(listDTO);
        when(itemService.getItemsByListId(1L)).thenReturn(new ArrayList<>());

        // When & Then — verify controller returns correct view & model, not template
        // rendering
        mockMvc.perform(get("/lists/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lists-details"))
                .andExpect(model().attributeExists("list"));

        verify(favoritesListService).findListById(1L);
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldCreateNewList() throws Exception {
        // Given
        when(favoritesListService.saveList(any(FavoritesListDTO.class))).thenReturn(1L);

        // When & Then
        mockMvc.perform(post("/lists/new")
                .param("listName", "New List"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lists/1"));

        verify(favoritesListService).saveList(any(FavoritesListDTO.class));
    }

    @Test
    @WithMockUser(username = "testuser")
    void shouldDeleteList() throws Exception {
        // When & Then — controller uses @GetMapping for delete
        mockMvc.perform(get("/lists/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lists"));

        verify(favoritesListService).delete(1L);
    }

    @Test
    void shouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        // When & Then — Spring Security form-login redirects to /login
        mockMvc.perform(get("/lists/1"))
                .andExpect(status().is3xxRedirection());
    }

    // Helper method
    private FavoritesListDTO createListDTO(Long id, String name) {
        return FavoritesListDTO.builder()
                .id(id)
                .listName(name)
                .sortedCount(0)
                .items(new ArrayList<>())
                .build();
    }
}
