package com.hanaro.dto.product;

import com.hanaro.entity.Product;
import lombok.Getter;

@Getter
public class ProductResponse {

	private Long id;
	private String name;
	private Product.ProductType productType;
	private Long depositAmount;
	private Product.PaymentCycle paymentCycle;
	private Integer subscriptionPeriod;
	private Double maturityRate;
	private Double earlyTerminationRate;
	private String imageUrl;

	public ProductResponse(Product product) {
		this.id = product.getId();
		this.name = product.getName();
		this.productType = product.getProductType();
		this.depositAmount = product.getDepositAmount();
		this.paymentCycle = product.getPaymentCycle();
		this.subscriptionPeriod = product.getSubscriptionPeriod();
		this.maturityRate = product.getMaturityRate();
		this.earlyTerminationRate = product.getEarlyTerminationRate();
		this.imageUrl = product.getImageUrl();
	}
}
