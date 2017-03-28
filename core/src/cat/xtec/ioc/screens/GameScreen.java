package cat.xtec.ioc.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.objects.Estacion;
import cat.xtec.ioc.objects.ScrollHandler;
import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.utils.Settings;

public class GameScreen implements Screen {

    // Els estats del joc
    public enum GameState {

        READY, RUNNING, GAMEOVER

    }

    private GameState currentState;

    // Objectes necessaris
    private Stage stage;
    private Spacecraft spacecraft;

    private ScrollHandler scrollHandler;
    private Container cMarcador;
    private Container cCombustible;
    private Label.LabelStyle textStyle;
    private Label marcador;
    private Label combustible;
    private int contadorRender;
    // Encarregats de dibuixar elements per pantalla
    private ShapeRenderer shapeRenderer;
    private Batch batch;
    private InputHandler inputHandler;
    // Per controlar l'animació de l'explosió
    private float explosionTime = 0;

    // Preparem el textLayout per escriure text
    private GlyphLayout textGameOver;
    //private GlyphLayout texteasy;
    //private GlyphLayout textMedium;
    //private GlyphLayout textHard;
    private int contMarcador = 0;
    private int contCombustible = 20;
    private SpaceRace game;

    public GameScreen(Batch prevBatch, Viewport prevViewport, SpaceRace game) {
        this.game = game;
        // Iniciem la música
        AssetManager.music.play();

        // Creem el ShapeRenderer
        shapeRenderer = new ShapeRenderer();

        // Creem l'stage i assginem el viewport
        stage = new Stage(prevViewport, prevBatch);
        batch = stage.getBatch();
        textStyle = new Label.LabelStyle(AssetManager.font, null);
        marcador = new Label("Marcador: " + contMarcador, textStyle);
        combustible = new Label("Fuel: " + contCombustible, textStyle);
        cMarcador = new Container(marcador);
        cCombustible = new Container(combustible);
        // Creem la nau i la resta d'objectes
        spacecraft = new Spacecraft(Settings.SPACECRAFT_STARTX, Settings.SPACECRAFT_STARTY, Settings.SPACECRAFT_WIDTH, Settings.SPACECRAFT_HEIGHT);
        scrollHandler = new ScrollHandler();
        // Afegim els actors a l'stage
        stage.addActor(scrollHandler);
        stage.addActor(spacecraft);
        stage.addActor(cMarcador);
        stage.addActor(cCombustible);
        // Donem nom a l'Actor
        spacecraft.setName("spacecraft");

        // Iniciem el GlyphLayout
        textGameOver = new GlyphLayout();
        currentState = GameState.RUNNING;

        //Iniciem marcador
        cMarcador.setTransform(true);
        cMarcador.setPosition(65, 10);
        cCombustible.setTransform(true);
        cCombustible.setPosition(170, 10);
        // Assignem com a gestor d'entrada la classe InputHandler
        Gdx.input.setInputProcessor(new InputHandler(this));

    }

    private void drawElements() {

        // Recollim les propietats del Batch de l'Stage
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        // Pintem el fons de negre per evitar el "flickering"
        //Gdx.gl20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //Gdx.gl20.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Inicialitzem el shaperenderer
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Definim el color (verd)
        shapeRenderer.setColor(new Color(0, 1, 0, 1));

        // Pintem la nau
        shapeRenderer.rect(spacecraft.getX(), spacecraft.getY(), spacecraft.getWidth(), spacecraft.getHeight());

        // Recollim tots els Asteroid
        ArrayList<Asteroid> asteroids = scrollHandler.getAsteroids();
        Asteroid asteroid;

        for (int i = 0; i < asteroids.size(); i++) {

            asteroid = asteroids.get(i);
            switch (i) {
                case 0:
                    shapeRenderer.setColor(1, 0, 0, 1);
                    break;
                case 1:
                    shapeRenderer.setColor(0, 0, 1, 1);
                    break;
                case 2:
                    shapeRenderer.setColor(1, 1, 0, 1);
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1);
                    break;
            }
            shapeRenderer.circle(asteroid.getX() + asteroid.getWidth() / 2, asteroid.getY() + asteroid.getWidth() / 2, asteroid.getWidth() / 2);
        }
        shapeRenderer.end();
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Dibuixem tots els actors de l'stage

