/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package realtimesimulation;

/**
 *
 * @author cstuser
 */
public class Planet extends GameObject{
    public Planet(Vector2D position, Vector2D velocity, float radius){
        super(position, velocity, new Vector2D(0,0), radius);
        circle.setFill(AssetManager.getRandomPlanet());
    }
    
}
