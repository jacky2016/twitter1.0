package com.xunku.ObjectModel;

//自定义枚举，和.net一致
public enum AuthorityType {
	  menu(1), ui(2),   action(3) ,all(-1);
	  private int value =0;
	  private AuthorityType(int value){
		  this.value = value;
	  }
	  
	  public static AuthorityType valueOf(int value) { 
	        switch (value) {
	        case 1:
	            return menu;
	        case 2:
	            return ui;
	        case 3:
	        	return action;
	        case -1:
	        	return all;
	        default:
	            return null;
	        }
	    }

	    public int value() {
	        return this.value;
	    }
}

 