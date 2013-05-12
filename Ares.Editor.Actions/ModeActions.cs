﻿/*
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
using System;
using Ares.Data;
using System.Windows.Forms;

namespace Ares.Editor.Actions
{
    public static class KeyDisplayExtensions
    {
        public static string GetNodeTitle(this IMode mode)
        {
            KeysConverter converter = new KeysConverter();
            return mode.Title + " (" + (mode.KeyCode != 0 ? converter.ConvertToString((System.Windows.Forms.Keys)mode.KeyCode) : StringResources.NoKey) + ")";
        }

        public static string GetNodeTitle(this IModeElement modeElement)
        {
            ITrigger trigger = modeElement.Trigger;
            if (trigger != null && trigger.TriggerType == TriggerType.Key)
            {
                KeysConverter converter = new KeysConverter();
                int keyCode = (trigger as IKeyTrigger).KeyCode;
                return modeElement.Title + " (" + (keyCode != 0 ? converter.ConvertToString((System.Windows.Forms.Keys)keyCode) : StringResources.NoKey) + ")";
            }
            else
            {
                // at first, as long as there are no other triggers
                return modeElement.Title + " (" + StringResources.NoKey + ")";
            }
        }
    }

    public class RenameProjectAction : Action
    {
        public RenameProjectAction(TreeNode node, String newName)
        {
            m_Node = node;
            m_OldName = (node.Tag as IProject).Title;
            m_NewName = newName;
        }

        public override void Do(Ares.Data.IProject project)
        {
            m_Node.Text = m_NewName;
            (m_Node.Tag as IProject).Title = m_NewName;
        }

        public override void Undo(Ares.Data.IProject project)
        {
            m_Node.Text = m_OldName;
            (m_Node.Tag as IProject).Title = m_OldName;
        }

        private TreeNode m_Node;
        private String m_OldName;
        private String m_NewName;
    }

    public class RenameModeAction : Action
    {
        public RenameModeAction(TreeNode node, String newName)
        {
            m_Node = node;
            m_OldName = (node.Tag as IMode).Title;
            m_NewName = newName;
        }

        public override void Do(Ares.Data.IProject project)
        {
            IMode mode = m_Node.Tag as IMode;
            mode.Title = m_NewName;
            m_Node.Text = mode.GetNodeTitle();
        }

        public override void Undo(Ares.Data.IProject project)
        {
            IMode mode = m_Node.Tag as IMode;
            mode.Title = m_OldName;
            m_Node.Text = mode.GetNodeTitle();
        }

        private TreeNode m_Node;
        private String m_OldName;
        private String m_NewName;
    }

    public class AddModeAction : Action
    {
        public AddModeAction(Ares.Data.IProject project, TreeNode projectNode, out TreeNode modeNode)
        {
            String name = StringResources.NewMode;
            m_ProjectNode = projectNode;
            IMode mode = (m_ProjectNode.Tag as IProject).AddMode(name);
            m_ModeNode = new TreeNode(name);
            m_ModeNode.Tag = mode;
            m_ProjectNode.Nodes.Add(m_ModeNode);
            m_Index = m_ProjectNode.Nodes.Count - 1;
            modeNode = m_ModeNode;
            Undo(project);
        }

        public AddModeAction(TreeNode projectNode, IMode importedMode, out TreeNode modeNode)
        {
            String name = importedMode.Title;
            m_ProjectNode = projectNode;
            m_ModeNode = new TreeNode(name);
            m_ModeNode.Tag = importedMode;
            m_Index = m_ProjectNode.Nodes.Count;
            modeNode = m_ModeNode;
        }

        public override void Do(Ares.Data.IProject project)
        {
            (m_ProjectNode.Tag as IProject).InsertMode(m_Index, m_ModeNode.Tag as IMode);
            m_ProjectNode.Nodes.Insert(m_Index, m_ModeNode);
            m_ProjectNode.Expand();
        }

        public override void Undo(Ares.Data.IProject project)
        {
            (m_ProjectNode.Tag as IProject).RemoveMode(m_ModeNode.Tag as IMode);
            m_ProjectNode.Nodes.Remove(m_ModeNode);
        }

        private TreeNode m_ProjectNode;
        private TreeNode m_ModeNode;
        private int m_Index;
    }

    public class DeleteModeAction : Action
    {
        public DeleteModeAction(TreeNode modeNode)
        {
            m_ProjectNode = modeNode.Parent;
            m_ModeNode = modeNode;
            m_Mode = modeNode.Tag as IMode;
            m_Index = m_ProjectNode.Nodes.IndexOf(modeNode);
        }

        public override void Do(Ares.Data.IProject project)
        {
            (m_ProjectNode.Tag as IProject).RemoveMode(m_Mode);
            m_ProjectNode.Nodes.Remove(m_ModeNode);
            foreach (IModeElement modeElement in m_Mode.GetElements())
            {
                Ares.Data.DataModule.ElementRepository.DeleteElement(modeElement.Id);
                Ares.ModelInfo.ModelChecks.Instance.CheckAll(project);
                ElementRemoval.NotifyRemoval(modeElement);
            }
        }

        public override void Undo(Ares.Data.IProject project)
        {
            foreach (IModeElement modeElement in m_Mode.GetElements())
            {
                Ares.Data.DataModule.ElementRepository.AddElement(modeElement);
                Ares.ModelInfo.ModelChecks.Instance.CheckAll(project);
                ElementRemoval.NotifyUndo(modeElement);
            }
            (m_ProjectNode.Tag as IProject).InsertMode(m_Index, m_Mode);
            m_ProjectNode.Nodes.Insert(m_Index, m_ModeNode);
        }

        private IMode m_Mode;
        private TreeNode m_ProjectNode, m_ModeNode;
        private int m_Index;
    }

    public class SetModeKeyAction : Action
    {
        public SetModeKeyAction(TreeNode modeNode, int newKey)
        {
            m_Node = modeNode;
            m_OldKey = (m_Node.Tag as IMode).KeyCode;
            m_NewKey = newKey;
        }

        public override void Do(Ares.Data.IProject project)
        {
            IMode mode = m_Node.Tag as IMode;
            mode.KeyCode = m_NewKey;
            m_Node.Text = mode.GetNodeTitle();
            Ares.ModelInfo.ModelChecks.Instance.Check(ModelInfo.CheckType.Key, project);
        }

        public override void Undo(Ares.Data.IProject project)
        {
            IMode mode = m_Node.Tag as IMode;
            mode.KeyCode = m_OldKey;
            m_Node.Text = mode.GetNodeTitle();
            Ares.ModelInfo.ModelChecks.Instance.Check(ModelInfo.CheckType.Key, project);
        }

        private TreeNode m_Node;
        private int m_OldKey;
        private int m_NewKey;
    }

    public class ModeMoveAction : Action
    {
        public ModeMoveAction(Ares.Data.IMode mode, int oldIndex, int newIndex, TreeNode parentNode, System.Action adaptButtons)
        {
            m_Mode = mode;
            m_OldIndex = oldIndex;
            m_NewIndex = newIndex;
            m_ParentNode = parentNode;
            m_AdaptButtons = adaptButtons;
        }

        public override void Do(IProject project)
        {
            project.RemoveMode(m_Mode);
            project.InsertMode(m_NewIndex, m_Mode);
            TreeNode node = m_ParentNode.Nodes[m_OldIndex];
            m_ParentNode.Nodes.RemoveAt(m_OldIndex);
            m_ParentNode.Nodes.Insert(m_NewIndex, node);
            m_AdaptButtons();
        }

        public override void Undo(IProject project)
        {
            project.RemoveMode(m_Mode);
            project.InsertMode(m_OldIndex, m_Mode);
            TreeNode node = m_ParentNode.Nodes[m_NewIndex];
            m_ParentNode.Nodes.RemoveAt(m_NewIndex);
            m_ParentNode.Nodes.Insert(m_OldIndex, node);
            m_AdaptButtons();
        }

        private TreeNode m_ParentNode;
        private Ares.Data.IMode m_Mode;
        private int m_OldIndex;
        private int m_NewIndex;
        private System.Action m_AdaptButtons;
    }

    public class ModeElementMoveAction : Action
    {
        public ModeElementMoveAction(Ares.Data.IMode mode, int oldIndex, int newIndex, TreeNode parentNode, System.Action adaptButtons)
        {
            m_Mode = mode;
            m_Element = mode.GetElements()[oldIndex];
            m_OldIndex = oldIndex;
            m_NewIndex = newIndex;
            m_ParentNode = parentNode;
            m_AdaptButtons = adaptButtons;
        }

        public override void Do(IProject project)
        {
            m_Mode.RemoveElement(m_Element);
            m_Mode.InsertElement(m_NewIndex, m_Element);
            TreeNode node = m_ParentNode.Nodes[m_OldIndex];
            m_ParentNode.Nodes.RemoveAt(m_OldIndex);
            m_ParentNode.Nodes.Insert(m_NewIndex, node);
            m_AdaptButtons();
        }

        public override void Undo(IProject project)
        {
            m_Mode.RemoveElement(m_Element);
            m_Mode.InsertElement(m_OldIndex, m_Element);
            TreeNode node = m_ParentNode.Nodes[m_NewIndex];
            m_ParentNode.Nodes.RemoveAt(m_NewIndex);
            m_ParentNode.Nodes.Insert(m_OldIndex, node);
            m_AdaptButtons();
        }

        private TreeNode m_ParentNode;
        private Ares.Data.IMode m_Mode;
        private Ares.Data.IModeElement m_Element;
        private int m_OldIndex;
        private int m_NewIndex;
        private System.Action m_AdaptButtons;
    }
}
