package com.recipes.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.recipes.dto.RecipeDTO;
import com.recipes.entity.Recipe;

@Service
public class SearchServiceImpl implements SearchService {

  public SearchServiceImpl() {
    // this.elasticsearchOperations = elasticsearchOperations;
  }

  @Override
  public List<RecipeDTO> search(String searchTerm, int cookingTime) {

    return null;
  }

  private RecipeDTO convertToDTO(Recipe recipe) {
    RecipeDTO dto = new RecipeDTO();
    // dto.setId(recipe.getId());
    // dto.setName(recipe.getRecipeName());
    // dto.setPrepTime(recipe.getPrepTime());
    // dto.setCookTime(recipe.getCookTime());
    // dto.setIngredients(recipe.getIngredients());
    // dto.setRating(recipe.getRating());
    return dto;
  }
}