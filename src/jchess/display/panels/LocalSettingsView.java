/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jchess.display.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import jchess.JChessApp;
import jchess.core.Game;
import jchess.utils.Settings;

/**
 *
 * @author Mateusz Lach ( matlak, msl )
 */
public class LocalSettingsView extends JPanel implements ActionListener, ItemListener
{
    private JCheckBox isUpsideDown;
    
    private JCheckBox isDisplayLegalMovesEnabled;
    
    private JCheckBox isRenderLabelsEnabled;    
     
    private GridBagConstraints gbc;
    
    private GridBagLayout gbl;
    
    private Game game;
    
    /**
     * htd
     */
    JButton timeConfirmButton;
    JCheckBox timeGame;
    JComboBox time4Game; 
    String times[] =
        {
            "1", "3", "5", "8", "10", "15", "20", "25", "30", "60", "120"
        };
    //
    
    public LocalSettingsView(Game game)
    {
        this.game = game;
        
        this.gbc = new GridBagConstraints();
        this.gbl = new GridBagLayout();
        
        /**
         * htd
         */
        this.timeConfirmButton = new JButton(Settings.lang("ok"));
        this.timeConfirmButton.setMnemonic(KeyEvent.VK_D);
        this.timeConfirmButton.setActionCommand("confirmTimeGame");
        this.timeConfirmButton.addActionListener(this);
        this.timeConfirmButton.setToolTipText(
        		"Click this button to confirm this is a time game");
        
        this.timeGame = new JCheckBox(Settings.lang("time_game_min"));
        this.timeGame.setMnemonic(KeyEvent.VK_T);
        this.timeGame.setSelected(false);
        this.timeGame.addItemListener(this);
        
        this.time4Game = new JComboBox(times);
        //htd
        
        this.setLayout(gbl);
        
        initUpsideDownControl();
        initDisplayLegalMovesControl();
        initRenderLabelsControl();
        refreshCheckBoxesState();
        
        
        /**
         * htd
         * new try, span to right side
         */
        this.gbc.gridx = 1;
        this.gbc.gridy = 3;
        this.gbl.setConstraints(timeGame, gbc);
        this.add(timeGame);
        
        this.gbc.gridx = 1;
        this.gbc.gridy = 4;
        this.gbl.setConstraints(time4Game, gbc);
        this.add(time4Game);
        
        this.gbc.gridx = 1;
        this.gbc.gridy = 5;
        this.gbl.setConstraints(timeConfirmButton, gbc);
        this.add(timeConfirmButton);
        this.timeConfirmButton.addActionListener(this);
        //
    }
    
    private void initUpsideDownControl()
    {
        this.isUpsideDown = new JCheckBox();
        this.isUpsideDown.setText(Settings.lang("upside_down"));
        this.isUpsideDown.setSize(this.isUpsideDown.getHeight(), this.getWidth());
        this.gbc.gridx = 0;
        this.gbc.gridy = 0;
        this.gbc.insets = new Insets(3, 3, 3, 3);
        this.gbl.setConstraints(isUpsideDown, gbc);
        this.add(isUpsideDown);
        
        isUpsideDown.addActionListener(this);
    }
    
    private void initDisplayLegalMovesControl()
    {
        this.isDisplayLegalMovesEnabled = new JCheckBox();
        this.isDisplayLegalMovesEnabled.setText(Settings.lang("display_legal_moves"));     
        
        this.gbc.gridx = 0;
        this.gbc.gridy = 1;
        this.gbl.setConstraints(isDisplayLegalMovesEnabled, gbc);
        this.add(isDisplayLegalMovesEnabled);
        
        isDisplayLegalMovesEnabled.addActionListener(this);        
    }
    
    private void initRenderLabelsControl()
    {
        this.isRenderLabelsEnabled = new JCheckBox();
        this.isRenderLabelsEnabled.setText(Settings.lang("display_labels"));     
        
        this.gbc.gridx = 0;
        this.gbc.gridy = 2;
        this.gbl.setConstraints(isRenderLabelsEnabled, gbc);
        this.add(isRenderLabelsEnabled);
        
        isRenderLabelsEnabled.addActionListener(this);        
    }
        
    private void refreshCheckBoxesState()
    {
        if (isInitiatedCorrectly())
        {
            isUpsideDown.setSelected(game.getSettings().isUpsideDown());
            isDisplayLegalMovesEnabled.setSelected(game.getSettings().isDisplayLegalMovesEnabled());
            isRenderLabelsEnabled.setSelected(game.getSettings().isRenderLabels());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JCheckBox clickedComponent = (JCheckBox) e.getSource();
        if (clickedComponent == isUpsideDown)
        {
            game.getSettings().setUpsideDown(isUpsideDown.isSelected());
        } 
        else if (clickedComponent == isDisplayLegalMovesEnabled)
        {
            game.getSettings().setDisplayLegalMovesEnabled(isDisplayLegalMovesEnabled.isSelected());
        }
        else if (clickedComponent == isRenderLabelsEnabled) 
        {
            game.getSettings().setRenderLabels(isRenderLabelsEnabled.isSelected());
            game.resizeGame();
        }
        game.repaint();
    }
    
    @Override
    public void repaint()
    {
        refreshCheckBoxesState();
        super.repaint();
    }

    private boolean isInitiatedCorrectly()
    {
        return null != isUpsideDown && null != isDisplayLegalMovesEnabled
                && null != isRenderLabelsEnabled;
    }

    /**
     * htd
     */
	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getItemSelectable();
		if(source == this.timeGame) {
			//todo
			
		}
		
		if(e.getStateChange() == ItemEvent.DESELECTED) {
			//todo
			this.timeConfirmButton.setEnabled(false);
		}else {
			this.timeConfirmButton.setEnabled(true);
		}
		
		this.game.repaint();
	}
	
	/**
	 * htd
	 */
	public void disableTimeConfirmButton() {
		//todo
		this.timeConfirmButton.setEnabled(false);
		String[] selectedItem = (String[]) time4Game.getSelectedItem();
		System.out.println("selected time = " + Arrays.toString(selectedItem));
		
		this.game.repaint();
	}
}
