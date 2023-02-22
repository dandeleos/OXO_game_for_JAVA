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


    public void reset() { gameModel.reset();}
}