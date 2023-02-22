package edu.uob;
import java.util.*;


public class OXOModel {

    //private OXOPlayer[][] cells;
    private List<List<OXOPlayer>> cells;

    private OXOPlayer[] players;
    private int currentPlayerNumber;
    private OXOPlayer winner;
    private boolean gameDrawn;
    private int winThreshold;

    public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
        winThreshold = winThresh;
        /*List<List<OXOPlayer>>*/
        cells = new ArrayList<>();
        for (int i = 0; i < numberOfRows; i++) {
            List<OXOPlayer> row = new ArrayList<OXOPlayer>();
            for (int j = 0; j < numberOfColumns; j++) {
                row.add(null);
            }
            cells.add(row);
        }
        players = new OXOPlayer[2];
    }

    public int getNumberOfPlayers() {
        return players.length;
    }

    public void addPlayer(OXOPlayer player) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = player;
                return;
            }
        }
    }

    public void changePlayer() {
        setCurrentPlayerNumber((getCurrentPlayerNumber() + 1) % getNumberOfPlayers());
    }

    public OXOPlayer getPlayerByNumber(int number) {
        return players[number];
    }

    public OXOPlayer getCurrentPlayer() {
        return this.getPlayerByNumber(this.getCurrentPlayerNumber());
    }

    public OXOPlayer getWinner() {
        return winner;
    }

    public void setWinner(OXOPlayer player) {
        winner = player;
    }

    public int getCurrentPlayerNumber() {
        return currentPlayerNumber;
    }

    public void setCurrentPlayerNumber(int playerNumber) {
        currentPlayerNumber = playerNumber;
    }

    public int getNumberOfRows() {
        return cells.size();
    }

    public int getNumberOfColumns() {
        return cells.get(0).size();
    }

    public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
        if(!isInRange(rowNumber,colNumber)){return null;}
        //System.out.println("line81row:"+rowNumber+'\n'+"col:"+colNumber);
        return cells.get(rowNumber).get(colNumber);
    }

    public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
        cells.get(rowNumber).set(colNumber,player);
    }

    public void setWinThreshold(int winThresh) {
        winThreshold = winThresh;
    }

    public int getWinThreshold() {
        return winThreshold;
    }

    public void setGameDrawn() {
        gameDrawn = true;
    }


    public boolean isGameDrawn() {
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            for (int j = 0; j < this.getNumberOfColumns(); j++) {
                if (this.cells.get(i).get(j) == null) {
                    this.gameDrawn = false;
                    return false;
                }
            }
        }
        this.setGameDrawn();
        return gameDrawn;
    }

    public boolean isGameWon() {
        for (int rowNum = 0; rowNum < this.getNumberOfRows(); rowNum++) {
            for (int colNum = 0; colNum < this.getNumberOfColumns(); colNum++) {
                if (cells.get(rowNum).get(colNum) == null) {
                    continue;
                }

                if (isHorizontalWin(rowNum, colNum) || isVerticalWin(rowNum, colNum) || isDiagonalWin(rowNum, colNum)) {
                    setWinner(getCellOwner(rowNum,colNum));
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isHorizontalWin(int rowNumber, int colNumber) {
        int cnt = 1;
//        if (this.getCellOwner(rowNumber,colNumber) == null){return false;}
        OXOPlayer player = this.getCellOwner(rowNumber, colNumber);
        int colPtr = colNumber + 1;

        while (isInRange(rowNumber, colPtr) && getCellOwner(rowNumber, colPtr) == player) {
//            System.out.println("1row:"+rowNumber+'\n'+"col:"+colNumber);

            cnt++;
            colPtr++;
            //System.out.println(cnt);
        }
        colPtr = colNumber - 1;

        while (isInRange(rowNumber, colPtr) && getCellOwner(rowNumber, colPtr) == player) {
            //System.out.println("2row:"+rowNumber+'\n'+"col:"+colNumber);

            cnt++;
            colPtr--;
            //System.out.println(cnt);
        }

        if (cnt >= getWinThreshold()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isVerticalWin(int rowNumber, int colNumber) {
        int cnt = 1;
//        if (this.getCellOwner(rowNumber,colNumber) == null){return false;}
        OXOPlayer player = this.getCellOwner(rowNumber, colNumber);
        int rowPtr = rowNumber + 1;
        while (isInRange(rowPtr, colNumber) && getCellOwner(rowPtr, colNumber) == player) {
            cnt++;
            rowPtr++;

        }
        rowPtr = rowNumber - 1;
        while (isInRange(rowPtr, colNumber) && getCellOwner(rowPtr, colNumber) == player) {
            cnt++;
            rowPtr--;
        }
        //System.out.println(cnt);
        if (cnt >= getWinThreshold()) {
            return true;
        } else {
            return false;
        }
    }


    public boolean isDiagonalWin(int rowNumber, int colNumber) {
        int cnt = 1;
        //split the diagonal direction align pieces into two sub sequences
        cnt += subDirectionCount(rowNumber, colNumber, subDirection.UPLEFT);
        cnt += subDirectionCount(rowNumber, colNumber, subDirection.DOWNRIGHT);
        if (cnt >= getWinThreshold()) return true;
        cnt = 1;
        cnt += subDirectionCount(rowNumber, colNumber, subDirection.UPRIGHT);
        cnt += subDirectionCount(rowNumber, colNumber, subDirection.DOWNLEFT);
        if (cnt >= getWinThreshold()) return true;
        return false;
    }

    private int subDirectionCount(int rowNumber, int colNumber, subDirection d) {
        int rowMove = 0;
        int colMove = 0;
        int rowPos;
        int colPos;
        int cnt = 0;
        OXOPlayer player = getCellOwner(rowNumber, colNumber);
        if (d == subDirection.UPLEFT) {
            rowMove = -1;
            colMove = -1;
        } else if (d == subDirection.DOWNRIGHT){
            rowMove = 1;
            colMove = 1;
        } else if(d == subDirection.UPRIGHT){
            rowMove = -1;
            colMove = 1;
        } else if(d == subDirection.DOWNLEFT){
            rowMove = 1;
            colMove = -1;
        }
            rowPos = rowNumber + rowMove;
            colPos = colNumber + colMove;

            while (isInRange(rowPos,colPos) &&getCellOwner(rowPos,colPos) == player){
                cnt++;
                rowPos += rowMove;
                colPos += colMove;
            }
            return cnt;

    }

    public boolean isInRange(int rowNumber,int colNumber){
        if (rowNumber >= getNumberOfRows() || rowNumber<0){
            return false;
        }
        if (colNumber >= getNumberOfColumns() || colNumber<0){
            return false;
        }
        return true;
    }

    public void addRow() {
        List<OXOPlayer> row = new ArrayList<OXOPlayer>();
        for (int j = 0; j < this.getNumberOfColumns();j++){
            row.add(null);
        }
        this.cells.add(row);
    }
    public void removeRow() {
        this.cells.remove(this.getNumberOfRows()-1);
    }
    public void addColumn() {
        for (int i = 0;i < this.getNumberOfRows();i++){
            this.getRowAccess(i).add(null);
        }
    }
    public void removeColumn() {
        for (int i = 0;i < this.getNumberOfRows();i++){
            this.getRowAccess(i).remove(this.getNumberOfColumns()-1);
        }
    }
    public void increaseWinThreshold() {
        this.setWinThreshold(this.getWinThreshold()+1);
    }
    public void decreaseWinThreshold() {
        this.setWinThreshold(this.getWinThreshold()-1);
    }
    public List<OXOPlayer> getRowAccess(int rowNumber){
        return this.cells.get(rowNumber);
    }

    public List<List<OXOPlayer>> getCells() {
        return cells;
    }

    public void reset() {
        for (int i =0; i < this.getNumberOfRows();i++){
            for (int j = 0; j< this.getNumberOfColumns();j++){
                this.setCellOwner(i,j, null);
            }
        }
        currentPlayerNumber = 0;
    }



}
