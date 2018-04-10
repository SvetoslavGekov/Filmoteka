package model;

import exceptions.InvalidGenreDataException;
import validation.Supp;

public class Genre {
	//Fields
	private int id;
	private String value;


	//Constructors
	//Constructor for creating a new genre
	public Genre(String value) throws InvalidGenreDataException {
		setValue(value);
	}

	//Constructor for loading an existing genre from the DB
	public Genre(int id, String value) throws InvalidGenreDataException {
		this(value);
		setId(id);
	}
	
	//Methods
	@Override
	public String toString() {
		return String.format("Id: %d	Genre: %s", this.id, this.value);
	}

	//Setters
	public void setId(int id) throws InvalidGenreDataException {
		if(id > 0) {
			this.id = id;
		}
		else {
			throw new InvalidGenreDataException("Invalid genre name.");
		}
	}
	
	public void setValue(String value) throws InvalidGenreDataException {
		if(Supp.isValidStr(value)) {
			this.value = value;
		}
		else {
			throw new InvalidGenreDataException("Invalid genre id.");
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
