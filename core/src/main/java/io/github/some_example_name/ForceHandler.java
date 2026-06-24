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
                    //System.out.println(obj.get(actor).getX());
                    Vector3 vicLoc = objects.get(victim).getLocation();
                    Vector3 actLoc = objects.get(actor).getLocation();
                    Vector3 distance = new Vector3(vicLoc.x - actLoc.x,vicLoc.y-actLoc.y,vicLoc.z - actLoc.z);
                    float magnitude = distance.len();
                    //System.out.println("distance from " +victim + " to " + actor + ": " + distance + magnitude);
                    //System.out.println(distance.nor().scl((float) (obj.get(actor).getMass() / Math.pow(magnitude, 2))));

                    if (magnitude!=0) {
                        Vector3 gravitation = distance.nor().scl((float) (-10 *(objects.get(actor).getMass() / Math.pow(magnitude, 2))));
                        objects.get(victim).newForce(gravitation);
                        objects.get(actor).newForce(gravitation.scl(-1));
                        System.out.println(gravitation);
                    }
                }
            }
        }

    }
}
