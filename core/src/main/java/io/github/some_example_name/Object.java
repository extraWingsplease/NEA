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
    public Object(float rad, float density, float startX, float startY, float startZ, float startVX, float startVY, float startVZ, ModelBuilder modelBuilder){
        mass = (float) (3.14159 * Math.pow(radius, 3) * density * 0.75);
        location = new Vector3(startX, startY, startZ);
        velocity = new Vector3(startVX, startVY, startVZ);
        resultantForce = new Vector3(0, 0,0);
        acceleration = new Vector3(0, 0,0);
        model = modelBuilder.createSphere(radius*2,radius*2,radius*2,20,20,new Material(ColorAttribute.createDiffuse(Color.WHITE)),Usage.Position | Usage.Normal);
        instance = new ModelInstance(model);
        instance.transform.setToTranslation(location);
        radius = rad;

    }
    public float getRadius() {return radius;}
    public void setRadius(float radius) {this.radius = radius;}

    public float getMass() {return mass;}
    public void setMass(float mass) {this.mass = mass;}

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

    public void advance(){
        acceleration = resultantForce.scl(1/mass);
        velocity.add(acceleration);
        location.add(velocity);
        resetForce();
        //System.out.println(location);
    }

    public void draw(ModelBatch modelBatch){
        instance.transform.setToTranslation(location);
        modelBatch.render(instance);
    }

}

