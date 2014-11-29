package com.marchah.onedayonepic.tools;

public final class Constants {

	public final class SharedPreferences {
		public final static String Timer = "TIMER_SYNCHRO";
		public final static String IdCategorie = "ID_CATEGORIE";
		public final static String IsAuto = "IsAuto";
	}
	
	public final class API {
		private final static String URL = ;
		public final static String 	Picture = URL + "/picture.php";
		public final static String 	Index = URL + "/index.php";
	}
	
	public static class Service {
		public static int IdNotification = 1;
		public final static int		DefaultTimer = 4242;
	}
	
	public static class IntentAction {
		public final static String SetWallpaper = "com.marchah.onedayonepic.intent.action.SET";
		public final static String DDLImage = "com.marchah.onedayonepic.intent.action.DDL";
	}
}
