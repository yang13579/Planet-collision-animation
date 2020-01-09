/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package realtimesimulation;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Circle;

/**
 *
 * @author bergeron
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    AnchorPane pane;
    
    private double lastFrameTime = 0.0;
    private ArrayList<Circle> circleList = new ArrayList<>();
    private ArrayList<Vector2D> circleVelocityList = new ArrayList<>();
    
    private ArrayList<GameObject> objectList = new ArrayList<>();
    private Blackhole leftBlackhole = null;
    private Blackhole rightBlackhole = null;
    private Crosshair cursor = null;

    public void addToPane(Node node)
    {
        pane.getChildren().add(node);
    }
    
    @FXML
    private void onKeyPressed(KeyEvent e){
        System.out.println("Key pressed: " + e.getCode());
    }
    
    @FXML
    private void onKeyReleased(KeyEvent e){
        System.out.println("Key released: " + e.getCode());
    }
    
    @FXML
    private void onMouseMoved(MouseEvent e){
        //System.out.println("Mouse moved: " + e.getX() + ", " + e.getY());
        cursor.setPosition(new Vector2D(e.getX(), e.getY()));
    }
    
    @FXML
    private void onMouseClicked(MouseEvent e){
        final float PLANET_SPEED = 300;
        //System.out.println("click!" + e.getButton());
        Vector2D position = leftBlackhole.getPosition();
        Vector2D mousePosition = new Vector2D(e.getX(), e.getY());
        
        Vector2D velocity = mousePosition.sub(position);
        velocity.normalize();
        velocity = velocity.mult(300);
        
        Planet planet = new Planet(position, velocity, 50);
        addToPane(planet.getCircle());
        objectList.add(planet);
        
        position = rightBlackhole.getPosition();
        velocity = mousePosition.sub(position);
        velocity.normalize();
        velocity = velocity.mult(PLANET_SPEED);
        
        planet = new Planet(position, velocity, 50);
        addToPane(planet.getCircle());
        objectList.add(planet);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        lastFrameTime = 0.0f;
        long initialTime = System.nanoTime();

        AssetManager.preloadAllAssets();
        
        leftBlackhole = new Blackhole(new Vector2D(300, 400));
        rightBlackhole = new Blackhole(new Vector2D(800, 400));
        cursor = new Crosshair();
        
        addToPane(leftBlackhole.getCircle());
        addToPane(rightBlackhole.getCircle());
        addToPane(cursor.getCircle());
        objectList.add(leftBlackhole);
        objectList.add(rightBlackhole);
        objectList.add(cursor);
        
        // set the background image
        Image backgroundImage = new Image(new File("./assets/images/background.png").toURI().toString());
        Background background = new Background(new BackgroundImage(backgroundImage,
                                               BackgroundRepeat.NO_REPEAT,
                                               BackgroundRepeat.NO_REPEAT,
                                               BackgroundPosition.DEFAULT,
                                               BackgroundSize.DEFAULT));
        pane.setBackground(background);
        
        // set the background music
        String musicFile = "./assets/music/backgroundMusic.mp3";
        Media sound = new Media(new File(musicFile).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
        
        // Add 20 random circles
        for (int i=0; i<20; ++i)
        {
            Random rng = new Random();
            int width = (int) pane.getPrefWidth();
            int height = (int) pane.getPrefHeight();
            int x = rng.nextInt(width);
            int y = rng.nextInt(height);
            int radius = rng.nextInt(20) + 10;

            Circle c = new Circle(0, 0, radius);
            c.setFill(AssetManager.getRandomPlanet());
            c.setCenterX(x);
            c.setCenterY(y);
            circleList.add(c);
            circleVelocityList.add(new Vector2D((rng.nextDouble()-0.5)*400, (rng.nextDouble()-0.5)*400));
            addToPane(c);
        }

        new AnimationTimer()
        {
            @Override
            public void handle(long now) {
                // Time calculation                
                double currentTime = (now - initialTime) / 1000000000.0;
                double  frameDeltaTime = currentTime - lastFrameTime;
                lastFrameTime = currentTime;

                for(GameObject obj : objectList){
                    obj.update(frameDeltaTime);
                }
                // Move circles every frame
                for (int i=0; i<circleList.size(); i++)
                {
                    Circle c = circleList.get(i);
                    Vector2D position = new Vector2D(c.getCenterX(), c.getCenterY());
                    Vector2D v = circleVelocityList.get(i);
                    position = position.add(v.mult(frameDeltaTime));
                    c.setCenterX(position.getX());
                    c.setCenterY(position.getY());
                    
                    // collision with edges
                    if (c.getCenterX() - c.getRadius() < 0)
                    {
                        v.setX(Math.abs(v.getX()));
                    }

                    if (c.getCenterX() + c.getRadius() > pane.getWidth())
                    {
                        v.setX(-Math.abs(v.getX()));
                    }
                    
                    if (c.getCenterY() - c.getRadius() < 0)
                    {
                        v.setY(Math.abs(v.getY()));
                    }
                    
                    if (c.getCenterY() + c.getRadius() > pane.getHeight())
                    {
                        v.setY(-Math.abs(v.getY()));
                    }
                }

                // Collision detection and response
                for(int i=0; i<circleList.size(); i++)
                {
                    for (int j=i+1; j<circleList.size(); j++)
                    {
                        Circle circle1 = circleList.get(i);
                        Circle circle2 = circleList.get(j);
                        
                        Vector2D c1 = new Vector2D(circle1.getCenterX(), circle1.getCenterY());
                        Vector2D c2 = new Vector2D(circle2.getCenterX(), circle2.getCenterY());
                        
                        Vector2D n = c2.sub(c1);
                        double distance = n.magnitude();
                        
                        if (distance < circle1.getRadius() + circle2.getRadius())
                        {
                            // Compute normal and tangent vectors
                            n.normalize();
                            Vector2D t = new Vector2D(-n.getY(), n.getX());
                            
                            // Separate circles - Compute new positions and assign to circles
                            double overlap = distance - (circle1.getRadius() + circle2.getRadius());
                            c1 = c1.add(n.mult(overlap/2));
                            c2 = c2.sub(n.mult(overlap/2));
                            circle1.setCenterX(c1.getX());
                            circle1.setCenterY(c1.getY());
                            circle2.setCenterX(c2.getX());
                            circle2.setCenterY(c2.getY());
                            
                            // Decompose velocities, project them on n and t
                            Vector2D v1 = circleVelocityList.get(i);
                            Vector2D v2 = circleVelocityList.get(j);
                            
                            Vector2D v1N = n.mult(v1.dot(n));
                            Vector2D v2N = n.mult(v2.dot(n));
                            
                            Vector2D v1T = t.mult(v1.dot(t));
                            Vector2D v2T = t.mult(v2.dot(t));
                            
                            // Change velocities
                            v1.set(v1T.add(v2N));
                            v2.set(v2T.add(v1N));
                            
                            // set the sound effect when two planets collide
                            AudioClip clip = new AudioClip(new File("./assets/soundfx/pop.wav").toURI().toString());
                            clip.play();
                        }
                    }
                }
            }
        }.start();        
    }    
    
}
