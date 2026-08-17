package io.github.some_example_name;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;
import java.util.Random;

public class ForceHandler {
    Breakdown breakdown;
    ArrayList<Object> noFakeObjects;
    Random random;
    ModelBuilder modelBuilder;
    Boolean skiploop;
    public ForceHandler(){
        //breakdown = new Breakdown();
        noFakeObjects = new ArrayList<Object>();
        random = new Random();
        modelBuilder = new ModelBuilder();
        skiploop = false;
    }
    public void refreshArray(ArrayList<Object> objects){
        noFakeObjects.clear();
        for(int i = 0; i < objects.size(); i++){
            if(!objects.get(i).getBreakaway()) {
                noFakeObjects.add(objects.get(i));
            }
        }
    }
    public void gravity(ArrayList<Object> objects){
        ArrayList<Object> nofakeobjects = noFakeObjects;
        for(int actor=0; actor<nofakeobjects.size(); actor++){
        Object a = nofakeobjects.get(actor);
        for(int victim=actor+1; victim<objects.size(); victim++){
            Object v = objects.get(victim);
                //System.out.println(objects.get(actor).getX());
                Vector3 vicLoc = v.getLocation();
                Vector3 actLoc = a.getLocation();
                Vector3 distance = new Vector3(vicLoc.x - actLoc.x,vicLoc.y-actLoc.y,vicLoc.z - actLoc.z);
                float magnitude = distance.len();
                //System.out.println("distance from " +victim + " to " + actor + ": " + distance + magnitude);
                //System.out.println(distance.nor().scl((float) (objects.get(actor).getMass() / Math.pow(magnitude, 2))));

                if (magnitude!= 0 && Math.pow(magnitude,2) >= Math.pow(a.radius,2)/4) {
                    Vector3 gravitation = distance.nor().scl((float) (-0.0000006743 * (a.getMass() * v.getMass()) / Math.pow(magnitude, 2)));
                    v.newForce(gravitation);
                    a.newForce(gravitation.scl(-1));
                }
            }
        }
    }

    /*
    public void contact(ArrayList<Object> objects) {

        for (int actor = 0; actor < objects.size(); actor++) {
            for (int victim = actor; victim < objects.size(); victim++) {
                if (victim != actor) {
                    Vector3 vicLoc = objects.get(victim).getLocation();
                    Vector3 actLoc = objects.get(actor).getLocation();
                    Vector3 distance = new Vector3(vicLoc.x - actLoc.x, vicLoc.y - actLoc.y, vicLoc.z - actLoc.z);
                    Vector3 velocityDifference = objects.get(actor).getVelocity().cpy().sub(objects.get(victim).getVelocity().cpy());
                    float magnitude = distance.len();
                    Vector3 gravitation = distance.nor().scl((float) (-0.1 *(objects.get(actor).getMass() / Math.pow(magnitude, 2))));
                    Vector3 gravitationAcceleration = gravitation.cpy().scl(1/objects.get(victim).getMass());
                    //Vector3 avgForce = new Vector3(objects.get(victim).getForce().x-objects.get(actor).getForce().x,objects.get(victim).getForce().y-objects.get(actor).getForce().y);

                    float radii = objects.get(victim).getRadius() + objects.get(actor).getRadius();
                    //System.out.println(radii);
                    if (magnitude <= radii) {
                        //System.out.println("COLLIDEDDDDDDDDDddd");
                        Vector3 actingForce = distance.cpy().nor();
                        //actingForce = distance.cpy().nor().scl((float) (Math.cos(Math.atan(distance.y / distance.x))));
                        //if (Math.cos(Math.atan(distance.y/distance.x))>0 && 0<(objects.get(victim).getVelocity().x*objects.get(victim).getVelocity().y*actingForce.x*actingForce.y)){
                        //if(velocityDifference.len() >= gravitationAcceleration.len()) {
                            //System.out.println("yes");
                            actingForce = actingForce.scl(((float) velocityDifference.dot(distance.cpy().nor()) * objects.get(victim).getMass()) / 10f).add(gravitation.cpy().scl(-1));;
                        //}
                        //breakdown.deconstruct(victim,velocityDifference,objects.get(actor).getMass()/objects.get(victim).getMass());
                        objects.get(victim).newForce(actingForce);
                        objects.get(actor).newForce(actingForce.scl(-1));

                        //System.out.println(actingForce);
                        /*
                        if (objects.get(victim).getMass() < objects.get(actor).getMass()) {
                            objects.get(victim).setVelocity(objects.get(victim).getVelocity().scl(0.97f));
                        }
                        */

