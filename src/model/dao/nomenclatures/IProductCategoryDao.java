package model.dao.nomenclatures;

import java.sql.SQLException;
import java.util.Map;

import exceptions.InvalidProductCategoryDataException;
import model.nomenclatures.ProductCategory;

public interface IProductCategoryDao {

	void saveProductCategory(ProductCategory pc) throws SQLException, InvalidProductCategoryDataException;

	void updateProductCategory(ProductCategory pc) throws SQLException;

	Map<Integer, ProductCategory> getAllProductCategories() throws SQLException, InvalidProductCategoryDataException;

}
