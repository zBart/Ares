/*
 Copyright (c) 2010 [Joerg Ruedenauer]
 
 This file is part of Ares.

 Ares is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 Ares is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with Ares; if not, write to the Free Software
 Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 */
package ares.controller.gui;


import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import java.awt.BorderLayout;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.swing.JComboBox;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import ares.controllers.control.Control;
import ares.controller.control.KeyAction;
import ares.controller.control.ComponentKeys;
import ares.controller.control.OnlineOperations;
import ares.controllers.control.Version;
import ares.controllers.data.Command;
import ares.controllers.data.Configuration;
import ares.controllers.data.KeyStroke;
import ares.controllers.data.Mode;
import ares.controllers.data.TitledElement;
import ares.controller.gui.util.ExampleFileFilter;
import ares.controllers.messages.IMessageListener;
import ares.controllers.messages.Message;
import ares.controllers.messages.Messages;
import ares.controllers.messages.Message.MessageType;
import ares.controllers.network.INetworkClient;
import ares.controllers.network.IServerListener;
import ares.controllers.network.ServerInfo;
import ares.controllers.network.ServerSearch;
import ares.controller.util.Directories;
import ares.controller.util.Localization;

public final class MainFrame extends FrameController implements IMessageListener, IServerListener, INetworkClient, CommandsPanelCreator.IActionCreator {

  private JPanel jContentPane = null;
  private JPanel buttonsPanel = null;
  private JLabel jLabel = null;
  private JButton fileButton = null;
  private JPanel networkPanel = null;
  private JLabel jLabel1 = null;
  private JComboBox serverBox = null;
  private JButton connectButton = null;
  private JToggleButton messagesButton = null;
  private JButton settingsButton = null;
  private JButton infoButton = null;
  
  private ServerSearch serverSearch = null;
  private JPanel statePanel;
  
  private final class MyWindowListener extends WindowAdapter {

    public void windowIconified(WindowEvent e) {
      FrameManagement.getInstance().iconifyAllFrames(MainFrame.this);
    }

    public void windowDeiconified(WindowEvent e) {
      FrameManagement.getInstance().deiconifyAllFrames(MainFrame.this);
    }

  }
  
  private boolean connectWithFirstServer = true;
  
  private static boolean isWindows()
  {
	  return System.getProperty("os.name").startsWith("Windows"); //$NON-NLS-1$ //$NON-NLS-2$
  }
  
  private String findLocalPlayerDir()
  {
	  URL myPath = MainFrame.class.getProtectionDomain().getCodeSource().getLocation();
	  File file;
	  try {
		  file = new File(myPath.toURI());
		  File myDir = file.isFile() ? file.getParentFile() : file; // ..../Ares/Controller
		  if (myDir == null) {
			  return null;
		  }
		  File baseDir = myDir.getParentFile(); // .../Ares
		  if (baseDir == null)
			  return null;
		  if (isWindows()) {
			  return baseDir.toString() + File.separator +  "Player_Editor" + File.separator; //$NON-NLS-1$
		  }
		  else {
			  return baseDir.toString() + File.separator +  "Player" + File.separator; //$NON-NLS-1$
		  }
	  }
	  catch (URISyntaxException e) {
		return null;
	  }
	  
  }
  
  private File findLocalPlayer() {
	  String localPlayerDir = findLocalPlayerDir();
	  if (localPlayerDir == null)
		  return null;
	  String playerFileName = localPlayerDir;
	  if (isWindows()) {
		  playerFileName += "Ares.CmdLinePlayer.exe"; //$NON-NLS-1$
	  }
	  else {
		  playerFileName += "Ares.CmdLinePlayer.sh"; //$NON-NLS-1$
	  }
	  File playerExe = new File(playerFileName); //$NON-NLS-1$ //$NON-NLS-2$
	  return playerExe.exists() ? playerExe : null;
  }
  
  private Timer firstTimer;
  private boolean hasLocalPlayer;
  private boolean globalHookPossible;
  private boolean globalHookActive;
  

  /**
   * This method initializes 
   * 
   */
  public MainFrame() {
  	super(Localization.getString("MainFrame.SoundController")); //$NON-NLS-1$
  	globalHookPossible = false;
  	globalHookActive = false;
	try {
		GlobalScreen.registerNativeHook();
		globalHookPossible = true;
	}
	catch (NativeHookException ex){
		Messages.addMessage(MessageType.Warning, Localization.getString("MainFrame.KeyCaptureNotPossible") + ex.getLocalizedMessage()); //$NON-NLS-1$
	}
  	Preferences prefs = Preferences.userNodeForPackage(OptionsDialog.class);
  	if (globalHookPossible && prefs.getBoolean("GlobalKeyHook", false)) //$NON-NLS-1$
  	{
  		addGlobalKeyHook();
  	}
  	initialize();
    this.addWindowListener(new MyWindowListener());
  	Messages.getInstance().addObserver(this);
  	Messages.addMessage(MessageType.Info, Localization.getString("MainFrame.RSCVersion") + Version.getCurrentVersionString() + Localization.getString("MainFrame.started")); //$NON-NLS-1$ //$NON-NLS-2$
  	ModeFrames.getInstance();
  	addButton(Localization.getString("MainFrame.Messages"), getMessagesButton()); //$NON-NLS-1$
  	enableProjectSpecificControls(false);
  	final boolean hasManualPlayer = !Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("AutoDetectPlayer", true); //$NON-NLS-1$
  	serverSearch = new ServerSearch(this, Preferences.userNodeForPackage(MainFrame.class).getInt("UDPPort", 8009)); //$NON-NLS-1$
  	if (!hasManualPlayer) {
  		serverSearch.startSearch();
  	}
    ComponentKeys.addAlwaysAvailableKeys(getRootPane());
    hasLocalPlayer = findLocalPlayer() != null;
    final boolean checkForNewVersion = Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("CheckForUpdate", true); //$NON-NLS-1$
  	final boolean startLocalPlayer = hasLocalPlayer && Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("StartLocalPlayer", true); //$NON-NLS-1$
  	if (checkForNewVersion || startLocalPlayer || hasManualPlayer) {
  		firstTimer = new Timer(2000, new ActionListener() {
  			public void actionPerformed(ActionEvent e) {
  				if (hasManualPlayer) {
  					Preferences prefs = Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class);
  					String serverName = prefs.get("ServerName", "localhost"); //$NON-NLS-1$ //$NON-NLS-2$
  					String ipAddress = prefs.get("IPAddress", "127.0.0.1"); //$NON-NLS-1$ //$NON-NLS-2$
  					int tcpPort = prefs.getInt("TCPPort", 11112); //$NON-NLS-1$
  					try {
  						servers.clear();
  						connectWithFirstServer = true;
  						serverFound(new ServerInfo(InetAddress.getByName(ipAddress), true, tcpPort, false, 0, serverName));
  					}
  					catch (java.net.UnknownHostException ex) {
  						JOptionPane.showMessageDialog(MainFrame.this, Localization.getString("MainFrame.Server2") + ipAddress + Localization.getString("MainFrame.NotFound"), Localization.getString("MainFrame.Ares"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  					}
  				}
  				else if (startLocalPlayer) {
  					maybeStartPlayer();
  				}
  				if (checkForNewVersion) {
  					OnlineOperations.checkForUpdate(MainFrame.this, false);
  				}
  			}
  		});
  		firstTimer.setRepeats(false);
  		firstTimer.start();
  	}
  }
  
  public void dispose() {
    storeLayout();
    Control.getInstance().disconnect(true);
    Messages.getInstance().removeObserver(this);
    serverSearch.dispose();
    FrameManagement.getInstance().setExiting();
    FrameManagement.getInstance().closeAllFrames(this);
    if (globalHookActive) {
    	removeGlobalKeyHook();
    }
    if (globalHookPossible) {
    	GlobalScreen.unregisterNativeHook();
    }
    super.dispose();
  }

  /**
   * This method initializes this
   * 
   */
  private void initialize() {
        this.setSize(new Dimension(456, 215));
        this.setContentPane(getJContentPane());
        this.setTitle(Localization.getString("MainFrame.SoundController")); //$NON-NLS-1$
        this.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);    
        this.setJMenuBar(createMenuBar());
        pack();
  }
  
