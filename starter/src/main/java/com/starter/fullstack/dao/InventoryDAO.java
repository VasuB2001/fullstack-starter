package com.starter.fullstack.dao;

import com.starter.fullstack.api.Inventory;
import java.util.List;
import java.util.Optional;
import javax.annotation.PostConstruct;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.util.Assert;

/**
 * Inventory DAO
 */
public class InventoryDAO {
  private final MongoTemplate mongoTemplate;
  private static final String NAME = "name";
  private static final String PRODUCT_TYPE = "productType";

  private static final String ID = "id";

  /**
   * Default Constructor.
   * @param mongoTemplate MongoTemplate.
   */
  public InventoryDAO(MongoTemplate mongoTemplate) {
    Assert.notNull(mongoTemplate, "MongoTemplate must not be null.");
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * Constructor to build indexes for rate blackout object
   */
  @PostConstruct
  public void setupIndexes() {
    IndexOperations indexOps = this.mongoTemplate.indexOps(Inventory.class);
    indexOps.ensureIndex(new Index(NAME, Sort.Direction.ASC));
    indexOps.ensureIndex(new Index(PRODUCT_TYPE, Sort.Direction.ASC));
  }

  /**
   * Find All Inventory.
   * @return List of found Inventory.
   */
  public List<Inventory> findAll() {
    return this.mongoTemplate.findAll(Inventory.class);
  }

  /**
   * Save Inventory.
   * @param inventory Inventory to Save/Update.
   * @return Created/Updated Inventory.
   */
  public Inventory create(Inventory inventory) {
    inventory.setId(null);
    return mongoTemplate.insert(inventory);
  }

  /**
   * Retrieve Inventory.
   * @param id Inventory id to Retrieve.
   * @return Found Inventory.
   */
  public Optional<Inventory> retrieve(String id) {
    Query query = new Query(Criteria.where(ID).is(id));
    return Optional.ofNullable(mongoTemplate.findOne(query, Inventory.class));
  }

  /**
   * Update Inventory.
   * @param id Inventory id to Update.
   * @param inventory Inventory to Update.
   * @return Updated Inventory.
   */
  public Optional<Inventory> update(String id, Inventory inventory) {
    Query query = new Query(Criteria.where(ID).is(id));
    Update update = new Update();
    update.set("name", inventory.getName());
    update.set("productType", inventory.getProductType());
    update.set("description", inventory.getDescription());
    update.set("averagePrice", inventory.getAveragePrice());
    update.set("amount", inventory.getAmount());
    update.set("unitOfMeasurement", inventory.getUnitOfMeasurement());
    update.set("bestBeforeDate", inventory.getBestBeforeDate());
    update.set("neverExpires", inventory.isNeverExpires());
    update.set("availableStores", inventory.getAvailableStores());
    return Optional.ofNullable(mongoTemplate.findAndModify(query, update, Inventory.class));
  }

  /**
   * Delete Inventory By Id.
   * @param id id of Inventory.
   * @return Deleted Inventory.
   */
  public Optional<Inventory> delete(String id) {

    Query query = new Query(Criteria.where(ID).is(id));
    return Optional.ofNullable(mongoTemplate.findAndRemove(query, Inventory.class));
  }
}
