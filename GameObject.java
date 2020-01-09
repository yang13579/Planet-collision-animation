package realtimesimulation;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;

public class GameObject {
    protected Circle circle;
    private Vector2D position;
    private Vector2D velocity;
    private Vector2D acceleration;
    
    public GameObject(Vector2D position, Vector2D velocity, Vector2D acceleration, double radius)
    {
        this.position = position;
        this.velocity = velocity;
        this.acceleration = acceleration; 
        
        circle = new Circle(0.0, 0.0, radius);
        circle.setCenterX(position.getX());
        circle.setCenterY(position.getY());
    }
    
    
    public Circle getCircle()
    {
        return circle;
    }
    
    public void update(double dt)
    {
        // Euler Integration
        // Update velocity
        Vector2D frameAcceleration = getAcceleration().mult(dt);
        velocity = getVelocity().add(frameAcceleration);

        // Update position
        position = getPosition().add(getVelocity().mult(dt));
        circle.setCenterX(getPosition().getX());
        circle.setCenterY(getPosition().getY());

        
    }

    public Vector2D getPosition() {
        return position;
    }

    public Vector2D getVelocity() {
        return velocity;
    }

    public Vector2D getAcceleration() {
        return acceleration;
    }
    
    public void setPosition(Vector2D v){
        this.position = v;
    }
}
