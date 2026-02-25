package com.ranker.web.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ranker.web.dto.ItemPositionDTO;
import com.ranker.web.dto.RankRequestDTO;
import com.ranker.web.dto.UpdateListNameRequestDTO;
import com.ranker.web.dto.UpdateSortedCountRequest;
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

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FavoritesListApiController.class)
@Import(SecurityConfig.class)
class FavoritesListApiControllerTest {

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

    // ==================== POST /api/lists/{id}/save-rankings ====================

    @Test
    @WithMockUser
    void shouldSaveRankings() throws Exception {
        // Given
        RankRequestDTO request = new RankRequestDTO();
        request.setItems(Arrays.asList(
                new ItemPositionDTO(1L, 2),
                new ItemPositionDTO(2L, 1)));

        // When & Then
        mockMvc.perform(post("/api/lists/1/save-rankings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(itemService).updatePositions(eq(1L), anyList());
    }

    // ==================== PATCH /api/lists/{id} ====================

    @Test
    @WithMockUser
    void shouldUpdateListName() throws Exception {
        // Given
        UpdateListNameRequestDTO request = new UpdateListNameRequestDTO();
        request.setListName("Updated Name");

        // When & Then
        mockMvc.perform(patch("/api/lists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(favoritesListService).updateListName(1L, "Updated Name");
    }

    @Test
    @WithMockUser
    void shouldRejectBlankListName() throws Exception {
        // Given
        UpdateListNameRequestDTO request = new UpdateListNameRequestDTO();
        request.setListName("   ");

        // When & Then
        mockMvc.perform(patch("/api/lists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verify(favoritesListService, never()).updateListName(anyLong(), anyString());
    }

    @Test
    @WithMockUser
    void shouldRejectNullListName() throws Exception {
        // Given
        UpdateListNameRequestDTO request = new UpdateListNameRequestDTO();
        // listName is null by default

        // When & Then
        mockMvc.perform(patch("/api/lists/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ==================== PATCH /api/lists/{id}/sorted-count ====================

    @Test
    @WithMockUser
    void shouldUpdateSortedCount() throws Exception {
        // Given
        UpdateSortedCountRequest request = new UpdateSortedCountRequest(5);

        // When & Then
        mockMvc.perform(patch("/api/lists/1/sorted-count")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(favoritesListService).updateSortedCount(1L, 5);
    }

    // ==================== Security ====================

    @Test
    void shouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(post("/api/lists/1/save-rankings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().is3xxRedirection());
    }
}
