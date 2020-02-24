package de.finetech.groovy.utils;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import de.finetech.groovy.utils.datatypes.FOFunction;
import groovy.lang.GroovyObject;
import groovy.lang.GroovyObjectSupport;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 */
public class GroovyFOFBufferMap extends GroovyObjectSupport
		implements Map<String, FOFunction>, Cloneable, Serializable, GroovyObject {

	private static final long serialVersionUID = 4338579360691818296L;

	protected GroovyFOScript script;

	@Override
	public void clear() {
	}

	public GroovyFOFBufferMap(GroovyFOScript script) {
		this.script = script;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}

	@Override
	public Set<Entry<String, FOFunction>> entrySet() {
		return null;
	}

	@Override
	public Object invokeMethod(String name, Object args) {
		return this.get(name).call((Object[]) args);
	}

	@Override
	public FOFunction get(Object key) {
		FOFunction function = new FOFunction(key.toString(), script);
		return function;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

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
	public FOFunction put(String key, FOFunction value) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends FOFunction> m) {
	}

	@Override
	public FOFunction remove(java.lang.Object key) {
		return null;
	}

	@Override
	public Collection<FOFunction> values() {
		return null;
	}

}
