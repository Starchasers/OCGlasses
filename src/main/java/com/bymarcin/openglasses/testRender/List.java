package com.bymarcin.openglasses.testRender;

import java.util.ArrayList;

public class List<E> {
	ArrayList<E> list = new ArrayList<E>();
	int iterator = -1;

	public void add(E obj) {
		list.add(obj);
		iterator = list.size() - 1;
	}

	public E previous() {
		iterator = iterator - 1;
		if (iterator < 0) {
			return null;
		}
		return list.get(iterator);
	}

	public E last() {
		return list.get(list.size() - 1);
	}

}
