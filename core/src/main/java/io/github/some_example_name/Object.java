package io.github.some_example_name;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector;
import com.badlogic.gdx.math.Vector2;
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
import java.util.Date;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.EventListener;
import java.util.Random;
import com.badlogic.gdx.InputProcessor;
import java.util.EventListener.*;
import com.badlogic.gdx.InputAdapter;

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
    public Object(float rad, float density, float startX, float startY, float startZ, float startVX, float startVY, float startVZ, ModelBuilder modelBuilder, Boolean breakw, Boolean merge){
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
        collision = !merge;
        time = new Date();
        currenttime = System.currentTimeMillis();
        dTime = 0;
        currentTimeConstant = 1;
        //System.out.println(currenttime);
        deletiontime = -1;
        delete = false;
        timeSinceLastCollision = -1;



    }
    public float getRadius() {return radius;}
    public void setRadius(float radius) {this.radius = radius;}

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
        deletiontime = (long) (System.currentTimeMillis() + (milliseconds * currentTimeConstant));
    }

    public void advance(float timeConstant, ModelBuilder modelBuilder){
        currentTimeConstant = timeConstant;
        dTime = (float) ( currentTimeConstant* (System.currentTimeMillis() - currenttime)) /1000;
        currenttime = System.currentTimeMillis();
        acceleration = resultantForce.scl(1/mass);
        velocity.add(acceleration.cpy().scl(dTime));
        location.add(velocity.cpy().scl(dTime));
        resetForce();
        timeSinceLastCollision +=  dTime;
        System.out.println(dTime);
        dTime = 0;
        //System.out.println(deletiontime);
        if(currenttime >= deletiontime && deletiontime != -1){
            setDelete(true);
            //System.out.println("deleted");
        }
        if(timeSinceLastCollision >= 120 && timeSinceLastCollision != -1 && getBreakaway()){
            if(getCollision()) {
                setCollision(false);
                material = new Material(ColorAttribute.createDiffuse(Color.GREEN));
                refreshmodel(modelBuilder);
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
        instance.transform.setToTranslation(location);
        modelBatch.render(instance);
    }

}

