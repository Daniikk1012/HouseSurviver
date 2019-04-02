package com.danikgames.housesurviver;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class Player extends Active {
    float speed;
    boolean sprint, W, A, S, D;
    boolean showBack, lookUp;
    float right, up;

    Player(MyGdxGame game, TextureRegion[] regions, String type, final Rectangle body, final float speed) {
        super(game, regions, type, body);
        game.mainScr.setKeyboardFocus(this);
        addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if(keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) sprint = true;
                if(keycode == Input.Keys.W) {
                    W = true;
                    lookUp = true;
                }
                if(keycode == Input.Keys.A) {
                    A = true;
                    mirrored = true;
                }
                if(keycode == Input.Keys.S) {
                    S = true;
                    lookUp = false;
                }
                if(keycode == Input.Keys.D) {
                    D = true;
                    mirrored = false;
                }
                return false;
            }

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if(keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT) sprint = false;
                if(keycode == Input.Keys.W) W = false;
                if(keycode == Input.Keys.A) A = false;
                if(keycode == Input.Keys.S) S = false;
                if(keycode == Input.Keys.D) D = false;
                return false;
            }
        });
        this.speed = speed;
        sprint = W = A = S = D = false;
        showBack = false;
        lookUp = true;

        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.position.set(body.x + body.width/2, body.y + body.height/2);
        phBody = game.mainScr.physics.createBody(bDef);
        FixtureDef fDef = new FixtureDef();
        fDef.density = 1;
        CircleShape shape = new CircleShape();
        shape.setRadius(body.width/2);
        fDef.shape = shape;
        phBody.createFixture(fDef);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        right = (float) (A ? (D ? 0 : ((W && !S) || (S && !W) ? 1/-Math.sqrt(2) : -1)) : (D ? ((W && !S) || (S && !W) ? 1/Math.sqrt(2) : 1) : 0));
        up = (float) (S ? (W ? 0 : ((A && !D) || (D && !A) ? 1/-Math.sqrt(2) : -1)) : (W ? ((A && !D) || (D && !A) ? 1/Math.sqrt(2) : 1) : 0));

        phBody.setLinearVelocity(speed * right * (sprint?2:1) * 50, speed * up * (sprint?2:1) * 50);

        //bodyByPhSync();
        if(body.x < 0)
            body.x = 0;
        if(body.y < 0)
            body.y = 0;
        if(body.x > game.mainScr.world.width - body.width)
            body.x = game.mainScr.world.width - body.width;
        if(body.y > game.mainScr.world.height - body.height)
            body.y = game.mainScr.world.height - body.height;
        boundSync();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
