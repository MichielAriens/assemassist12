package logic.assemblyline;

import logic.car.CarOrder;

public class OrderRegistrator {
		private static OrderRegistrator orderRegistrator= new OrderRegistrator();
		private OrderRegistrator(){
			
		}
		public static OrderRegistrator getInstance(){
			return orderRegistrator;
		}
		
		public void addOrder(CarOrder order) {
			
		}
}
