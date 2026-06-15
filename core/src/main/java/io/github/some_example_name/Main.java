package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector;
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
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Random;
import com.badlogic.gdx.InputProcessor;

import java.util.ArrayList;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    PerspectiveCamera camera;
    Vector3 vertical;
    boolean locked;
    ModelInstance sphereInstance;
    ModelBuilder modelBuilder;
    ModelBatch modelBatch;
    Model model;
    Model model1;
    Vector3 camPosition;
    Vector3 camDirection;
    ArrayList<Model> models;
    ArrayList<ModelInstance> modelInstances;
    Random random;
    int amount;

    @Override
    public void create() {
        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPosition = new Vector3(10,10,10);
        camera.position.set(camPosition);
        random = new Random();

        camera.lookAt(0,10,0);
        camDirection = camera.direction.cpy().nor();
        camera.near = 0.1f;
        camera.far = 1000f;
        camera.update();
        vertical = new Vector3(0,1,0);
        boolean locked = false;
        modelBuilder = new ModelBuilder();
        models = new ArrayList<Model>();
        modelInstances = new ArrayList<ModelInstance>();
        amount= 10000;

        for(int i =0; i<amount; i++) {
            float randombetween2 = random.nextFloat(0, 2);
            models.add(modelBuilder.createSphere(randombetween2, randombetween2, randombetween2, 12, 12, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal));

        }

        model = modelBuilder.createBox(5f, 5f, 5f,
            new Material(ColorAttribute.createDiffuse(Color.GREEN)),
            Usage.Position | Usage.Normal);
        model1 = modelBuilder.createSphere(5f,5f,5f,12,12,new Material(ColorAttribute.createDiffuse(Color.GREEN)),Usage.Position | Usage.Normal);
        sphereInstance = new ModelInstance(model1);
        for(int i =0; i<amount; i++) {
            modelInstances.add(new ModelInstance(models.get(i)));
            modelInstances.get(i).transform.setToTranslation(random.nextFloat(-1000, 1000),random.nextFloat(-1000, 1000),random.nextFloat(-1000, 1000));
        }

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
            camPosition.add(camDirection.cpy().nor().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.S)){
            camPosition.sub(camDirection.cpy().nor().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            camPosition.add(camDirection.cpy().nor().crs(0,1,0).scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camPosition.sub(camDirection.cpy().nor().crs(0,1,0).scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            camPosition.add(vertical.cpy().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            camPosition.sub(vertical.cpy().scl(speed));
        }
        //if(Gdx.input.getInputProcessor().scrolled(1,0))
        Gdx.input.setCursorCatched(locked);
        if(locked){

            camDirection.rotate(camDirection.cpy().nor().crs(vertical), -sensitivity * (Gdx.input.getY() - ((float) Gdx.graphics.getHeight() /2)));
            camera.update();
            if(Math.pow(camDirection.y,2) < 0.931225) {
                camDirection.rotate(vertical, -sensitivity * (Gdx.input.getX() - ((float) Gdx.graphics.getWidth() / 2)));
            }
            camera.update();
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }

        camera.position.set(camPosition);
        camera.direction.set(camDirection);
        System.out.println(camera.position);
;
    }
    @Override
    public void render() {
        ScreenUtils.clear(0f,0f,0f,0f);
        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            locked = !locked;

        }
        doCameraMovement(camera,1,0.5f,locked);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
        camera.update();
        for(int i =0; i<amount; i++) {
            modelBatch.render(modelInstances.get(i));
        }

        modelBatch.end();
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
        modelBatch.dispose();
        model.dispose();
    }
}
