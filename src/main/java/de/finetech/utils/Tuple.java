package de.finetech.utils;

/**
 * 
 * @author Michael Rothenbücher, Finetech GmbH & Co. KG
 *
 * @param <T>
 * @param <A>
 */
public class Tuple<T, A> {

	private T key;
	private A value;

	public Tuple(T val1, A val2) {
		this.key = val1;
		this.value = val2;
	}

	public T getKey() {
		return key;
	}

	public void setKey(T key) {
		this.key = key;
	}

	public A getValue() {
		return value;
	}

	public void setValue(A value) {
		this.value = value;
	}

}
