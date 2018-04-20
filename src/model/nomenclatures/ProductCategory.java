package model.nomenclatures;

import exceptions.InvalidProductCategoryDataException;
import validation.Supp;

public final class ProductCategory {
	//Fields
	private int id;
	private String value;


	//Constructors
	//Constructor for creating a new genre
	public ProductCategory(String value) throws InvalidProductCategoryDataException {
		setValue(value);
	}

	//Constructor for loading an existing genre from the DB
	public ProductCategory(int id, String value) throws InvalidProductCategoryDataException {
		this(value);
		setId(id);
	}
	
	//Methods
	@Override
	public String toString() {
		return String.format("Id: %d	Category: %s", this.id, this.value);
	}

	//Setters
	public void setId(int id) throws InvalidProductCategoryDataException {
		if(id > 0) {
			this.id = id;
		}
		else {
			throw new InvalidProductCategoryDataException("Invalid product category name.");
		}
	}
	
	public void setValue(String value) throws InvalidProductCategoryDataException {
		if(Supp.isValidStr(value)) {
			this.value = value;
		}
		else {
			throw new InvalidProductCategoryDataException("Invalid product category id.");
		}
	}
	
	//Getters
	public int getId() {
		return this.id;
	}
	
	public String getValue() {
		return this.value;
	}
}
