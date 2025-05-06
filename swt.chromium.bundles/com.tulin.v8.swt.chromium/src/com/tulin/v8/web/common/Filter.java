package com.tulin.v8.web.common;

public abstract interface Filter<T> {
	public abstract Acceptance accept(T paramT);

	public static enum Acceptance {
		YES, NO, TEST_CHILDREN;
	}
}
