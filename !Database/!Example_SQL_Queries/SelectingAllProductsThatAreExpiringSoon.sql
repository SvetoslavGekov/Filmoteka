SELECT user_id, product_id, validity FROM user_has_products
	WHERE validity = DATE_ADD(curdate(), INTERVAL 1 DAY);