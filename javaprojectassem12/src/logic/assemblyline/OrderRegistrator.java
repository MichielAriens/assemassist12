package logic.assemblyline;

public class OrderRegistrator {
		private static OrderRegistrator orderRegistrator= new OrderRegistrator();
		private OrderRegistrator(){
			
		}
		public static OrderRegistrator getInstance(){
			return orderRegistrator;
		}
}
