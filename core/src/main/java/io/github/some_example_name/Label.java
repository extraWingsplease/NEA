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

public class Label {
    Vector2 position;
    float width;
    float height;
    String model;
    Texture texture;
    public Label(String Model, float x, float y, float width, float height){
        model = Model;
        texture = new Texture(model);
        position = new Vector2(x,y);
        this.width = width;
        this.height = height;
    }

    public Vector2 getPosition() {return position;}
    public void setPosition(Vector2 position) {this.position = position;}

    public float getWidth() {return width;}

    public void setWidth(float width) {this.width = width;}

    public float getHeight() {return height;}
    public void setHeight(float height) {this.height = height;}

    public String getModel() {return model;}
    public void setModel(String model) {this.model = model;
    texture = new Texture(model);}

    public void draw(SpriteBatch batch){
        Sprite sprite = new Sprite(texture);
        sprite.setPosition(position.x, position.y);
        sprite.setSize(width, height);
        sprite.draw(batch);
    }
}