        stage.draw();
        // Depenent de l'estat del joc farem unes accions o unes altres
        switch (currentState) {
            case GAMEOVER:
                updateGameOver(delta);
                contMarcador = 0;
                marcador.setText("Marcador: " + contMarcador);
                contCombustible = 20;
                combustible.setText("Fuel: " + contCombustible);
                dispose();
                break;
            case RUNNING:
                updateRunning(delta);
                contadorRender = contadorRender + 1;
                if (contadorRender == 100) {
                    contMarcador++;
                    marcador.setText("Marcador: " + contMarcador);
                    contadorRender = 0;
                    contCombustible--;
                    combustible.setText("Fuel: " + contCombustible);
                }
                break;
            case READY:
                updateReady();
                break;
        }

        //drawElements();

    }

    private void updateReady() {

        // Dibuixem el text al centre de la pantalla
        batch.begin();
        AssetManager.font.draw(batch, textGameOver, (Settings.GAME_WIDTH / 2) - textGameOver.width / 2, (Settings.GAME_HEIGHT / 2) - textGameOver.height / 2);
        //AssetManager.font.draw(batch, texteasy, 15, 110);
        //AssetManager.font.draw(batch, textMedium, (Settings.GAME_WIDTH / 2) - textMedium.width / 2, 110);
        //AssetManager.font.draw(batch, textHard, 180, 110);
        //stage.addActor(textLbl);

        batch.end();

    }

    private void updateRunning(float delta) {
        stage.act(delta);
        if (scrollHandler.collides(spacecraft) || contCombustible == 0) {
            // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
            AssetManager.explosionSound.play();
            stage.getRoot().findActor("spacecraft").remove();
            textGameOver.setText(AssetManager.font, "Game Over :'(");
            currentState = GameState.GAMEOVER;
        } else if (scrollHandler.collidesStation(spacecraft)) {
            contCombustible += 2;
            combustible.setText("Fuel: " + contCombustible);
        } else if (scrollHandler.collidesStationAsteroid()) {
        }
    }

    private void updateGameOver(float delta) {
        stage.act(delta);
        batch.begin();
        AssetManager.font.draw(batch, textGameOver, (Settings.GAME_WIDTH - textGameOver.width) / 2, (Settings.GAME_HEIGHT - textGameOver.height) / 2);
        // Si hi ha hagut col·lisió: Reproduïm l'explosió i posem l'estat a GameOver
        batch.draw(AssetManager.explosionAnim.getKeyFrame(explosionTime, false), (spacecraft.getX() + spacecraft.getWidth() / 2) - 32, spacecraft.getY() + spacecraft.getHeight() / 2 - 32, 64, 64);
        batch.end();

        explosionTime += delta;

    }

    public void reset() {

        // Posem el text d'inici

        // Cridem als restart dels elements.
        spacecraft.reset();
        scrollHandler.resetAsteroids();


        // Posem l'estat a 'Ready'
        currentState = GameState.READY;

        // Afegim la nau a l'stage
        stage.addActor(spacecraft);

        // Posem a 0 les variables per controlar el temps jugat i l'animació de l'explosió
        explosionTime = 0.0f;


    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    public Spacecraft getSpacecraft() {
        return spacecraft;
    }

    public Stage getStage() {
        return stage;
    }

    public ScrollHandler getScrollHandler() {
        return scrollHandler;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setCurrentState(GameState currentState) {
        this.currentState = currentState;
    }

    public SpaceRace getGame() {
        return game;
    }
}
