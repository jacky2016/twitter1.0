package com.xunku.app.helpers.ibatis;

import java.lang.reflect.InvocationTargetException;

public interface Invoker {
	String getName();

	Object invoke(Object target, Object[] args) throws IllegalAccessException,
			InvocationTargetException;
}
