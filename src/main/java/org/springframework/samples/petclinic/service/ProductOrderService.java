
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.ProductOrder;
import org.springframework.samples.petclinic.repository.BaseRepository;
import org.springframework.samples.petclinic.repository.ProductOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductOrderService extends BaseService<ProductOrder> {

	private ProductOrderRepository productOrderRepository;


	@Autowired
	public ProductOrderService(BaseRepository<ProductOrder> repository, ProductOrderRepository productOrderRepository) {
		super(repository);
		this.productOrderRepository = productOrderRepository;
	}

	@Transactional(readOnly = true)
	public Iterable<ProductOrder> findProductOrderByOrder(int orderId) {
		return productOrderRepository.findProductOrderByOrder(orderId);
	}

}
