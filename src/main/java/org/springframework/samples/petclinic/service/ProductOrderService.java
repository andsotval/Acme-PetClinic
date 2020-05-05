/**
 * DP2 - Grupo 8
 * LAB F1.33
 * Date: 05-may-2020
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.model.Provider;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOrderService extends BaseService<ProductOrder> {

	private ProductOrderRepository	productOrderRepository;

	private EntityManager			em;


	@Autowired
	public ProductOrderService(BaseRepository<ProductOrder> repository, ProductOrderRepository productOrderRepository,
		EntityManager em) {
		super(repository);
		this.productOrderRepository = productOrderRepository;
		this.em = em;
	}

	@Transactional(readOnly = true)
	public Collection<ProductOrder> findProductOrderByOrder(int orderId) {
		return productOrderRepository.findProductOrderByOrder(orderId);
	}

	@Transactional(readOnly = true)
	public Provider findProviderByOrder(int orderId) {
		Query query = em.createNamedQuery("ProductOrder.findProvider");
		query.setMaxResults(1);
		query.setParameter(1, orderId);
		Provider provider = (Provider) query.getSingleResult();
		return provider;
	}

}
