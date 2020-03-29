package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Order;

public interface OrderRepository extends CrudRepository<Order, Integer> {}
