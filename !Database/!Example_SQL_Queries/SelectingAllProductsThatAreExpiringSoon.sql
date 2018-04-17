SELECT up.user_id, up.product_id, up.validity, u.first_name, u.last_name, u.username, u.password, u.email
	FROM user_has_products AS up
	JOIN users AS u USING (user_id)
	WHERE validity = DATE_ADD(curdate(), INTERVAL 1 DAY);