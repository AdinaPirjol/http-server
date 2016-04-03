/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

/**
 *
 * @author nnao9_000
 */
public interface Subject {
    public void registerObserver(Observer observer);
    
    public void unregisterObserver(Observer observer);
    
    public void notifyObservers(String message);
}
