package groovy.runtime.metaclass.java.lang;

import groovy.lang.DelegatingMetaClass;
import groovy.lang.MetaClass;

public class IntegerMetaClass extends DelegatingMetaClass {

	public IntegerMetaClass(Class<?> theClass) {
		super(theClass);
	}

	public IntegerMetaClass(MetaClass metaClass) {
		super(metaClass);
	}

	@Override
	public Object invokeMethod(Object object, String name, Object[] args) {
		// implementiert 3 >> "test"
		if (name.equals("rightShift")) {
			if (args.length == 1) {
				if (args[0] instanceof String) {
					String str = args[0].toString();
					int x = ((Integer) object).intValue();
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
		// implementiert 3 << "test"
		if (name.equals("leftShift")) {
			if (args.length == 1) {
				if (args[0] instanceof String) {
					String str = args[0].toString();
					int x = ((Integer) object).intValue();
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
