package controller.manager;

import model.dao.OrderDao;

public final class OrderManager {
	//Fields
	private static OrderManager instance;
	private OrderDao dao;
	
	private OrderManager() {
		//Instantiate the dao object
		this.dao = OrderDao.getInstance();
	}

	public static synchronized OrderManager getInstance() {
		if (instance == null) {
			instance = new OrderManager();
		}
		return instance;
	}
}
