package com.xunku.ObjectModel;

public class Base_Custom_Config {
	private int _ID;
	private String _Name;
	private String _Label;
	private String _Unit;
	private Boolean _Enabled;
	private String _GroupName;
	private String _Description;
	public int get_ID() {
		return _ID;
	}
	public void set_ID(int _id) {
		_ID = _id;
	}
	public String get_Name() {
		return _Name;
	}
	public void set_Name(String name) {
		_Name = name;
	}
	public String get_Label() {
		return _Label;
	}
	public void set_Label(String label) {
		_Label = label;
	}
	public String get_Unit() {
		return _Unit;
	}
	public void set_Unit(String unit) {
		_Unit = unit;
	}
	public Boolean get_Enabled() {
		return _Enabled;
	}
	public void set_Enabled(Boolean enabled) {
		_Enabled = enabled;
	}
	public String get_GroupName() {
		return _GroupName;
	}
	public void set_GroupName(String groupName) {
		_GroupName = groupName;
	}
	public String get_Description() {
		return _Description;
	}
	public void set_Description(String description) {
		_Description = description;
	}
}
