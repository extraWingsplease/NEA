package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.Date;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EventListener;
import java.util.List;
import java.util.Random;
import com.badlogic.gdx.InputProcessor;
import java.util.EventListener.*;
import com.badlogic.gdx.InputAdapter;

import java.util.ArrayList;

/*
CONSTANTS/NOTABLE THINGS FOR THIS SIMULATION:
Gravitational constant - 0.0000006743
Speed of light - 173.9848013
Density of the sun - 1
Radius of the sun - 1000
all calculations are done relative to the sun's density and radius
 */

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    PerspectiveCamera camera;
    Vector3 vertical;
    boolean locked;
    ModelInstance sphereInstance;
    ModelBuilder modelBuilder;
    ModelBatch modelBatch;
    Environment environment;
    Texture texture;
    Model model;
    Vector3 camPosition;
    Vector3 camDirection;
    ArrayList<Model> models;
    Date time;

    ArrayList<ModelInstance> modelInstances;
    Random random;
    mouseScroll mouse;
    float trueSpeed;
    Object testball;
    Object testball2;
    Object testball3;
    Object testball4;
    Object testball5;
    ArrayList<Object> testballs;
    ArrayList<Object> objects;
    ArrayList<Object> breakDownObjects;
    ForceHandler forces;
    //int amount;
    Breakdown breakdown;
    int chunksize;
    ChunkHandler chunkz;
    float timeSpeed;
    float previousspeed;
    boolean gamerunning;


    @Override
    public void create() {
        modelBatch = new ModelBatch();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPosition = new Vector3(-2000,10,10);
        camera.position.set(camPosition);
        mouse = new mouseScroll();
        random = new Random();
        camera.lookAt(100,10,10);
        camDirection = camera.direction.cpy().nor();
        camera.near = 0.1f;
        camera.far = 100000000f;
        camera.update();
        vertical = new Vector3(0,1,0);
        boolean locked = false;
        modelBuilder = new ModelBuilder();        models = new ArrayList<Model>();
        environment = new Environment();
        environment.add(new PointLight().set(1f,1f,1f, new Vector3(0,0,0), 1f));
        modelInstances = new ArrayList<ModelInstance>();
        trueSpeed = 1;
        chunksize = 10;
        Gdx.input.setInputProcessor(mouse);
        objects = new ArrayList<Object>();
        testballs = new ArrayList<Object>();
        forces = new ForceHandler();
        chunkz = new ChunkHandler(chunksize);
        time = new Date();
        timeSpeed = 15;
        previousspeed = timeSpeed;
        gamerunning = false;
        breakDownObjects = new ArrayList<Object>();


        testball = new Object(1000, 1, 0,0,0, 0,0f,0,modelBuilder, false);
        testball2 = new Object(10, 16, 8000,0,0, 0,0,0.5f,modelBuilder, false);
        testball3 = new Object(9.5f, 4, 10000,0,0, 0f,0f,0.5f,modelBuilder, false);
        testball4 = new Object(280000000, (float) 1 /15000, 1E10F,0,0, 0f,0f,0,modelBuilder, false);
        testball5 = new Object(2.7f, 4, 8000,0,50, 0.03f,0f,0.5f,modelBuilder, false);
        objects.add(testball);
        objects.add(testball2);
        objects.add(testball3);
        //objects.add(testball4);
        objects.add(testball5);
        for (Object object : objects) {
            object.assignCategory();
            object.assignProperties(modelBuilder);
            object.refreshmodel(modelBuilder);
            System.out.println(testball5.category);

        }



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
        if(Gdx.input.isKeyPressed(Input.Keys.U)){
            System.out.println(camDirection);
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
    public void triggerBreakaway(ArrayList<Object> objects, Object object){
        breakdown = new Breakdown(object.getLocation(), object.getRadius());
        for(int i =0; i< breakdown.getAmount(); i++) {
            //objects.add(new Object(random.nextFloat(0.1f,0.3f), 100, 100, 10, 10, 0, 0, 0, modelBuilder, true));
            objects.add(new Object(random.nextFloat((float) Math.pow(object.getRadius()/60,1/2f), (float) (object.getRadius()/7.5)), object.getDensity(), breakdown.getCoordinates()[i].x, breakdown.getCoordinates()[i].y, breakdown.getCoordinates()[i].z, object.getVelocity().x, object.getVelocity().y, object.getVelocity().z, modelBuilder, true));
        }
    }
    @Override
    public void render() {
        if (!gamerunning) {


            //if(Gdx.input.isKeyJustPressed(Input.Keys.X)) {
            forces.refreshArray(objects);
            chunkz.clear();
            for (Object object : objects) {
                object.advance(timeSpeed,modelBuilder);
                chunkz.positionOnGrid(object);
                //System.out.println(object.getCollision());
                //System.out.println(object.getMass());

            }
            ArrayList<Object> blacklist = new ArrayList<>();
            for (int object = 0; object<objects.size();object++) {
                if (objects.get(object).getDelete()) {
                    //System.out.println("delete!");
                    blacklist.add(objects.get(object));
                }
                //System.out.println(object);
            }
            for(int i =0; i<blacklist.size(); i++){
                objects.remove(blacklist.get(i));
            }
            blacklist.clear();
            //System.out.println(objects.get(0).getRadius());
            //System.out.println(objects.get(0).getDensity());
            //System.out.println(objects.size());

            for (Object object : objects) {
                //System.out.println(chunkz.centreOfBreakawayMass((int) (object.getLocation().x / chunksize), (int) (object.getLocation().y / chunksize), (int) (object.getLocation().z / chunksize)));
            }


        /*
        for(Object object : objects){
            ArrayList<Object> collisionobj = chunkz.potentialColliders(object);
            forces.contact(collisionobj);
        }
        */
            breakDownObjects.clear();
            breakDownObjects = forces.contact(objects);
            forces.gravity(objects);
            for(int i=0; i<breakDownObjects.size(); i++){
                triggerBreakaway(objects,breakDownObjects.get(i));
                breakDownObjects.get(i).setCollision(false);
                breakDownObjects.get(i).setDelete(true);
            }
            //}


            int currentspeed = mouse.currentSpeedLevel;
            if (mouse.currentSpeedLevel > mouse.scrollMax) {
                mouse.currentSpeedLevel -= Math.floorDiv(currentspeed, 10);
            }
            if (mouse.currentSpeedLevel < -mouse.scrollMax) {
                mouse.currentSpeedLevel -= Math.floorDiv(currentspeed, 10) + 1;
            }
            //System.out.println(mouse.currentSpeedLevel);
            ScreenUtils.clear(0f, 0f, 0f, 0f);
            if (Gdx.input.isKeyJustPressed(Input.Keys.CONTROL_LEFT)) {
                Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
                locked = !locked;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                timeSpeed *= 1.1;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                if (timeSpeed > 0) {
                    timeSpeed /= 1.1;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {

                if (timeSpeed != 0) {
                    previousspeed = timeSpeed;
                    timeSpeed *= 0;

                } else {
                    timeSpeed = previousspeed;
                }
            }
            float apparentspeed = (float) (trueSpeed * Math.exp(0.35 * mouse.currentSpeedLevel));
            doCameraMovement(camera, apparentspeed, 0.15f, locked);
            Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            Gdx.gl.glClearColor(0.05f,0.05f,0.05f,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
            modelBatch.begin(camera);
            camera.update();
        /*
        for(int i =0; i<breakdown.getCoordinates().length; i++) {
            modelBatch.render(modelInstances.get(i));
        }

         */


            for (Object object : objects) {
                object.draw(modelBatch);
            }

            modelBatch.end();


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
        modelBatch.dispose();
        model.dispose();
    }
}
