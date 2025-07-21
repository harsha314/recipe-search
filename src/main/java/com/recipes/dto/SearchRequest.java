package com.recipes.dto;

import lombok.Data;

@Data
public class SearchRequest {
  // filters
  String searchTerm;
  int cookingTime;
}