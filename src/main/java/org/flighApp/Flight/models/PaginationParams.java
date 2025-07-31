package org.flighApp.Flight.models;

import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.FindOptions;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.flighApp.utils.Utils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PaginationParams {
  private int page;
  private int size;
  private int limit;
  private int skip;
  private String field;
  private String direction;
  private List<String> selectedFields;

  public FindOptions toFindOptions() {
    String sortField = (Utils.isNull(field) || field.trim().isEmpty() ? "ticketType" : field);
    Bson sort =
        (Utils.isNull(direction) || direction.equalsIgnoreCase("ASC")
            ? Sorts.ascending(sortField)
            : Sorts.descending(sortField));

    FindOptions options = new FindOptions().skip(page * size).limit(size).sort(sort);

    if (Utils.notNull(selectedFields) && !selectedFields.isEmpty()) {
      Document projection = new Document();
      selectedFields.forEach(field -> projection.append(field, 1));
      options.projection(projection);
    }

    return options;
  }
}
