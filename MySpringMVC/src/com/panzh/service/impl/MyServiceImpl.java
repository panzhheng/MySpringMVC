package com.panzh.service.impl;

import com.panzh.annotation.Service;

@Service("MyServiceImpl") 
public class MyServiceImpl implements MyService {

	@Override
	public int insert() {
		System.out.println("MyServiceImpl insert");
		return 0;
	}

	@Override
	public int delete() {
		System.out.println("MyServiceImpl delete");
		return 0;
	}

	@Override
	public int update() {
		System.out.println("MyServiceImpl update");
		return 0;
	}

	@Override
	public int select() {
		System.out.println("MyServiceImpl select");
		return 0;
	}

	@Override
	public String sayHello() {
		String string = "ƒ„∫√£∫ª∂”≠∑√Œ 1";
		return string;
	}

	
}
