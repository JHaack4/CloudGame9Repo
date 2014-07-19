package com.jerhis.cloudgame.game;

public abstract class GameObject {
	
	public enum ObjectType {
		Raindrop, Lightning
	}
	
	public GameObject() {
		gone = false;
	}
	
	public GameObject(ObjectType obj) {
		objectType = obj;
		gone = false;
	}
	
	public void update(float delta) {
		
	}
	
	public ObjectType objectType;
	public boolean gone;

}
