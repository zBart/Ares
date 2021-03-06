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
namespace Ares.Editor.ElementEditorControls
{
    partial class ChoiceContainerControl
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
            base.Dispose(disposing);
        }

        #region Vom Komponenten-Designer generierter Code

        /// <summary> 
        /// Erforderliche Methode für die Designerunterstützung. 
        /// Der Inhalt der Methode darf nicht mit dem Code-Editor geändert werden.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle2 = new System.Windows.Forms.DataGridViewCellStyle();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(ChoiceContainerControl));
            System.Windows.Forms.DataGridViewCellStyle dataGridViewCellStyle1 = new System.Windows.Forms.DataGridViewCellStyle();
            this.groupBox1 = new System.Windows.Forms.GroupBox();
            this.elementsGrid = new System.Windows.Forms.DataGridView();
            this.dataGridViewNumericUpDownColumn1 = new DataGridViewNumericUpDownElements.DataGridViewNumericUpDownColumn();
            this.dataGridViewTextBoxColumn1 = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.NameColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.artistColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.albumColumn = new System.Windows.Forms.DataGridViewTextBoxColumn();
            this.ChanceColumn = new DataGridViewNumericUpDownElements.DataGridViewNumericUpDownColumn();
            this.columnMenu = new System.Windows.Forms.ContextMenuStrip(this.components);
            this.nameToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.artistToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.albumToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.chanceToolStripMenuItem = new System.Windows.Forms.ToolStripMenuItem();
            this.groupBox1.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.elementsGrid)).BeginInit();
            this.columnMenu.SuspendLayout();
            this.SuspendLayout();
            // 
            // groupBox1
            // 
            this.groupBox1.Controls.Add(this.elementsGrid);
            resources.ApplyResources(this.groupBox1, "groupBox1");
            this.groupBox1.Name = "groupBox1";
            this.groupBox1.TabStop = false;
            this.groupBox1.UseCompatibleTextRendering = true;
            // 
            // elementsGrid
            // 
            this.elementsGrid.AllowUserToAddRows = false;
            this.elementsGrid.AutoSizeRowsMode = System.Windows.Forms.DataGridViewAutoSizeRowsMode.AllCells;
            this.elementsGrid.BackgroundColor = System.Drawing.SystemColors.Control;
            this.elementsGrid.BorderStyle = System.Windows.Forms.BorderStyle.None;
            this.elementsGrid.ClipboardCopyMode = System.Windows.Forms.DataGridViewClipboardCopyMode.Disable;
            this.elementsGrid.ColumnHeadersBorderStyle = System.Windows.Forms.DataGridViewHeaderBorderStyle.Single;
            this.elementsGrid.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.elementsGrid.Columns.AddRange(new System.Windows.Forms.DataGridViewColumn[] {
            this.NameColumn,
            this.artistColumn,
            this.albumColumn,
            this.ChanceColumn});
            dataGridViewCellStyle2.Alignment = System.Windows.Forms.DataGridViewContentAlignment.MiddleLeft;
            dataGridViewCellStyle2.BackColor = System.Drawing.SystemColors.Control;
            dataGridViewCellStyle2.Font = new System.Drawing.Font("Microsoft Sans Serif", 8.25F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            dataGridViewCellStyle2.ForeColor = System.Drawing.SystemColors.ControlText;
            dataGridViewCellStyle2.Padding = new System.Windows.Forms.Padding(5, 0, 0, 5);
            dataGridViewCellStyle2.SelectionBackColor = System.Drawing.SystemColors.ControlDark;
            dataGridViewCellStyle2.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            dataGridViewCellStyle2.WrapMode = System.Windows.Forms.DataGridViewTriState.False;
            this.elementsGrid.DefaultCellStyle = dataGridViewCellStyle2;
            resources.ApplyResources(this.elementsGrid, "elementsGrid");
            this.elementsGrid.GridColor = System.Drawing.SystemColors.ControlDarkDark;
            this.elementsGrid.Name = "elementsGrid";
            this.elementsGrid.RowHeadersVisible = false;
            this.elementsGrid.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect;
            this.elementsGrid.CellDoubleClick += new System.Windows.Forms.DataGridViewCellEventHandler(this.elementsGrid_CellDoubleClick);
            this.elementsGrid.CellEndEdit += new System.Windows.Forms.DataGridViewCellEventHandler(this.elementsGrid_CellEndEdit);
            this.elementsGrid.ColumnHeaderMouseClick += new System.Windows.Forms.DataGridViewCellMouseEventHandler(this.elementsGrid_ColumnHeaderMouseClick);
            // 
            // dataGridViewNumericUpDownColumn1
            // 
            this.dataGridViewNumericUpDownColumn1.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.AllCells;
            resources.ApplyResources(this.dataGridViewNumericUpDownColumn1, "dataGridViewNumericUpDownColumn1");
            this.dataGridViewNumericUpDownColumn1.Maximum = new decimal(new int[] {
            1000,
            0,
            0,
            0});
            this.dataGridViewNumericUpDownColumn1.Name = "dataGridViewNumericUpDownColumn1";
            this.dataGridViewNumericUpDownColumn1.Resizable = System.Windows.Forms.DataGridViewTriState.True;
            this.dataGridViewNumericUpDownColumn1.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.Automatic;
            // 
            // dataGridViewTextBoxColumn1
            // 
            this.dataGridViewTextBoxColumn1.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            resources.ApplyResources(this.dataGridViewTextBoxColumn1, "dataGridViewTextBoxColumn1");
            this.dataGridViewTextBoxColumn1.Name = "dataGridViewTextBoxColumn1";
            this.dataGridViewTextBoxColumn1.ReadOnly = true;
            // 
            // NameColumn
            // 
            this.NameColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill;
            resources.ApplyResources(this.NameColumn, "NameColumn");
            this.NameColumn.Name = "NameColumn";
            this.NameColumn.ReadOnly = true;
            // 
            // artistColumn
            // 
            this.artistColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.DisplayedCells;
            resources.ApplyResources(this.artistColumn, "artistColumn");
            this.artistColumn.Name = "artistColumn";
            this.artistColumn.ReadOnly = true;
            // 
            // albumColumn
            // 
            this.albumColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.DisplayedCells;
            resources.ApplyResources(this.albumColumn, "albumColumn");
            this.albumColumn.Name = "albumColumn";
            this.albumColumn.ReadOnly = true;
            // 
            // ChanceColumn
            // 
            this.ChanceColumn.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.AllCells;
            dataGridViewCellStyle1.BackColor = System.Drawing.SystemColors.Window;
            dataGridViewCellStyle1.ForeColor = System.Drawing.SystemColors.WindowText;
            dataGridViewCellStyle1.SelectionBackColor = System.Drawing.SystemColors.ControlDark;
            dataGridViewCellStyle1.SelectionForeColor = System.Drawing.SystemColors.HighlightText;
            this.ChanceColumn.DefaultCellStyle = dataGridViewCellStyle1;
            resources.ApplyResources(this.ChanceColumn, "ChanceColumn");
            this.ChanceColumn.Maximum = new decimal(new int[] {
            1000,
            0,
            0,
            0});
            this.ChanceColumn.Name = "ChanceColumn";
            this.ChanceColumn.Resizable = System.Windows.Forms.DataGridViewTriState.True;
            this.ChanceColumn.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.Automatic;
            // 
            // columnMenu
            // 
            this.columnMenu.ImageScalingSize = new System.Drawing.Size(24, 24);
            this.columnMenu.Items.AddRange(new System.Windows.Forms.ToolStripItem[] {
            this.nameToolStripMenuItem,
            this.artistToolStripMenuItem,
            this.albumToolStripMenuItem,
            this.chanceToolStripMenuItem});
            this.columnMenu.Name = "columnMenu";
            resources.ApplyResources(this.columnMenu, "columnMenu");
            // 
            // nameToolStripMenuItem
            // 
            this.nameToolStripMenuItem.Checked = true;
            this.nameToolStripMenuItem.CheckState = System.Windows.Forms.CheckState.Checked;
            resources.ApplyResources(this.nameToolStripMenuItem, "nameToolStripMenuItem");
            this.nameToolStripMenuItem.Name = "nameToolStripMenuItem";
            // 
            // artistToolStripMenuItem
            // 
            this.artistToolStripMenuItem.CheckOnClick = true;
            this.artistToolStripMenuItem.Name = "artistToolStripMenuItem";
            resources.ApplyResources(this.artistToolStripMenuItem, "artistToolStripMenuItem");
            this.artistToolStripMenuItem.Click += new System.EventHandler(this.artistToolStripMenuItem_Click);
            // 
            // albumToolStripMenuItem
            // 
            this.albumToolStripMenuItem.CheckOnClick = true;
            this.albumToolStripMenuItem.Name = "albumToolStripMenuItem";
            resources.ApplyResources(this.albumToolStripMenuItem, "albumToolStripMenuItem");
            this.albumToolStripMenuItem.Click += new System.EventHandler(this.albumToolStripMenuItem_Click);
            // 
            // chanceToolStripMenuItem
            // 
            this.chanceToolStripMenuItem.Checked = true;
            this.chanceToolStripMenuItem.CheckState = System.Windows.Forms.CheckState.Checked;
            resources.ApplyResources(this.chanceToolStripMenuItem, "chanceToolStripMenuItem");
            this.chanceToolStripMenuItem.Name = "chanceToolStripMenuItem";
            // 
            // ChoiceContainerControl
            // 
            resources.ApplyResources(this, "$this");
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.Controls.Add(this.groupBox1);
            this.Name = "ChoiceContainerControl";
            this.groupBox1.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.elementsGrid)).EndInit();
            this.columnMenu.ResumeLayout(false);
            this.ResumeLayout(false);

        }

        #endregion

        private System.Windows.Forms.GroupBox groupBox1;
        private System.Windows.Forms.DataGridView elementsGrid;
#if !MONO
        private DataGridViewNumericUpDownElements.DataGridViewNumericUpDownColumn dataGridViewNumericUpDownColumn1;
#else
        private System.Windows.Forms.DataGridViewTextBoxColumn ChanceColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn dataGridViewNumericUpDownColumn1;
#endif
        private System.Windows.Forms.DataGridViewTextBoxColumn dataGridViewTextBoxColumn1;
        private System.Windows.Forms.DataGridViewTextBoxColumn NameColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn artistColumn;
        private System.Windows.Forms.DataGridViewTextBoxColumn albumColumn;
        private DataGridViewNumericUpDownElements.DataGridViewNumericUpDownColumn ChanceColumn;
        private System.Windows.Forms.ContextMenuStrip columnMenu;
        private System.Windows.Forms.ToolStripMenuItem nameToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem artistToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem albumToolStripMenuItem;
        private System.Windows.Forms.ToolStripMenuItem chanceToolStripMenuItem;
    }
}
