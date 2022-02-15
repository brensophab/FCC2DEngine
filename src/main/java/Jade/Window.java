package Jade;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static java.sql.Types.NULL;
import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class Window {
    private int width, height;
    private String title;
    private long glfwWindow;

    public float r,g,b,a;
    private boolean fadeToBlack = false;

    private static Window window = null;



    private static Scene currentScene;


    private Window(){
        this.width=1920;
        this.height=1080;
        this.title="Mario";
        r=1;
        g=1;
        b=1;
        a=1;
    }

    public static void changeScene(int newScene){
        switch(newScene){
            case 0:
            currentScene = new LevelEditorScene();
            currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene '" + newScene + "'";
                break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();


        }
        return Window.window; //Window only created in first call
    }
    public void run(){
        System.out.println("Hello LWJGL "+ Version.getVersion()+"!");

        init();
        loop();

        //Free memory once exit loop
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        //terminate GLFW and free error callback memory
        glfwTerminate();
        glfwSetErrorCallback(null).free(); //OS does this already, but just to be proper :D
    }
    public void init(){
        // Setup error callback where glfw prints to in case of error in build log
        GLFWErrorCallback.createPrint(System.err).set(); //System.err.println("We have an error")

        //Initialize GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize glfw");
        }

        //Config glfw
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);//invisible until window is
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);

        //create window
        glfwWindow=glfwCreateWindow(this.width, this.height, this.title, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("failed to create glfw window");

        }
        //Callbacks glfw input
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePosCallback);// x->(x) x+2
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyListener::keyCallback);


        //Make OpenGL context current
        glfwMakeContextCurrent(glfwWindow);
        //Enable Vsync
        glfwSwapInterval(1);
        //Make window visible
        glfwShowWindow(glfwWindow);

        //This line is critical for LWJGL's interoperation with GLFW's
        //OpenGL context, or any context that is managed externally.
        //LWJGL detects the context that is current in the current thread
        //creates the GLCapabilities instance and makes OpenGL
        //bindings available for use.
        GL.createCapabilities(); //Makes sure we can use bindings

        Window.changeScene(0);
    }

    public void loop(){
        float beginTime = Time.getTime();
        float endTime;
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){

                //poll events
                glfwPollEvents();

                glClearColor(r, g, b, a);
                glClear(GL_COLOR_BUFFER_BIT); //Tells OpenGL how to clear buffer


                if (dt >= 0) {
                    currentScene.update(dt);
                }


                glfwSwapBuffers(glfwWindow);

                endTime = Time.getTime();
                dt = endTime - beginTime;
                beginTime = endTime;

        }
    }
}
/*nanotime() = (1*10^9)/1s
(1*10^-9s)/1ns for FPS
Frames per Second = dt s/F *1/dt =F/s

 */