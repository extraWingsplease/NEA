package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;

import com.badlogic.gdx.InputProcessor;
import java.util.EventListener.*;
import com.badlogic.gdx.InputAdapter;

public class ChunkHandler {
    int chunkSize;
    int maxsquare;

    HashMap<Long, ArrayList<Object>> grid = new HashMap<Long, ArrayList<Object>>();
    public ChunkHandler(int chunkSize){
        this.chunkSize = chunkSize;
        maxsquare = 0;

    }
    public void clear(){
        grid.clear();
    }

    public void positionOnGrid(io.github.some_example_name.Object obj){
        int chunkX = (int) obj.getLocation().x/chunkSize;
        int chunkY = (int) obj.getLocation().y/chunkSize;
        int chunkZ = (int) obj.getLocation().z/chunkSize;


        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        grid.computeIfAbsent(longKey, k -> new ArrayList<>()).add(obj);
    }

    public ArrayList<Object> potentialColliders(io.github.some_example_name.Object obj){
        ArrayList<Object> vicinity = new ArrayList<>();
        int chunkX = (int) obj.getLocation().x/chunkSize;
        int chunkY = (int) obj.getLocation().y/chunkSize;
        int chunkZ = (int) obj.getLocation().z/chunkSize;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
                        (((long) chunkY & 0x1FFFFF) << 21) |
                        ((long) chunkZ & 0x1FFFFF);
                    ArrayList<Object> cellObjects = grid.get(longKey);
                    if(cellObjects != null){
                        vicinity.addAll(cellObjects);
                    }
                }
            }
        }
        return vicinity;
    }

    public float collectiveMass(int chunkX, int chunkY, int chunkZ){
        float chunkMass = 0;
        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        ArrayList<Object> cellObjects = grid.get(longKey);
        for (Object cellObject : cellObjects) {
            chunkMass += cellObject.getMass();
        }
        return chunkMass;
    }

    public float collectiveBreakawayMass(int chunkX, int chunkY, int chunkZ){
        float chunkMass = 0;
        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        ArrayList<Object> cellObjects = grid.get(longKey);
        for (Object cellObject : cellObjects) {
            if( cellObject.getBreakaway() ){
                chunkMass += cellObject.getMass();
            }
        }
        return chunkMass;
    }
    public float averageBreakawayMass(int chunkX, int chunkY, int chunkZ){
        float chunkMass = 0;
        int count = 0;
        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        ArrayList<Object> cellObjects = grid.get(longKey);
        for (Object cellObject : cellObjects) {
            if( cellObject.getBreakaway() ){
                chunkMass += cellObject.getMass();
                count ++;
            }
        }
        return chunkMass/count;
    }

    public Vector3 centreOfMass(int chunkX, int chunkY, int chunkZ){
        float xMass = 0;
        float yMass = 0;
        float zMass = 0;
        float chunkMass = collectiveMass(chunkX, chunkY,chunkZ);
        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        ArrayList<Object> cellObjects = grid.get(longKey);
        for (Object cellObject : cellObjects){
            xMass += cellObject.getMass() * cellObject.getLocation().x;
            yMass += cellObject.getMass() * cellObject.getLocation().y;
            zMass += cellObject.getMass() * cellObject.getLocation().z;
        }
        xMass = xMass/chunkMass;
        yMass = yMass/chunkMass;
        zMass = zMass/chunkMass;
        return new Vector3(xMass,yMass,zMass);
    }
    public Vector3 centreOfBreakawayMass(int chunkX, int chunkY, int chunkZ){
        float xMass = 0;
        float yMass = 0;
        float zMass = 0;
        float chunkMass = collectiveBreakawayMass(chunkX, chunkY,chunkZ);
        long longKey = (((long) chunkX & 0x1FFFFF) << 42) |
            (((long) chunkY & 0x1FFFFF) << 21) |
            ((long) chunkZ & 0x1FFFFF);
        ArrayList<Object> cellObjects = grid.get(longKey);
        for (Object cellObject : cellObjects){
            if(cellObject.getBreakaway()) {
                xMass += cellObject.getMass() * cellObject.getLocation().x;
                yMass += cellObject.getMass() * cellObject.getLocation().y;
                zMass += cellObject.getMass() * cellObject.getLocation().z;
            }
        }
        xMass = xMass/chunkMass;
        yMass = yMass/chunkMass;
        zMass = zMass/chunkMass;
        return new Vector3(xMass,yMass,zMass);
    }
    public Vector3 centreOfVicinityBreakawayMass(int chunkX, int chunkY, int chunkZ, int reach){
        int chunknum = (int) Math.pow(1+2*reach,3);
        float vicinityMass = 0;
        float xMass = 0;
        float yMass = 0;
        float zMass = 0;
        for(int x = chunkX-reach; x<= chunkX + reach; x++){
            for(int y = chunkY-reach; y<= chunkY + reach; y++){
                for(int z = chunkZ-reach; z<= chunkZ + reach; z++){
                    vicinityMass += collectiveBreakawayMass(x,y,z);
                    xMass += collectiveBreakawayMass(x,y,z) * centreOfBreakawayMass(x,y,z).x;
                    yMass += collectiveBreakawayMass(x,y,z) * centreOfBreakawayMass(x,y,z).y;
                    zMass += collectiveBreakawayMass(x,y,z) * centreOfBreakawayMass(x,y,z).z;
                }
            }
        }
        xMass = xMass/vicinityMass;
        yMass = yMass/vicinityMass;
        zMass = zMass/vicinityMass;
        return new Vector3(xMass,yMass,zMass);
    }

    public boolean validToCombine(int chunkX, int chunkY, int chunkZ, int cutoff){
        if(averageBreakawayMass(chunkX,chunkY,chunkZ)!=0){
        if(collectiveBreakawayMass(chunkX,chunkY,chunkZ)/averageBreakawayMass(chunkX,chunkY,chunkZ) >=cutoff){
            return true;
            }
        }
        return false;
    }
}
