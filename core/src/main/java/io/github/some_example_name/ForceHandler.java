package io.github.some_example_name;


import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import java.util.*;

public class ForceHandler {
    Breakdown breakdown;
    ArrayList<Object> noFakeObjects;
    public ForceHandler(){
        //breakdown = new Breakdown();
        noFakeObjects = new ArrayList<Object>();
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
            for(int victim=actor; victim<objects.size(); victim++){
                if( victim != actor) {
                    //System.out.println(objects.get(actor).getX());
                    Vector3 vicLoc = objects.get(victim).getLocation();
                    Vector3 actLoc = nofakeobjects.get(actor).getLocation();
                    Vector3 distance = new Vector3(vicLoc.x - actLoc.x,vicLoc.y-actLoc.y,vicLoc.z - actLoc.z);
                    float magnitude = distance.len();
                    //System.out.println("distance from " +victim + " to " + actor + ": " + distance + magnitude);
                    //System.out.println(distance.nor().scl((float) (objects.get(actor).getMass() / Math.pow(magnitude, 2))));

                    if (magnitude!=0) {
                        Vector3 gravitation = distance.nor().scl((float) (-0.0000006743 *(nofakeobjects.get(actor).getMass() * objects.get(victim).getMass()) / Math.pow(magnitude, 2)));
                        objects.get(victim).newForce(gravitation);
                        nofakeobjects.get(actor).newForce(gravitation.scl(-1));
                    }
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
                            public void contact(ArrayList<Object> objects) {
                                // 1. Classical O(N^2) pair-wise loop
                                for (int actor = 0; actor < objects.size(); actor++) {
                                    Object a = objects.get(actor);

                                    // Start victim at actor + 1 to automatically avoid victim == actor
                                    // and avoid double-checking pairs (a vs b, then b vs a)
                                    for (int victim = actor + 1; victim < objects.size(); victim++) {
                                        Object v = objects.get(victim);

                                        // 2. Calculate distance vector from actor to victim
                                        Vector3 distance = v.getLocation().cpy().sub(a.getLocation());
                                        float magnitude = distance.len();
                                        float radii = v.getRadius() + a.getRadius();

                                        // Check for collision overlap
                                        if (magnitude <= radii && magnitude > 0) {

                                            // Normal vector pointing from actor to victim
                                            Vector3 normal = distance.cpy().nor();

                                            // Relative velocity (Actor relative to Victim)
                                            Vector3 relativeVelocity = a.getVelocity().cpy().sub(v.getVelocity());

                                            // Velocity along the normal vector
                                            float velocityAlongNormal = relativeVelocity.dot(normal);

                                            // Do not resolve if velocities are already separating
                                            if (velocityAlongNormal < 0) {

                                                // Coefficient of Restitution (1.0 = perfectly elastic, 0.0 = sticky plastic)
                                                float restitution = 0f;

                                                // Calculate impulse scalar using reduced mass
                                                float massA = a.getMass();
                                                float massV = v.getMass();

                                                float impulseScalar = -(1 + restitution) * velocityAlongNormal;
                                                impulseScalar /= (1 / massA + 1 / massV);

                                                // Apply impulse vector to change velocities instantly
                                                Vector3 impulse = normal.cpy().scl(impulseScalar);

                                                a.getVelocity().add(impulse.cpy().scl(1 / massA));
                                                v.getVelocity().sub(impulse.cpy().scl(1 / massV));

                                                // 3. Positional Correction (Prevents objects sinking/sticking together)
                                                float overlap = radii - magnitude;
                                                float percent = 0.8f; // Penetration allowance percentage
                                                float slop = 0.01f;   // Penetration allowance buffer

                                                Vector3 correction = normal.cpy().scl(Math.max(overlap - slop, 0.0f) / (1 / massA + 1 / massV) * percent);

                                                a.getLocation().sub(correction.cpy().scl(1 / massA));
                                                v.getLocation().add(correction.cpy().scl(1 / massV));
                                            }
                                        }
                                    }
                                }
                            }


}
