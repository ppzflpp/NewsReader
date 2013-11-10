package com.dragon.newsreader.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownLoader {

	private static final boolean DEBUG = true;
	
	private static DownLoader sLoader;
	ExecutorService mPool;

	private DownLoader() {
		mPool = Executors.newFixedThreadPool(10);

	}

	public static DownLoader getInstance() {
		sLoader = new DownLoader();
		return sLoader;
	}
	
	public void execute(Runnable task){
		if(task != null){
			if(DEBUG){
				System.out.println("execute runnable...");
			}
			mPool.execute(task);
		}
	}

	public void shutdown(){
		if(mPool != null){
			if(DEBUG){
				System.out.println("shutdown...");
			}
			mPool.shutdown();
			mPool = null;
		}
	}
	
}
