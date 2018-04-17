SELECT g.value, g.genre_id, p.product_id, p.name, p.release_year, p.pg_rating, p.duration, p.rent_cost,
	p.buy_cost, p.description, p.poster, p.trailer, p.writers, p.actors, p.sale_percent, p.sale_validity
	FROM products AS p
	JOIN product_has_genres USING(product_id) JOIN genres AS g USING(genre_id) 
	WHERE 
	(p.name IS NULL OR p.name LIKE '%%')
	AND (p.release_year IS NULL OR (p.release_year >= 1990 AND p.release_year <= 2005))
	AND (p.duration IS NULL OR (p.duration >=15 AND p.duration <= 300))
	AND (p.buy_cost IS NULL OR (p.buy_cost >= 3 AND p.buy_cost <= 20))
	AND (p.rent_cost IS NULL OR (p.rent_cost >= 3 AND p.rent_cost <= 20))
	AND (g.genre_id IS NULL OR g.genre_id IN (1,3,8,10,16))
	GROUP BY p.name ORDER BY product_id DESC;