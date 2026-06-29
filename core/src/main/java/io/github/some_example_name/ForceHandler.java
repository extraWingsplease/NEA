package io.github.some_example_name;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;

public class ForceHandler {
    public ForceHandler(){

    }
    public void gravity(ArrayList<Object> objects){
        for(int actor=0; actor<objects.size(); actor++){
            for(int victim=0; victim<objects.size(); victim++){
                if( victim != actor && objects.get(actor).getMass() > 0.01 * objects.get(actor).getMass()) {
                    //System.out.println(objects.get(actor).getX());
                    Vector3 vicLoc = objects.get(victim).getLocation();
                    Vector3 actLoc = objects.get(actor).getLocation();
                    Vector3 distance = new Vector3(vicLoc.x - actLoc.x,vicLoc.y-actLoc.y,vicLoc.z - actLoc.z);
                    float magnitude = distance.len();
                    //System.out.println("distance from " +victim + " to " + actor + ": " + distance + magnitude);
                    //System.out.println(distance.nor().scl((float) (objects.get(actor).getMass() / Math.pow(magnitude, 2))));

                    if (magnitude!=0) {
                        Vector3 gravitation = distance.nor().scl((float) (-10 *(objects.get(actor).getMass() / Math.pow(magnitude, 2))));
                        objects.get(victim).newForce(gravitation);
                        objects.get(actor).newForce(gravitation.scl(-1));
                        //System.out.println(gravitation);
                    }
                }
            }
        }
    }

    public void contact(List<Object> objects) {
        for (int actor = 0; actor < objects.size(); actor++) {
            for (int victim = 0; victim < objects.size(); victim++) {
                if (victim != actor) {
                    Vector3 vicLoc = objects.get(victim).getLocation();
                    Vector3 actLoc = objects.get(actor).getLocation();
                    Vector3 distance = new Vector3(vicLoc.x - actLoc.x, vicLoc.y - actLoc.y, vicLoc.z - actLoc.z);
                    //Vector3 avgForce = new Vector3(objects.get(victim).getForce().x-objects.get(actor).getForce().x,objects.get(victim).getForce().y-objects.get(actor).getForce().y);
                    float magnitude = distance.len();
                    float radii = objects.get(victim).getRadius() + objects.get(actor).getRadius();
                    System.out.println(magnitude-radii);
                    if (magnitude <= radii) {
                        System.out.println("COLLIDEDDDDDDDDDddd");
                        Vector3 actingForce = distance.nor().scl((float) (objects.get(victim).getResultantforce().len() * Math.cos(Math.atan(distance.y / distance.x))));
                        //if (Math.cos(Math.atan(distance.y/distance.x))>0 && 0<(objects.get(victim).getVelocity().x*objects.get(victim).getVelocity().y*actingForce.x*actingForce.y)){
                        actingForce = actingForce.nor().scl(((float) (radii - magnitude)));
                        objects.get(victim).newForce(actingForce);
                        objects.get(actor).newForce(actingForce.scl(-1));
                        //System.out.println(actingForce);
                        //objects.get(victim).setVelocity(objects.get(victim).getVelocity().scl(actingForce.nor().scl(1f)));
                        if (objects.get(victim).getMass() < objects.get(actor).getMass()) {
                            objects.get(victim).setVelocity(objects.get(victim).getVelocity().scl(0.97f));
                        }
                            /*
                            else{
                                objects.get(actor).setVelocity(objects.get(actor).getVelocity().scl(0.97f));
                            }
                            */
                        //objects.get(victim).addVelocity(actingForce.scl(objects.get(victim).getVelocity().len() * 0.000001f/objects.get(victim).getMass()));
                        //}
                    }
                }
            }
        }
    }
}
