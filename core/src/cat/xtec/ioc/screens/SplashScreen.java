package cat.xtec.ioc.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import cat.xtec.ioc.SpaceRace;
import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.helpers.InputHandler;
import cat.xtec.ioc.objects.Asteroid;
import cat.xtec.ioc.utils.Settings;


public class SplashScreen implements Screen {

    private Stage stage;
    private SpaceRace game;
    private Rectangle recEasy;
    private Label.LabelStyle textStyle;
    private Label textLbl;
    private Settings settings;

    public SplashScreen(SpaceRace game) {

        this.game = game;

        // Creem la càmera de les dimensions del joc
        OrthographicCamera camera = new OrthographicCamera(Settings.GAME_WIDTH, Settings.GAME_HEIGHT);
        // Posant el paràmetre a true configurem la càmera per a
        // que faci servir el sistema de coordenades Y-Down
        camera.setToOrtho(true);

        // Creem el viewport amb les mateixes dimensions que la càmera
        StretchViewport viewport = new StretchViewport(Settings.GAME_WIDTH, Settings.GAME_HEIGHT, camera);

        // Creem l'stage i assginem el viewport
        stage = new Stage(viewport);
        //Creem el rectangle
        recEasy = new Rectangle();
        // Afegim el fons
        stage.addActor(new Image(AssetManager.background));

        // Creem l'estil de l'etiqueta i l'etiqueta33
        textStyle = new Label.LabelStyle(AssetManager.font, null);
        textLbl = new Label("SpaceRace", textStyle);

        // Creem el contenidor necessari per aplicar-li les accions
        Container container = new Container(textLbl);
        container.setTransform(true);
        container.center();
        container.setPosition(Settings.GAME_WIDTH / 2, Settings.GAME_HEIGHT / 2);

        // Afegim les accions de escalar: primer es fa gran i després torna a l'estat original ininterrompudament
        container.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.scaleTo(1.5f, 1.5f, 1), Actions.scaleTo(1, 1, 1))));
        stage.addActor(container);

        // Creem la imatge de la nau i li assignem el moviment en horitzontal
        Image spacecraft = new Image(AssetManager.spacecraft);
        float y = Settings.GAME_HEIGHT / 2 + textLbl.getHeight();
        spacecraft.addAction(Actions.repeat(RepeatAction.FOREVER, Actions.sequence(Actions.moveTo(0 - spacecraft.getWidth(), y), Actions.moveTo(Settings.GAME_WIDTH, y, 5))));

        stage.addActor(spacecraft);


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stage.draw();
        stage.act(delta);
        recEasy.set(30, 30, 30, 30);
        // Si es fa clic en la pantalla, canviem la pantalla
        if (Gdx.input.isTouched()) {
            pintarBotones();
            dispose();
        }

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

    public void pintarBotones() {
        //HACER BOTON
        Texture textura = new Texture(Gdx.files.internal("buttons/buttonfacil.png"));
        Texture textura2 = new Texture(Gdx.files.internal("buttons/buttonNormal.png"));
        Texture textura3 = new Texture(Gdx.files.internal("buttons/buttonDificil.png"));
        textura.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textura2.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        textura3.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        TextureRegion texturaR = new TextureRegion(textura);
        TextureRegion texturaR2 = new TextureRegion(textura2);
        TextureRegion texturaR3 = new TextureRegion(textura3);
        texturaR.flip(false, true);
        texturaR2.flip(false, true);
        texturaR3.flip(false, true);
        TextureRegionDrawable imagen = new TextureRegionDrawable(texturaR);
        TextureRegionDrawable imagen2 = new TextureRegionDrawable(texturaR2);
        TextureRegionDrawable imagen3 = new TextureRegionDrawable(texturaR3);
        final ImageButton buttonFacil = new ImageButton(imagen);
        final ImageButton buttonNormal = new ImageButton(imagen2);
        final ImageButton buttonDificil = new ImageButton(imagen3);
        buttonFacil.setPosition(15, 85);
        buttonFacil.setWidth(50);
        buttonFacil.setHeight(60);
        stage.addActor(buttonFacil);
        buttonNormal.setPosition(80, 80);
        buttonNormal.setWidth(70);
        buttonNormal.setHeight(70);
        stage.addActor(buttonNormal);
        buttonDificil.setPosition(165, 85);
        buttonDificil.setWidth(60);
        buttonDificil.setHeight(60);
        stage.addActor(buttonDificil);
        Gdx.input.setInputProcessor(stage);

        buttonFacil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonFacil.setVisible(false);
                buttonNormal.setVisible(false);
                buttonDificil.setVisible(false);
                Settings.ASTEROID_SPEED = -150;
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport(), game));
            }
        });
        buttonNormal.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonFacil.setVisible(false);
                buttonNormal.setVisible(false);
                buttonDificil.setVisible(false);
                Settings.ASTEROID_SPEED = -200;
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport(), game));
            }
        });
        buttonDificil.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonFacil.setVisible(false);
                buttonNormal.setVisible(false);
                buttonDificil.setVisible(false);
                Settings.ASTEROID_SPEED = -200;
                Settings.ASTEROID_GAP = 50;
                game.setScreen(new GameScreen(stage.getBatch(), stage.getViewport(), game));
            }
        });
    }
}
