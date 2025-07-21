package com.recipes.data;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.nio.file.Path;
import java.util.ArrayList;

import org.apache.commons.csv.CSVRecord;

public class DataImport {
  public void importData() {
    Path recipesCSVPath = Path.of("src", "main", "resources", "recipes.csv");
    // File file = new Files(Paths.)
    // try(Reader reader = new FileReader(file)) {

    // }
    Iterable<CSVRecord> csvRecords = new ArrayList<>();
    // for (CSVRecord csvRecord : csvRecords) {
    // csvRecord.get("id");
    // csvRecord.get("recipe_name");
    // csvRecord.get("prep_time");
    // csvRecord.get("cook_time");
    // csvRecord.get("ingredients");
    // csvRecord.get("rating");
    // }
  }
}
