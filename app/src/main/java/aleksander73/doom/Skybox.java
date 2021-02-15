package aleksander73.doom;

import aleksander73.doom.game_object.Model;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Shaders;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.materials.Texture;

public class Skybox extends Model {
    public Skybox(Texture mainTexture) {
        super(
            "Skybox",
            new Transform(), GameEngine.getResourceSystem().loadMesh("skybox.mesh"),
            Shaders.getSkyboxShader(),
            new Colour(128, 128, 128, 255),
            mainTexture
        );
    }
}
