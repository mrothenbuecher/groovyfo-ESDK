package groovy.runtime.metaclass.java.lang;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;

public class StringMetaClass extends DelegatingMetaClass {

	public StringMetaClass(Class<?> theClass) {
		super(theClass);
	}

	public StringMetaClass(MetaClass metaClass) {
		super(metaClass);
	}

	/**
	 * 
	 * see https://extranet.abas.de/sub_de/help/hd/html/9.1.5.14.html#FO.OPER.STR.AZ
	 * 
	 */
	@Override
	public Object invokeMethod(Object object, String name, Object[] args) {
		// implementiert +"test"
		if (name.equals("positive")) {
			return object.toString().length();
		}
		// implementiert -"test"
		if (name.equals("negative")) {
			return object.toString().toUpperCase().replaceAll("\u00C4", "AE").replaceAll("\u00DC", "UE")
					.replaceAll("\u00D6", "OE").replaceAll("\u00DF", "SS");
		}

		// implementiert "test"/"test"
		// Achtung implementiert keinen Matchcode sondern regex
		if (name.equals("div")) {
			if (args != null && args.length == 1 && args[0] instanceof String) {
				return object.toString().matches(args[0].toString());
			}
		}

		// implementiert "test" >> 3
		if (name.equals("rightShift")) {
			if (args != null && args.length == 1) {
				if (args[0] instanceof Integer) {
					String str = object.toString();
					int x = ((Integer) args[0]).intValue();
					if (str.length() > x) {
						return str.substring(str.length() - x);
					}
					return str;
				} else if (args[0] instanceof String && args[0].toString().isEmpty()) {
					return object.toString().replaceAll("\\s+$", "");
				}
			}
		}
		// implementiert "test" << 3
		if (name.equals("leftShift")) {
			if (args != null && args.length == 1) {
				if (args[0] instanceof Integer) {
					String str = object.toString();
					int x = ((Integer) args[0]).intValue();
					if (str.length() > x) {
						return str.substring(0, x);
					}
					return str;
				} else if (args[0] instanceof String && args[0].toString().isEmpty()) {
					return object.toString().replaceAll("^\\s+", "");
				}
			}
		}
		return super.invokeMethod(object, name, args);
	}

}
