package ViewModel;

import Model.IModel;
import View.MyViewController;
import javafx.scene.input.KeyEvent;

import java.util.Observable;
import java.util.Observer;

public class MyViewModel extends Observable implements Observer {

    private IModel model;
    private int [][] maze;
    private int [][] solutionPath;
    private int rowChar;
    private int colChar;


    public MyViewModel(IModel model) {
        this.model = model;
        this.model.assignObserver(this);
        this.maze = null;
        this.solutionPath = null;
    }


    public int[][] getMaze() {
        return maze;
    }

    public int getRowChar() {
        return rowChar;
    }

    public int getColChar() {
        return colChar;
    }

    public int[][] getSolutionPath() {
        return solutionPath;
    }

    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof IModel)
        {
            if(maze == null)//init - generateMaze
            {
                this.maze = model.getMaze();
            }
            else {
                int[][] mazeModel = this.model.getMaze();
                if(mazeModel != this.maze){ //user pressed generateMaze
                    this.maze = mazeModel;
                }
                else { // user made step
                    if (this.rowChar != this.model.getRowChar() || this.colChar != this.model.getColChar()) { //character moved
                        this.rowChar = this.model.getRowChar();
                        this.colChar = this.model.getColChar();
                        if (this.model.getRowChar() == this.model.getRowGoal() && this.model.getColChar() == this.model.getColGoal()) { //character has reached the goal
                            System.out.println("Goal! woohoo!!");
                        }
                    } else { //character didnt move - invalid step
                        System.out.println("invalid step");
                    }
                }
            }
            setChanged();
            notifyObservers();
        }
    }


    public void generateMaze(int row,int col)
    {
        this.model.generateRandomMaze(row,col);
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
            case UP:
                direction = 1;
                break;
            case NUMPAD8:
                direction = 1;
                break;
            case NUMPAD9:
                direction = 2;
                break;
            case RIGHT:
                direction = 3;
                break;
            case NUMPAD6:
                direction = 3;
                break;
            case NUMPAD3:
                direction = 4;
                break;
            case DOWN:
                direction = 5;
                break;
            case NUMPAD2:
                direction = 5;
                break;
            case NUMPAD1:
                direction = 6;
                break;
            case LEFT:
                direction = 7;
                break;
            case NUMPAD4:
                direction = 7;
                break;
            case NUMPAD7:
                direction = 8;
                break;

        }

        model.updateCharacterLocation(direction);
    }

    public void solveMaze()
    {
        model.solveMaze();
    }

    public void getSolution()
    {
        model.getSolution();
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

}