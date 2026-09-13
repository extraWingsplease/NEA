package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.PointLight;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.PointSpriteParticleBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.ScreenUtils;

import java.io.*;
import java.util.*;

/*
CONSTANTS/NOTABLE THINGS FOR THIS SIMULATION:
Gravitational constant - 0.0000006743
Speed of light - 3000
Density of the sun - 1
Radius of the sun - 1000
all calculations are done relative to the sun's density and radius
 */
/*
CURRENT KEYBINDS

CTRL - lock/unlock mouse from 1st person

WASD,SHIFT,SPACE - movement
SCROLL WHEEL - control player
U - output players current position and direction

RIGHT ARROW - speed up simulation time
LEFT ARROW - slow down simulation time
P - pause/unpause time

UP ARROW - increase FOV
DOWN ARROW - decrease FOV

SEMI COLON - set current place as a waypoint
APOSTROPHE - warp back to placed waypoint

L - load demo solar system world
K - save current world
I - load saved world

X - create arbitrary planet

 */

/** {@link ApplicationListener} implementation shared by all platforms. */
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
    Boolean wayPoint;
    Vector3 wayPointPosition;
    Vector3 wayPointDirection;
    float wayPointSpeed;
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
    Object testball6;
    Object testball7;
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
    ParticleSystem particleSystem;
    PointSpriteParticleBatch pointSpriteBatch;
    ParticleEffect particleEffect;


    @Override
    public void create() {
        particleSystem = new ParticleSystem();
        modelBatch = new ModelBatch();
        environment = new Environment();
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camPosition = new Vector3(-1,0,0);
        camera.position.set(camPosition);
        mouse = new mouseScroll();
        random = new Random();
        camera.lookAt(0,0,0);
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
        wayPoint = false;
        wayPointPosition = new Vector3(0,0,0);
        wayPointDirection = new Vector3(0,0,0);
        wayPointSpeed = trueSpeed;

        particleEffect = new ParticleEffect();
        //particleEffect.load(Gdx.files.internal("Test Glow Particle"), Gdx.files.internal(""));


        testball = new Object(1000, 1, 0,0,0, 0,0f,0,modelBuilder, false, -1, -1, -1);
        testball2 = new Object(10, 16, 8000,0,0, 0,0,0.5f,modelBuilder, false, -1, -1, -1);
        testball3 = new Object(9.5f, 4, 10000,0,0, 0f,0f,0.5f,modelBuilder, false, -1, -1, -1);
        testball4 = new Object(2.7f, 4, 8000,0,50, 0.03f,0f,0.5f,modelBuilder, false, 0.5f, 0.5f, 0.5f);
        testball5 = new Object(1/70f, 2.7E14F, 0,0,0, 0,0f,0,modelBuilder, false, -1, -1, -1);
        testball6 = new Object(1000f, 2.7E11F, 0,0,-10000000, 0,0f,0,modelBuilder, false, -1, -1, -1);
        testball7 = new Object(10, 4, 10000,0,100, 0,0f,0,modelBuilder, false, -1, -1, -1);
        objects.add(testball);
        //objects.add(testball2);
        //objects.add(testball3);
        //objects.add(testball4);
        //objects.add(testball7);
        //objects.add(testball5);
        //objects.add(testball6);
        //objects.add(testball7);
        //File file = new File(".");
        //for(String fileNames : file.list()) System.out.println(fileNames);



        for (Object object : objects) {

            object.assignCategory();
            object.assignProperties(modelBuilder,environment);
            object.refreshmodel(modelBuilder);
            System.out.println(object.schwarzschildRadius);
            System.out.println(object.getR() + object.getG() + object.getB());
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

    public void doCameraMovement(PerspectiveCamera camera, float speed, float sensitivity, boolean locked) throws IOException {
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.U)){
            System.out.println(camPosition);
            System.out.println(camDirection);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if (camera.fieldOfView <= 100) {
                camera.fieldOfView += 1;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            if (camera.fieldOfView >= 20) {
                camera.fieldOfView -= 1;
            }
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
            Object obj = new Object(random.nextFloat((float) Math.pow(object.getRadius()/60,1/2f), (float) (object.getRadius()/7.5)), object.getDensity(), breakdown.getCoordinates()[i].x, breakdown.getCoordinates()[i].y, breakdown.getCoordinates()[i].z, object.getVelocity().x, object.getVelocity().y, object.getVelocity().z, modelBuilder, true, (float) (object.getR() * 0.95 + random.nextFloat(0,0.05f)), (float) (object.getG() * 0.95 + random.nextFloat(0,0.05f)), (float) (object.getB() * 0.95 + random.nextFloat(0,0.05f)));
            objects.add(obj);
            obj.assignCategory();
            obj.assignProperties(modelBuilder,environment);
        }
    }
    public void loadWorld(String fileName) throws IOException {
        System.out.println("loading...");
        objects.clear();
        float Oradius;
        float Odensity;
        float Ox;
        float Oy;
        float Oz;
        float OVx;
        float OVy;
        float OVz;
        float Obreak;
        boolean Obreakaway = true;
        float Or;
        float Og;
        float Ob;
        float Ocoll;
        boolean Ocollision = false;
        FileReader fileReader = new FileReader(fileName);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        int count = (int) bufferedReader.lines().count();
        fileReader = new FileReader(fileName);
        bufferedReader = new BufferedReader(fileReader);
        List<String> playerInfo;
        String playerInfoString;
        playerInfoString = bufferedReader.readLine();
        playerInfo = Arrays.asList(playerInfoString.split(","));
        System.out.println(playerInfoString);
        System.out.println(playerInfo);
        float x = Float.parseFloat(playerInfo.get(0));
        float y = Float.parseFloat(playerInfo.get(1));
        float z = Float.parseFloat(playerInfo.get(2));
        float Lx = Float.parseFloat(playerInfo.get(3));
        float Ly = Float.parseFloat(playerInfo.get(4));
        float Lz = Float.parseFloat(playerInfo.get(5));
        Vector3 newPosition = new Vector3(x,y,z);
        Vector3 newDirection = new Vector3(Lx,Ly,Lz).nor();
        camPosition.set(newPosition);
        camDirection.set(newDirection.cpy().nor());
        for(int i=0; i<count-1; i++){
            List<String> nextObject;
            String nextObjectString;
            nextObjectString = bufferedReader.readLine();
            nextObject = Arrays.asList(nextObjectString.split(","));
            Oradius = Float.parseFloat(nextObject.get(0));
            Odensity = Float.parseFloat(nextObject.get(1));
            Ox = Float.parseFloat(nextObject.get(2));
            Oy = Float.parseFloat(nextObject.get(3));
            Oz = Float.parseFloat(nextObject.get(4));
            OVx = Float.parseFloat(nextObject.get(5));
            OVy = Float.parseFloat(nextObject.get(6));
            OVz = Float.parseFloat(nextObject.get(7));
            Obreak = Float.parseFloat(nextObject.get(8));
            if(Obreak == 0){
                Obreakaway = false;
            }
            else if (Obreak == 1){
                Obreakaway = true;
            }
            Or = Float.parseFloat(nextObject.get(9));
            Og = Float.parseFloat(nextObject.get(10));
            Ob = Float.parseFloat(nextObject.get(11));
            Ocoll = Float.parseFloat(nextObject.get(12));
            if(Ocoll == 0){
                Ocollision = false;
            }
            else if (Ocoll == 1){
                Ocollision = true;
            }
            Object obj = new Object(Oradius,Odensity,Ox,Oy,Oz,OVx,OVy,OVz,modelBuilder,Obreakaway,Or,Og,Ob);
            obj.setCollision(Ocollision);
            objects.add(obj);

        }
        bufferedReader.close();;
        for (Object object : objects) {
            object.assignCategory();
            object.assignProperties(modelBuilder,environment);
            object.refreshmodel(modelBuilder);
        }
        System.out.println("loaded with " + count + " objects");

    }
    public void saveWorld(String fileName) throws IOException {
        System.out.println("Saving...");
        FileWriter fileWriter = new FileWriter(fileName,false);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(camPosition.x + "," + camPosition.y + "," + camPosition.z + "," + camDirection.x + "," + camDirection.y + "," + camDirection.z);
        for(Object object : objects) {
            bufferedWriter.newLine();
            int breakaway = 0;
            if(object.getBreakaway()){
                breakaway = 1;
            }
            int collision = 0;
            if(object.getCollision()){
                collision = 1;
            }
            bufferedWriter.write(object.getRadius() + "," + object.getDensity() + "," + object.getLocation().x + "," + object.getLocation().y + "," + object.getLocation().z + "," + object.getVelocity().x + "," + object.getVelocity().y + "," + object.getVelocity().z + "," + breakaway + "," + object.getR() + "," + object.getG() + "," + object.getB() + "," + collision);

        }
        System.out.println("Saved!");
        bufferedWriter.close();
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
                if(object.getCategory() != -1){
                object.assignProperties(modelBuilder,environment);
                }
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
            if (Gdx.input.isKeyJustPressed(Input.Keys.X)) {
                Object obj = new Object(random.nextFloat(2,20f), 4, camera.position.x + camera.direction.cpy().nor().scl(100).x,camera.position.y + camera.direction.cpy().nor().scl(100).y,camera.position.z + camera.direction.cpy().nor().scl(100).z, 0,0,0f,modelBuilder, false, -1, -1, -1);
                objects.add(obj);
                obj.assignCategory();
                obj.assignProperties(modelBuilder,environment);
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.APOSTROPHE)) {
                if(wayPoint){
                    camPosition.set(wayPointPosition.cpy());
                    camDirection.set(wayPointDirection.cpy());
                    mouse.currentSpeedLevel = (int) wayPointSpeed;
                }
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.SEMICOLON)) {
                    wayPointPosition.set(camPosition.cpy());
                    wayPointDirection.set(camDirection.cpy());
                    wayPointSpeed = mouse.currentSpeedLevel;
                    wayPoint = true;

            }
            float apparentspeed = (float) (trueSpeed * Math.exp(0.35 * mouse.currentSpeedLevel));
            try {
                doCameraMovement(camera, apparentspeed, 0.15f, locked);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.L)){
                try {
                    loadWorld("LoadTest");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
                try {
                    saveWorld("SaveTest");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.I)){
                try {
                    loadWorld("SaveTest");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
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
                object.draw(modelBatch,environment);
            }
            //environment.clear();

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
