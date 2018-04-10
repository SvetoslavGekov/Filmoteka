package user;

import products.Product;

public interface IAdministrator {
	
	void createNewProduct();

	void editProductInfo();
	
	void deleteProduct();
	
	void addProductToCatalog(Product product);
	
	void removeProductFromCatalog(Product product);
	
	void printProductsManagementMenu();
	
	boolean selectFromProductsManagementMenu();
}
