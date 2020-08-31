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

	@Override
	public Object invokeMethod(Object object, String name, Object[] args) {
		// implementiert "test" >> 3
		if (name.equals("rightShift")) {
			if (args.length == 1) {
				if (args[0] instanceof Integer) {
					String str = object.toString();
					int x = ((Integer) args[0]).intValue();
					if (str.length() > x) {
						return str.substring(str.length() - x);
					}
					return str;
				} else {
					throw new IllegalArgumentException("wrong argument type, should be integer");
				}
			} else {
				throw new IllegalArgumentException("too many arguments");
			}
		}
		// implementiert "test" << 3
		if (name.equals("leftShift")) {
			if (args.length == 1) {
				if (args[0] instanceof Integer) {
					String str = object.toString();
					int x = ((Integer) args[0]).intValue();
					if (str.length() > x) {
						return str.substring(0,x);
					}
					return str;
				} else {
					throw new IllegalArgumentException("wrong argument type, should be integer");
				}
			} else {
				throw new IllegalArgumentException("too many arguments");
			}
		}
		else {
			return super.invokeMethod(object, name, args);
		}
	}

}
