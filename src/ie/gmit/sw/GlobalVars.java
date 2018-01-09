package ie.gmit.sw;

public class GlobalVars {
	
	private static String DB_PATH;
	private static String FILE_PATH;
	private static int SHINGLE_SIZE;
	private static int MAX_HASHES;
	public static String getDB_PATH() {
		return DB_PATH;
	}
	public static void setDB_PATH(String dB_PATH) {
		DB_PATH = dB_PATH;
	}
	public static String getFILE_PATH() {
		return FILE_PATH;
	}
	public static void setFILE_PATH(String fILE_PATH) {
		FILE_PATH = fILE_PATH;
	}
	public static int getSHINGLE_SIZE() {
		return SHINGLE_SIZE;
	}
	public static void setSHINGLE_SIZE(int sHINGLE_SIZE) {
		SHINGLE_SIZE = sHINGLE_SIZE;
	}
	public static int getMAX_HASHES() {
		return MAX_HASHES;
	}
	public static void setMAX_HASHES(int mAX_HASHES) {
		MAX_HASHES = mAX_HASHES;
	}
	
	
}