  private JMenuBar menuBar;
  
  private JMenuBar createMenuBar() {
	  if (menuBar == null) {
		  menuBar = new JMenuBar();
		  menuBar.add(getFileMenu());
		  menuBar.add(getPlayMenu());
		  menuBar.add(getExtrasMenu());
		  menuBar.add(getHelpMenu());
	  }
	  return menuBar;
  }
  
  private JMenu recentMenu;
  private JMenuItem openItem;
  
  private JMenu getFileMenu() {
	  JMenu fileMenu = new JMenu(Localization.getString("MainFrame.Project")); //$NON-NLS-1$
	  openItem = new JMenuItem(Localization.getString("MainFrame.OpenMenu")); //$NON-NLS-1$
	  openItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  selectFile();
		  }
	  });
	  openItem.setEnabled(Control.getInstance().isConnected());
	  recentMenu = new JMenu(Localization.getString("MainFrame.Recent")); //$NON-NLS-1$
	  JMenuItem exitItem = new JMenuItem(Localization.getString("MainFrame.Exit")); //$NON-NLS-1$
	  exitItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  exitProgram();
		  }
	  });
	  fileMenu.add(openItem);
	  fileMenu.add(recentMenu);
	  fileMenu.addSeparator();
	  fileMenu.add(exitItem);
	  return fileMenu;
  }
  
  public void exitProgram()
  {
	  dispose();
  }
  
  private JMenuItem repeatItem;
  
  private JMenu getPlayMenu() {
	  JMenu playMenu = new JMenu(Localization.getString("MainFrame.Play")); //$NON-NLS-1$
	  JMenuItem stopItem = new JMenuItem(new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
		}
	  });
	  stopItem.setText(Localization.getString("MainFrame.StopAll")); //$NON-NLS-1$
	  JMenuItem previousItem = new JMenuItem(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
			}
		  });
	  previousItem.setText(Localization.getString("MainFrame.PreviousMusic")); //$NON-NLS-1$
	  JMenuItem nextItem = new JMenuItem(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
			}
		  });
	  nextItem.setText(Localization.getString("MainFrame.NextMusic")); //$NON-NLS-1$
	  repeatItem = new JMenuItem(new AbstractAction() {
		  @Override
		  public void actionPerformed(ActionEvent e) 
		  {
			  toggleRepeat();
		  }
	  });
	  repeatItem.setText(Localization.getString("MainFrame.repeatMusic")); //$NON-NLS-1$
	  playMenu.add(stopItem);
	  playMenu.add(previousItem);
	  playMenu.add(nextItem);
	  playMenu.add(repeatItem);
	  return playMenu;
  }
  
  private JMenuItem messagesMenuItem;
  private JMenuItem webControllerItem;
  
  private JMenu getExtrasMenu() {
	  JMenu extrasMenu = new JMenu(Localization.getString("MainFrame.Extras")); //$NON-NLS-1$
	  messagesMenuItem = new JCheckBoxMenuItem(Localization.getString("MainFrame.MessagesMenu")); //$NON-NLS-1$
	  messagesMenuItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  getMessagesButton().doClick();
		  }
	  });
	  webControllerItem = new JMenuItem(Localization.getString("MainFrame.WebController")); //$NON-NLS-1$
	  webControllerItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  openWebController();
		  }
	  });
	  JMenuItem settingsItem = new JMenuItem(Localization.getString("MainFrame.SettingsMenu")); //$NON-NLS-1$
	  settingsItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  showSettingsDialog();
		  }
	  });
	  final JMenuItem globalKeyItem = new JCheckBoxMenuItem(Localization.getString("MainFrame.GlobalKeyCapture")); //$NON-NLS-1$
	  globalKeyItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  if (!globalHookActive)
				  addGlobalKeyHook();
			  else
				  removeGlobalKeyHook();
			  globalKeyItem.setSelected(globalHookActive);
			  Preferences prefs = Preferences.userNodeForPackage(OptionsDialog.class);
			  prefs.putBoolean("GlobalKeyHook", globalHookActive); //$NON-NLS-1$
		  }
	  });
	  globalKeyItem.setSelected(globalHookActive);
	  if (!globalHookPossible) {
		  globalKeyItem.setEnabled(false);
	  }
	  extrasMenu.add(messagesMenuItem);
	  extrasMenu.add(webControllerItem);
	  extrasMenu.addSeparator();
	  extrasMenu.add(globalKeyItem);
	  extrasMenu.add(settingsItem);
	  return extrasMenu;
  }
  
  private class GlobalKeyListener implements NativeKeyListener {

	@Override
	public void nativeKeyPressed(NativeKeyEvent arg0) {
		Control.getInstance().sendKey(ComponentKeys.getKeyStroke(arg0.getKeyCode()));
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
	}
	  
  }
  
  private GlobalKeyListener m_GlobalKeyListener = new GlobalKeyListener();
  
  private void addGlobalKeyHook() {
	  GlobalScreen.getInstance().addNativeKeyListener(m_GlobalKeyListener);
	  globalHookActive = true;
	  KeyAction.setKeysEnabled(false);
  }
  
  private void removeGlobalKeyHook() {
	  GlobalScreen.getInstance().removeNativeKeyListener(m_GlobalKeyListener);
	  globalHookActive = false;
	  KeyAction.setKeysEnabled(true);
  }
  
  private JMenu getHelpMenu() {
	  JMenu helpMenu = new JMenu(Localization.getString("MainFrame.Help")); //$NON-NLS-1$
	  JMenuItem helpItem = new JMenuItem(Localization.getString("MainFrame.HelpMenuItem")); //$NON-NLS-1$
	  helpItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  showHelpPage();
		  }
	  });
	  JMenuItem updateItem = new JMenuItem(Localization.getString("MainFrame.CheckForUpdate")); //$NON-NLS-1$
	  updateItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  OnlineOperations.checkForUpdate(MainFrame.this, true);
		  }
	  });
	  JMenuItem aboutItem = new JMenuItem(Localization.getString("MainFrame.AboutMenu")); //$NON-NLS-1$
	  aboutItem.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			 showAboutDialog();
		  }
	  });
	  helpMenu.add(helpItem);
	  helpMenu.add(updateItem);
	  helpMenu.addSeparator();
	  helpMenu.add(aboutItem);
	  return helpMenu;
  }
  
  private void storeLayout() {
    if (Control.getInstance().getConfiguration() != null) {
      File layoutsFile = new File(Directories.getUserDataPath() + Control.getInstance().getFileName() + ".layout"); //$NON-NLS-1$
      try {
        FrameLayouts.getInstance().storeToFile(layoutsFile.getAbsolutePath());
      }
      catch (IOException e) {
        Messages.addMessage(MessageType.Warning, Localization.getString("MainFrame.LayoutWriteError")); //$NON-NLS-1$
      }
    }    
  }
  
  private JPanel modesPanel;
  private JPanel modeCommandsPanel;
  
  private JPanel getModesPanel() {
	  if (modesPanel == null) {
		  modesPanel = new JPanel(new BorderLayout());
		  modesPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
				  Localization.getString("ModesFrame.Modes"), //$NON-NLS-1$
				  TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
	  }
	  return modesPanel;
  }
  
  private void enableProjectSpecificControls(boolean enabled) {
	  getStopAllButton().setEnabled(enabled);
	  getNextTrackButton().setEnabled(enabled);
	  getPreviousButton().setEnabled(enabled);
	  if (!enabled) {
		  getModesPanel().setVisible(false);
		  if (modeCommandsPanel != null) {
			  getModesPanel().remove(modeCommandsPanel);
			  modeCommandsPanel = null;
		  }
		  getMusicListButton().setEnabled(false);
		  getTagsButton().setEnabled(false);
	  }
	  else {
          Configuration config = Control.getInstance().getConfiguration();
          if (config == null) return;
          List<Mode> modes = config.getModes();
          if (modes.size() == 0) return;
          List<Command> commands = new ArrayList<Command>();
          commands.addAll(modes);
          if (modeCommandsPanel != null)
		  {
        	  getModesPanel().remove(modeCommandsPanel);
		  }
          modeCommandsPanel = CommandsPanelCreator.createPanel(commands, this, getRootPane(), 3, getMusicListButton(), getTagsButton());
          getMusicListButton().setEnabled(true);
          getTagsButton().setEnabled(true);
          getModesPanel().add(modeCommandsPanel, BorderLayout.CENTER);
          getModesPanel().setVisible(true);
	  }
	  refillContentPane(getJContentPane());
	  pack();
  }
  
  private JToggleButton musicListButton;
  private MusicListFrame musicListFrame;
  
  private JToggleButton getMusicListButton() {
	  if (musicListButton == null) {
		  musicListButton = new JToggleButton();
		  musicListButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if (musicListFrame != null) {
					musicListFrame.dispose();
					musicListFrame = null;
				}
				else {
					musicListFrame = new MusicListFrame(Localization.getString("MainFrame.MusicList")); //$NON-NLS-1$
					musicListFrame.setTitles(currentMusicList);
					musicListFrame.setActiveTitle(currentShortTitle);
					musicListFrame.addWindowListener(new WindowAdapter() {
		                public void windowClosing(java.awt.event.WindowEvent e) {
		                	musicListFrame = null;
		                }
		                public void windowClosed(java.awt.event.WindowEvent e) {
		                	musicListFrame = null;
		                }
		              });
					musicListFrame.setVisible(true);
				}
			} 
		  });
		  musicListButton.setText(Localization.getString("MainFrame.MusicList")); //$NON-NLS-1$
		  addButton(Localization.getString("MainFrame.MusicList"), musicListButton); //$NON-NLS-1$
	  }
	  return musicListButton;
  }
  
  private JToggleButton tagsButton;
  private TagsFrame tagsFrame;
  
  private JToggleButton getTagsButton() {
	  if (tagsButton == null) {
		  tagsButton = new JToggleButton();
		  tagsButton.setAction(new AbstractAction() {
			public void actionPerformed(ActionEvent arg0) {
				if (tagsFrame != null) {
					tagsFrame.dispose();
					tagsFrame = null;
				}
				else {
					tagsFrame = new TagsFrame(Localization.getString("MainFrame.MusicTags")); //$NON-NLS-1$
					tagsFrame.addWindowListener(new WindowAdapter() {
		                public void windowClosing(java.awt.event.WindowEvent e) {
		                	tagsFrame = null;
		                }
		                public void windowClosed(java.awt.event.WindowEvent e) {
		                	tagsFrame = null;
		                }
		              });
					tagsFrame.setTags(currentCategories, currentTags);
					tagsFrame.setActiveTags(currentActiveTags);
					tagsFrame.setCategoryCombination(tagCategoryCombination);
					tagsFrame.setFading(musicTagsFadeTime, musicTagsFadeOnlyOnChange);
					tagsFrame.setVisible(true);
				}
			} 
		  });
		  tagsButton.setText(Localization.getString("MainFrame.MusicTags")); //$NON-NLS-1$
		  addButton(Localization.getString("MainFrame.MusicTags"), tagsButton); //$NON-NLS-1$
	  }
	  return tagsButton;
  }
  
  private static void updateLastProjects(String path, String name) {
	  java.util.prefs.Preferences prefs = java.util.prefs.Preferences
	  	.userNodeForPackage(MainFrame.class);
	  int nrOfLastProjects = prefs.getInt("LastUsedProjectsCount", 0); //$NON-NLS-1$
	  if (nrOfLastProjects < 4) nrOfLastProjects++;
	  int previousPos = nrOfLastProjects - 1;
	  for (int i = nrOfLastProjects - 2; i >= 0; --i) {
		  String oldPath = prefs.get("LastUsedProjectFile" + i, ""); //$NON-NLS-1$ //$NON-NLS-2$
		  if (oldPath.equals(path)) {
			  previousPos = i;
			  --nrOfLastProjects;
			  break;
		  }
	  }
	  for (int i = previousPos; i > 0; --i) {
		  String oldPath = prefs.get("LastUsedProjectFile" + (i - 1), ""); //$NON-NLS-1$ //$NON-NLS-2$
		  String oldName = prefs.get("LastUsedProjectName" + (i - 1), ""); //$NON-NLS-1$ //$NON-NLS-2$
		  prefs.put("LastUsedProjectFile" + i, oldPath); //$NON-NLS-1$
		  prefs.put("LastUsedProjectName" + i, oldName); //$NON-NLS-1$
	  }
	  prefs.put("LastUsedProjectFile" + 0, path); //$NON-NLS-1$
	  prefs.put("LastUsedProjectName" + 0, name); //$NON-NLS-1$
	  prefs.putInt("LastUsedProjectsCount", nrOfLastProjects); //$NON-NLS-1$
  }

  public void openFile(String fileName) {
    Control.getInstance().openFile(fileName);
  }
  
  private void projectOpened(Configuration config, String fileName) {
	if (fileName.equals(Control.getInstance().getFileName())) {
		return;
	}
    storeLayout();
	Control.getInstance().setConfiguration(config, fileName);
	

    List<SubFrame> openFrames = new ArrayList<SubFrame>();
    openFrames.add(this);
    if (messageFrame != null) openFrames.add(messageFrame);
    SubFrame[] frames = new SubFrame[openFrames.size()];
    openFrames.toArray(frames);
    FrameManagement.getInstance().closeAllFrames(frames);
      
    if (Control.getInstance().getConfiguration() != null) {
      enableProjectSpecificControls(true);
      
      File layoutsFile = new File(Directories.getUserDataPath() + fileName + ".layout"); //$NON-NLS-1$
      if (layoutsFile.exists()) try {
        FrameLayouts.getInstance().readFromFile(layoutsFile.getAbsolutePath());
        FrameLayouts.getInstance().restoreLastLayout();
      }
      catch (IOException e) {
        Messages.addMessage(MessageType.Warning, Localization.getString("MainFrame.LayoutReadError")); //$NON-NLS-1$
      }

      String filePath = Control.getInstance().getFilePath();
      if (filePath.endsWith(fileName)) {
    	  Preferences.userNodeForPackage(MainFrame.class).put("LastConfiguration", filePath); //$NON-NLS-1$
    	  updateLastProjects(filePath, Control.getInstance().getConfiguration().getTitle());
    	  rebuildLastProjectsMenu();
      }
    }
    else {
      enableProjectSpecificControls(false);
    }
    updateProjectTitle();
    pack();
  }
  
  private void updateProjectTitle() {
	    if (Control.getInstance().getConfiguration() != null) {
	    	String text = Control.getInstance().getConfiguration().getTitle();
	        projectLabel.setText(text);
	    }
	    else if (Control.getInstance().isConnected()) {
	    	projectLabel.setText(Localization.getString("MainFrame.NoProjectOpened2"));          //$NON-NLS-1$
	    }
	    else {
	        projectLabel.setText(Localization.getString("MainFrame.NoProjectOpened"));	    	 //$NON-NLS-1$
	    }
  }

  /**
   * This method initializes jContentPane	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getJContentPane() {
    if (jContentPane == null) {
      jContentPane = new JPanel();
      refillContentPane(jContentPane);
    }
    return jContentPane;
  }
  
  private void refillContentPane(JPanel contentPane) {
	  contentPane.removeAll();
      GroupLayout layout = new GroupLayout(contentPane);
      contentPane.setLayout(layout);
      contentPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
      layout.setHorizontalGroup(layout.createParallelGroup()
    		  .addComponent(getButtonsPanel())
    		  .addComponent(getVolumesPanel())
    	      .addComponent(getStatePanel())
    	      .addComponent(getNetworkPanel())
    	      .addComponent(getModesPanel())
    	      );
      layout.setVerticalGroup(layout.createSequentialGroup()
    		  .addComponent(getButtonsPanel())
    		  .addComponent(getVolumesPanel())
    	      .addComponent(getStatePanel())
    	      .addComponent(getNetworkPanel())
    	      .addComponent(getModesPanel())
    	      );
  }

  /**
   * This method initializes jPanel	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getButtonsPanel() {
    if (buttonsPanel == null) {
      jLabel = new JLabel();
      jLabel.setText(Localization.getString("MainFrame.File")); //$NON-NLS-1$
      buttonsPanel = new JPanel();
      buttonsPanel.setLayout(new BorderLayout(5, 5));
      JPanel buttonPanel = new JPanel();
      buttonPanel.setLayout(new GridLayout(1, 5, 5, 5));
      buttonPanel.add(getFileButton());
      buttonPanel.add(getMessagesButton());
      buttonPanel.add(getSettingsButton());
      buttonPanel.add(getInfoButton());
      JPanel northPanel = new JPanel();
      northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.LINE_AXIS));
      northPanel.add(buttonPanel);
      northPanel.add(Box.createHorizontalStrut(15));
      JPanel buttonPanel2 = new JPanel();
      buttonPanel2.setLayout(new GridLayout(1, 4, 5, 5));
      buttonPanel2.add(getStopAllButton());
      buttonPanel2.add(getPreviousButton());
      buttonPanel2.add(getNextTrackButton());
      buttonPanel2.add(getRepeatButton());
      northPanel.add(buttonPanel2);
      JPanel inner = new JPanel();
      inner.setLayout(new BorderLayout());
      inner.add(northPanel, BorderLayout.NORTH);
      buttonsPanel.add(inner, BorderLayout.WEST);
      buttonsPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
    }
    return buttonsPanel;
  }
  
  private JPanel volumesPanel;
  private JSlider mainVolumeSlider, musicVolumeSlider, soundVolumeSlider;
  
  private JPanel getVolumesPanel() {
	  if (volumesPanel == null) {
		  JPanel inner = new JPanel(new SpringLayout());
		  inner.add(new JLabel(Localization.getString("MainFrame.Overall"))); //$NON-NLS-1$
		  inner.add(getMainVolumeSlider());
		  inner.add(new JLabel(Localization.getString("MainFrame.Music"))); //$NON-NLS-1$
		  inner.add(getMusicVolumeSlider());
		  inner.add(new JLabel(Localization.getString("MainFrame.Sounds"))); //$NON-NLS-1$
		  inner.add(getSoundVolumeSlider());
		  ares.controller.gui.util.SpringUtilities.makeCompactGrid(inner, 3, 2, 5, 5, 5, 5);
		  inner.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), 
				  Localization.getString("MainControlsFrame.Volume"), //$NON-NLS-1$
				  TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
		  volumesPanel = new JPanel(new BorderLayout(5, 5));
		  volumesPanel.add(inner, BorderLayout.NORTH);
	  }
	  return volumesPanel;
  }
  
  enum VolumeType { Sounds, Music, Both }
  
  private JSlider getMainVolumeSlider() {
	  if (mainVolumeSlider == null) {
		  mainVolumeSlider = getSlider(VolumeType.Both);
	  }
	  return mainVolumeSlider;
  }
  
  private JSlider getMusicVolumeSlider() {
	  if (musicVolumeSlider == null) {
		  musicVolumeSlider = getSlider(VolumeType.Music);
	  }
	  return musicVolumeSlider;
  }
  
  private JSlider getSoundVolumeSlider() {
	  if (soundVolumeSlider == null) {
		  soundVolumeSlider = getSlider(VolumeType.Sounds);
	  }
	  return soundVolumeSlider;
  }
  
  private boolean listenForVolume = true;
  
  private JSlider getSlider(final VolumeType volType) {
	  JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
	  slider.setMajorTickSpacing(10);
	  slider.setPaintLabels(false);
	  slider.setPaintTicks(false);
	  slider.addChangeListener(new ChangeListener() {

		public void stateChanged(ChangeEvent arg0) {
			if (!listenForVolume) return;
			Control.getInstance().setVolume(volType.ordinal(), ((JSlider)arg0.getSource()).getValue());
		}
	  });
	  return slider;
  }

  private JLabel projectLabel, modeLabel, elementsLabel, musicLabel, elementsDescLabel;
  
  private JPanel getStatePanel() {
	if (statePanel == null) {
	  JPanel inner = new JPanel(new SpringLayout());
	  modeLabel = new JLabel();
	  elementsLabel = new JLabel();
	  musicLabel = new JLabel();
	  projectLabel = new JLabel();
	  projectLabel.setText(Localization.getString("MainFrame.NoProjectOpened"));	    	 //$NON-NLS-1$
	  inner.add(new JLabel(Localization.getString("MainFrame.Configuration"))); //$NON-NLS-1$
	  inner.add(projectLabel);
	  inner.add(new JLabel(Localization.getString("MainFrame.Mode"))); //$NON-NLS-1$
	  inner.add(modeLabel);
	  elementsDescLabel = new JLabel(Localization.getString("MainFrame.Elements")); //$NON-NLS-1$ 
	  inner.add(elementsDescLabel);
	  inner.add(elementsLabel);
	  inner.add(new JLabel(Localization.getString("MainFrame.Music"))); //$NON-NLS-1$
	  inner.add(musicLabel);
	  ares.controller.gui.util.SpringUtilities.makeCompactGrid(inner, 4, 2, 5, 5, 5, 5);
	  inner.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), Localization.getString("MainFrame.Status"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$
	  statePanel = new JPanel(new BorderLayout(5, 5));
	  statePanel.add(inner, BorderLayout.NORTH);
	}
	return statePanel;
  }

  /**
   * This method initializes fileButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getFileButton() {
    if (fileButton == null) {
      fileButton = new JButton();
      fileButton.setIcon(new ImageIcon(getClass().getResource("openHS.png"))); //$NON-NLS-1$
      fileButton.setToolTipText(Localization.getString("MainFrame.Select")); //$NON-NLS-1$
      fileButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          selectFile();
        }
      });
      fileButton.setEnabled(Control.getInstance().isConnected());
    }
    return fileButton;
  }
  
  private void selectFile() {
	if (isLocalPlayer) {
	    JFileChooser chooser = new JFileChooser();
	    chooser.setAcceptAllFileFilterUsed(true);
	    chooser.setFileFilter(new ExampleFileFilter(new String[] {"ares", "apkg"}, Localization.getString("MainFrame.ConfigFiles"))); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	    chooser.setCurrentDirectory(Directories.getLastUsedDirectory(this, "ConfigurationFiles")); //$NON-NLS-1$
	    chooser.setMultiSelectionEnabled(false);
	    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
	      File file = chooser.getSelectedFile();
	      Directories.setLastUsedDirectory(this, "ConfigurationFiles", file); //$NON-NLS-1$
	   	  openFile(file.getAbsolutePath());
	    }
	}
	else {
		Control.getInstance().requestProjectFiles();
	}
  }
  
  public void selectFileFromRemotePlayer(List<String> files) {
	  if (files.size() == 0) {
		  JOptionPane.showMessageDialog(this, Localization.getString("MainFrame.NoAvailableProjects"), Localization.getString("MainFrame.Ares"), JOptionPane.INFORMATION_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		  return;
	  }
	  FileSelectionDialog dialog = new FileSelectionDialog(this, files);
	  dialog.setVisible(true);
	  String file = dialog.getSelectedFile();
	  if (file != null && file.length() > 0) {
		  openFile(Control.REMOTE_FILE_TAG + file);
	  }
  }

  /**
   * This method initializes jPanel1	
   * 	
   * @return javax.swing.JPanel	
   */
  private JPanel getNetworkPanel() {
    if (networkPanel == null) {
      jLabel1 = new JLabel();
      jLabel1.setText(Localization.getString("MainFrame.Server")); //$NON-NLS-1$
      networkPanel = new JPanel();
      networkPanel.setLayout(new BorderLayout());
      JPanel innerPanel = new JPanel();
      innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.LINE_AXIS));
      innerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
      innerPanel.add(jLabel1);
      innerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
      innerPanel.add(getServerBox());
      innerPanel.add(Box.createRigidArea(new Dimension(5, 0)));
      innerPanel.add(getConnectButton());
      networkPanel.add(innerPanel, BorderLayout.NORTH);
      networkPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED), Localization.getString("MainFrame.Network"), TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null)); //$NON-NLS-1$ //$NON-NLS-2$
    }
    return networkPanel;
  }
  
  /**
   * This method initializes serverBox	
   * 	
   * @return javax.swing.JComboBox	
   */
  private JComboBox getServerBox() {
    if (serverBox == null) {
      serverBox = new JComboBox();
      serverBox.addActionListener(new ActionListener() {
    	  public void actionPerformed(ActionEvent e) {
    		  updateWebControllerItem();
    	  }
      });
    }
    return serverBox;
  }
  
  private void updateWebControllerItem() {
	  if (getServerBox().getSelectedItem() == null) {
		  webControllerItem.setEnabled(false);
		  return;
	  }
      String server = getServerBox().getSelectedItem().toString();
      ServerInfo serverInfo = servers.get(server);
      webControllerItem.setEnabled(serverInfo != null && serverInfo.hasTcpServer());
  }

  /**
   * This method initializes connectButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getConnectButton() {
    if (connectButton == null) {
      connectButton = new JButton();
      connectButton.setEnabled(hasLocalPlayer);
      connectButton.setText(Localization.getString("MainFrame.Connect")); //$NON-NLS-1$
      connectButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          connectOrDisconnect();
        }
      });
    }
    return connectButton;
  }
  
  boolean isLocalPlayer = false;
  
  private void startLocalPlayer() {
		String commandLine = findLocalPlayer().getAbsolutePath();
		Preferences prefs = Preferences.userNodeForPackage(OptionsDialog.class);
		commandLine += " --NonInteractive"; //$NON-NLS-1$
		commandLine += " --UdpPort=" + prefs.getInt("UDPPort", 8009); //$NON-NLS-1$ //$NON-NLS-2$ 
		try {
    		connectWithFirstServer = true;
    		isLocalPlayer = true;
			Runtime.getRuntime().exec(commandLine, null, new File(findLocalPlayerDir()));
		} 
		catch (IOException e) {
			JOptionPane.showMessageDialog(this, Localization.getString("MainFrame.CouldNotStartPlayer") + e.getLocalizedMessage(), Localization.getString("MainFrame.Ares"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$
		}	  
  }
  
  private void maybeStartPlayer() {
	if (Control.getInstance().isConnected())
		return;
	boolean askBeforeStart = Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("AskForPlayerStart", true); //$NON-NLS-1$
  	if (!askBeforeStart || JOptionPane.showConfirmDialog(this, Localization.getString("MainFrame.AutoStartLocalPlayer"), Localization.getString("MainFrame.Ares"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
  		startLocalPlayer();
		if (askBeforeStart)
		{
			Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).putBoolean("StartLocalPlayer", true); //$NON-NLS-1$
			Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).putBoolean("AskForPlayerStart", false); //$NON-NLS-1$
		}
	}
  	else if (askBeforeStart) {
		if (askBeforeStart)
		{
			Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).putBoolean("StartLocalPlayer", false); //$NON-NLS-1$
			Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).putBoolean("AskForPlayerStart", false); //$NON-NLS-1$
		}
  	}
  }
  
  private void connectOrDisconnect() {
    if (Control.getInstance().isConnected()) {
    	Control.getInstance().disconnect(true);
    }
    else if (getServerBox().getItemCount() == 0 && hasLocalPlayer) {
    	if (JOptionPane.showConfirmDialog(this, Localization.getString("MainFrame.ManualStartLocalPlayer"), Localization.getString("MainFrame.Ares"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$ //$NON-NLS-2$
    		startLocalPlayer();
    	}
    }
    else if (getServerBox().getItemCount() > 0) {
      String server = getServerBox().getSelectedItem().toString();
      ServerInfo serverInfo = servers.get(server);
      if (serverInfo != null) {
    	if (serverInfo.hasTcpServer()) {
    		Control.getInstance().connect(serverInfo, this, isLocalPlayer);
    	}
    	else {
    		// must have a web server
    		OnlineOperations.showWebpage(this, makeWebserverUrl(serverInfo));
    	}
      }
    }
    updateNetworkState();
  }
  
  private void openWebController() {
      String server = getServerBox().getSelectedItem().toString();
      ServerInfo serverInfo = servers.get(server);
      if (serverInfo != null && serverInfo.hasWebServer()) {
    	  OnlineOperations.showWebpage(this, makeWebserverUrl(serverInfo));
      }	  
  }
  
  private String makeWebserverUrl(ServerInfo info) {
	  return "http://" + info.getAddress() + ":" + info.getWebPort() + "/"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }
  
  private void updateNetworkState() {
    if (Control.getInstance().isConnected()) {
      serverSearch.stopSearch();
      getConnectButton().setText(Localization.getString("MainFrame.Disconnect")); //$NON-NLS-1$
      getFileButton().setEnabled(true);
      openItem.setEnabled(true);
      for (JMenuItem item : lastProjectItems) {
    	  item.setEnabled(true);
      }
    }
    else {
      servers.clear();
      getServerBox().removeAllItems();
      getConnectButton().setEnabled(hasLocalPlayer);
      getConnectButton().setText(Localization.getString("MainFrame.Connect")); //$NON-NLS-1$
      updateWebControllerItem();
      serverSearch.startSearch();
      getFileButton().setEnabled(false);
      openItem.setEnabled(false);
      for (JMenuItem item : lastProjectItems) {
    	  item.setEnabled(false);
      }
    }
    updateProjectTitle();
  }

  /**
   * This method initializes messagesBox	
   * 	
   * @return javax.swing.JCheckBox	
   */
  private JToggleButton getMessagesButton() {
    if (messagesButton == null) {
      messagesButton = new JToggleButton(new ImageIcon(getClass().getResource("LegendHS.png"))); //$NON-NLS-1$
      messagesButton.setToolTipText(Localization.getString("MainFrame.Messages")); //$NON-NLS-1$
      messagesButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          if (messageFrame != null && messageFrame.isVisible()) {
            messageFrame.dispose();
            messageFrame = null;
            messagesMenuItem.setSelected(false);
          }
          else {
            messageFrame = new MessageFrame();
            messageFrame.addWindowListener(new WindowAdapter() {
              public void windowClosing(java.awt.event.WindowEvent e) {
                MainFrame.this.getMessagesButton().setSelected(false);
                messagesMenuItem.setSelected(false);
              }
            });
            messagesMenuItem.setSelected(true);
            messageFrame.setVisible(true);
          }
        }

      });
    }
    return messagesButton;
  }
  
  private MessageFrame messageFrame = null;

  /**
   * This method initializes jButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getSettingsButton() {
    if (settingsButton == null) {
      settingsButton = new JButton();
      settingsButton.setIcon(new ImageIcon(getClass().getResource("Properties.png"))); //$NON-NLS-1$
      settingsButton.setToolTipText(Localization.getString("MainFrame.Settings")); //$NON-NLS-1$
      settingsButton.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent e) {
          showSettingsDialog();
        }
      });
    }
    return settingsButton;
  }
  
  private JButton getInfoButton() {
	  if (infoButton == null) {
		  infoButton = new JButton();
		  infoButton.setIcon(new ImageIcon(getClass().getResource("Information.gif"))); //$NON-NLS-1$
		  infoButton.setToolTipText(Localization.getString("MainFrame.AboutMenu")); //$NON-NLS-1$
		  infoButton.addActionListener(new java.awt.event.ActionListener() {
			  public void actionPerformed(java.awt.event.ActionEvent e) {
				  showAboutDialog();
			  }
		  });
	  }
	  return infoButton;
  }

  private void showAboutDialog() {
	  AboutDialog dialog = new AboutDialog(this);
	  dialog.setVisible(true);
  }
  
  private void showHelpPage() {
	  ares.controller.control.OnlineOperations.showHomepage(this);
  }

  private JButton stopAllButton = null;

  private JButton getStopAllButton() {
	if (stopAllButton == null) {
	    stopAllButton = new JButton();
	    stopAllButton.setAction(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0));
			}
	    });
	    stopAllButton.setIcon(new ImageIcon(getClass().getResource("StopSmall.png"))); //$NON-NLS-1$
	    stopAllButton.setToolTipText(Localization.getString("MainControlsFrame.StopAll")); //$NON-NLS-1$
	}
	return stopAllButton;
  }
  
  private JButton nextTrackButton = null;
  private JButton previousButton = null;
  private JButton repeatButton = null;

  private JButton getNextTrackButton() {
	  if (nextTrackButton == null) {
		  nextTrackButton = new JButton();
		  nextTrackButton.setAction(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
				}
		    });
		  nextTrackButton.setIcon(new ImageIcon(getClass().getResource("forward.png"))); //$NON-NLS-1$
		  nextTrackButton.setToolTipText(Localization.getString("MainControlsFrame.NextTrack")); //$NON-NLS-1$
	  }
	  return nextTrackButton;
  }

  /**
   * This method initializes previousButton	
   * 	
   * @return javax.swing.JButton	
   */
  private JButton getPreviousButton() {
	  if (previousButton == null) {
		  previousButton = new JButton();
		  previousButton.setAction(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Control.getInstance().sendKey(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0));
				}
		    });
		  previousButton.setIcon(new ImageIcon(getClass().getResource("back.png"))); //$NON-NLS-1$
		  previousButton.setToolTipText(Localization.getString("MainControlsFrame.PreviousTrack")); //$NON-NLS-1$
	  }
	  return previousButton;
  }
  
  private JButton getRepeatButton() {
	  if (repeatButton == null) {
		  repeatButton = new JButton();
		  repeatButton.setAction(new AbstractAction() {
				@Override
				public void actionPerformed(ActionEvent e) {
					toggleRepeat();
				}
		    });
		  repeatButton.setIcon(new ImageIcon(getClass().getResource("RepeatHS.png"))); //$NON-NLS-1$
		  repeatButton.setToolTipText(Localization.getString("MainFrame.repeatMusic")); //$NON-NLS-1$
	  }
	  return repeatButton;
  }

  

  /* (non-Javadoc)
   * @see ares.controller.messages.IMessageListener#messageAdded(ares.controller.messages.Message)
   */
  @Override
  public void messageAdded(Message message) {
    if (message.getType().ordinal() <= Message.MessageType.Warning.ordinal()) {
      int messageLevel = Preferences.userNodeForPackage(MainFrame.class).getInt("MessageLevel", Message.MessageType.Warning.ordinal()); //$NON-NLS-1$
      if (message.getType().ordinal() <= messageLevel) {
        if (!getMessagesButton().isSelected()) {
          getMessagesButton().doClick();
        }
      }
    }
  }

  /* (non-Javadoc)
   * @see ares.controller.network.IServerListener#serverFound(ares.controller.network.ServerInfo)
   */
  @Override
  public void serverFound(ServerInfo server) {
    if (servers.containsKey(server.getName())) return;
    if (!server.hasTcpServer() && !server.hasWebServer()) return;
    Messages.addMessage(MessageType.Info, Localization.getString("MainFrame.ServerFound") + server.getName() + Localization.getString("MainFrame.33")); //$NON-NLS-1$ //$NON-NLS-2$
    getServerBox().addItem(server.getName());
    getConnectButton().setEnabled(true);
    servers.put(server.getName(), server);
    updateWebControllerItem();
    if (connectWithFirstServer) {
    	connectOrDisconnect();
    	connectWithFirstServer = false;
    }
  }
  
  private Map<String, ServerInfo> servers = new HashMap<String, ServerInfo>();
  
  private List<String> modeElements = new ArrayList<String>();
  
  private void updateModeElements()
  {
	  String text = new String(); 
	  for (int i = 0; i < modeElements.size(); ++i) {
		  text += modeElements.get(i);
		  if (i != modeElements.size() - 1) {
			  text += ", "; //$NON-NLS-1$
		  }
	  }
	  
	String fit = compact(text, elementsLabel, getStatePanel().getWidth() - 25 - elementsDescLabel.getWidth());
	elementsLabel.setText(fit);
	if (!fit.equals(text))
	{
		elementsLabel.setToolTipText(text);
	}
	else
	{
		elementsLabel.setToolTipText(""); //$NON-NLS-1$
	}
  }

	@Override
	public void modeChanged(final String newMode) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				modeLabel.setText(newMode);
				if (!globalHookActive)
				{
					FrameManagement.getInstance().activateFrame(newMode);
				}
			}
		});
	}
	
	@Override
	public void modeElementStarted(final int element) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CommandButtonMapping.getInstance().commandStateChanged(element, true);
				String title = Control.getInstance().getConfiguration().getCommandTitle(element);
				if (title != null) {
					modeElements.add(title);
					updateModeElements();
				}
			}
		});
	}
	
	@Override
	public void modeElementStopped(final int element) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CommandButtonMapping.getInstance().commandStateChanged(element, false);
				String title = Control.getInstance().getConfiguration().getCommandTitle(element);
				if (title != null) {
					modeElements.remove(title);
					updateModeElements();
				}
			}
		});
	}
	
	@Override
	public void allModeElementsStopped() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				CommandButtonMapping.getInstance().allCommandsInactive();
				modeElements.clear();
				updateModeElements();
			}
		});
	}
	
	@Override
	public void volumeChanged(final int index, final int value) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				listenForVolume = false;
				switch (index) {
				case 2:
					getMainVolumeSlider().setValue(value);
					break;
				case 1:
					getMusicVolumeSlider().setValue(value);
					break;
				case 0:
					getSoundVolumeSlider().setValue(value);
					break;
				default:
					break;
				}
				listenForVolume = true;
			}
		});
	}
	
	private static String compact(String text, JLabel label, int labelMaxWidth)
	{
		Graphics g = label.getGraphics();
		FontMetrics fm = g.getFontMetrics();
		int width = fm.stringWidth(text);
		
		if (width <= labelMaxWidth)
			return text;

		int len = 0;
		int seg = text.length();
		String fit = ""; //$NON-NLS-1$
		
		final String ELLIPSIS = "..."; //$NON-NLS-1$

		// find the longest string that fits into
		// the control boundaries using bisection method 
		while (seg > 1)
		{
			seg -= seg / 2;

			int left = len + seg;
			int right = text.length();

			if (left > right)
				continue;

			right -= left;
			left = 0;

			// build and measure a candidate string with ellipsis
			String tst = text.substring(0, left) + 
				ELLIPSIS + text.substring(right);
			
		    int testWidth = fm.stringWidth(tst);
				
			// candidate string fits into control boundaries, 
			// try a longer string
			// stop when seg <= 1 
			if (testWidth <= labelMaxWidth)
			{
				len += seg;
				fit = tst;
			}
		}

		if (len == 0) // string can't fit into control
		{
			return ELLIPSIS;
		}
		return fit;
	}
	
	private String currentShortTitle = ""; //$NON-NLS-1$
	
	@Override
	public void musicChanged(final String newMusic, final String shortTitle) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String fit = compact(newMusic, musicLabel, getStatePanel().getWidth() - 25 - elementsDescLabel.getWidth());
				musicLabel.setText(fit);
				if (!fit.equals(newMusic))
				{
					musicLabel.setToolTipText(newMusic);
				}
				else
				{
					musicLabel.setToolTipText(""); //$NON-NLS-1$
				}
				if (musicListFrame != null) {
					musicListFrame.setActiveTitle(shortTitle);
				}
				currentShortTitle = shortTitle;
			}
		});
	}
	
	@Override
	public void musicRepeatChanged(final boolean repeat) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				getRepeatButton().setSelected(repeat);
				repeatItem.setSelected(repeat);
			}
		});
	}
	
	private boolean musicOnAllSpeakers = false;
	private int musicFadingOption = 0;
	private int musicFadingTime = 0;
	
	public void musicOnAllSpeakersChanged(final boolean onAllSpeakers) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				musicOnAllSpeakers = onAllSpeakers;
			}
		});
	}
	
	public void musicFadingChanged(final int fadingOption, final int fadingTime) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				musicFadingOption = fadingOption;
				musicFadingTime = fadingTime;
			}
		});		
	}
	
	private List<TitledElement> currentCategories = new ArrayList<TitledElement>();
	private Map<Integer, List<TitledElement>> currentTags = new HashMap<Integer, List<TitledElement>>();
	private HashSet<Integer> currentActiveTags = new HashSet<Integer>();
	
	@Override
	public void tagsChanged(final List<TitledElement> newCategories, final Map<Integer, List<TitledElement>> newTags) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				currentCategories = newCategories;
				currentTags = newTags;
				if (tagsFrame != null) {
					tagsFrame.setTags(currentCategories, currentTags);
				}
			}
		});
	}
	
	@Override
	public void activeTagsChanged(final List<Integer> newActiveTags) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				currentActiveTags.clear();
				currentActiveTags.addAll(newActiveTags);
				if (tagsFrame != null) {
					tagsFrame.setActiveTags(currentActiveTags);
				}
			}
		});
	}
	
	@Override
	public void tagSwitched(final int tagId, final boolean active) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				if (active) {
					currentActiveTags.add(tagId);
				}
				else {
					currentActiveTags.remove(tagId);
				}
				if (tagsFrame != null) {
					tagsFrame.setTagActive(tagId, active);
				}
			}
		});
	}
	
	private INetworkClient.CategoryCombination tagCategoryCombination = INetworkClient.CategoryCombination.Or;
	
	@Override
	public void tagCategoryCombinationChanged(final INetworkClient.CategoryCombination combination) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				tagCategoryCombination = combination;
				if (tagsFrame != null) {
					tagsFrame.setCategoryCombination(combination);
				}
			}
		});
	}
	
	private int musicTagsFadeTime = 0;
	private boolean musicTagsFadeOnlyOnChange = false;
	
	@Override
	public void musicTagFadingChanged(final int fadeTime, final boolean fadeOnlyOnChange)
	{
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				musicTagsFadeTime = fadeTime;
				musicTagsFadeOnlyOnChange = fadeOnlyOnChange;
				if (tagsFrame != null) {
					tagsFrame.setFading(fadeTime, fadeOnlyOnChange);
				}
			}
		});
	}
	
	public void toggleRepeat() {
		boolean isRepeat = getRepeatButton().isSelected();
		Control.getInstance().setMusicRepeat(!isRepeat);
	}
	
	private List<TitledElement> currentMusicList = null;
	
	@Override
	public void musicListChanged(final List<TitledElement> newList) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				currentMusicList = newList;
				if (musicListFrame != null) {
					musicListFrame.setTitles(newList);
				}
			}
		});
	}
	
	@Override
	public void configurationChanged(final Configuration newConfiguration, final String fileName) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame.this.projectOpened(newConfiguration, fileName);
			}
		});
	}
	
	@Override
	public void importProgressChanged(final int percent, final String additionalInfo) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String text = Localization.getString("MainFrame.Importing"); // $NON-NLS-1$
				if (percent >= 0 && percent <= 100) {
					text += " (" + percent + "%)"; // $NON-NLS-1$ $NON-NLS-2$
				}
				projectLabel.setText(text);
			}
		});
	}
	
	@Override
	public void projectFilesRetrieved(final List<String> files) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MainFrame.this.selectFileFromRemotePlayer(files);
			}
		});
	}
	
	@Override
	public void disconnect() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Control.getInstance().disconnect(false);
				updateNetworkState();
			}
		});
	}
  
	
	public void connectionFailed() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Control.getInstance().disconnect(false);
				updateNetworkState();
			}
		});
	}

	private static class SelectModeAction extends AbstractAction {

		private String title;

		public SelectModeAction(Command command) {
			this.title = command.getTitle();
		}

		public void actionPerformed(ActionEvent arg0) {
			if (ModeFrames.getInstance().isFrameOpen(title)) {
				ModeFrames.getInstance().closeFrame(title);
			}
			else {
				ModeFrames.getInstance().openFrame(title);
			}
		}
	}

	public AbstractAction createAction(Command command, AbstractButton button) {
		addButton(command.getTitle(), button);
		button.setSelected(ModeFrames.getInstance().isFrameOpen(command.getTitle()));
		return new SelectModeAction(command);
	}

	private void showSettingsDialog() {
		int oldPort = Preferences.userNodeForPackage(MainFrame.class).getInt("UDPPort", 8009);  //$NON-NLS-1$
		boolean oldKeys = Preferences.userNodeForPackage(OptionsDialog.class).getBoolean("ShowKeys", false); //$NON-NLS-1$
		boolean oldManual = !Preferences.userNodeForPackage(OptionsDialog.class).getBoolean("AutoDetectPlayer", true); //$NON-NLS-1$
		String oldAddress = Preferences.userNodeForPackage(OptionsDialog.class).get("IPAddress", "127.0.0.1"); //$NON-NLS-1$ //$NON-NLS-2$
		int oldTcpPort = Preferences.userNodeForPackage(MainFrame.class).getInt("TCPPort", 11112);  //$NON-NLS-1$
		OptionsDialog dialog = new OptionsDialog(MainFrame.this);
		dialog.setMusicOptions(musicOnAllSpeakers, musicFadingOption, musicFadingTime);
		dialog.setLocationRelativeTo(this);
		dialog.setModal(true);
		dialog.setVisible(true);
		int newPort = Preferences.userNodeForPackage(MainFrame.class).getInt("UDPPort", 8009);  //$NON-NLS-1$
		boolean newKeys = Preferences.userNodeForPackage(OptionsDialog.class).getBoolean("ShowKeys", false); //$NON-NLS-1$
		boolean newManual = !Preferences.userNodeForPackage(OptionsDialog.class).getBoolean("AutoDetectPlayer", true);  //$NON-NLS-1$
		String newAddress = Preferences.userNodeForPackage(OptionsDialog.class).get("IPAddress", "127.0.0.1"); //$NON-NLS-1$ //$NON-NLS-2$
		int newTcpPort = Preferences.userNodeForPackage(MainFrame.class).getInt("TCPPort", 11112);  //$NON-NLS-1$
		
		if (serverSearch != null && (oldPort != newPort) || oldManual != newManual) {
			boolean wasSearching = serverSearch.isSearching();
			if (wasSearching) {
				serverSearch.stopSearch();
			}
			serverSearch.dispose();
			serverSearch = new ServerSearch(MainFrame.this, newPort);
			if (wasSearching && !newManual) {
				serverSearch.startSearch();
			}
		}
		boolean reconnect = false;
		if (oldManual != newManual)
			reconnect = true;
		else if (newManual && (!oldAddress.equals(newAddress)) || oldTcpPort != newTcpPort)
			reconnect = true;
		if (reconnect) {
			servers.clear();
			serverBox.removeAllItems();
			if (Control.getInstance().isConnected()) {
				Control.getInstance().disconnect(true);
			}
			getConnectButton().setText(Localization.getString("MainFrame.Connect")); //$NON-NLS-1$
			if (newManual) {
				try {
					connectWithFirstServer = true;
					String serverName = Preferences.userNodeForPackage(OptionsDialog.class).get("ServerName", "localhost");  //$NON-NLS-1$ //$NON-NLS-2$
					serverFound(new ServerInfo(InetAddress.getByName(newAddress), true, newTcpPort, false, 0, serverName));
				}
				catch (java.net.UnknownHostException ex) {
					JOptionPane.showMessageDialog(MainFrame.this, Localization.getString("MainFrame.Server2") + newAddress + Localization.getString("MainFrame.NotFound"), Localization.getString("MainFrame.Ares"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				}
			}
			else {
			    hasLocalPlayer = findLocalPlayer() != null;
			  	boolean startLocalPlayer = hasLocalPlayer && Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("StartLocalPlayer", true); //$NON-NLS-1$
			  	if (startLocalPlayer) {
			  		maybeStartPlayer();
			  	}
				serverSearch.dispose();
				serverSearch = new ServerSearch(MainFrame.this, newPort);
				serverSearch.startSearch();
			}
		}
		else if (servers.isEmpty() && !newManual) {
		    hasLocalPlayer = findLocalPlayer() != null;
		  	boolean startLocalPlayer = hasLocalPlayer && Preferences.userNodeForPackage(ares.controller.gui.OptionsDialog.class).getBoolean("StartLocalPlayer", true); //$NON-NLS-1$
		  	if (startLocalPlayer) {
		  		maybeStartPlayer();
		  	}			
		}
		if (oldKeys != newKeys && Control.getInstance().getConfiguration() != null) {
			final ArrayList<String> openFrames = closeAllFrames();
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					enableProjectSpecificControls(true);
					reopenFrames(openFrames);					
				}
			});
		}
		if (dialog.wasClosedByOK()) {
			musicOnAllSpeakers = dialog.getMusicOnAllSpeakers();
			musicFadingOption = dialog.getMusicFadingOption();
			musicFadingTime = dialog.getMusicFadingTime();
			if (Control.getInstance().isConnected()) {
				Control.getInstance().setMusicOnAllSpeakers(musicOnAllSpeakers);
				Control.getInstance().setMusicFading(musicFadingOption, musicFadingTime);
			}
		}
	}
	
	private List<JMenuItem> lastProjectItems = new ArrayList<JMenuItem>();
	
	private void buildLastProjectsMenu() {
		java.util.prefs.Preferences prefs = java.util.prefs.Preferences
		.userNodeForPackage(getClass());
		int nrOfLastProjects = prefs.getInt("LastUsedProjectsCount", 0); //$NON-NLS-1$
		class LastProjectsActionListener implements java.awt.event.ActionListener {
			public LastProjectsActionListener(String fileName) {
				this.fileName = fileName;
			}

			public void actionPerformed(java.awt.event.ActionEvent e) {
				openFile(fileName);
			}

			private final String fileName;
		}
		for (int i = 0; i < nrOfLastProjects; ++i) {
			JMenuItem lastProjectItem = new JMenuItem();
			lastProjectItem.setText((i + 1) + " " //$NON-NLS-1$
					+ prefs.get("LastUsedProjectName" + i, "Project " + (i + 1))); //$NON-NLS-1$ //$NON-NLS-2$
			lastProjectItem.setMnemonic(java.awt.event.KeyEvent.VK_1 + i);
			lastProjectItem.addActionListener(new LastProjectsActionListener(prefs.get(
					"LastUsedProjectFile" + i, ""))); //$NON-NLS-1$ //$NON-NLS-2$
			lastProjectItem.setEnabled(Control.getInstance().isConnected());
			lastProjectItems.add(lastProjectItem);
			recentMenu.add(lastProjectItem);
		}
	}

	private void rebuildLastProjectsMenu() {
		recentMenu.removeAll();
		lastProjectItems.clear();
		buildLastProjectsMenu();
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"
