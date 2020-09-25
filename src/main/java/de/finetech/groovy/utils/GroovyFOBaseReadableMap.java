package de.finetech.groovy.utils;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.abas.eks.jfop.FOPException;
import de.abas.jfop.base.buffer.BaseReadableBuffer;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 * @param <T>
 */
public class GroovyFOBaseReadableMap<T extends BaseReadableBuffer> extends GroovyObjectSupport
		implements Map<String, Object>, Cloneable, Serializable, GroovyObject {

	private static final long serialVersionUID = 4146145334512673667L;
	// private String buffer = null;
	protected GroovyFOScript script;
	protected T buffer;

	@Override
	public void clear() {
	}

	public GroovyFOBaseReadableMap(T buffer, GroovyFOScript script) {
		this.script = script;
		this.buffer = buffer;
	}

	@Override
	public boolean containsKey(Object key) {
		return buffer.isVarDefined(key.toString());
	}

	@Override
	public boolean containsValue(java.lang.Object value) {
		return false;
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return null;
	}

	@Override
	public Object get(Object key) {
		try {
			// buffer.
			String skey = key.toString();
			return script.getValue(buffer.getQualifiedFieldName(skey), buffer.getStringValue(skey));
		} catch (FOPException e) {
			e.printStackTrace();
		} catch (GroovyFOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object propertyMissing(String name) {
		return this.get(name);
	}

	public String getType(Object key) {
		return buffer.getFieldType(key.toString());
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	// TODO prüfen ob es eine müglichkeit gibt alle Variablen eines Puffers zu
	// ermitteln
	@Override
	public Set<String> keySet() {
		return null;
	}

	// TODO siehe oben
	@Override
	public int size() {
		return 0;
	}

	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		// TODO implementieren
	}

	@Override
	public Object remove(java.lang.Object key) {
		return null;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}

	public Object call(Object key) {
		return this.get(key.toString());
	}

	/**
	 * erlaubt zugriff mittels Pipe, wie von FO gewohnt h|fooo anstelle von h.fooo
	 * 
	 * @param key
	 * @return
	 */
	public Object or(String key) {
		return this.get(key);
	}

}
