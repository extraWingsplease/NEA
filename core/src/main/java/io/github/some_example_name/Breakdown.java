package io.github.some_example_name;

import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;


public class Breakdown {
    //ArrayList<Object> objects;
    Vector3[] coordinates;
    boolean done = false;
    int amount;
    Vector3 centre;
    public Breakdown(Vector3 centre){
        amount = 1728;
        if(!done){
            coordinates = new Vector3[amount];
            int index = 0;
                float sideAmount = (float) Math.pow(amount,1/3f);
                for (int i = (int) Math.ceil(-sideAmount/2); i <= (int) Math.floor(sideAmount/2)-1; i++) {
                    for (int j = (int) Math.ceil(-sideAmount/2); j <= (int) Math.floor(sideAmount/2)-1; j++) {
                        for (int k = (int) Math.ceil(-sideAmount/2); k <= (int) Math.floor(sideAmount/2)-1; k++) {
                            if (index < amount) {
                                coordinates[index] = new Vector3((i + ((float) (j + (j % 2)) / 2))/5+centre.x, (float) (k * (Math.pow(2, 0.5f) / Math.pow(3, 0.5f)))/5+centre.y, (float) (((3 * j) + (j)) / Math.pow(12, 0.5f))/5+centre.z);
                                //System.out.println(coordinates[index]);
                                index++;
                                System.out.println("index " + index + ": "+i +", "+ j +", "+ k);

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
