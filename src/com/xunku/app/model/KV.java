package com.xunku.app.model;

/**
 * 一个KV描述
 * @author wujian
 * @created on Jun 5, 2014 3:46:45 PM
 * @param <TKey>
 * @param <TValue>
 */
public class KV<TKey, TValue> {
	
	private TKey key;
	private TValue value;

	public KV(TKey key, TValue value) {
		this.key = key;
		this.value = value;
	}

	public TKey getKey() {
		return this.key;
	}

	public TValue getValue() {
		return this.value;
	}
}
