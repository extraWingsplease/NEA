package io.github.some_example_name;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.environment.*;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.Date;

import java.util.Random;
import java.util.Vector;

public class Object {
    float radius;
    float mass;
    float density;
    Vector3 location;
    Vector3 velocity;
    Vector3 resultantForce;
    Vector3 acceleration;
    Model model;
    ModelInstance instance;
    Environment environment;
    Texture texture;
    Boolean breakaway;
    Boolean collision;
    Date time;
    float dTime;
    long currenttime;
    long deletiontime;
    Boolean delete;
    float currentTimeConstant;
    float timeSinceLastCollision;
    Material material;
    float schwarzschildRadius;
    int category;
    Random random;
    public Object(float rad, float density, float startX, float startY, float startZ, float startVX, float startVY, float startVZ, ModelBuilder modelBuilder, Boolean breakw){
        breakaway = breakw;
        radius = (float) rad;
        this.density = density;
        mass = (float) (3.14159 * Math.pow(radius, 3) * density * 4/3);
        location = new Vector3(startX, startY, startZ);
        velocity = new Vector3(startVX, startVY, startVZ);
        resultantForce = new Vector3(0, 0,0);
        acceleration = new Vector3(0, 0,0);
        material = new Material(ColorAttribute.createDiffuse(Color.WHITE));
        model = modelBuilder.createSphere(radius*2,radius*2,radius*2,20,20,material,Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);
        instance.transform.setToTranslation(location);
        environment = new Environment();
        environment.add(new PointLight().set(1f,1f,1f, new Vector3(0,0,1000), 1000000f));
        collision = true;
        time = new Date();
        currenttime = System.currentTimeMillis();
        dTime = 0;
        currentTimeConstant = 1;
        //System.out.println(currenttime);
        deletiontime = -1;
        delete = false;
        timeSinceLastCollision = -1;
        random = new Random();

        category = 4;
    }
    public float getRadius() {return radius;}
    public void setRadius(float radius) {this.radius = radius;}

    public void assignCategory(){
        schwarzschildRadius = (float) ((0.0000006743*2*getMass())/Math.pow(173.9848013,2));
        //System.out.println(schwarzschildRadius);
        /*
        -1. TEMPORARY BREAKAWAY OBJECT
        - Create no gravity of their own
        - Have a chance to delete upon collision with a non-breakaway object
        - Will turn of collision checking if a set amount of time has passed without a collision
         */
        if(getBreakaway()){
            category = -1;
        }

        /*
        0. BLACK HOLE
        - Completely Black
        - Do not check for collisions, instead merge other objects into itself and increase its radius accordingly
        - 2 black holes colliding will result in a singular larger black hole
        - Objects such as stars/planets will break apart when coming near this, into non collision objects - like spaghettification
         */
        else if(getRadius()<=schwarzschildRadius){
            category = 0;
        }

        /*
        1. NEUTRON STAR
        - Bluish-white
        - Act similarly to a rocky planet, just incredibly dense
        - Size around a small city
        - Emit incredibly bright light
         */
        else if((getDensity() >= 2.6E14) && (getDensity() <= 4.1E14)){
            category = 1;
        }

        /*
        2. STAR
        - Spectrum of colours from red-blue
        - Collisions off, objects such as planets will merge into the star like a black hole
        - Emit bright light
         */
        else if(getRadius()>=86 && getDensity()<75){
            category = 2;
        }

        /*
        3. GAS GIANT
        - Wide range of colours
        - Similar in behaviour to stars, just way less massive
        - Collisions off
         */
        else if(getDensity()<=10 && radius >= 16){
            category = 3;
        }

        /*
        4. ROCKY PLANET/OBJECT
        - Wide range of colours
        - Collisions on
        - Break into many breakaway objects upon collisions
         */
        else{
            category = 4;
        }
        //System.out.println(schwarzschildRadius);
    }
    public int getCategory() {return category;}

