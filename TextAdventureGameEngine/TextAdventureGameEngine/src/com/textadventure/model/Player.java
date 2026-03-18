package com.textadventure.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Iterator;

public class Player {

    private String currentRoomName;
    private List<Item> inventory;

    public Player(String startingRoomName) {
        if (startingRoomName == null || startingRoomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Player starting room name cannot be null or empty.");
        }
        this.currentRoomName = startingRoomName.trim();
        this.inventory = new ArrayList<>();
    }

    // -------- INVENTORY METHODS --------

    public void takeItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add a null item to player inventory.");
        }
        this.inventory.add(item);
    }

    //  Keep this (used conceptually in design)
    public boolean dropItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot drop a null item from player inventory.");
        }
        return this.inventory.remove(item);
    }

    // 🔁 Keep this (used in your codebase)
    public boolean removeItem(String itemName) {
        if (itemName == null || itemName.isBlank()) {
            return false;
        }

        Iterator<Item> iterator = inventory.iterator();
        while (iterator.hasNext()) {
            Item item = iterator.next();

            if (item.getName() != null && item.getName().equalsIgnoreCase(itemName)) {
                iterator.remove();
                System.out.println("[Player Debug] Removed '" + itemName + "' from inventory.");
                return true;
            }
        }

        System.out.println("[Player Debug] Item '" + itemName + "' not found in inventory.");
        return false;
    }

    public Optional<Item> findItemInInventory(String itemName) {
        if (itemName == null || itemName.trim().isEmpty()) {
            return Optional.empty();
        }

        for (Item item : inventory) {
            if (item.getName().equalsIgnoreCase(itemName)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    // -------- GETTERS / SETTERS --------

    public List<Item> getInventory() {
        return this.inventory; // keep mutable (your Game.java depends on it)
    }

    public String getCurrentRoomName() {
        return this.currentRoomName;
    }

    public void setCurrentRoomName(String newRoomName) {
        if (newRoomName == null || newRoomName.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot set current room name to null or empty.");
        }
        this.currentRoomName = newRoomName.trim();
    }
}