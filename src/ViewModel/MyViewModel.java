package ViewModel;

import Model.IModel;
import View.MyViewController;
import algorithms.search.Solution;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int [][] maze;
    private int [][] solutionPath;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
        this.solutionPath = null;
    }


    public int[][] getMaze() {
        return model.getMaze();
    }

    public int getRowChar() {
        return model.getRowChar();
    }

    public int getColChar() {
        return model.getColChar();
    }

    public int[][] getSolutionPath() {
        return solutionPath;
    }


    private void mazeGenerated() {
        this.maze = model.getMaze();
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            setChanged();
            notifyObservers(arg);
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateMaze(row,col);
    }

    public void moveCharacter(KeyEvent keyEvent)
    {
        /*
            direction = 1 -> Up
            direction = 2 -> Up Right
            direction = 3 -> Right
            direction = 4 -> Right Down
            direction = 5 -> Down
            direction = 6 -> Down Left
            direction = 7 -> Left
            direction = 8 -> Left Up
         */
        int direction = -1;

        switch (keyEvent.getCode()){
            case UP -> direction = 1;
            case NUMPAD8 -> direction = 1;
            case NUMPAD9-> direction = 2;
            case RIGHT-> direction = 3;
            case NUMPAD6-> direction = 3;
            case NUMPAD3-> direction = 4;
            case DOWN -> direction = 5;
            case NUMPAD2-> direction = 5;
            case NUMPAD1 -> direction = 6;
            case LEFT-> direction = 7;
            case NUMPAD4 -> direction = 7;
            case NUMPAD7 -> direction = 8;
            default -> {
                //pressed other key
                return;
            }
        }
        model.updateCharacterLocation(direction);
    }

    public void solveMaze()
    {
        model.solveMaze();
    }

    public Solution getSolution()
    {
        return model.getSolution();
    }

    public int getRowStart()
    {
        return this.model.getRowStart();
    }

    public int getColStart()
    {
        return this.model.getColStart();
    }

    public int getRowGoal()
    {
        return this.model.getRowGoal();
    }

    public int getColGoal()
    {
        return this.model.getColGoal();
    }

    public void saveMaze() {
        this.model.saveMaze();
    }

    public void loadMaze() {
        this.model.loadMaze();
    }
}