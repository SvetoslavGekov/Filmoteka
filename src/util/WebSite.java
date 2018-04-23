package util;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import exceptions.InvalidGenreDataException;
import exceptions.InvalidOrderDataException;
import exceptions.InvalidProductCategoryDataException;
import exceptions.InvalidProductDataException;
import exceptions.InvalidUserDataException;
import model.dao.ProductDao;
import model.dao.nomenclatures.GenreDao;
import model.dao.nomenclatures.ProductCategoryDao;
import model.nomenclatures.Genre;
import model.nomenclatures.ProductCategory;
import util.productFilters.ProductQueryInfo;
import util.productFilters.ProductQueryInfoDeserializer;
import util.taskExecutors.CustomTaskExecutor;
import util.taskExecutors.ExpiredProductsDeleter;
import util.taskExecutors.ExpiringProductsNotifier;

public final class WebSite {
	// Fields
	private static final LocalTime TASKS_STARTING_TIME = LocalTime.now().withHour(18).withMinute(20).withSecond(00);
	private static final Map<Integer,Genre> GENRES = new TreeMap<>();
	private static final Map<Integer, ProductCategory> PRODUCT_CATEGORIES = new TreeMap<>();
	private static final List<CustomTaskExecutor> TASKS = new ArrayList<>();
	
	// Constructors --> never instantiated
	private WebSite() {

	}

	// Methods
	public static Genre getGenreById(int id) {
		return GENRES.get(id);
	}
	
	public static void addGenre(Genre g) {
		if (g != null) {
			GENRES.put(g.getId(),g);
		}
	}
	
	public static Map<Integer,Genre> getAllGenres() {
		return Collections.unmodifiableMap(GENRES);
	}
	
	public static ProductCategory getProductCategoryById(int id) {
		return PRODUCT_CATEGORIES.get(id);
	}
	
	public static void addProductCategory(ProductCategory pc) {
		if (pc != null) {
			PRODUCT_CATEGORIES.put(pc.getId(),pc);
		}
	}

	public static Collection<CustomTaskExecutor> getAllTasks(){
		return Collections.unmodifiableCollection(TASKS);
	}

	public static void main(String[] args) throws SQLException, InvalidGenreDataException, InvalidProductDataException,
	InvalidUserDataException, InvalidOrderDataException, InvalidProductCategoryDataException {
		//Load all genres
		GENRES.putAll(GenreDao.getInstance().getAllGenres());
		
		//Load all product categories
		PRODUCT_CATEGORIES.putAll(ProductCategoryDao.getInstance().getAllProductCategories());
		
		//Create all utility tasks
		TASKS.add(new CustomTaskExecutor(ExpiringProductsNotifier.getInstance())); //Expiring products notifier
		TASKS.add(new CustomTaskExecutor(ExpiredProductsDeleter.getInstance()));// Expired products deleter
		
		//Start all task at the same time
		for (CustomTaskExecutor taskExecutor : TASKS) {
			taskExecutor.startExecutionAt(TASKS_STARTING_TIME.getHour(), TASKS_STARTING_TIME.getMinute(), TASKS_STARTING_TIME.getSecond());
		}
		
		//Example for a productQueryInfo deserialization
		String json = "{\"name\":null,\"minReleaseYear\":1984,\"maxReleaseYear\":2008,\"minDuration\":22,\"maxDuration\":194,\"minBuyCost\":5,\"maxBuyCost\":15,\"minRentCost\":3,\"maxRentCost\":10,\"genres\":[{\"id\":1,\"value\":\"ACTION\"},{\"id\":2,\"value\":\"ADVENTURE\"},{\"id\":3,\"value\":\"ANIMATION\"},{\"id\":4,\"value\":\"BIOGRAPHY\"},{\"id\":5,\"value\":\"COMEDY\"},{\"id\":6,\"value\":\"CRIME\"},{\"id\":7,\"value\":\"DOCUMENTARY\"},{\"id\":8,\"value\":\"DRAMA\"},{\"id\":9,\"value\":\"FAMILY\"},{\"id\":10,\"value\":\"FANTASY\"},{\"id\":11,\"value\":\"HISTORY\"},{\"id\":12,\"value\":\"HORROR\"},{\"id\":13,\"value\":\"MUSIC\"},{\"id\":14,\"value\":\"MUSICAL\"},{\"id\":15,\"value\":\"MYSTERY\"},{\"id\":16,\"value\":\"ROMANCE\"},{\"id\":17,\"value\":\"SCIFI\"},{\"id\":18,\"value\":\"SPORT\"},{\"id\":19,\"value\":\"SUPERHERO\"},{\"id\":20,\"value\":\"THRILLER\"},{\"id\":21,\"value\":\"WAR\"},{\"id\":22,\"value\":\"WESTERN\"},{\"id\":31,\"value\":\"tAKO\"}],\"orderedBy\":\"name\",\"isAscending\":true}";
		
		//Create GSON builder
		GsonBuilder gBuilder = new GsonBuilder().setPrettyPrinting();
		gBuilder.registerTypeAdapter(ProductQueryInfo.class, new ProductQueryInfoDeserializer());
		Gson gson = gBuilder.create();
		
		ProductQueryInfo pqi = gson.fromJson(json, ProductQueryInfo.class);
		System.out.println(pqi);
		
		for (Integer inti : ProductDao.getInstance().getFilteredProducts(pqi)) {
			System.out.println(inti);
		}
		
	}
}
