package com.company;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Scanner;

public class MainProject extends JFrame implements MouseListener {
    int[][] all_values = new int[SCREEN_WIDTH][SCREEN_HEIGHT];
    static int iterations = 500;
    static final int SCREEN_WIDTH = 1920;
    static final int SCREEN_HEIGHT = 1080;
    double current_x = 0;
    double current_y = 0;
    double size = 1;
    double julia_a;
    double julia_b;
    boolean isMandelbrot = false;
    boolean isAnimated = false;
    ArrayList<Image> image_list = new ArrayList<Image>();
    public MainProject(){
        addMouseListener(this);
    }
    // Given the center of the screen, fetch the upper and lower bounds for x or y
    public double getLowerX(){
        return current_x-(size*(SCREEN_WIDTH/(double)SCREEN_HEIGHT));
    }
    public double getUpperX(){
        return current_x+(size*(SCREEN_WIDTH/(double)SCREEN_HEIGHT));
    }
    public double getLowerY(){
        return current_y-size;
    }
    public double getUpperY(){
        return current_y+size;
    }
    // MouseListener
    @Override
    public void mouseClicked(MouseEvent e) {
    }
    @Override
    public void mouseEntered(MouseEvent e) {
    }
    @Override
    public void mouseExited(MouseEvent e) {
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();
        current_x = xValue(x,getLowerX(),getUpperX());
        current_y = yValue(y,getLowerY(),getUpperY());
        if (e.getButton() == MouseEvent.BUTTON3){
            size *= 10;
            iterations /= 1.1;
        }
        else{
            size /= 10;
            iterations *= 1.1;
        }
        System.out.println("(" + current_x + ", " + current_y + ")");
        System.out.println("Size: " + size + "\tIterations: "+ iterations);
        if (isMandelbrot){
            checkMandelbrot(getLowerX(), getUpperX(), getLowerY(), getUpperY());
        }
        else{
            checkJuliaSet(new ComplexNumber(julia_a,julia_b),getLowerX(), getUpperX(), getLowerY(), getUpperY());
        }
        prerenderAnimation();
        repaint();
    }

