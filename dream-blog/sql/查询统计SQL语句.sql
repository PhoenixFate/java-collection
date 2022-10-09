-- 统计各分类下的文章数
CREATE VIEW v_category_total
	AS
SELECT
	t1.`name`,
	IFNULL(SUM(t3.total), 0) AS `value`
FROM
	mxg_category t1
LEFT JOIN mxg_label t2 ON t1.id = t2.category_id
LEFT JOIN (
	SELECT
		m2.label_id,
		COUNT(m1.id) total
	FROM
		mxg_article m1
	JOIN mxg_article_label m2 ON m1.id = m2.article_id
	WHERE
		m1. STATUS = 2
	AND m1.ispublic = 1
	GROUP BY
		m2.label_id
) t3 ON t2.id = t3.label_id
GROUP BY
	t1.`name`;
	
	
-- 通过视图查询数据
-- SELECT `name`, `value` FROM v_category_total;
	
	
	
-- 查询近6个月发布的文章数
CREATE VIEW v_month_aritcle_total
AS
SELECT t1.`year_month`, count(t2.id)  AS total FROM 
(
-- 先查询近6个月的月份
SELECT DATE_FORMAT(CURDATE(), '%Y-%m') AS `year_month` 
UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 1 MONTH), '%Y-%m') AS `year_month` 
UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 2 MONTH), '%Y-%m') AS `year_month` 
UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 3 MONTH), '%Y-%m') AS `year_month` 
UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 4 MONTH), '%Y-%m') AS `year_month` 
UNION SELECT DATE_FORMAT((CURDATE() - INTERVAL 5 MONTH), '%Y-%m') AS `year_month` 
) t1 
LEFT JOIN	mxg_article t2 
	ON t1.`year_month` = DATE_FORMAT(t2.create_date, '%Y-%m') 
	-- 注意：使用使用 AND, 不要使用 WHERE 不然月份没有发布文章就不会显示对应月份出来
	AND  t2.`status` = 2 AND t2.ispublic = 1
GROUP BY t1.`year_month`;
		
-- 通过视图查询数据
-- SELECT `year_month`, `total` FROM v_month_aritcle_total