package io.github.some_example_name;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class Breakdown {
    ArrayList<Object> objects;
    Vector3[] coordinates;
    public Breakdown(ArrayList<Object> objs){
        objects = objs;
        coordinates = new Vector3[740];
        for(int index=0;index<740;index++) {
            for (int i = 0; i <= 10; i++) {
                for (int j = 0; j <= 10; j++) {
                    for (int k = 0; k <= 10; k++) {
                        coordinates[index] = new Vector3((i + (float) (j + (j % 2)) /2), (float) (k*(Math.pow(2,0.5f)/Math.pow(3,0.5f))), (float) ((3*j+(j%2))/Math.pow(12,0.5f)));
                    }
                }
            }
        }
    }
    public void deconstruct(int index, Vector3 relativeVelocity, float massRatio){
        int numberOfParts;
        numberOfParts = (int) ((relativeVelocity.len()* objects.get(index).getVolume())/(massRatio*5));
        float massOfParts = objects.get(index).getMass()/numberOfParts;
        System.out.println(numberOfParts);
    }
}
