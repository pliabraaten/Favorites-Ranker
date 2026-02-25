package com.ranker.web.controllers;

import com.ranker.web.dto.FavoritesListDTO;
import com.ranker.web.dto.ItemDTO;
import com.ranker.web.security.CustomUserDetailsService;
import com.ranker.web.security.SecurityConfig;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
@Import(SecurityConfig.class)
class ItemControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private ItemService itemService;

        @MockitoBean
        private FavoritesListService favoritesListService;

        @MockitoBean
        private CustomUserDetailsService customUserDetailsService;

        // ==================== GET /items/{listId}/new ====================

        @Test
        @WithMockUser(username = "testuser")
        void shouldShowCreateItemForm() throws Exception {
                // Given
                FavoritesListDTO listDTO = FavoritesListDTO.builder()
                                .id(1L)
                                .listName("Movies")
                                .sortedCount(0)
                                .items(new ArrayList<>())
                                .build();
                when(favoritesListService.findListById(1L)).thenReturn(listDTO);
                when(itemService.getItemsByListId(1L)).thenReturn(new ArrayList<>());

                // When & Then
                mockMvc.perform(get("/items/1/new"))
                                .andExpect(status().isOk())
                                .andExpect(view().name("items-create"))
                                .andExpect(model().attributeExists("list"))
                                .andExpect(model().attributeExists("item"));
        }

        // ==================== POST /items/{listId} ====================

        @Test
        @WithMockUser(username = "testuser")
        void shouldCreateItemAndRedirect() throws Exception {
                // Given
                when(itemService.saveItem(eq(1L), anyString())).thenReturn(1);

                // When & Then
                mockMvc.perform(post("/items/1")
                                .param("name", "The Matrix"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/items/1/new"));

                verify(itemService).saveItem(1L, "The Matrix");
        }

        @Test
        @WithMockUser(username = "testuser")
        void shouldReturnFormOnValidationError() throws Exception {
                // Given — mock needed for the view that is rendered on validation error
                FavoritesListDTO listDTO = FavoritesListDTO.builder()
                                .id(1L)
                                .listName("Movies")
                                .sortedCount(0)
                                .items(new ArrayList<>())
                                .build();
                when(favoritesListService.findListById(1L)).thenReturn(listDTO);
                when(itemService.getItemsByListId(1L)).thenReturn(new ArrayList<>());

                // When & Then — blank name should trigger @NotBlank validation
                mockMvc.perform(post("/items/1")
                                .param("name", ""))
                                .andExpect(status().isOk())
                                .andExpect(view().name("items-create"));
        }

        // ==================== POST /items/{itemId}/reposition ====================

        @Test
        @WithMockUser(username = "testuser")
        void shouldRepositionItemAndRedirect() throws Exception {
                // Given
                when(itemService.getListIdByItemId(1L)).thenReturn(5L);

                // When & Then
                mockMvc.perform(post("/items/1/reposition")
                                .param("direction", "Up"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/lists/5"));

                verify(itemService).repositionItem(1L, "Up");
        }

        // ==================== POST /items/{itemId}/delete ====================

        @Test
        @WithMockUser(username = "testuser")
        void shouldDeleteItemAndRedirect() throws Exception {
                // Given
                ItemDTO itemDTO = ItemDTO.builder().id(1L).name("Movie").listId(5L).build();
                when(itemService.findItemById(1L)).thenReturn(itemDTO);

                // When & Then
                mockMvc.perform(post("/items/1/delete"))
                                .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/lists/5"));

                verify(itemService).deleteItem(1L);
                verify(favoritesListService).decrementSortedCount(5L);
        }

        // ==================== Security ====================

        @Test
        void shouldRedirectToLoginWhenNotAuthenticated() throws Exception {
                mockMvc.perform(get("/items/1/new"))
                                .andExpect(status().is3xxRedirection());
        }
}
