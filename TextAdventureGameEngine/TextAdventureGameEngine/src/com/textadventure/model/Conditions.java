package com.textadventure.model;

public class Conditions {
	private Object requiresItem;
	private String failMessage;
	
	public Object getRequiresItem() {
        return requiresItem;
    }
	public String getFailMessage() {
        return failMessage;
    }
	public void setRequiresItem(Object requiresItem) {
	    this.requiresItem = requiresItem;
	}
	public void setFailMessage(String failMessage) {
	    this.failMessage = failMessage;
	}
	public Conditions() {}
}
