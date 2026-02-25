package com.ranker.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranker.web.dto.UpdateItemNameRequestDTO;
import com.ranker.web.security.CustomUserDetailsService;
import com.ranker.web.security.SecurityConfig;
import com.ranker.web.services.FavoritesListService;
import com.ranker.web.services.ItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemApiController.class)
@Import(SecurityConfig.class)
class ItemApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FavoritesListService favoritesListService;

    @MockitoBean
    private ItemService itemService;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    // ==================== PATCH /api/items/{id} ====================

    @Test
    @WithMockUser
    void shouldUpdateItemName() throws Exception {
        // Given
        UpdateItemNameRequestDTO request = new UpdateItemNameRequestDTO();
        request.setItemName("Updated Item");

        // When & Then
        mockMvc.perform(patch("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemService).updateItemName(1L, "Updated Item");
    }

    @Test
    @WithMockUser
    void shouldRejectBlankItemName() throws Exception {
        // Given
        UpdateItemNameRequestDTO request = new UpdateItemNameRequestDTO();
        request.setItemName("   ");

        // When & Then
        mockMvc.perform(patch("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(itemService, never()).updateItemName(anyLong(), anyString());
    }

    @Test
    @WithMockUser
    void shouldRejectNullItemName() throws Exception {
        // Given
        UpdateItemNameRequestDTO request = new UpdateItemNameRequestDTO();
        // itemName is null by default

        // When & Then
        mockMvc.perform(patch("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==================== Security ====================

    @Test
    void shouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(patch("/api/items/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is3xxRedirection());
    }
}
