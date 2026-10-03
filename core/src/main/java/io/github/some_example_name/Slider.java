package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Slider {
    BitmapFont font = new BitmapFont();
    Vector2 sliderPosition;
    float relativeWedgePosition;
    float width;
    float height;
    String model1;
    String model2;
    Texture wedgeTexture;
    Texture barTexture;
    float rotationDeg;
    float rotationRad;
    float min;
    float max;
    float value;
    public Slider(String wedge, String bar, float x, float y, float width, float height, float rotation, float minimum, float maximum, float startposition){
        model1 = wedge;
        model2 = bar;
        wedgeTexture = new Texture(wedge);
        barTexture = new Texture(bar);
        sliderPosition = new Vector2(x,y);
        this.width = width;
        this.height = height;
        rotationDeg = (float) (rotation);
        rotationRad = (float) (rotation * 2 * Math.PI)/360;
        min = minimum;
        max = maximum;
        value = (startposition * (max-min)) + min;
    }

    public Vector2 getPosition() {return sliderPosition;}
    public void setPosition(Vector2 position) {sliderPosition = position;}

    public float getWidth() {return width;}
    public void setWidth(float width) {this.width = width;}

    public float getHeight() {return height;}
    public void setHeight(float height) {this.height = height;}

    public String getBarModel() {return model2;}
    public void setBarModel(String bar) {model2 = bar;}

    public String getWedgeModel() {return model1;}
    public void setWedgeModel(String wedge) {model1 = wedge;}

    public float getRotation() {
        return rotationDeg;
    }

    public void setRotation(float rotation) {
        rotationDeg = rotation;
    }

    public float mapToMouse(){
        Vector2 mouseToSlider = new Vector2(Gdx.input.getX()-sliderPosition.x, (Gdx.graphics.getHeight()- Gdx.input.getY())-sliderPosition.y);
        float perpendicularDistance;
        int sign = 1;
        if(rotationDeg == 0 || rotationDeg == 360 || rotationDeg== 180){
            perpendicularDistance = (float) ((mouseToSlider.y));
            //System.out.println("x");
            if(mouseToSlider.x < 0){
                sign = -1;
            }
        }
        else if(rotationDeg == 90 || rotationDeg == 270){
            perpendicularDistance = (float) ((mouseToSlider.x));
            //System.out.println("y");
            if(mouseToSlider.y < 0){
                sign = -1;
            }
        }
        else{
            perpendicularDistance = (float) (mouseToSlider.y - Math.tan(rotationRad)*mouseToSlider.x/(Math.sqrt(Math.pow(mouseToSlider.y,2) + Math.pow(mouseToSlider.x,2))));
            //System.out.println("x and y");
            if(mouseToSlider.x < 0 || mouseToSlider.y < 0){
                sign = -1;
            }
        }

        float distanceAlong = (float) Math.sqrt(Math.pow(mouseToSlider.cpy().len(),2) - Math.pow(perpendicularDistance,2)) * sign;
        //System.out.println(mouseToSlider+" "+perpendicularDistance + " " + distanceAlong);
        return distanceAlong;

    }

    public void slide(){
        if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
            float mapped = mapToMouse();
            relativeWedgePosition = mapped/(width);
            if(relativeWedgePosition <0){
                relativeWedgePosition = 0;
            }
            if(relativeWedgePosition >1){
                relativeWedgePosition = 1;
            }
            value = (relativeWedgePosition * (max-min)) + min;
        }
    }
    public void draw(SpriteBatch batch){
        Sprite barSprite = new Sprite(barTexture);
        Sprite wedgeSprite = new Sprite(wedgeTexture);
        barSprite.setPosition(sliderPosition.x, sliderPosition.y);
        barSprite.setOrigin(barSprite.getX(), barSprite.getY());

        barSprite.setRotation(rotationDeg);
        rotationRad = (float) (rotationDeg * 2 * Math.PI)/360;
        barSprite.setSize(width,height);
        wedgeSprite.setPosition((float) ((float) (sliderPosition.x + (width*relativeWedgePosition*Math.cos(rotationRad))) -((height/2) * Math.cos(rotationRad))+ ((height)/2 * Math.sin(rotationRad))), (float) (sliderPosition.y + (width*relativeWedgePosition*Math.sin(rotationRad)) - ((height)/2 * Math.cos(rotationRad))- ((height)/2 * Math.sin(rotationRad))));
        wedgeSprite.setOrigin(barSprite.getX(),barSprite.getY());
        wedgeSprite.setRotation(rotationDeg);
        //System.out.println(wedgeSprite.getX()+ " " + wedgeSprite.getY());
        wedgeSprite.setSize(height,height*2);
        wedgeSprite.draw(batch);
        barSprite.draw(batch);
        System.out.println(barSprite.getX() + " " + barSprite.getY());

    }
}
