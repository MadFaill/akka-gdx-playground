package com.mad.rpg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class Dragon {

    private static final int FRAME_COLS = 5;
    private static final int FRAME_ROWS = 8;

    private AnimatedTiledMapTile animation;
    private MapLayer layer;
    private int myId;

    private Vector3 movePoint;
    public float velocity = 3.5f;
    public float scale;

    public Dragon (MapLayer objectLayer, int colId, int rowId, int posX, int posY, float camScale, float iVelocity) {

        scale = camScale / 1.4f;
        velocity = iVelocity;

        Texture dragonsSheet = new Texture(Gdx.files.internal("actors/dragons.png"));
        TextureRegion[][] tmp = TextureRegion.split(dragonsSheet, dragonsSheet.getWidth()/FRAME_COLS, dragonsSheet.getHeight()/FRAME_ROWS);

        Array<StaticTiledMapTile> walkFrames = new Array<StaticTiledMapTile>();

        walkFrames.add(new StaticTiledMapTile(tmp[rowId][colId]));
        walkFrames.add(new StaticTiledMapTile(tmp[rowId+1][colId]));

        animation = new AnimatedTiledMapTile(0.425f, walkFrames);
        layer = objectLayer;


        TextureMapObject tmo = new TextureMapObject(animation.getCurrentFrame().getTextureRegion());
        tmo.setX(posX);
        tmo.setY(posY);

        tmo.setScaleX(scale);
        tmo.setScaleY(scale);

        movePoint = new Vector3(posX, posY, 0);

        objectLayer.getObjects().add(tmo);
        myId = objectLayer.getObjects().getCount() - 1;
    }

    public int getMyId() {
        return myId;
    }

    public void update() {
        TextureMapObject character = (TextureMapObject) layer.getObjects().get(myId);

        TextureRegion region = animation.getCurrentFrame().getTextureRegion();
        character.setTextureRegion(region);

        Vector3 position = getPosition();


        float x, y;

        if (position.x != movePoint.x) {
            if (position.x > movePoint.x) {
                x = position.x - velocity;
                if (x < movePoint.x) {
                    x = movePoint.x;
                }
            } else {
                x = position.x + velocity;
                if (x > movePoint.x) {
                    x = movePoint.x;
                }
            }
            character.setX(x);
        }

        if (position.y != movePoint.y) {
            if (position.y > movePoint.y) {
                y = position.y - velocity;
                if (y < movePoint.y) {
                    y = movePoint.y;
                }
            } else {
                y = position.y + velocity;
                if (y > movePoint.y) {
                    y = movePoint.y;
                }
            }
            character.setY(y);
        }
    }

    public void touchDown(float x, float y) {
        movePoint = new Vector3(x, y, 0);
    }

    public Vector3 getPosition() {
        TextureMapObject character = (TextureMapObject) layer.getObjects().get(myId);
        return new Vector3(character.getX(), character.getY(), 0);
    }
}
