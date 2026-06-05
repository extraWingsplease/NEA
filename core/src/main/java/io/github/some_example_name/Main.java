package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    PerspectiveCamera camera;
    Vector3 vertical;
    boolean locked;

    @Override
    public void create() {
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(0,0,0);
        camera.lookAt(1,0,0);
        camera.update();
        vertical = new Vector3(0,0,1);
        boolean locked = false;


    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
    }

    public void doCameraMovement(PerspectiveCamera camera, float speed, float sensitivity, boolean locked){
        if(Gdx.input.isKeyPressed(Input.Keys.W)){
            camera.position.add(camera.direction.cpy().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camera.position.sub(camera.direction.cpy().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camera.position.add(camera.direction.cpy().crs(0,0,1).scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camera.position.sub(camera.direction.cpy().crs(0,0,1).scl(speed));
        }
        if(locked){
            camera.direction.rotate(camera.direction.cpy().crs(0,0,1), sensitivity * (Gdx.input.getY() - ((float) Gdx.graphics.getHeight() /2));
            camera.direction.rotate(vertical, sensitivity * (Gdx.input.getX() - ((float) Gdx.graphics.getWidth() /2));
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }

    }
    @Override
    public void render() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            locked = !locked;
        }
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}
