﻿/*
 Copyright (c) 2015 [Joerg Ruedenauer]
 
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
namespace Ares.Player
{
    partial class Player
    {
        /// <summary>
        /// Erforderliche Designervariable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Verwendete Ressourcen bereinigen.
        /// </summary>
        /// <param name="disposing">True, wenn verwaltete Ressourcen gelöscht werden sollen; andernfalls False.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            if (disposing)
            {
#if !MONO
                m_Instance.Dispose();
#endif
			}
            base.Dispose(disposing);
        }

        #region Vom Windows Form-Designer generierter Code

        /// <summary>
        /// Erforderliche Methode für die Designerunterstützung.
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(Player));
            this.projectNameLabel = new System.Windows.Forms.Label();
            this.groupBox2 = new System.Windows.Forms.GroupBox();
            this.soundVolumeBar = new System.Windows.Forms.TrackBar();
            this.label4 = new System.Windows.Forms.Label();
            this.musicVolumeBar = new System.Windows.Forms.TrackBar();
            this.label3 = new System.Windows.Forms.Label();
            this.overallVolumeBar = new System.Windows.Forms.TrackBar();
            this.label2 = new System.Windows.Forms.Label();
            this.groupBox3 = new System.Windows.Forms.GroupBox();
            this.label9 = new System.Windows.Forms.Label();
            this.soundsLabel = new System.Windows.Forms.Label();
            this.label8 = new System.Windows.Forms.Label();
            this.musicLabel = new System.Windows.Forms.Label();
            this.label7 = new System.Windows.Forms.Label();
            this.elementsLabel = new System.Windows.Forms.Label();
            this.label6 = new System.Windows.Forms.Label();
            this.modeLabel = new System.Windows.Forms.Label();
            this.label5 = new System.Windows.Forms.Label();
            this.openFileDialog1 = new System.Windows.Forms.OpenFileDialog();
            this.fileSystemWatcher1 = new System.IO.FileSystemWatcher();
            this.toolStrip1 = new System.Windows.Forms.ToolStrip();
            this.openButton = new System.Windows.Forms.ToolStripButton();
            this.messagesButton = new System.Windows.Forms.ToolStripButton();
            this.editorButton = new System.Windows.Forms.ToolStripButton();
            this.settingsButton = new System.Windows.Forms.ToolStripButton();
            this.aboutButton = new System.Windows.Forms.ToolStripButton();
            this.toolStripSeparator1 = new System.Windows.Forms.ToolStripSeparator();
            this.stopButton = new System.Windows.Forms.ToolStripButton();
            this.previousButton = new System.Windows.Forms.ToolStripButton();
            this.nextButton = new System.Windows.Forms.ToolStripButton();
            this.repeatButton = new System.Windows.Forms.ToolStripButton();
            this.broadCastTimer = new System.Windows.Forms.Timer(this.components);
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.controllerPortLabel = new System.Windows.Forms.Label();
            this.webAddressLabel = new System.Windows.Forms.LinkLabel();
            this.label12 = new System.Windows.Forms.Label();
            this.label10 = new System.Windows.Forms.Label();
            this.networkSettingsButton = new System.Windows.Forms.Button();
            this.clientStateLabel = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            this.menuStrip1 = new System.Windows.Forms.MenuStrip();
            this.projectToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.openToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.importToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.recentToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.dummyToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator2 = new System.Windows.Forms.ToolStripSeparator();
            this.exitToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.playToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.stopAllToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.previousMusicToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.nextMusicTitleToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.repeatCurrentMusicToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.extrasToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.startEditorToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.messagesToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator3 = new System.Windows.Forms.ToolStripSeparator();
            this.showKeysMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.globalKeyHookItem = new System.Windows.Forms.ToolStripMenuItem();
            this.settingsToolStripMenuItem1 = new System.Windows.Forms.ToolStripMenuItem();
            this.helpToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.helpOnlineToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.checkForUpdateToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.toolStripSeparator4 = new System.Windows.Forms.ToolStripSeparator();
            this.aboutToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.notifyIcon = new System.Windows.Forms.NotifyIcon(this.components);
            this.importFileDialog = new System.Windows.Forms.OpenFileDialog();
            this.saveFileDialog = new System.Windows.Forms.SaveFileDialog();
            this.groupBox4 = new System.Windows.Forms.GroupBox();
            this.modesList = new System.Windows.Forms.ListBox();
            this.groupBox5 = new System.Windows.Forms.GroupBox();
            this.tagsPanel = new System.Windows.Forms.Panel();
            this.groupBox7 = new System.Windows.Forms.GroupBox();
            this.clearTagsButton = new System.Windows.Forms.Button();
            this.tagSelectionPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.currentTagsLabel = new System.Windows.Forms.Label();
            this.label15 = new System.Windows.Forms.Label();
            this.groupBox6 = new System.Windows.Forms.GroupBox();
            this.tagCategoryCombinationBox = new System.Windows.Forms.ComboBox();
            this.fadeOnlyOnChangeBox = new System.Windows.Forms.CheckBox();
            this.label17 = new System.Windows.Forms.Label();
            this.tagFadeUpDown = new System.Windows.Forms.NumericUpDown();
            this.label16 = new System.Windows.Forms.Label();
            this.label14 = new System.Windows.Forms.Label();
            this.label13 = new System.Windows.Forms.Label();
            this.musicTagCategoryBox = new System.Windows.Forms.ComboBox();
            this.elementsPanel = new System.Windows.Forms.FlowLayoutPanel();
            this.musicList = new System.Windows.Forms.ListBox();
            this.groupBox2.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.soundVolumeBar)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.musicVolumeBar)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.overallVolumeBar)).BeginInit();
            this.groupBox3.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.fileSystemWatcher1)).BeginInit();
            this.toolStrip1.SuspendLayout();
            this.groupBox1.SuspendLayout();
            this.menuStrip1.SuspendLayout();
            this.groupBox4.SuspendLayout();
            this.groupBox5.SuspendLayout();
            this.tagsPanel.SuspendLayout();
            this.groupBox7.SuspendLayout();
            this.groupBox6.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.tagFadeUpDown)).BeginInit();
            this.SuspendLayout();
            // 
            // projectNameLabel
            // 
            resources.ApplyResources(this.projectNameLabel, "projectNameLabel");
            this.projectNameLabel.Name = "projectNameLabel";
            this.projectNameLabel.UseCompatibleTextRendering = true;
            // 
            // groupBox2
            // 
            resources.ApplyResources(this.groupBox2, "groupBox2");
            this.groupBox2.Controls.Add(this.soundVolumeBar);
            this.groupBox2.Controls.Add(this.label4);
            this.groupBox2.Controls.Add(this.musicVolumeBar);
            this.groupBox2.Controls.Add(this.label3);
            this.groupBox2.Controls.Add(this.overallVolumeBar);
            this.groupBox2.Controls.Add(this.label2);
            this.groupBox2.Name = "groupBox2";
            this.groupBox2.TabStop = false;
            this.groupBox2.UseCompatibleTextRendering = true;
            // 
            // soundVolumeBar
            // 
            resources.ApplyResources(this.soundVolumeBar, "soundVolumeBar");
            this.soundVolumeBar.Maximum = 100;
            this.soundVolumeBar.Name = "soundVolumeBar";
            this.soundVolumeBar.SmallChange = 5;
            this.soundVolumeBar.TickFrequency = 5;
            this.soundVolumeBar.TickStyle = System.Windows.Forms.TickStyle.None;
            this.soundVolumeBar.Value = 100;
            this.soundVolumeBar.Scroll += new System.EventHandler(this.soundVolumeBar_Scroll);
            // 
            // label4
            // 
            resources.ApplyResources(this.label4, "label4");
            this.label4.Name = "label4";
            this.label4.UseCompatibleTextRendering = true;
            // 
            // musicVolumeBar
            // 
            resources.ApplyResources(this.musicVolumeBar, "musicVolumeBar");
            this.musicVolumeBar.Maximum = 100;
            this.musicVolumeBar.Name = "musicVolumeBar";
            this.musicVolumeBar.SmallChange = 5;
            this.musicVolumeBar.TickFrequency = 5;
            this.musicVolumeBar.TickStyle = System.Windows.Forms.TickStyle.None;
            this.musicVolumeBar.Value = 100;
            this.musicVolumeBar.Scroll += new System.EventHandler(this.musicVolumeBar_Scroll);
            // 
            // label3
            // 
            resources.ApplyResources(this.label3, "label3");
            this.label3.Name = "label3";
            this.label3.UseCompatibleTextRendering = true;
            // 
            // overallVolumeBar
            // 
            resources.ApplyResources(this.overallVolumeBar, "overallVolumeBar");
            this.overallVolumeBar.Maximum = 100;
            this.overallVolumeBar.Name = "overallVolumeBar";
            this.overallVolumeBar.SmallChange = 5;
            this.overallVolumeBar.TickFrequency = 5;
            this.overallVolumeBar.TickStyle = System.Windows.Forms.TickStyle.None;
            this.overallVolumeBar.Value = 100;
            this.overallVolumeBar.Scroll += new System.EventHandler(this.overallVolumeBar_Scroll);
            // 
            // label2
            // 
            resources.ApplyResources(this.label2, "label2");
            this.label2.Name = "label2";
            this.label2.UseCompatibleTextRendering = true;
            // 
            // groupBox3
            // 
            resources.ApplyResources(this.groupBox3, "groupBox3");
            this.groupBox3.Controls.Add(this.label9);
            this.groupBox3.Controls.Add(this.soundsLabel);
            this.groupBox3.Controls.Add(this.label8);
            this.groupBox3.Controls.Add(this.musicLabel);
            this.groupBox3.Controls.Add(this.label7);
            this.groupBox3.Controls.Add(this.projectNameLabel);
            this.groupBox3.Controls.Add(this.elementsLabel);
            this.groupBox3.Controls.Add(this.label6);
            this.groupBox3.Controls.Add(this.modeLabel);
            this.groupBox3.Controls.Add(this.label5);
            this.groupBox3.Name = "groupBox3";
            this.groupBox3.TabStop = false;
            this.groupBox3.UseCompatibleTextRendering = true;
            // 
            // label9
            // 
            resources.ApplyResources(this.label9, "label9");
            this.label9.Name = "label9";
            this.label9.UseCompatibleTextRendering = true;
            // 
            // soundsLabel
            // 
            resources.ApplyResources(this.soundsLabel, "soundsLabel");
            this.soundsLabel.Name = "soundsLabel";
            this.soundsLabel.UseCompatibleTextRendering = true;
            // 
            // label8
            // 
            resources.ApplyResources(this.label8, "label8");
            this.label8.Name = "label8";
            this.label8.UseCompatibleTextRendering = true;
            // 
            // musicLabel
            // 
            resources.ApplyResources(this.musicLabel, "musicLabel");
            this.musicLabel.Name = "musicLabel";
            this.musicLabel.UseCompatibleTextRendering = true;
            // 
            // label7
            // 
            resources.ApplyResources(this.label7, "label7");
            this.label7.Name = "label7";
            this.label7.UseCompatibleTextRendering = true;
            // 
            // elementsLabel
            // 
            resources.ApplyResources(this.elementsLabel, "elementsLabel");
            this.elementsLabel.Name = "elementsLabel";
            this.elementsLabel.UseCompatibleTextRendering = true;
            // 
            // label6
            // 
            resources.ApplyResources(this.label6, "label6");
            this.label6.Name = "label6";
            this.label6.UseCompatibleTextRendering = true;
            // 
            // modeLabel
            // 
            resources.ApplyResources(this.modeLabel, "modeLabel");
            this.modeLabel.Name = "modeLabel";
            this.modeLabel.UseCompatibleTextRendering = true;
            // 
            // label5
            // 
            resources.ApplyResources(this.label5, "label5");
            this.label5.Name = "label5";
            this.label5.UseCompatibleTextRendering = true;
            // 
            // openFileDialog1
            // 
            this.openFileDialog1.DefaultExt = "ares";
            resources.ApplyResources(this.openFileDialog1, "openFileDialog1");
            // 
            // fileSystemWatcher1
            // 
            this.fileSystemWatcher1.EnableRaisingEvents = true;
            this.fileSystemWatcher1.Filter = "*.ares";
            this.fileSystemWatcher1.NotifyFilter = System.IO.NotifyFilters.LastWrite;
            this.fileSystemWatcher1.SynchronizingObject = this;
            // 
            // toolStrip1
            // 
            resources.ApplyResources(this.toolStrip1, "toolStrip1");
            this.toolStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openButton,
            this.messagesButton,
            this.editorButton,
            this.settingsButton,
            this.aboutButton,
            this.toolStripSeparator1,
            this.stopButton,
            this.previousButton,
            this.nextButton,
            this.repeatButton});
            this.toolStrip1.Name = "toolStrip1";
            // 
            // openButton
            // 
            resources.ApplyResources(this.openButton, "openButton");
            this.openButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.openButton.Name = "openButton";
            this.openButton.Click += new System.EventHandler(this.openButton_Click);
            // 
            // messagesButton
            // 
            resources.ApplyResources(this.messagesButton, "messagesButton");
            this.messagesButton.CheckOnClick = true;
            this.messagesButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.messagesButton.Name = "messagesButton";
            this.messagesButton.CheckStateChanged += new System.EventHandler(this.messagesButton_CheckedChanged);
            // 
            // editorButton
            // 
            resources.ApplyResources(this.editorButton, "editorButton");
            this.editorButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.editorButton.Name = "editorButton";
            this.editorButton.Click += new System.EventHandler(this.editorButton_Click);
            // 
            // settingsButton
            // 
            resources.ApplyResources(this.settingsButton, "settingsButton");
            this.settingsButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.settingsButton.Name = "settingsButton";
            this.settingsButton.Click += new System.EventHandler(this.settingsButton_Click);
            // 
            // aboutButton
            // 
            resources.ApplyResources(this.aboutButton, "aboutButton");
            this.aboutButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.aboutButton.Name = "aboutButton";
            this.aboutButton.Click += new System.EventHandler(this.aboutButton_Click);
            // 
            // toolStripSeparator1
            // 
            resources.ApplyResources(this.toolStripSeparator1, "toolStripSeparator1");
            this.toolStripSeparator1.Name = "toolStripSeparator1";
            // 
            // stopButton
            // 
            resources.ApplyResources(this.stopButton, "stopButton");
            this.stopButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.stopButton.Name = "stopButton";
            this.stopButton.Click += new System.EventHandler(this.stopButton_Click);
            // 
            // previousButton
            // 
            resources.ApplyResources(this.previousButton, "previousButton");
            this.previousButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.previousButton.Name = "previousButton";
            this.previousButton.Click += new System.EventHandler(this.previousButton_Click);
            // 
            // nextButton
            // 
            resources.ApplyResources(this.nextButton, "nextButton");
            this.nextButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.nextButton.Name = "nextButton";
            this.nextButton.Click += new System.EventHandler(this.nextButton_Click);
            // 
            // repeatButton
            // 
            resources.ApplyResources(this.repeatButton, "repeatButton");
            this.repeatButton.DisplayStyle = System.Windows.Forms.ToolStripItemDisplayStyle.Image;
            this.repeatButton.Name = "repeatButton";
            this.repeatButton.Click += new System.EventHandler(this.repeatButton_Click);
            // 
            // groupBox1
            // 
            resources.ApplyResources(this.groupBox1, "groupBox1");
            this.groupBox1.Controls.Add(this.controllerPortLabel);
            this.groupBox1.Controls.Add(this.webAddressLabel);
            this.groupBox1.Controls.Add(this.label12);
            this.groupBox1.Controls.Add(this.label10);
            this.groupBox1.Controls.Add(this.networkSettingsButton);
            this.groupBox1.Controls.Add(this.clientStateLabel);
            this.groupBox1.Controls.Add(this.label1);
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.TabStop = false;
            this.groupBox1.UseCompatibleTextRendering = true;
            // 
            // controllerPortLabel
            // 
            resources.ApplyResources(this.controllerPortLabel, "controllerPortLabel");
            this.controllerPortLabel.Name = "controllerPortLabel";
            // 
            // webAddressLabel
            // 
            resources.ApplyResources(this.webAddressLabel, "webAddressLabel");
            this.webAddressLabel.Name = "webAddressLabel";
            this.webAddressLabel.TabStop = true;
            this.webAddressLabel.LinkClicked += new System.Windows.Forms.LinkLabelLinkClickedEventHandler(this.webAddressLabel_LinkClicked);
            // 
            // label12
            // 
            resources.ApplyResources(this.label12, "label12");
            this.label12.Name = "label12";
            this.label12.UseCompatibleTextRendering = true;
            // 
            // label10
            // 
            resources.ApplyResources(this.label10, "label10");
            this.label10.Name = "label10";
            this.label10.UseCompatibleTextRendering = true;
            // 
            // networkSettingsButton
            // 
            resources.ApplyResources(this.networkSettingsButton, "networkSettingsButton");
            this.networkSettingsButton.Name = "networkSettingsButton";
            this.networkSettingsButton.UseCompatibleTextRendering = true;
            this.networkSettingsButton.UseVisualStyleBackColor = true;
            this.networkSettingsButton.Click += new System.EventHandler(this.networkSettingsButton_Click);
            // 
            // clientStateLabel
            // 
            resources.ApplyResources(this.clientStateLabel, "clientStateLabel");
            this.clientStateLabel.Name = "clientStateLabel";
            this.clientStateLabel.UseCompatibleTextRendering = true;
            // 
            // label1
            // 
            resources.ApplyResources(this.label1, "label1");
            this.label1.Name = "label1";
            this.label1.UseCompatibleTextRendering = true;
            // 
            // menuStrip1
            // 
            resources.ApplyResources(this.menuStrip1, "menuStrip1");
            this.menuStrip1.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.projectToolStripMenuItem,
            this.playToolStripMenuItem,
            this.extrasToolStripMenuItem,
            this.helpToolStripMenuItem});
            this.menuStrip1.Name = "menuStrip1";
            // 
            // projectToolStripMenuItem
            // 
            resources.ApplyResources(this.projectToolStripMenuItem, "projectToolStripMenuItem");
            this.projectToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.openToolStripMenuItem,
            this.importToolStripMenuItem,
            this.recentToolStripMenuItem,
            this.toolStripSeparator2,
            this.exitToolStripMenuItem});
            this.projectToolStripMenuItem.Name = "projectToolStripMenuItem";
            // 
            // openToolStripMenuItem
            // 
            resources.ApplyResources(this.openToolStripMenuItem, "openToolStripMenuItem");
            this.openToolStripMenuItem.Name = "openToolStripMenuItem";
            this.openToolStripMenuItem.Click += new System.EventHandler(this.openButton_Click);
            // 
            // importToolStripMenuItem
            // 
            resources.ApplyResources(this.importToolStripMenuItem, "importToolStripMenuItem");
            this.importToolStripMenuItem.Name = "importToolStripMenuItem";
            this.importToolStripMenuItem.Click += new System.EventHandler(this.importToolStripMenuItem_Click);
            // 
            // recentToolStripMenuItem
            // 
            resources.ApplyResources(this.recentToolStripMenuItem, "recentToolStripMenuItem");
            this.recentToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.dummyToolStripMenuItem});
            this.recentToolStripMenuItem.Name = "recentToolStripMenuItem";
            this.recentToolStripMenuItem.DropDownOpening += new System.EventHandler(this.recentToolStripMenuItem_DropDownOpening);
            // 
            // dummyToolStripMenuItem
            // 
            resources.ApplyResources(this.dummyToolStripMenuItem, "dummyToolStripMenuItem");
            this.dummyToolStripMenuItem.Name = "dummyToolStripMenuItem";
            // 
            // toolStripSeparator2
            // 
            resources.ApplyResources(this.toolStripSeparator2, "toolStripSeparator2");
            this.toolStripSeparator2.Name = "toolStripSeparator2";
            // 
            // exitToolStripMenuItem
            // 
            resources.ApplyResources(this.exitToolStripMenuItem, "exitToolStripMenuItem");
            this.exitToolStripMenuItem.Name = "exitToolStripMenuItem";
            this.exitToolStripMenuItem.Click += new System.EventHandler(this.exitToolStripMenuItem_Click);
            // 
            // playToolStripMenuItem
            // 
            resources.ApplyResources(this.playToolStripMenuItem, "playToolStripMenuItem");
            this.playToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.stopAllToolStripMenuItem,
            this.previousMusicToolStripMenuItem,
            this.nextMusicTitleToolStripMenuItem,
            this.repeatCurrentMusicToolStripMenuItem});
            this.playToolStripMenuItem.Name = "playToolStripMenuItem";
            // 
            // stopAllToolStripMenuItem
            // 
            resources.ApplyResources(this.stopAllToolStripMenuItem, "stopAllToolStripMenuItem");
            this.stopAllToolStripMenuItem.Name = "stopAllToolStripMenuItem";
            this.stopAllToolStripMenuItem.Click += new System.EventHandler(this.stopButton_Click);
            // 
            // previousMusicToolStripMenuItem
            // 
            resources.ApplyResources(this.previousMusicToolStripMenuItem, "previousMusicToolStripMenuItem");
            this.previousMusicToolStripMenuItem.Name = "previousMusicToolStripMenuItem";
            this.previousMusicToolStripMenuItem.Click += new System.EventHandler(this.previousButton_Click);
            // 
            // nextMusicTitleToolStripMenuItem
            // 
            resources.ApplyResources(this.nextMusicTitleToolStripMenuItem, "nextMusicTitleToolStripMenuItem");
            this.nextMusicTitleToolStripMenuItem.Image = global::Ares.Player.Properties.Resources.forward;
            this.nextMusicTitleToolStripMenuItem.Name = "nextMusicTitleToolStripMenuItem";
            this.nextMusicTitleToolStripMenuItem.Click += new System.EventHandler(this.nextButton_Click);
            // 
            // repeatCurrentMusicToolStripMenuItem
            // 
            resources.ApplyResources(this.repeatCurrentMusicToolStripMenuItem, "repeatCurrentMusicToolStripMenuItem");
            this.repeatCurrentMusicToolStripMenuItem.Name = "repeatCurrentMusicToolStripMenuItem";
            this.repeatCurrentMusicToolStripMenuItem.Click += new System.EventHandler(this.repeatButton_Click);
            // 
            // extrasToolStripMenuItem
            // 
            resources.ApplyResources(this.extrasToolStripMenuItem, "extrasToolStripMenuItem");
            this.extrasToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.startEditorToolStripMenuItem,
            this.messagesToolStripMenuItem,
            this.toolStripSeparator3,
            this.showKeysMenuItem,
            this.globalKeyHookItem,
            this.settingsToolStripMenuItem1});
            this.extrasToolStripMenuItem.Name = "extrasToolStripMenuItem";
            this.extrasToolStripMenuItem.DropDownOpening += new System.EventHandler(this.extrasToolStripMenuItem_DropDownOpening);
            // 
            // startEditorToolStripMenuItem
            // 
            resources.ApplyResources(this.startEditorToolStripMenuItem, "startEditorToolStripMenuItem");
            this.startEditorToolStripMenuItem.Name = "startEditorToolStripMenuItem";
            this.startEditorToolStripMenuItem.Click += new System.EventHandler(this.editorButton_Click);
            // 
            // messagesToolStripMenuItem
            // 
            resources.ApplyResources(this.messagesToolStripMenuItem, "messagesToolStripMenuItem");
            this.messagesToolStripMenuItem.Name = "messagesToolStripMenuItem";
            this.messagesToolStripMenuItem.Click += new System.EventHandler(this.messagesToolStripMenuItem_Click);
            // 
            // toolStripSeparator3
            // 
            resources.ApplyResources(this.toolStripSeparator3, "toolStripSeparator3");
            this.toolStripSeparator3.Name = "toolStripSeparator3";
            // 
            // showKeysMenuItem
            // 
            resources.ApplyResources(this.showKeysMenuItem, "showKeysMenuItem");
            this.showKeysMenuItem.CheckOnClick = true;
            this.showKeysMenuItem.Name = "showKeysMenuItem";
            this.showKeysMenuItem.CheckedChanged += new System.EventHandler(this.showKeysMenuItem_CheckedChanged);
            // 
            // globalKeyHookItem
            // 
            resources.ApplyResources(this.globalKeyHookItem, "globalKeyHookItem");
            this.globalKeyHookItem.CheckOnClick = true;
            this.globalKeyHookItem.Name = "globalKeyHookItem";
            this.globalKeyHookItem.CheckedChanged += new System.EventHandler(this.globalKeyHookItem_CheckedChanged);
            // 
            // settingsToolStripMenuItem1
            // 
            resources.ApplyResources(this.settingsToolStripMenuItem1, "settingsToolStripMenuItem1");
            this.settingsToolStripMenuItem1.Name = "settingsToolStripMenuItem1";
            this.settingsToolStripMenuItem1.Click += new System.EventHandler(this.settingsButton_Click);
            // 
            // helpToolStripMenuItem
            // 
            resources.ApplyResources(this.helpToolStripMenuItem, "helpToolStripMenuItem");
            this.helpToolStripMenuItem.DropDownItems.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.helpOnlineToolStripMenuItem,
            this.checkForUpdateToolStripMenuItem,
            this.toolStripSeparator4,
            this.aboutToolStripMenuItem});
            this.helpToolStripMenuItem.Name = "helpToolStripMenuItem";
            // 
            // helpOnlineToolStripMenuItem
            // 
            resources.ApplyResources(this.helpOnlineToolStripMenuItem, "helpOnlineToolStripMenuItem");
            this.helpOnlineToolStripMenuItem.Name = "helpOnlineToolStripMenuItem";
            this.helpOnlineToolStripMenuItem.Click += new System.EventHandler(this.helpOnlineToolStripMenuItem_Click);
            // 
            // checkForUpdateToolStripMenuItem
            // 
            resources.ApplyResources(this.checkForUpdateToolStripMenuItem, "checkForUpdateToolStripMenuItem");
            this.checkForUpdateToolStripMenuItem.Name = "checkForUpdateToolStripMenuItem";
            this.checkForUpdateToolStripMenuItem.Click += new System.EventHandler(this.checkForUpdateToolStripMenuItem_Click);
            // 
            // toolStripSeparator4
            // 
            resources.ApplyResources(this.toolStripSeparator4, "toolStripSeparator4");
            this.toolStripSeparator4.Name = "toolStripSeparator4";
            // 
            // aboutToolStripMenuItem
            // 
            resources.ApplyResources(this.aboutToolStripMenuItem, "aboutToolStripMenuItem");
            this.aboutToolStripMenuItem.Name = "aboutToolStripMenuItem";
            this.aboutToolStripMenuItem.Click += new System.EventHandler(this.aboutButton_Click);
            // 
            // notifyIcon
            // 
            resources.ApplyResources(this.notifyIcon, "notifyIcon");
            this.notifyIcon.DoubleClick += new System.EventHandler(this.notifyIcon_DoubleClick);
            // 
            // importFileDialog
            // 
            this.importFileDialog.FileName = "apkg";
            resources.ApplyResources(this.importFileDialog, "importFileDialog");
            // 
            // saveFileDialog
            // 
            this.saveFileDialog.DefaultExt = "ares";
            resources.ApplyResources(this.saveFileDialog, "saveFileDialog");
            // 
            // groupBox4
            // 
            resources.ApplyResources(this.groupBox4, "groupBox4");
            this.groupBox4.Controls.Add(this.modesList);
            this.groupBox4.Name = "groupBox4";
            this.groupBox4.TabStop = false;
            // 
            // modesList
            // 
            resources.ApplyResources(this.modesList, "modesList");
            this.modesList.FormattingEnabled = true;
            this.modesList.Name = "modesList";
            this.modesList.SelectedIndexChanged += new System.EventHandler(this.modesList_SelectedIndexChanged);
            // 
            // groupBox5
            // 
            resources.ApplyResources(this.groupBox5, "groupBox5");
            this.groupBox5.Controls.Add(this.tagsPanel);
            this.groupBox5.Controls.Add(this.elementsPanel);
            this.groupBox5.Controls.Add(this.musicList);
            this.groupBox5.Name = "groupBox5";
            this.groupBox5.TabStop = false;
            // 
            // tagsPanel
            // 
            resources.ApplyResources(this.tagsPanel, "tagsPanel");
            this.tagsPanel.Controls.Add(this.groupBox7);
            this.tagsPanel.Controls.Add(this.groupBox6);
            this.tagsPanel.Name = "tagsPanel";
            // 
            // groupBox7
            // 
            resources.ApplyResources(this.groupBox7, "groupBox7");
            this.groupBox7.Controls.Add(this.clearTagsButton);
            this.groupBox7.Controls.Add(this.tagSelectionPanel);
            this.groupBox7.Controls.Add(this.currentTagsLabel);
            this.groupBox7.Controls.Add(this.label15);
            this.groupBox7.Name = "groupBox7";
            this.groupBox7.TabStop = false;
            // 
            // clearTagsButton
            // 
            resources.ApplyResources(this.clearTagsButton, "clearTagsButton");
            this.clearTagsButton.Name = "clearTagsButton";
            this.clearTagsButton.UseVisualStyleBackColor = true;
            this.clearTagsButton.Click += new System.EventHandler(this.clearTagsButton_Click);
            // 
            // tagSelectionPanel
            // 
            resources.ApplyResources(this.tagSelectionPanel, "tagSelectionPanel");
            this.tagSelectionPanel.Name = "tagSelectionPanel";
            // 
            // currentTagsLabel
            // 
            resources.ApplyResources(this.currentTagsLabel, "currentTagsLabel");
            this.currentTagsLabel.Name = "currentTagsLabel";
            this.currentTagsLabel.UseCompatibleTextRendering = true;
            // 
            // label15
            // 
            resources.ApplyResources(this.label15, "label15");
            this.label15.Name = "label15";
            // 
            // groupBox6
            // 
            resources.ApplyResources(this.groupBox6, "groupBox6");
            this.groupBox6.Controls.Add(this.tagCategoryCombinationBox);
            this.groupBox6.Controls.Add(this.fadeOnlyOnChangeBox);
            this.groupBox6.Controls.Add(this.label17);
            this.groupBox6.Controls.Add(this.tagFadeUpDown);
            this.groupBox6.Controls.Add(this.label16);
            this.groupBox6.Controls.Add(this.label14);
            this.groupBox6.Controls.Add(this.label13);
            this.groupBox6.Controls.Add(this.musicTagCategoryBox);
            this.groupBox6.Name = "groupBox6";
            this.groupBox6.TabStop = false;
            // 
            // tagCategoryCombinationBox
            // 
            resources.ApplyResources(this.tagCategoryCombinationBox, "tagCategoryCombinationBox");
            this.tagCategoryCombinationBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.tagCategoryCombinationBox.FormattingEnabled = true;
            this.tagCategoryCombinationBox.Items.AddRange(new object[] {
            resources.GetString("tagCategoryCombinationBox.Items"),
            resources.GetString("tagCategoryCombinationBox.Items1"),
            resources.GetString("tagCategoryCombinationBox.Items2")});
            this.tagCategoryCombinationBox.Name = "tagCategoryCombinationBox";
            this.tagCategoryCombinationBox.DropDown += new System.EventHandler(this.tagCategoryCombinationBox_DropDown);
            this.tagCategoryCombinationBox.SelectedIndexChanged += new System.EventHandler(this.tagCategoryCombinationBox_SelectedIndexChanged);
            this.tagCategoryCombinationBox.DropDownClosed += new System.EventHandler(this.tagCategoryCombinationBox_DropDownClosed);
            // 
            // fadeOnlyOnChangeBox
            // 
            resources.ApplyResources(this.fadeOnlyOnChangeBox, "fadeOnlyOnChangeBox");
            this.fadeOnlyOnChangeBox.Name = "fadeOnlyOnChangeBox";
            this.fadeOnlyOnChangeBox.UseVisualStyleBackColor = true;
            this.fadeOnlyOnChangeBox.CheckedChanged += new System.EventHandler(this.fadeOnlyOnChangeBox_CheckedChanged);
            // 
            // label17
            // 
            resources.ApplyResources(this.label17, "label17");
            this.label17.Name = "label17";
            // 
            // tagFadeUpDown
            // 
            resources.ApplyResources(this.tagFadeUpDown, "tagFadeUpDown");
            this.tagFadeUpDown.Increment = new decimal(new int[] {
            100,
            0,
            0,
            0});
            this.tagFadeUpDown.Maximum = new decimal(new int[] {
            50000,
            0,
            0,
            0});
            this.tagFadeUpDown.Name = "tagFadeUpDown";
            this.tagFadeUpDown.ValueChanged += new System.EventHandler(this.tagFadeUpDown_ValueChanged);
            // 
            // label16
            // 
            resources.ApplyResources(this.label16, "label16");
            this.label16.Name = "label16";
            // 
            // label14
            // 
            resources.ApplyResources(this.label14, "label14");
            this.label14.Name = "label14";
            // 
            // label13
            // 
            resources.ApplyResources(this.label13, "label13");
            this.label13.Name = "label13";
            // 
            // musicTagCategoryBox
            // 
            resources.ApplyResources(this.musicTagCategoryBox, "musicTagCategoryBox");
            this.musicTagCategoryBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.musicTagCategoryBox.FormattingEnabled = true;
            this.musicTagCategoryBox.Name = "musicTagCategoryBox";
            this.musicTagCategoryBox.SelectedIndexChanged += new System.EventHandler(this.musicTagCategoryBox_SelectedIndexChanged);
            // 
            // elementsPanel
            // 
            resources.ApplyResources(this.elementsPanel, "elementsPanel");
            this.elementsPanel.Name = "elementsPanel";
            // 
            // musicList
            // 
            resources.ApplyResources(this.musicList, "musicList");
            this.musicList.FormattingEnabled = true;
            this.musicList.Name = "musicList";
            this.musicList.SelectedIndexChanged += new System.EventHandler(this.musicList_SelectedIndexChanged);
            // 
            // Player
            // 
            resources.ApplyResources(this, "$this");
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox5);
            this.Controls.Add(this.groupBox4);
            this.Controls.Add(this.groupBox1);
            this.Controls.Add(this.toolStrip1);
            this.Controls.Add(this.menuStrip1);
            this.Controls.Add(this.groupBox3);
            this.Controls.Add(this.groupBox2);
            this.KeyPreview = true;
            this.MainMenuStrip = this.menuStrip1;
            this.Name = "Player";
            this.FormClosing += new System.Windows.Forms.FormClosingEventHandler(this.Player_FormClosing);
            this.Load += new System.EventHandler(this.Player_Load);
            this.Shown += new System.EventHandler(this.Player_Shown);
            this.Resize += new System.EventHandler(this.Player_Resize);
            this.groupBox2.ResumeLayout(false);
            this.groupBox2.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.soundVolumeBar)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.musicVolumeBar)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.overallVolumeBar)).EndInit();
            this.groupBox3.ResumeLayout(false);
            this.groupBox3.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.fileSystemWatcher1)).EndInit();
            this.toolStrip1.ResumeLayout(false);
            this.toolStrip1.PerformLayout();
            this.groupBox1.ResumeLayout(false);
            this.groupBox1.PerformLayout();
            this.menuStrip1.ResumeLayout(false);
            this.menuStrip1.PerformLayout();
            this.groupBox4.ResumeLayout(false);
            this.groupBox5.ResumeLayout(false);
            this.tagsPanel.ResumeLayout(false);
            this.groupBox7.ResumeLayout(false);
            this.groupBox7.PerformLayout();
            this.groupBox6.ResumeLayout(false);
            this.groupBox6.PerformLayout();
            ((System.ComponentModel.ISupportInitialize)(this.tagFadeUpDown)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.Label projectNameLabel;
        private System.Windows.Forms.GroupBox groupBox2;
        private System.Windows.Forms.TrackBar soundVolumeBar;
        private System.Windows.Forms.Label label4;
        private System.Windows.Forms.TrackBar musicVolumeBar;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.TrackBar overallVolumeBar;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.GroupBox groupBox3;
        private System.Windows.Forms.Label soundsLabel;
        private System.Windows.Forms.Label label8;
        private System.Windows.Forms.Label musicLabel;
        private System.Windows.Forms.Label label7;
        private System.Windows.Forms.Label elementsLabel;
        private System.Windows.Forms.Label label6;
        private System.Windows.Forms.Label modeLabel;
        private System.Windows.Forms.Label label5;
        private System.Windows.Forms.OpenFileDialog openFileDialog1;
        private System.IO.FileSystemWatcher fileSystemWatcher1;
        private System.Windows.Forms.Label label9;
        private System.Windows.Forms.ToolStrip toolStrip1;
        private System.Windows.Forms.ToolStripButton openButton;
        private System.Windows.Forms.ToolStripButton messagesButton;
        private System.Windows.Forms.ToolStripButton editorButton;
        private System.Windows.Forms.ToolStripButton settingsButton;
        private System.Windows.Forms.ToolStripButton aboutButton;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator1;
        private System.Windows.Forms.ToolStripButton stopButton;
        private System.Windows.Forms.ToolStripButton previousButton;
        private System.Windows.Forms.ToolStripButton nextButton;
        private System.Windows.Forms.Timer broadCastTimer;
        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.Label clientStateLabel;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.Button networkSettingsButton;
        private System.Windows.Forms.Label label10;
        private System.Windows.Forms.MenuStrip menuStrip1;
        private System.Windows.Forms.ToolStripMenuItem projectToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem openToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem recentToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator2;
        private System.Windows.Forms.ToolStripMenuItem exitToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem playToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem stopAllToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem previousMusicToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem nextMusicTitleToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem extrasToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem startEditorToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem messagesToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator3;
        private System.Windows.Forms.ToolStripMenuItem settingsToolStripMenuItem1;
        private System.Windows.Forms.ToolStripMenuItem helpToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem aboutToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem dummyToolStripMenuItem;
        private System.Windows.Forms.Label label12;
        private System.Windows.Forms.ToolStripMenuItem helpOnlineToolStripMenuItem;
        private System.Windows.Forms.ToolStripSeparator toolStripSeparator4;
        private System.Windows.Forms.ToolStripMenuItem checkForUpdateToolStripMenuItem;
        private System.Windows.Forms.NotifyIcon notifyIcon;
        private System.Windows.Forms.ToolStripMenuItem importToolStripMenuItem;
        private System.Windows.Forms.OpenFileDialog importFileDialog;
        private System.Windows.Forms.SaveFileDialog saveFileDialog;
        private System.Windows.Forms.GroupBox groupBox5;
        private System.Windows.Forms.FlowLayoutPanel elementsPanel;
        private System.Windows.Forms.ListBox musicList;
        private System.Windows.Forms.GroupBox groupBox4;
        private System.Windows.Forms.ListBox modesList;
        private System.Windows.Forms.ToolStripMenuItem showKeysMenuItem;
        private System.Windows.Forms.ToolStripMenuItem globalKeyHookItem;
        private System.Windows.Forms.ToolStripButton repeatButton;
        private System.Windows.Forms.ToolStripMenuItem repeatCurrentMusicToolStripMenuItem;
        private System.Windows.Forms.Panel tagsPanel;
        private System.Windows.Forms.GroupBox groupBox7;
        private System.Windows.Forms.FlowLayoutPanel tagSelectionPanel;
        private System.Windows.Forms.Label currentTagsLabel;
        private System.Windows.Forms.Label label15;
        private System.Windows.Forms.GroupBox groupBox6;
        private System.Windows.Forms.Label label14;
        private System.Windows.Forms.Label label13;
        private System.Windows.Forms.ComboBox musicTagCategoryBox;
        private System.Windows.Forms.Button clearTagsButton;
        private System.Windows.Forms.CheckBox fadeOnlyOnChangeBox;
        private System.Windows.Forms.Label label17;
        private System.Windows.Forms.NumericUpDown tagFadeUpDown;
        private System.Windows.Forms.Label label16;
        private System.Windows.Forms.ComboBox tagCategoryCombinationBox;
        private System.Windows.Forms.Label controllerPortLabel;
        private System.Windows.Forms.LinkLabel webAddressLabel;
    }
}

