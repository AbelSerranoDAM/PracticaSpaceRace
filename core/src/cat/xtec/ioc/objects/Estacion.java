package cat.xtec.ioc.objects;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.scenes.scene2d.actions.RepeatAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;

import java.awt.Rectangle;
import java.util.Random;

import cat.xtec.ioc.helpers.AssetManager;
import cat.xtec.ioc.utils.Methods;
import cat.xtec.ioc.utils.Settings;

/**
 * Created by ALUMNEDAM on 15/03/2017.
 */

public class Estacion extends Scrollable {
    private Circle collisionCircle;
    private Random r;
    Estacion station;

    public Estacion(float x, float y, float width, float height, float velocity) {
        super(x, y, width, height, velocity);

        collisionCircle = new Circle();
        RotateByAction rotateAction = new RotateByAction();
        rotateAction.setAmount(-90f);
        rotateAction.setDuration(0.2f);
        RepeatAction repeat = new RepeatAction();
        repeat.setAction(rotateAction);
        repeat.setCount(RepeatAction.FOREVER);
        this.addAction(repeat);
        r = new Random();
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        // Actualitzem el cercle de col·lisions (punt central de l'asteroid i el radi.
        collisionCircle.set(position.x + height / 2.0f, position.y + width / 2.0f, width / 2.0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        //batch.draw(AssetManager.estacio, position.x, position.y, this.getOriginX(), this.getOriginY(), width, height, this.getScaleX(), this.getScaleY(), this.getRotation());
        batch.draw(AssetManager.estacio, position.x, position.y);
    }

    public boolean collides(Spacecraft nau) {

        if (position.x <= nau.getX() + nau.getWidth()) {
            // Comprovem si han col·lisionat sempre i quan la estacio estigui a la mateixa alçada que la spacecraft
            return (Intersector.overlaps(collisionCircle, nau.getCollisionRect()));
        }
        return false;
    }


    @Override
    public void reset(float newX) {
        super.reset(newX);
        // Obtenim un número al·leatori entre MIN i MAX

        // Modificarem l'alçada i l'amplada segons l'al·leatori anterior
        width = height = 34 * Settings.HEIGHT_STATION;
        // La posició serà un valor aleatòri entre 0 i l'alçada de l'aplicació menys l'alçada
        position.y = new Random().nextInt(Settings.GAME_HEIGHT - (int) height);
        int randomNumber = r.nextInt(5);
        if (randomNumber == 2 || randomNumber == 3) {
            this.setVisible(true);
        }
    }


    public Circle getCollisionCircle() {
        return collisionCircle;
    }

    public Estacion getStation() {
        return station;
    }
}
