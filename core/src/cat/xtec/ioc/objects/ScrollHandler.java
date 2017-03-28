package cat.xtec.ioc.objects;


import com.badlogic.gdx.scenes.scene2d.Group;

import java.util.ArrayList;
import java.util.Random;

import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

public class ScrollHandler extends Group {

    // Fons de pantalla
    Background bg, bg_back;

    // Asteroides
    int numAsteroids, numEstacions;
    private ArrayList<Asteroid> asteroids;
    private ArrayList<Estacion> estacions;


    // Objecte Random
    Random r;

    public ScrollHandler() {

        // Creem els dos fons
        bg = new Background(0, 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);
        bg_back = new Background(bg.getTailX(), 0, Settings.GAME_WIDTH * 2, Settings.GAME_HEIGHT, Settings.BG_SPEED);

        // Afegim els fons al grup
        addActor(bg);
        addActor(bg_back);

        // Creem l'objecte random
        r = new Random();

        // Comencem amb 3 asteroids
        numAsteroids = 3;
        //Comencem amb 3 estacions
        numEstacions = 3;
        // Creem l'ArrayList
        asteroids = new ArrayList<Asteroid>();
        estacions = new ArrayList<Estacion>();

        // Definim una mida aleatòria entre el mínim i el màxim
        float newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
        float widthStation = Settings.WIDTH_STATION * 34;
        float heightStation = Settings.HEIGHT_STATION * 34;
        // Afegim el primer Asteroid a l'Array i al grup
        Asteroid asteroid = new Asteroid(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
        asteroids.add(asteroid);
        addActor(asteroid);

        //Afegim la primera estacio a l'array i al grup

        Estacion estacion = new Estacion(Settings.GAME_WIDTH, r.nextInt(Settings.GAME_HEIGHT - (int) widthStation), widthStation, heightStation, Settings.ASTEROID_SPEED);
        estacions.add(estacion);
        addActor(estacion);
        estacion.setVisible(false);
        // Des del segon fins l'últim asteroide
        for (int i = 1; i < numAsteroids; i++) {
            // Creem la mida al·leatòria
            newSize = Methods.randomFloat(Settings.MIN_ASTEROID, Settings.MAX_ASTEROID) * 34;
            // Afegim l'asteroid.
            asteroid = new Asteroid(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) newSize), newSize, newSize, Settings.ASTEROID_SPEED);
            // Afegim l'asteroide a l'ArrayList
            asteroids.add(asteroid);
            // Afegim l'asteroide al grup d'actors
            addActor(asteroid);
        }
        //Des de la segona a la ultima estacio
        for (int i = 1; i < numEstacions; i++) {
            estacion = new Estacion(estacions.get(estacions.size() - 1).getTailX() + Settings.ASTEROID_GAP, r.nextInt(Settings.GAME_HEIGHT - (int) widthStation), widthStation, heightStation, Settings.ASTEROID_SPEED);
            estacions.add(estacion);
            addActor(estacion);
            int randomNumber = r.nextInt(5);
            if (randomNumber == 2 || randomNumber == 3 && !collidesStationAsteroid()) {
                estacions.get(i).setVisible(true);
            } else {
                estacions.get(i).setVisible(false);
            }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Si algun element està fora de la pantalla, fem un reset de l'element.
        if (bg.isLeftOfScreen()) {
            bg.reset(bg_back.getTailX());

        } else if (bg_back.isLeftOfScreen()) {
            bg_back.reset(bg.getTailX());

        }

        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid = asteroids.get(i);
            if (asteroid.isLeftOfScreen()) {
                if (i == 0) {
                    asteroid.reset(asteroids.get(asteroids.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    asteroid.reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
        for (int i = 0; i < estacions.size(); i++) {
            Estacion estacion = estacions.get(i);
            if (estacion.isLeftOfScreen()) {
                if (i == 0) {
                    estacion.reset(estacions.get(estacions.size() - 1).getTailX() + Settings.ASTEROID_GAP);
                } else {
                    estacion.reset(estacions.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
                }
            }
        }
    }

    public boolean collides(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i la nau
        for (Asteroid asteroid : asteroids) {
            if (asteroid.collides(nau)) {
                return true;
            }
        }

        return false;
    }

    public boolean collidesStation(Spacecraft nau) {

        // Comprovem les col·lisions entre cada asteroid i una nau
        for (Estacion station : estacions) {
            if (station.collides(nau) && station.isVisible()) {
                station.setVisible(false);
                return true;
            }
        }

        return false;
    }

    public boolean collidesStationAsteroid() {
        for (Estacion station : estacions) {
            for (Asteroid asteroid : asteroids) {
                if (asteroid.collidesStation(station)) {
                    station.setVisible(false);
                    return true;
                }
            }
        }
        return false;
    }

    public void resetAsteroids() {

        // Posem el primer asteroid fora de la pantalla per la dreta
        asteroids.get(0).reset(Settings.GAME_WIDTH);
        // Calculem les noves posicions de la resta d'asteroids.
        for (int i = 1; i < asteroids.size(); i++) {
            asteroids.get(i).reset(asteroids.get(i - 1).getTailX() + Settings.ASTEROID_GAP);
        }
    }


//    public void resetStations() {
//        int positionStation = r.nextInt(5);
//        System.out.println(positionStation);
//
//            estacions.reset(Settings.GAME_WIDTH + positionStation);
//            if (positionStation == 3 && !asteroids.get(i).collidesStation(estacions.get(i))) {
//                estacions.get(i).setVisible(true);
//            }
//        }


    public ArrayList<Asteroid> getAsteroids() {
        return asteroids;
    }

    public ArrayList<Estacion> getEstacions() {
        return estacions;
    }


}