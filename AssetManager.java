package realtimesimulation;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;

public class AssetManager {
    static private Background backgroundImage = null;
    static private ArrayList<ImagePattern> planets = new ArrayList<>();
    static private ImagePattern blackHoleImage = null;
    static private ImagePattern crossHairImage = null;
    
    static private Media backgroundMusic = null;
    static private AudioClip newPlanetSound = null;
    static private AudioClip shootingSound = null;
    
    
    static private String fileURL(String relativePath)
    {
        return new File(relativePath).toURI().toString();
    }
    
    static public void preloadAllAssets()
    {
        // Preload all images
        Image background = new Image(fileURL("./assets/images/background.png"));
        backgroundImage = new Background(
                            new BackgroundImage(background, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundRepeat.NO_REPEAT, 
                                                BackgroundPosition.DEFAULT,
                                                BackgroundSize.DEFAULT));
        
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/mercury.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/venus.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/earth.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/jupiter.png"))));
        planets.add(new ImagePattern(new Image(fileURL("./assets/images/saturn.png"))));
        
        blackHoleImage = new ImagePattern(new Image(fileURL("./assets/images/blackhole.png")));
        crossHairImage = new ImagePattern(new Image(fileURL("./assets/images/blackhole.png")));
        // Preload all music tracks
        backgroundMusic = new Media(fileURL("./assets/music/backgroundMusic.mp3"));

        // Preload all sound effects
        newPlanetSound = new AudioClip(fileURL("./assets/soundfx/newPlanet.wav"));
        shootingSound =  new AudioClip(fileURL("./assets/soundfx/shooting.wav"));
    }
    
    static public Background getBackgroundImage()
    {
        return backgroundImage;        
    }
    
    static public ImagePattern getRandomPlanet()
    {
        Random rng = new Random();
        int i = rng.nextInt(planets.size());
        return planets.get(i);
    }

    static public ImagePattern getBlackHoleImage()
    {
        return blackHoleImage;
    }
    
    static public ImagePattern getCrosshairImage()
    {
        return crossHairImage;
    }

    static public Media getBackgroundMusic()
    {
        return backgroundMusic;
    }
    
    static public AudioClip getNewPlanetSound()
    {
        return newPlanetSound;
    }
}
