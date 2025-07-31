package org.flighApp.mongoDb;

import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.FindOptions;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import java.util.List;
import org.bson.conversions.Bson;
import org.flighApp.Flight.models.PaginationParams;
import org.flighApp.utils.Utils;

import javax.swing.*;

public class MongoDbUtil {

  public static <T> Uni<List<T>> aggregate(
      ReactiveMongoCollection<T> collection, List<Bson> pipeline) {
    return collection.aggregate(pipeline).collect().asList();
  }

  public static <T, Z> Uni<List<Z>> aggregate(
      ReactiveMongoCollection<T> collection, List<Bson> pipeline, Class<Z> clazz) {
    return collection.aggregate(pipeline, clazz).collect().asList();
  }

  public static <T> Uni<List<T>> find(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.find(filter).collect().asList();
  }

  public static <T> Uni<List<T>> findAll(ReactiveMongoCollection<T> collection) {
    return collection.find().collect().asList();
  }

  public static <T> Uni<Boolean> exists(ReactiveMongoCollection<T> collection, Bson filter) {
    return collection.find(filter).collect().asList().map(Utils::notNull);
  }

  public static <T> Uni<List<T>> find(
      ReactiveMongoCollection<T> collection, PaginationParams paginationParams) {
    FindOptions options = paginationParams.toFindOptions();
    return collection.find(options).collect().asList();
  }
}
