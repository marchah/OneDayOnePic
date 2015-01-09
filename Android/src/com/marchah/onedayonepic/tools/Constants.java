package com.marchah.onedayonepic.tools;

public final class Constants {

    public final class SharedPreferences {
	public final static String Timer = "TIMER_SYNCHRO";
	public final static String IdCategorie = "ID_CATEGORIE";
	public final static String IdUser = "ID_USER";
	public final static String IsAuto = "IsAuto";
    }
    
    public final class API {
	private final static String URL = "http://5.135.163.236:3344";
	public final static String Picture = URL + "/picture/";
	public final static String Init = URL + "/init";
	public final static String Categories = URL + "/categories/";
    }
    
    public final class Style {
	public final static String Font = "fonts/ComingSoon.ttf";
    }
    
    public static class Service {
	public final static int IdNotification = 1;
	public final static int DefaultTimer = 4242;
    }
    
    public static class IntentAction {
	public final static String SetWallpaper = "com.marchah.onedayonepic.intent.action.SET";
	public final static String DDLImage = "com.marchah.onedayonepic.intent.action.DDL";
    }
}
