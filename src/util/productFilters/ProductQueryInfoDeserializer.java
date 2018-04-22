package util.productFilters;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import exceptions.InvalidProductQueryInfoException;
import model.nomenclatures.Genre;
import util.WebSite;

public class ProductQueryInfoDeserializer implements JsonDeserializer<ProductQueryInfo> {

	@Override
	public ProductQueryInfo deserialize(JsonElement json, Type ProductQueryInfo, JsonDeserializationContext context)
			throws JsonParseException {
		// Start deserialization of the JSON string
		JsonObject jsonObject = json.getAsJsonObject();

		// Deserialize the individual fields
		JsonElement jName = jsonObject.get("name");
		String name = !jName.isJsonNull() ? jName.getAsString() : null;
		
		int minReleaseYear = jsonObject.get("minReleaseYear").getAsInt();
		int maxReleaseYear = jsonObject.get("maxReleaseYear").getAsInt();
		int minDuration = jsonObject.get("minDuration").getAsInt();
		int maxDuration = jsonObject.get("maxDuration").getAsInt();
		double minBuyCost = jsonObject.get("minBuyCost").getAsDouble();
		double maxBuyCost = jsonObject.get("maxBuyCost").getAsDouble();
		double minRentCost = jsonObject.get("minRentCost").getAsDouble();;
		double maxRentCost = jsonObject.get("maxRentCost").getAsDouble();
		String orderedBy = jsonObject.get("orderedBy").getAsString();
		boolean isAscending = jsonObject.get("isAscending").getAsBoolean();
		
		//Deserialize the array of genres
		List<Genre> genres = new ArrayList<Genre>();
		
		JsonArray jsonGenres = jsonObject.get("genres").getAsJsonArray();
		for (int i = 0; i< jsonGenres.size();i++) {
			JsonObject genreObject = jsonGenres.get(i).getAsJsonObject();
		
			Integer genreId = genreObject.get("id").getAsInt();
			genres.add(WebSite.getGenreById(genreId));
			
		}
		
		//Create new ProductQueryInfo instance and return it
		try {
			return new ProductQueryInfo(name, minReleaseYear, maxReleaseYear, minDuration,
					maxDuration, minBuyCost, maxBuyCost, minRentCost, maxRentCost, genres, orderedBy, isAscending);
		}
		catch (InvalidProductQueryInfoException e) {
			throw new JsonParseException(e.getMessage(), e);
		}
	}

}
