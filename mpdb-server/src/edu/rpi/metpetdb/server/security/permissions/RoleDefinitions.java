package edu.rpi.metpetdb.server.security.permissions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class RoleDefinitions {
	
	/** Stores what roles have what privilages, key is the role rank */
	public final static Map<Integer, Collection<Privilages>> roleDefinitions;
	
	static {
		roleDefinitions = new HashMap<Integer, Collection<Privilages>>();
		roleDefinitions.put(-1, new ArrayList<Privilages>()); //non-logged in user
		roleDefinitions.put(0, new ArrayList<Privilages>());
		roleDefinitions.put(1, new ArrayList<Privilages>());
		roleDefinitions.put(2, new ArrayList<Privilages>());
		//Anonymous
		roleDefinitions.get(-1).add(Privilages.LOAD_PUBLIC_DATA);
		//Member
		roleDefinitions.get(0).addAll(roleDefinitions.get(-1));
		//Contributor
		roleDefinitions.get(1).add(Privilages.LOAD_PRIVATE_DATA);
		roleDefinitions.get(1).add(Privilages.SAVE_PRIVATE_DATA);
		roleDefinitions.get(1).add(Privilages.SAVE_OWN_PUBLIC_DATA);
		roleDefinitions.get(1).addAll(roleDefinitions.get(0));
	}

}
