package com.out.link.server.http.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisBuilder {
	
	private JedisPool pool = null;
	
	private String host;
	
	private int port;
	
	private int timeout;

	private boolean flag;
	
	public JedisBuilder(String host, int port, int timeout, int maxactive, int maxidle, boolean flag){
		this.host = host;
		this.port = port;
		this.timeout = timeout;	
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxActive(maxactive);
		config.setMaxIdle(maxidle); 
		config.setTestOnBorrow(flag);
		pool = new JedisPool(config, host, port, timeout);
		this.flag = flag;
	}
	
	public void initialize() {
		pool = new JedisPool(new JedisPoolConfig(), host, port, timeout);
	}
	
	public Jedis buildJedis(int db){
		Jedis jedis = null;
		try{
			jedis = pool.getResource();	
			if (!jedis.isConnected()){
				jedis.connect();
			}
			if(flag){
				jedis.select(db);
			}
		}catch(Exception e){}
		return jedis;
	}
	
	public void returnResource(Jedis jedis){
		pool.returnResource(jedis);
	}
	
	public void returnBrokenResource(Jedis jedis){
		pool.returnBrokenResource(jedis);
	}
	
	public void stop() {
		pool.destroy();
	}
}