    public void assignProperties(ModelBuilder modelBuilder){
        if(category == 0){
            collision = false;
            material = new Material(ColorAttribute.createDiffuse(0f,0f,0f,1f));
            refreshmodel(modelBuilder);
        }
        if(category == 1){
            collision = true;
            material = new Material(ColorAttribute.createDiffuse(0.9f,0.9f,1f,1f));
            refreshmodel(modelBuilder);
        }
        if(category == 2){
            collision = false;
            //float colourScale = random.nextFloat(0,3);
            float colourScale = ((getRadius()-86)/2500000) * 6;
            if (colourScale >= 6){
                colourScale = 6;
            }
            if(colourScale >4) {
                material = new Material(ColorAttribute.createDiffuse(1f, 3- colourScale/2, 0f, 1f));


            }
            else if(colourScale >1){
                material = new Material(ColorAttribute.createDiffuse(1f, 1f, 1f-(colourScale-1)/3, 1f));
            }

            else{
                material = new Material(ColorAttribute.createDiffuse(1- colourScale, 1- colourScale, 1f, 1f));
            }
            refreshmodel(modelBuilder);
        }
        if(category == 3){
            collision = false;
        }
        if(category == 4){
            collision = true;
        }

    }

    public float getVolume() {return (float) ((float) Math.pow(radius,3) * (4/3f)* Math.PI);}

    public float getMass() {return mass;}
    public void setMass(float mass) {this.mass = mass;}
    public void addMassWRADIUS(float mass) {
        this.mass+=mass;
        radius = (float) Math.pow(((this.mass/density)/(Math.PI * 4.0/3.0)),1.0/3.0);
    }
    public void addMassWDENSITY(float mass) {
        this.mass += mass;
        density = (float) (this.mass/(Math.pow(radius,3)*Math.PI*4/3f));
    }

    public float getDensity() {return density;}
    public void setDensity(float density) {this.density = density;}

    public Vector3 getLocation() {return location;}
    public void setLocation(Vector3 location) {this.location = location;}

    public Vector3 getVelocity() {return velocity;}
    public void setVelocity(Vector3 velocity) {this.velocity = velocity;}

    public Vector3 getResultantforce() {return resultantForce;}
    public void setResultantforce(Vector3 resultantForce) {this.resultantForce = resultantForce;}

    public Vector3 getAcceleration() {return acceleration;}
    public void setAcceleration(Vector3 acceleration) {this.acceleration = acceleration;}

    public void newForce(Vector3 incomingForce){resultantForce.add(incomingForce);}
    public void resetForce(){resultantForce.scl(0);}

    public Boolean getBreakaway() {return breakaway;}
    public void setBreakaway(Boolean breakaway) {this.breakaway = breakaway;}

    public Boolean getCollision() {return collision;}
    public void setCollision(Boolean collision) {this.collision = collision;}

    public Boolean getDelete() {return delete;}
    public void setDelete(Boolean delete) {this.delete = delete;}

    public float getTimeSinceLastCollision() {return timeSinceLastCollision;}
    public void setTimeSinceLastCollision(float timeSinceLastCollision) {this.timeSinceLastCollision = timeSinceLastCollision;}

    public void startDeletiontimer(int milliseconds){
        if( deletiontime == -1) {
            deletiontime = (long) (System.currentTimeMillis() + (milliseconds * currentTimeConstant));
        }
    }

    public void advance(float timeConstant, ModelBuilder modelBuilder){
        assignCategory();
        currentTimeConstant = timeConstant;
        dTime = (float) ( currentTimeConstant* (System.currentTimeMillis() - currenttime)) /1000;
        currenttime = System.currentTimeMillis();
        acceleration = resultantForce.scl(1/mass);
        velocity.add(acceleration.cpy().scl(dTime));
        location.add(velocity.cpy().scl(dTime));
        resetForce();
        timeSinceLastCollision +=  dTime;
        //System.out.println(dTime);
        dTime = 0;
        //System.out.println(deletiontime);
        if(currenttime >= deletiontime && deletiontime != -1){
            setDelete(true);
            //System.out.println("deleted");
        }
        if(timeSinceLastCollision >= 120 && timeSinceLastCollision != -1 && getBreakaway()){
            if(getCollision()) {
                setCollision(false);
                //material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
                //refreshmodel(modelBuilder);
            }
            //System.out.println("collision off");

        }

    }
    public void refreshmodel(ModelBuilder modelBuilder){
        model.dispose();
        model = modelBuilder.createSphere(radius*2,radius*2,radius*2,20,20,material,Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);
    }

    public void draw(ModelBatch modelBatch){
        instance.transform.setToTranslation(location.cpy().sub(velocity.cpy().scl(0)));
        modelBatch.render(instance);
        //instance.transform.setToTranslation(location.cpy().sub(velocity.cpy().scl(0)));
        //modelBatch.render(instance);
    }

}

