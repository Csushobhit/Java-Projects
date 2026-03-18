package com.textadventure.model;
import com.textadventure.model.ExitModification;

public class Item {
	private String name;
	private String description;
	private Usability usability;
	
	
		
	public Item(String name, String description)
	{
		if(name == null || name.trim().isEmpty())
		{
			throw new IllegalArgumentException("Item name cannot be null or empty");
		}
		if(description == null)
		{
			throw new IllegalArgumentException("Item description cannot be null");
		}
		this.name = name;
		this.description = description;
	}
	
	
	public String getName()
	{
		return this.name;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	public Usability getUsability() {
	    return usability;
	}
	public static class Usability {
	    private String target;
	    private String effectDescription;
	    private boolean consumesItem;
	    private String unlocksExit;
	    private String removesTarget;
	    private String addsTarget;
	    private String changesRoomDescriptionTo;
	    private ExitModification modifiesExit;
	    private String addsItemToInventory;

	    public String getTarget() { return target; }
	    public String getEffectDescription() { return effectDescription; }
	    public boolean isConsumesItem() { return consumesItem; }
	    public String getUnlocksExit() { return unlocksExit; }
	    public String getRemovesTarget() { return removesTarget; }
	    public String getAddsTarget() { return addsTarget; }
	    public String getChangesRoomDescriptionTo() { return changesRoomDescriptionTo; }
	    public String getAddsItemToInventory() { return addsItemToInventory; }
	    public ExitModification getModifiesExit() { return modifiesExit; }
	    
	}
	
}
