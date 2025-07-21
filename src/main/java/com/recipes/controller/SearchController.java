package com.recipes.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.recipes.dto.RecipeDTO;
import com.recipes.dto.SearchRequest;
import com.recipes.dto.SearchResponse;
import com.recipes.service.SearchService;

@RestController
@RequestMapping(path = "/recipe-search")
public class SearchController {

  SearchService searchService;

  public SearchController(SearchService searchService) {
    this.searchService = searchService;
  }

  @RequestMapping(path = "/search", method = RequestMethod.POST)
  public SearchResponse searchRecipe(@RequestBody SearchRequest searchRequest) {
    String searchTerm = searchRequest.getSearchTerm();
    int cookingTime = searchRequest.getCookingTime();
    List<RecipeDTO> recipes = this.searchService.search(searchTerm, cookingTime);
    SearchResponse searchResponse = SearchResponse.builder().recipes(recipes).build();
    return searchResponse;
  }
}