package com.toppatch.mv;

import java.util.ArrayList;
import java.util.Iterator;

public class Permissions {
	
	private static ArrayList<String> permissions = new ArrayList<String>();
	
	public static boolean hasPermission(String permission){
		Iterator<String> it=permissions.iterator();
		String p = null;
		while((p=it.next())!=null){
			if(p.equals(permission)){
				return true;
			}
		}
		return false;
	}
}