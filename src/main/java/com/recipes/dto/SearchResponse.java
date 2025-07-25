package com.recipes.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SearchResponse {
  List<RecipeDTO> recipes;
}