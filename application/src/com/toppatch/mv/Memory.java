package com.toppatch.mv;

public class Memory {

	/**
	 * I just want to download a licence key and let another class to use it after the registration is done to register the device so that we can use the samsung apis.
	 */	
	//private static String licenceKey =  "89A4DE09F576AC09D86A39F706F8823195151D9427EE8B4E1444ACD117C1837E3E1406F8020A8C42D662280349F0731E3B3ADCEF14B1792A2993A0CA3D3F5363";
	private static String licenceKey =  "8AB678771F7850EC617704FEBAA412E45116EE519C5E71CBBBCCFBBD90911596E21BF3334AA59B07E65FDDDBA5F2292CB88D766B7D8AAD7FCEB691E46ACF4411";
	/**
	 * Set the licence key
	 * @param l String containing the licence key
	 */
	public static void setLicenceKey(String l){
		licenceKey=l;
	}
	
	/**
	 * Use to get the licence key. This method can be used only once. After that the licence key should be requested again.
	 * @return the stored licence key. Returns null the second time onward it's called.
	 */
	public static String getLicenceKey(){
		/*String temp = licenceKey;
		licenceKey=null;*/
		return licenceKey;
	}
	
	/**
	 * Sets the licence key to null. Useful in protecting the licence key 
	 */
	public static void resetLicenceKey(){
		licenceKey=null;
	}
}
