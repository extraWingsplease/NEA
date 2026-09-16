package io.github.some_example_name;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Button {
    BitmapFont font = new BitmapFont();
    Vector2 position;
    float width;
    float height;
    String model1;
    String model2;
    Texture texture1;
    Texture texture2;
    int currentModelIndex;
    public Button(String ambientModel, String activeModel, float x, float y, float width, float height){
        model1 = ambientModel;
        model2 = activeModel;
        texture1 = new Texture(ambientModel);
        texture2 = new Texture(activeModel);
        position = new Vector2(x,y);
        this.width = width;
        this.height = height;
        currentModelIndex = 1;
    }

    public Vector2 getPosition() {return position;}
    public void setPosition(Vector2 position) {this.position = position;}

    public float getWidth() {return width;}

    public void setWidth(float width) {this.width = width;}

    public float getHeight() {return height;}
    public void setHeight(float height) {this.height = height;}

    public String getAmbientModel() {return model1;}
    public void setAmbientModel(String ambientModel) {model1 = ambientModel;}

    public String getActiveModel() {return model2;}
    public void setActiveModel(String activeModel) {model2 = activeModel;}

    public boolean isMouseHovering(boolean switchModel){
        if(Gdx.input.getX() >= position.x && Gdx.input.getX() <= position.x + width && Gdx.input.getY() >= position.y && Gdx.input.getY() <= position.y + height){
            if(switchModel){
                currentModelIndex = 2;
            }
            return true;
        }
        else{
            currentModelIndex = 1;
            return false;
        }
    }
    public void draw(SpriteBatch batch){
        Texture currentTexture = null;
        if(currentModelIndex == 1){
            currentTexture = texture1;
        }
        else if(currentModelIndex == 2){
            currentTexture = texture2;
        }
        assert currentTexture != null;
        Sprite sprite = new Sprite(currentTexture);
        sprite.draw(batch);
    }
}
