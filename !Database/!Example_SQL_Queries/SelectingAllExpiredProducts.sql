SELECT user_id, product_id, validity FROM user_has_products
	WHERE validity < CURDATE();