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

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EventListener;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.InputProcessor;
import java.util.EventListener.*;
import com.badlogic.gdx.InputAdapter;

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
    Vector3 camPosition;
    Vector3 camDirection;
    ArrayList<Model> models;
    ArrayList<ModelInstance> modelInstances;
    Random random;
    mouseScroll mouse;
    float trueSpeed;
    Object testball;
    Object testball2;
    Object testball3;
    ArrayList<Object> objects;
    ForceHandler forces;
    //int amount;


    @Override
    public void create() {
        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPosition = new Vector3(10,10,10);
        camera.position.set(camPosition);
        mouse = new mouseScroll();
        random = new Random();
        camera.lookAt(150,10,0);
        camDirection = camera.direction.cpy().nor();
        camera.near = 0.1f;
        camera.far = 10000f;
        camera.update();
        vertical = new Vector3(0,1,0);
        boolean locked = false;
        modelBuilder = new ModelBuilder();
        models = new ArrayList<Model>();
        modelInstances = new ArrayList<ModelInstance>();
        trueSpeed = 1;
        Gdx.input.setInputProcessor(mouse);
        objects = new ArrayList<Object>();
        forces = new ForceHandler();


        testball = new Object(10, 100, 200,0,50, 0,0,0,modelBuilder);
        testball2 = new Object(10, 100, 100,0,-50, 0,0,0,modelBuilder);
        testball3 = new Object(10, 100, 100,50,0, 0,0,0,modelBuilder);
        objects.add(testball);
        objects.add(testball2);
        objects.add(testball3);
        //amount= 10000;
        /*
        for(int i =0; i<amount; i++) {
            float randombetween2 = random.nextFloat(0, 2);
            models.add(modelBuilder.createSphere(randombetween2, randombetween2, randombetween2, 20, 20, new Material(ColorAttribute.createDiffuse(Color.WHITE)), Usage.Position | Usage.Normal));
        }

        for(int i =0; i<amount; i++) {
            modelInstances.add(new ModelInstance(models.get(i)));
            modelInstances.get(i).transform.setToTranslation(random.nextFloat(-1000, 1000),random.nextFloat(-1000, 1000),random.nextFloat(-1000, 1000));
        }
        */

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
            camPosition.add(camDirection.cpy().crs(0,1,0).nor().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.A)){
            camPosition.sub(camDirection.cpy().crs(0,1,0).nor().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            camPosition.add(vertical.cpy().nor().scl(speed));
        }
        if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)){
            camPosition.sub(vertical.cpy().nor().scl(speed));
        }



        Gdx.input.setCursorCatched(locked);
        if(locked){
            if(camDirection.y > 0.965){
                camDirection.set(camDirection.x, 0.965f,camDirection.z);
            }
            else if(camDirection.y < -0.965){
                camDirection.set(camDirection.x, -0.965f,camDirection.z);
            }
            if(camDirection.y < 0.965 && camDirection.y > -0.965) {
                camDirection.rotate(vertical, -sensitivity * (Gdx.input.getX() - ((float) Gdx.graphics.getWidth() / 2)));
            }
            camDirection.rotate(camDirection.cpy().nor().crs(vertical), -sensitivity * (Gdx.input.getY() - ((float) Gdx.graphics.getHeight() /2)));
            camera.update();
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
        }
        camera.position.set(camPosition);
        camera.direction.set(camDirection);
        //System.out.println(camera.direction);
;
    }
    @Override
    public void render() {
        forces.gravity(objects);
        testball.advance();
        int currentspeed = mouse.currentSpeedLevel;
        if(mouse.currentSpeedLevel > mouse.scrollMax){
            mouse.currentSpeedLevel -= Math.floorDiv(currentspeed,10);
        }
        if(mouse.currentSpeedLevel < -mouse.scrollMax){
            mouse.currentSpeedLevel -= Math.floorDiv(currentspeed,10)+1;
        }
        System.out.println(mouse.currentSpeedLevel);
        ScreenUtils.clear(0f,0f,0f,0f);
        if(Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)){
            Gdx.input.setCursorPosition(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
            locked = !locked;
        }
        float apparentspeed = (float) (trueSpeed * Math.exp(0.35*mouse.currentSpeedLevel));
        doCameraMovement(camera,apparentspeed,0.1f,locked);
        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        modelBatch.begin(camera);
        camera.update();
        /*
        for(int i =0; i<amount; i++) {
            modelBatch.render(modelInstances.get(i));
        }

        */
        for(int i =0; i<objects.size(); i++) {
            objects.get(i).draw(modelBatch);
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
