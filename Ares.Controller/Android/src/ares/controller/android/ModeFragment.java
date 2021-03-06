/*
 Copyright (c) 2011 [Joerg Ruedenauer]
 
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
package ares.controller.android;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.GridView;
import ares.controllers.control.Control;
import ares.controllers.data.Command;
import ares.controllers.data.Mode;

public class ModeFragment extends ModeLikeFragment {
	
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.mode_button_table, container, false);
    	return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!isOnTablet()) {
        	registerGestures();
        }
    }
    
    public void onStart() {
    	super.onStart();
    	uninitializeViews();
    	initializeViews();
    }
    
    public void onDestroyView() {
    	uninitializeViews();
    	super.onDestroyView();
    }
    
    public void projectLoaded() {
    	initializeViews();
    }
    
    private void uninitializeViews() {
    	if (Control.getInstance().getConfiguration() != null) {
	    	for (Command command : Control.getInstance().getConfiguration().getModes().get(getMode()).getCommands()) {
	    		CommandButtonMapping.getInstance().unregisterButton(command.getId());
	    	}    	
    	}
    }
    
    private void initializeViews() {
    	GridView buttonGrid = (GridView)getActivity().findViewById(R.id.modeButtonGrid);
        if (buttonGrid == null) return;
    	mAdapter = new ButtonAdapter(getActivity(), getMode());
    	buttonGrid.setAdapter(mAdapter);
        TextView title = ((TextView)getActivity().findViewById(R.id.modeTitle));
        if (Control.getInstance().getConfiguration() != null) {
            List<Mode> modes = Control.getInstance().getConfiguration().getModes();
            if (modes.size() > getMode())
            {
                title.setText(modes.get(getMode()).getTitle());
            }
            else
            {
                title.setText(R.string.no_mode);
            }
        }
        else {
        	title.setText(R.string.no_mode);
        }
    }
    
    private ButtonAdapter mAdapter;
    
    private static boolean sCommandsActive = true;
    
    public static void setCommandsActive(boolean active) {
    	sCommandsActive = active;
    }
    
    private class CommandSender implements OnCheckedChangeListener {
		public void onCheckedChanged(CompoundButton button, boolean checked) {
			boolean active = CommandButtonMapping.getInstance().isCommandActive(mId);
			if (button.isChecked() != active) {
				button.setChecked(active);
				button.setSelected(active);
			}
			if (!sCommandsActive)
				return;
			Log.d("ModeFragment", "Switching element " + mId);
			Control.getInstance().switchElement(mId);
		}
		
		public CommandSender(int id) {
			mId = id;
		}

		private int mId;
    }
    
    private class ButtonAdapter extends BaseAdapter {
    	
    	private Context mContext;
    	private Mode mMode;
    	
    	public ButtonAdapter(Context c, int mode) {
    		mContext = c;
    		if (Control.getInstance().getConfiguration() != null) {
    			mMode = Control.getInstance().getConfiguration().getModes().get(mode);
    		}
    		else {
    			mMode = null;
    		}
    	}
    	
		public int getCount() {
			if (mMode != null)
			{
				List<Command> commands = mMode.getCommands();
				return commands.size();
			}
			else
				return 0;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}
		
		private HashMap<Integer, ToggleButton> mButtons = new HashMap<Integer, ToggleButton>(); 

		public View getView(int position, View convertView, ViewGroup parent) {
			ToggleButton button;
			if (convertView == null) {
				if (mButtons.containsKey(position)) {
					return mButtons.get(position);
				}
				else {
					button = new ToggleButton(mContext);
					List<Command> commands = mMode.getCommands();
					Command command = position < commands.size() ? commands.get(position) : null;
					button.setPadding(5, 5, 5, 5);
					if (command != null) {
						button.setText(command.getTitle());
						button.setTextOn(command.getTitle());
						button.setTextOff(command.getTitle());
						CommandButtonMapping.getInstance().registerButton(command.getId(), button);
						button.setOnCheckedChangeListener(new CommandSender(command.getId()));					
					}
					else {
						button.setText("<Error>");
					}
					mButtons.put(position, button);
					return button;
				}
			}
			else {
				return (ToggleButton)convertView;
			}
		}
    	
    }
}
