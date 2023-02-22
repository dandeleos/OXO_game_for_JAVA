package edu.uob;

import javax.lang.model.type.NullType;
import java.util.ArrayList;
import java.util.List;
/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            佛祖保佑       永无BUG
 */
public class OXOController {
    OXOModel gameModel;

    public OXOController(OXOModel model) {
        gameModel = model;
    }

    private void isCommandLengthCorrect(String command) throws OXOMoveException{
        if (command.length() != 2){
            throw new OXOMoveException.InvalidIdentifierLengthException(command.length());
        }
    }

    private void isCharacterInRange(String command) throws OXOMoveException{
        char chr0 = Character.toLowerCase(command.charAt(0));
        if (chr0 < 'a' && chr0 > 'i'){
            throw new OXOMoveException.InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.ROW,chr0);
        }
        if (command.charAt(1) <'0' && command.charAt(1) >'9' ){
            throw new OXOMoveException.InvalidIdentifierCharacterException(OXOMoveException.RowOrColumn.COLUMN,command.charAt(1));
        }
    }

    private void isCellPositionCorrect(int rowNumber,int colNumber) throws OXOMoveException{
        if (rowNumber >= gameModel.getNumberOfRows() || rowNumber<0){
            throw new OXOMoveException.OutsideCellRangeException( OXOMoveException.RowOrColumn.ROW,rowNumber);
        }
        if (colNumber >= gameModel.getNumberOfColumns() || colNumber<0){
            throw new OXOMoveException.OutsideCellRangeException( OXOMoveException.RowOrColumn.COLUMN,colNumber);
        }
    }

    private void isEmptyCell(int rowNumber,int colNumber) throws OXOMoveException{
        if (gameModel.getCellOwner(rowNumber,colNumber) != null){
            throw new OXOMoveException.CellAlreadyTakenException (rowNumber,colNumber);
        }
    }
    public void handleIncomingCommand(String command) throws OXOMoveException {
        if (gameModel.isGameDrawn() || gameModel.isGameWon()){
            return;
        }

        isCommandLengthCorrect(command);
        isCharacterInRange(command);

        int rowNumber = (int) Character.toLowerCase(command.charAt(0)) -'a';
        int colNumber = (int) command.charAt(1)-'1';
        isCellPositionCorrect(rowNumber,colNumber);
        isEmptyCell(rowNumber,colNumber);
        gameModel.setCellOwner(rowNumber,colNumber,gameModel.getCurrentPlayer());
        if (gameModel.isGameDrawn() || gameModel.isGameWon()){
            return;
        }
        this.gameModel.changePlayer();

    }

    public void addRow() {gameModel.addRow();}
    public void removeRow() {gameModel.removeRow();}
    public void addColumn() {gameModel.addColumn();}
    public void removeColumn() {gameModel.removeColumn();}
    public void increaseWinThreshold() {gameModel.increaseWinThreshold();}
    public void decreaseWinThreshold() {gameModel.decreaseWinThreshold();}

//    public void testWin(int rowNumber,int colNumber){
//        char letter= this.gameModel.getPlayerByNumber(this.gameModel.getCurrentPlayerNumber()).getPlayingLetter();
//        testWinHorizontal(rowNumber,letter);
//        testWinVertical(colNumber,letter);
//        testSlashDirection(rowNumber,colNumber,letter);
//        testBackSlashDirection(rowNumber,colNumber,letter);
//    }
//
//    private void testWinHorizontal(int rowNum, char letter){
//        int cnt = 0;
//        for (int i = 0;i < gameModel.getNumberOfColumns();i++ ){
//            if (gameModel.getRowAccess(rowNum).get(i) != null && gameModel.getRowAccess(rowNum).get(i).getPlayingLetter() == letter){
//                cnt++;
//            }
//        }
//        if (cnt == gameModel.getWinThreshold()){
//            gameModel.setWinner(this.gameModel.getPlayerByNumber(this.gameModel.getCurrentPlayerNumber()));
//        }
//    }
//
//    private void testWinVertical(int colNumber,char letter){
//        int cnt = 0;
//        for (int i = 0; i < gameModel.getNumberOfRows();i++){
//            if (gameModel.getRowAccess(i).get(colNumber) != null && gameModel.getRowAccess(i).get(colNumber).getPlayingLetter() == letter ){
//                cnt++;
//            }
//        }
//        if (cnt == gameModel.getWinThreshold()){
//            gameModel.setWinner(this.gameModel.getPlayerByNumber(this.gameModel.getCurrentPlayerNumber()));
//        }
//    }
//
//    private void testWinDiagonal(int rowNumber,int colNumber,char letter){
//        testSlashDirection(rowNumber,colNumber,letter);
//        testBackSlashDirection(rowNumber,colNumber,letter);
//    }
//
//    private void testSlashDirection(int rowNumber,int colNumber,char letter){
//        int cnt = 0;
//        int x = 0;
//        int y = 0;
////        System.out.println(rowNumber);
////        System.out.println(colNumber);
//        while(rowNumber > 0 && colNumber < gameModel.getNumberOfColumns()-1){
//            rowNumber--;
//            colNumber++;
//        }
////        System.out.println(rowNumber);
////        System.out.println(colNumber);
//        x = rowNumber;
//        y = colNumber;
//        for (;x < gameModel.getNumberOfRows() && y >= 0;x++,y--){
//            if (gameModel.getRowAccess(x).get(y) != null && gameModel.getRowAccess(x).get(y).getPlayingLetter() == letter){
//                cnt++;
//            }
//        }
//        if (cnt == gameModel.getWinThreshold()){
//            gameModel.setWinner(this.gameModel.getPlayerByNumber(this.gameModel.getCurrentPlayerNumber()));
//        }
//    }
//    private void testBackSlashDirection(int rowNumber,int colNumber,char letter){
//        int cnt = 0;
//        int x = 0;
//        int y = 0;
//        if ((rowNumber - colNumber)>=0){
//            x = (rowNumber - colNumber);
//        }else {
//            y = -(rowNumber - colNumber);
//        }
//        for (;x < gameModel.getNumberOfRows()&& y < gameModel.getNumberOfColumns();x++,y++){
//            if (gameModel.getRowAccess(x).get(y) != null && gameModel.getRowAccess(x).get(y).getPlayingLetter() == letter){
//                cnt++;
//            }
//        }
//        if (cnt == gameModel.getWinThreshold()){
//            gameModel.setWinner(this.gameModel.getPlayerByNumber(this.gameModel.getCurrentPlayerNumber()));
//        }
//    }
    public void reset() { gameModel.reset();}
}