    // Takes user input for which picture to render and what c value to use for Julia set.
    void getUserInput(){
        Scanner in = new Scanner(System.in);
        System.out.println("Type m for mandelbrot, or j for julia set.");
        String input = in.next();
        if (input.equals("m")){
            isMandelbrot = true;
        }
        if (isMandelbrot){
            current_x = -0.6;
        }
        else{
            System.out.println("Input the real value in the complex number:");
            julia_a = in.nextDouble();
            System.out.println("Input the imaginary value in the complex number:");
            julia_b = in.nextDouble();
        }
        System.out.println("Animate the rendered image? (y/n)");
        String animate = in.next();
        if (animate.toLowerCase().equals("y")) isAnimated = true;
    }
    // Generates the window and calls the calculations
    private void createAndShowGUI() {
        //Create and set up the window.
        getUserInput();
        if(isMandelbrot) checkMandelbrot(getLowerX(), getUpperX(), getLowerY(), getUpperY());
        else checkJuliaSet(new ComplexNumber(julia_a, julia_b), getLowerX(), getUpperX(), getLowerY(), getUpperY());
        prerenderAnimation();

        //Display the window.
        setUndecorated(true);
        setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
        toFront();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
    // gets the x double value for a give pixel
    public double xValue(int x_pixel_index, double lower_x, double upper_x){
        return lower_x + x_pixel_index*((upper_x-lower_x)/SCREEN_WIDTH);
    }
    // gets the y double value of a given pixel
    public double yValue(int y_pixel_index, double lower_y, double upper_y){
        return upper_y - y_pixel_index*((upper_y-lower_y)/SCREEN_HEIGHT);
    }
    // checks all pixels on screen for the julia set
    public void checkJuliaSet(ComplexNumber n, double lower_x, double upper_x, double lower_y, double upper_y){
        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                double x_value = xValue(i,lower_x,upper_x);
                double y_value = yValue(j,lower_y,upper_y);
                all_values[i][j] = inJuliaSet(n,x_value, y_value);
            }
        }
    }
    public int inJuliaSet1(ComplexNumber c, double a, double b){
        ComplexNumber z = new ComplexNumber(a,b);
        int n = 0;
        while(z.absValue()<2 && n<iterations){
            ComplexNumber denomenator = z.subtract(z.multByCN(z)).multByScaler(1.0/2.0);
            ComplexNumber numerator = new ComplexNumber(1,0).subtract(z.multByCN(z.multByCN(z))).multByScaler(1.0/6.0);
            z = numerator.divideByCN(denomenator.multByCN(denomenator)).add(c);
            n++;
        }
        return n;
    }
    // Checks whether the given pixel's a and b values are in the julia set, returning the number of iterations
    public int inJuliaSet(ComplexNumber c, double a, double b){
        ComplexNumber z = new ComplexNumber(a,b);
        int n = 0;
        while (z.absValue()<2 && n<iterations){
            z = z.multByCN(z).add(c);
            n++;
        }
        return n;
    }
    // checks all pixels on screen for the Mandelbrot set
    public void checkMandelbrot(double lower_x, double upper_x, double lower_y, double upper_y){
        for (int x = 0; x < SCREEN_WIDTH; x++) {
            for (int y = 0; y < SCREEN_HEIGHT; y++) {
                double x_value = xValue(x,lower_x,upper_x);
                double y_value = yValue(y,lower_y,upper_y);
                all_values[x][y] = inMandelbrotSet(x_value,y_value);
            }
        }
    }
    // checks whether the given pixel'a a and b values are in the Mandelbrot set
    public int inMandelbrotSet(double a, double b){
        ComplexNumber c = new ComplexNumber(a,b);
        ComplexNumber z = c;
        int n = 0;
        while (z.absValue()<2 && n<iterations){
            z = z.multByCN(z).add(c);
            n++;
        }
        return n;
    }
    public void sleep(int sec){
        try{
            Thread.sleep(sec);
        }
        catch(Exception e){}
    }
    public int getLowestValue(){
        int ret = iterations;
        for (int i = 0; i < SCREEN_WIDTH; i++) {
            for (int j = 0; j < SCREEN_HEIGHT; j++) {
                if(all_values[i][j]<ret){
                    ret = all_values[i][j];
                }
            }
        }
        return ret;
    }
    public void prerenderAnimation(){
        image_list.clear();
        if (isAnimated) {
            int previ = 0;
            int lowest = getLowestValue();
            for (int i = lowest; i < iterations; i *= 1.1) {
                if (previ == i) i++;
                Image img = createImage(i);
                image_list.add(img);
                previ = i;
            }
        }
        else{
            Image img = createImage(iterations);
            image_list.add(img);
        }
    }
    public void paint(Graphics g) {
        System.out.println("Painting");
        System.out.println(image_list);
        for (int i = 0; i < image_list.size(); i++) {
            g.drawImage(image_list.get(i),0,0,this);
            sleep(25);
        }
    }
    // Creates a buffered image displaying our 2D array called all_values
    public Image createImage(int index){
        BufferedImage buffimg = new BufferedImage(SCREEN_WIDTH,SCREEN_HEIGHT,BufferedImage.TYPE_INT_RGB);
        Graphics2D g = (Graphics2D) buffimg.getGraphics();
        for (int i = 0; i < all_values.length; i++) {
            for (int j = 0; j < all_values[0].length; j++) {
                if (all_values[i][j]>=index || all_values[i][j]==iterations) g.setColor(Color.BLACK);
                else g.setColor(Color.getHSBColor(((float)(all_values[i][j]*2)%360)/360,(float)1,(float)1));
                g.fillRect(i,j,1,1);
            }
        }
        return buffimg;
    }
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MainProject m = new MainProject();
                m.createAndShowGUI();
            }
        });
    }
}