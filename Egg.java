/* I declare that this code is my own work */
/* Author Xiaofeng Hu xhu73@sheffield.ac.uk */

import gmaths.*;
import com.jogamp.opengl.*;
import java.util.List;

/* I declare that this code is my own work */
/* Author Xiaofeng Hu xhu73@sheffield.ac.uk */

public class Egg{

    private Model sphere;
    private SGNode eggRoot;
    private float xPosition = 0;
    private TransformNode jumpY, translateX, rotateAllY, rotateAllZ, makeEgg;
    private float rotateAllAngleStart = 5, rotateAllAngleY = rotateAllAngleStart ,rotateAllAngleZ = rotateAllAngleStart;
    private float jumpYStart = 0, jumpYHeight = jumpYStart;
    private float jumpHeght = 0.3f;
    private int i=0;

    public Egg(GL3 gl, Camera camera,  List<Light> lightList, Shader shader, int[] textureId1, int[] textureId2) {
        // make the egg and for its movement, a root is created
        Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
        Material material = new Material(new Vec3(1.0f, 0.5f, 0.31f), new Vec3(1.0f, 0.5f, 0.31f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
        Mat4 modelMatrix = Mat4.multiply(Mat4Transform.scale(4,4,4), Mat4Transform.translate(0,0.5f,0));
        sphere = new Model(gl, camera, lightList, shader, material, modelMatrix, mesh, textureId1, textureId2);

        eggRoot = new NameNode("egg");
        jumpY = new TransformNode("jump", Mat4Transform.translate(0,jumpYHeight,0));
        translateX = new TransformNode("translate", Mat4Transform.translate(xPosition,0,0));
        rotateAllY = new TransformNode("rotateAroundZ", Mat4Transform.rotateAroundX(0));
        rotateAllZ = new TransformNode("rotateAroundZ", Mat4Transform.rotateAroundZ(0));

        NameNode egg = new NameNode("egg");
        Mat4 m = Mat4Transform.scale(2,2.5f,2);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.9f,0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(i));
        makeEgg = new TransformNode("scale(2,2.5f,2);translate(0, 1.9f,0)", m);
        ModelNode eggNode = new ModelNode("Sphere(egg)", sphere);

        eggRoot.addChild(translateX);
        translateX.addChild(jumpY);
        jumpY.addChild(rotateAllY);
        rotateAllY.addChild(rotateAllZ);
        rotateAllZ.addChild(egg);
        egg.addChild(makeEgg);
        makeEgg.addChild(eggNode);

        eggRoot.update();
    }

    public void updateEgg(double elapsedTime) {
        // make the egg jumping and spinning
        rotateAllAngleY = rotateAllAngleStart * (float) Math.sin(elapsedTime * 6);
        rotateAllAngleZ = rotateAllAngleStart * (float) Math.cos(elapsedTime * 4);
        i+=1;
        Mat4 m = Mat4Transform.scale(2,2.5f,2);
        m = Mat4.multiply(m, Mat4Transform.translate(0, 1.9f,0));
        m = Mat4.multiply(m, Mat4Transform.rotateAroundY(i));
        makeEgg.setTransform(m);
        rotateAllY.setTransform(Mat4Transform.rotateAroundX(rotateAllAngleY));
        rotateAllZ.setTransform(Mat4Transform.rotateAroundZ(rotateAllAngleZ));
        jumpYHeight = jumpYStart + jumpHeght + (float) Math.sin(elapsedTime * 12) * jumpHeght;
        jumpY.setTransform(Mat4Transform.translate(0, jumpYHeight, 0));
        eggRoot.update(); // IMPORTANT – the scene graph has changed
    }

    public void render(GL3 gl) {
        eggRoot.update();
        eggRoot.draw(gl);
    }

    public void dispose(GL3 gl) {
        sphere.dispose(gl);
    }
}

