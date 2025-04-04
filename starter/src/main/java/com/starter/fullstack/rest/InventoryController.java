package com.starter.fullstack.rest;

import com.starter.fullstack.api.Inventory;
import com.starter.fullstack.dao.InventoryDAO;
import java.util.List;
import java.util.Optional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Inventory Controller.
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {
  private final InventoryDAO inventoryDAO;

  /**
   * Default Constructor.
   * @param inventoryDAO inventoryDAO.
   */
  public InventoryController(InventoryDAO inventoryDAO) {
    Assert.notNull(inventoryDAO, "Inventory DAO must not be null.");
    this.inventoryDAO = inventoryDAO;
  }

  /**
   * Find Inventories.
   * @return List of Inventories.
   */
  @GetMapping
  public List<Inventory> findInventories() {
    return this.inventoryDAO.findAll();
  }

  /**
   * Create a new inventory
   * @param inventory from request
   * @return a newly created Inventory object.
   */
  @PostMapping
  public Inventory createInventory(@RequestBody Inventory inventory) {
    return this.inventoryDAO.create(inventory);

  }

  @PutMapping
  public Inventory updateInventory(@RequestBody Inventory inventory) {
    return this.inventoryDAO.update(inventory).orElse(null);
  }

  /**
   * deletes an inventory, if it exists
   * @param id is the id of the inventory
   */
  @DeleteMapping
  public void deleteInventory(@RequestBody List<String> id) {
    Optional<List<Inventory>> deletedInventory = this.inventoryDAO.delete(id);

    // return deletedInventory.orElse(null);
  }
}

