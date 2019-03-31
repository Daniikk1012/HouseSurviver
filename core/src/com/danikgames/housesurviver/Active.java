package com.danikgames.housesurviver;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;

class Active extends Actor {

    MyGdxGame game;
    TextureRegion[] regions;
    String type;
    Rectangle body;
    boolean mirrored;
    int frame;
    Body phBody;

    Active(MyGdxGame game, TextureRegion[] regions, String type, Rectangle body){
        this.game = game;
        this.regions = regions;
        this.type = type;
        this.body = body;
        mirrored = false;
        frame = 0;
    }

    void boundSync(){
        setBounds(body.x, body.y, body.width, body.height);
    }

    @Override
    public void setX(float x) {
        body.x = x;
        phBody.setTransform(x + body.width/2, phBody.getPosition().y, 0);
    }

    @Override
    public void setY(float y) {
        body.y = y;
        phBody.setTransform(phBody.getPosition().x, y + body.height/2, 0);
    }

    @Override
    public void setWidth(float width) {
        body.width = width;
    }

    @Override
    public void setHeight(float height) {
        body.height = height;
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        body.set(x,y,width,height);
        phBody.setTransform(x + body.width/2, y + body.height/2, 0);
    }

    @Override
    public void setPosition(float x, float y) {
        body.setPosition(x,y);
        phBody.setTransform(x + body.width/2, y + body.height/2, 0);
    }

    @Override
    public void setSize(float width, float height) {
        body.setSize(width,height);
    }

    @Override
    public float getX() {
        return body.x;
    }

    @Override
    public float getY() {
        return body.y;
    }

    @Override
    public float getWidth() {
        return body.width;
    }

    @Override
    public float getHeight() {
        return body.height;
    }

    void nextFrame(int min, int max, int turn){
        if(frame<min)frame = min-turn;
        frame+=turn;
        frame=(frame-min)%(max-min+1)+min;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(regions[frame],game.mainScr.world.x + (mirrored ? body.x + body.width : body.x), game.mainScr.world.y + body.y, (mirrored ? -1 : 1) * body.width, body.height);
    }

    @Override
    public void act(float delta) {
        nextFrame(0,regions.length-1,1);
        bodyByPhSync();
    }

    void bodyByPhSync(){
        body.setPosition(phBody.getPosition().x - body.width/2, phBody.getPosition().y - body.height/2);
    }
}
