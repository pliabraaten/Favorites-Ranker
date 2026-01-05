package com.ranker.web.dto;


import com.ranker.web.models.FavoritesList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDTO {

    private Long id;
    private String name;
    private int position;
    private Long listId;

}
