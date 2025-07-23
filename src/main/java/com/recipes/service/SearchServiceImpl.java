package com.recipes.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.recipes.dto.RecipeDTO;

@Service
public class SearchServiceImpl implements SearchService {

  Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);

  public SearchServiceImpl() {
  }

  @Override
  public List<RecipeDTO> search(String searchTerm, int cookingTime) {

    try {
      logger.info("client connected");

    } catch (Exception e) {

    }
    return null;
  }
}