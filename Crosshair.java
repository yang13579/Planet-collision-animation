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
public class Crosshair extends GameObject{
    public Crosshair(){
        super(new Vector2D(0,0), new Vector2D(0,0), new Vector2D(0,0), 50.0);
        circle.setFill(AssetManager.getCrosshairImage());
    }
    
    @Override
    public void update(double dt){
        super.update(dt);
        
        circle.toFront();
    }
    
}
