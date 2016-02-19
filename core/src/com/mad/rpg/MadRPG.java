package com.mad.rpg;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

public class MadRPG extends ApplicationAdapter implements InputProcessor {

	public float camScale = 1.1f;

	TiledMap tiledMap;
	OrthographicCamera camera;
	OrthogonalTiledMapRendererWithSprites tiledMapRenderer;
	Array<Dragon> dragons;
	Dragon character;
	DragonAI ai;

	@Override
	public void create () {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false,w,h);
		camera.update();

		tiledMap = new TmxMapLoader().load("tmx/Snow Desert.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRendererWithSprites(tiledMap, camScale);
		Gdx.input.setInputProcessor(this);

		MapLayer objectLayer = tiledMap.getLayers().get("actors");

		dragons = new Array<Dragon>();

		// main =)
		dragons.add(new Dragon(objectLayer, 0, 0, 400, 400, camScale, 3.2f));
		character = dragons.get(0);

		// other
		dragons.add(new Dragon(objectLayer, 1, 0, 100, 100, camScale, 1.9f));
		dragons.add(new Dragon(objectLayer, 2, 2, 170, 200, camScale, 1.5f));

		ai = new DragonAI();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		for (int i=0; i < dragons.size; i++) {

			// only if mob =)
			if (i!=0) {
				Vector2 mapSize = getMapSize();
				ai.moveDragon(dragons.get(i), mapSize.x, mapSize.y);
			}

			dragons.get(i).update();
		}

		updateCamera();
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
//		if(keycode == Input.Keys.LEFT)
//			camera.translate(-32,0);
//		if(keycode == Input.Keys.RIGHT)
//			camera.translate(32,0);
//		if(keycode == Input.Keys.UP)
//			camera.translate(0,-32);
//		if(keycode == Input.Keys.DOWN)
//			camera.translate(0,32);
//		if(keycode == Input.Keys.NUM_1)
//			tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
//		if(keycode == Input.Keys.NUM_2)
//			tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
		return false;
	}

	@Override
	public boolean keyTyped(char character) {

		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
		Vector3 position = camera.unproject(clickCoordinates);
		Vector2 mapSize = getMapSize();

		if (position.x < 0 ) {
			position.x = 0;
		}

		if (position.x > mapSize.x) {
			position.x = mapSize.x;
		}

		if (position.y < 0) {
			position.y = 0;
		}

		if (position.y > mapSize.y) {
			position.y = mapSize.y;
		}

		character.touchDown(position.x, position.y);

		return true;
	}

	private void updateCamera() {
		Vector3 currentPosition = character.getPosition();
		Vector2 mapSize = getMapSize();

		float camX = currentPosition.x;
		float camY = currentPosition.y;

		Vector2 camMin = new Vector2(camera.viewportWidth/2f, camera.viewportHeight/2f);
		Vector2 camMax = new Vector2(
				mapSize.x - (camera.viewportWidth / (2f * camScale)),
				mapSize.y - (camera.viewportHeight / (2f * camScale))
		);

		//keep camera within borders
		camX = Math.min(camMax.x, Math.max(camMin.x, camX));
		camY = Math.min(camMax.y, Math.max(camMin.y, camY));

		Vector2 nextPosition = new Vector2(camX, camY);

		// use only
		camera.position.set(nextPosition.x, nextPosition.y, camera.position.z);
	}

	private Vector2 getMapSize() {
		MapProperties prop = tiledMap.getProperties();

		int mapWidth = prop.get("width", Integer.class) - 1;
		int mapHeight = prop.get("height", Integer.class) - 1;
		int tilePixelWidth = prop.get("tilewidth", Integer.class);
		int tilePixelHeight = prop.get("tileheight", Integer.class);

		return new Vector2(mapWidth*tilePixelWidth*camScale, mapHeight*tilePixelHeight*camScale);
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
}
