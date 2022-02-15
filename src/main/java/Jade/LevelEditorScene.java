package Jade;

import org.lwjgl.BufferUtils;

import java.awt.event.KeyEvent;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene {
//get shaders to draw to scene
    private String vertexShaderSrc = "#version 330 core\n" +
        "layout(location=0) in vec3 aPos;\n" +
        "layout(location=1) in vec4 aColor;\n" +
        "\n" +
        "\n" +
        "out vec4 fColor;\n" +
        "\n" +
        "void main()\n" +
        "{\n" +
        "    fColor = aColor;\n" +
        "    gl_Position = vec4(aPos, 1.0);\n" +
        "    \n" +
        "}";
    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexID, fragmentID, shaderProgram; //shader is a combination of vertex and fragment id sources.

    private float[] vertexArray ={
            //position                  //color       //Alpha
             0.5f, -0.5f,  0.0f,        1.0f, 0.0f, 0.0f, 1.0f  //bottom right 0
            -0.5f,  0.5f,  0.0f,        0.0f, 1.0f, 0.0f, 1.0f, //top left     1
             0.5f,  0.5f,  0.0f,        0.0f, 0.0f, 1.0f, 1.0f, //top right    2
            -0.5f, -0.5f,  0.0f,        1.0f, 1.0f, 0.0f, 1.0f  //bottom left  3
    };

    // IMPORTANT must be in counter-clockwise order
    private int[] elementArray={ //triangle init

            2,1,0, // top right triangle
            0,1,3 // bottom left triangle


    };

    private int vaoID, vboID, eboID; //vertex array object, vertex buffer object, element buffer object

    public LevelEditorScene(){


    }

    @Override
    public void init(){
        //==============================
        //Compile and Link shaders
        //==============================

        //First load and compile vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);

        //pass shader source code to gpu
        glShaderSource(vertexID, vertexShaderSrc);
        glCompileShader(vertexID);

        //Check for error in compilation
        int success=glGetShaderi(vertexID,GL_COMPILE_STATUS);
        if(success==GL_FALSE){
            int len = glGetShaderi(vertexID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tVertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID,len));
            assert false: "";
        }
        //First load and compile fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);

        //pass shader source code to gpu
        glShaderSource(fragmentID , fragmentShaderSrc);
        glCompileShader(fragmentID );

        //Check for error in compilation
         success=glGetShaderi(fragmentID,GL_COMPILE_STATUS);
        if(success==GL_FALSE){
            int len = glGetShaderi(fragmentID,GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tFragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID,len));
            assert false: "";
        }
        //link shaders and check for errors
        shaderProgram=glCreateProgram();
        glAttachShader(shaderProgram,vertexID);
        glAttachShader(shaderProgram,fragmentID);
        glLinkProgram(shaderProgram);

        //Check for linking errors
        success = glGetProgrami(shaderProgram,GL_LINK_STATUS);
        if(success == GL_FALSE){
            int len = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'defaultShader.glsl'\n\tLinking shaders failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram,len));
            assert false: "";

        }
        //==============================================================
        //Generate VAO, VBO, and EBO buffer objects and send data to gpu
        //==============================================================
        vaoID =glGenVertexArrays();
        glBindVertexArray(vaoID);

        //create float buffer for vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //create VBO and upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER,vboID);
        glBufferData(GL_ARRAY_BUFFER,vertexBuffer,GL_STATIC_DRAW);

        //create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID =glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        //ADD vertex attribute pointers
    }

    @Override
    public void update(float dt) {

    }
}
