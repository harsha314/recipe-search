package com.recipes.service;

import java.util.List;

import com.recipes.dto.RecipeDTO;

public interface SearchService {
  List<RecipeDTO> search(String searchTerm, int cookingTime);
}
