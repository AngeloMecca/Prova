// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

package org.argouml.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.argouml.application.ArgoVersion;
import org.argouml.application.api.Argo;
import org.argouml.application.api.Configuration;
import org.argouml.application.api.SettingsTabPanel;
import org.argouml.application.helpers.SettingsTabHelper;
import org.argouml.swingext.LabelledLayout;

/**
 *  Provides settings for altering the appearance of the Argo application.
 *
 *  @author Linus Tolke
 *  @author Jeremy Jones
 *  @since  0.9.7
 */
public class SettingsTabAppearance
    extends SettingsTabHelper
    implements SettingsTabPanel
{

    private JComboBox	lookAndFeel;
    private JComboBox	metalTheme;
    private JLabel      metalLabel;
    private JCheckBox   smoothEdges;
    
    /**
     * The constructor.
     * 
     */
    public SettingsTabAppearance() {
        super();

        setLayout(new BorderLayout());

        int labelGap = 10;
        int componentGap = 10;
        JPanel top = new JPanel(new LabelledLayout(labelGap, componentGap));

        JLabel label = createLabel("label.look-and-feel");
        lookAndFeel =
	    new JComboBox(LookAndFeelMgr.getInstance()
			  .getAvailableLookAndFeelNames());
        lookAndFeel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                setMetalThemeState();
            }
        });
        label.setLabelFor(lookAndFeel);
        top.add(label);
        top.add(lookAndFeel);

        metalLabel = createLabel("label.metal-theme");

        metalTheme = new JComboBox(LookAndFeelMgr.getInstance()
                .getAvailableThemeNames());
        metalLabel.setLabelFor(metalTheme);
        top.add(metalLabel);
        top.add(metalTheme);
        
        smoothEdges = createCheckBox("label.smooth-edges");
        JLabel emptyLabel = new JLabel();
        emptyLabel.setLabelFor(smoothEdges);
        
        top.add(emptyLabel);
        top.add(smoothEdges);
        
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(top, BorderLayout.CENTER);

        JLabel restart = createLabel("label.restart-application");
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setVerticalAlignment(SwingConstants.CENTER);
        restart.setBorder(BorderFactory.createEmptyBorder(10, 2, 10, 2));
        add(restart, BorderLayout.SOUTH);

        setMetalThemeState();
    }
    
    /**
     * Enables or disables the metal theme controls depending on whether
     * or not themes are supported by the selected look and feel.
    **/
    private void setMetalThemeState()
    {
        String lafName = (String) lookAndFeel.getSelectedItem();
        boolean enabled =
	    LookAndFeelMgr.getInstance().isThemeCompatibleLookAndFeel(
		LookAndFeelMgr.getInstance().getLookAndFeelFromName(lafName));

        metalLabel.setEnabled(enabled);
        metalTheme.setEnabled(enabled);
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabRefresh()
     */
    public void handleSettingsTabRefresh() {
        String laf = LookAndFeelMgr.getInstance().getCurrentLookAndFeelName();
    	String theme = LookAndFeelMgr.getInstance().getCurrentThemeName();

        lookAndFeel.setSelectedItem(laf);
        metalTheme.setSelectedItem(theme);		
        
        smoothEdges.setSelected(Configuration.getBoolean(
            Argo.KEY_SMOOTH_EDGES, false));
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabSave()
     */
    public void handleSettingsTabSave() {
        LookAndFeelMgr.getInstance().setCurrentLookAndFeel(
            LookAndFeelMgr.getInstance().getLookAndFeelFromName(
                (String) lookAndFeel.getSelectedItem()));
    
        LookAndFeelMgr.getInstance().setCurrentTheme(
            LookAndFeelMgr.getInstance().getThemeFromName(
                (String) metalTheme.getSelectedItem()));
    
        Configuration.setBoolean(Argo.KEY_SMOOTH_EDGES, 
            smoothEdges.isSelected());
    }

    /**
     * @see org.argouml.application.api.SettingsTabPanel#handleSettingsTabCancel()
     */
    public void handleSettingsTabCancel() { }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleName()
     */
    public String getModuleName() { return "SettingsTabAppearance"; }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleDescription()
     */
    public String getModuleDescription() { return "Appearance Settings"; }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleAuthor()
     */
    public String getModuleAuthor() { return "ArgoUML Core"; }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleVersion()
     */
    public String getModuleVersion() { return ArgoVersion.getVersion(); }
    
    /**
     * @see org.argouml.application.api.ArgoModule#getModuleKey()
     */
    public String getModuleKey() { return "module.settings.appearance"; }
    
    /**
     * @see org.argouml.application.api.SettingsTabPanel#getTabKey()
     */
    public String getTabKey() { return "tab.appearance"; }
}
