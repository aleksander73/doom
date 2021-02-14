package aleksander73.doom.game_object;

import aleksander73.vector.core.GameObject;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.materials.Material;
import aleksander73.vector.rendering.materials.Texture;
import aleksander73.vector.rendering.mesh.Mesh;
import aleksander73.vector.rendering.renderers.MeshRenderer;
import aleksander73.vector.rendering.shaders.Shader;

public class Model extends GameObject {
    private final Texture mainTexture;

    public Model(String name, Transform transform, Mesh mesh, Shader shader, Colour colour, Texture mainTexture) {
        super(name);
        Material material = new Material(colour, mainTexture, shader);
        MeshRenderer meshRenderer = new MeshRenderer(mesh);
        this.addComponents(transform, mesh, material, meshRenderer);
        this.mainTexture = material.getTexture();
    }

    public Texture getMainTexture() {
        return mainTexture;
    }
}
