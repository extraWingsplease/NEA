package io.github.some_example_name;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class Breakdown {
    //ArrayList<Object> objects;
    Vector3[] coordinates;
    boolean done = false;
    int amount;
    public Breakdown(){
        amount = 2000;
        if(!done){
            coordinates = new Vector3[amount];
            int index = 0;
                for (int i = 0; i <= Math.ceil(Math.pow(amount,1/3f)); i++) {
                    for (int j = 0; j <= Math.ceil(Math.pow(amount,1/3f)); j++) {
                        for (int k = 0; k <= Math.ceil(Math.pow(amount,1/3f)); k++) {
                            if (index < amount) {
                                coordinates[index] = new Vector3((i + ((float) (j + (j % 2)) / 2)), (float) (k * (Math.pow(2, 0.5f) / Math.pow(3, 0.5f))), (float) (((3 * j) + (j)) / Math.pow(12, 0.5f)));
                                System.out.println(coordinates[index]);
                                index++;
                            }
                        }
                    }
                }
                done = true;
            }
    }

    public Vector3[] getCoordinates() {return coordinates;}
    public void setCoordinates(Vector3[] coordinates) {this.coordinates = coordinates;}

    public void deconstruct(int index, Vector3 relativeVelocity, float massRatio){
        int numberOfParts;
        /*
        numberOfParts = (int) ((relativeVelocity.len()* objects.get(index).getVolume())/(massRatio*5));
        float massOfParts = objects.get(index).getMass()/numberOfParts;

         */
        //System.out.println(numberOfParts);
    }
}
