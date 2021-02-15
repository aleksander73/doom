package aleksander73.doom.other;

import aleksander73.doom.game_object.Model;
import aleksander73.math.linear_algebra.Vector2d;
import aleksander73.math.linear_algebra.Vector3d;
import aleksander73.vector.core.GameEngine;
import aleksander73.vector.core.Transform;
import aleksander73.vector.rendering.Shaders;
import aleksander73.vector.rendering.materials.Colour;
import aleksander73.vector.rendering.mesh.Face;
import aleksander73.vector.rendering.mesh.Mesh;
import aleksander73.vector.rendering.mesh.Vertex;

public class Floor extends Model {
    public Floor(Vector3d position, Mesh mesh) {
        super(
            "Floor",
            new Transform(position),
            mesh,
            Shaders.getStandardShader(),
            new Colour(255, 255, 255, 255),
            GameEngine.getResourceSystem().getTexture("floor.png")
        );
    }

    public static Floor createFloor(Vector2d position, Vector2d dimensions) {
        float w = dimensions.getX() / 2.0f;
        float d = dimensions.getY() / 2.0f;
        Vertex[] vertices = new Vertex[] {
            new Vertex(0, new Vector3d(-w, 0.0f, d), new Vector2d(0.0f, d)),
            new Vertex(1, new Vector3d(-w, 0.0f, -d), new Vector2d(0.0f, 0.0f)),
            new Vertex(2, new Vector3d(w, 0.0f, -d), new Vector2d(w, 0.0f)),
            new Vertex(3, new Vector3d(w, 0.0f, d), new Vector2d(w, d))
        };
        Face[] faces = new Face[] {
            new Face(new Vertex[] {vertices[0], vertices[1], vertices[2]}),
            new Face(new Vertex[] {vertices[0], vertices[2], vertices[3]})
        };
        return new Floor(new Vector3d(position.getX(), 0.0f, position.getY()), new Mesh(vertices, faces));
    }
}