                            /*
                            else{
                                objects.get(actor).setVelocity(objects.get(actor).getVelocity().scl(0.97f));
                            }

                        //objects.get(victim).addVelocity(actingForce.scl(objects.get(victim).getVelocity().len() * 0.000001f/objects.get(victim).getMass()));
                        //}
                    }
                }
            }
        }
    }


     */
    public void triggerBreakaway(ArrayList<Object> objects, Object object){
        breakdown = new Breakdown(object.getLocation(), object.getRadius());
        for(int i =0; i< breakdown.getAmount(); i++) {
            //objects.add(new Object(random.nextFloat(0.1f,0.3f), 100, 100, 10, 10, 0, 0, 0, modelBuilder, true));
            objects.add(new Object(random.nextFloat(0.05f,0.4f), 100, breakdown.getCoordinates()[i].x, breakdown.getCoordinates()[i].y, breakdown.getCoordinates()[i].z, object.getVelocity().x, object.getVelocity().y, object.getVelocity().z, modelBuilder, true,false));
        }
        objects.remove(object);
    }
    public void contact(ArrayList<Object> REALobjects) {
        ArrayList<Object> objects = new ArrayList<Object>();
        for(int i=0; i<REALobjects.size(); i++){
            if(REALobjects.get(i).getCollision()){
                objects.add(REALobjects.get(i));
            }
        }
        //System.out.println(objects.size() + ":" + REALobjects.size());
        for (int actor = 0; actor < objects.size(); actor++) {
            Object a = objects.get(actor);
            for (int victim = 0; victim < REALobjects.size(); victim++) {
                Object v = REALobjects.get(victim);
                if(v!=a){
                Vector3 distance = v.getLocation().cpy().sub(a.getLocation());
                float magnitude = distance.len();
                float radii = v.getRadius() + a.getRadius();
                if(v.getBreakaway() && !a.getBreakaway() && Math.pow(magnitude,2) <= Math.pow(radii,2)){
                    //System.out.println("collision");
                    if (a.collision) {
                        if (random.nextInt(0, 100) >= 90) {
                            v.setCollision(false);
                            v.startDeletiontimer(5);
                            if (random.nextInt(0, 100) >= 90) {
                                a.addMassWRADIUS(v.getMass());
                                a.refreshmodel(modelBuilder);
                            }
                            else{
                                a.addMassWDENSITY(v.getMass());
                            }
                        }
                    } else {
                        if (random.nextFloat() >= 0.997f) {
                            v.startDeletiontimer(5);
                            if (random.nextInt(0, 100) >= 90) {
                                a.addMassWDENSITY(v.getMass());
                            }
                            else{
                                a.addMassWRADIUS(v.getMass());
                                a.refreshmodel(modelBuilder);
                            }

                        }
                    }
                }
                if (Math.pow(magnitude,2) <= Math.pow(radii,2) && a.collision && v.collision) {
                    skiploop = false;
                    if(!v.getBreakaway() && !a.getBreakaway() && a.getMass() > v.getMass()/100){
                        triggerBreakaway(objects,v);
                        skiploop = true;
                    }
                    if(!skiploop) {
                        Vector3 normal = distance.cpy().nor();
                        Vector3 relativeVelocity = a.getVelocity().cpy().sub(v.getVelocity());
                        float velocityAlongNormal = relativeVelocity.dot(normal);
                        float massA = a.getMass();
                        float massV = v.getMass();

                        float impulseScalar = -(1.99f) * velocityAlongNormal;
                        impulseScalar *= 1 / (1 / massA + 1 / massV);
                        Vector3 impulse = normal.cpy().scl(impulseScalar);

                        a.getVelocity().add(impulse.cpy().scl(1 / massA));
                        v.getVelocity().sub(impulse.cpy().scl(1 / massV));
                        float overlap = radii - magnitude;
                        float allowance = 0.01f;

                        Vector3 correction = normal.scl(Math.max(overlap - allowance, 0.0f) / (1 / massA + 1 / massV));

                        a.getLocation().sub(correction.cpy().scl(1 / massA));
                        v.getLocation().add(correction.cpy().scl(1 / massV));
                        a.setTimeSinceLastCollision(0);
                        v.setTimeSinceLastCollision(0);
                    }

                }
                }
            }
        }
    }


}
