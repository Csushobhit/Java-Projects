package com.textadventure.model;

public class ExitModification {

    private String direction;
    private boolean clearRequiresItem;
    private String setFailMessage;
    private String addsItemToInventory;

    public String getDirection() {
        return direction;
    }

    public boolean isClearRequiresItem() {
        return clearRequiresItem;
    }
    
    public String getSetFailMessage() {
        return setFailMessage;
    }

    public ExitModification() {}
}