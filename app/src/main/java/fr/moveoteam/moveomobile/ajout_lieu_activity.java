
	 
	/*
	 *	This content is generated from the PSD File Info.
	 *	(Alt+Shift+Ctrl+I).
	 *
	 *	@desc 		Normale
	 *	@file 		ajout_lieu
	 *	@date 		sRVB
	 *	@title 		
	 *	@author 	
	 *	@keywords 	
	 *	@generator 	Export Kit v1.2.8
	 *
	 */
	

	package fr.moveoteam.moveomobile;
	
	import android.app.Activity;
	import android.os.Bundle;
	

import android.widget.TextView;
import android.view.View;
import android.widget.ImageView;
	
	public class ajout_lieu_activity extends Activity {
	
		
	private TextView ou_annuler;
	private View button_ajout;
	private TextView ajouter;
	private View input_adresse;
	private TextView description;
	private View input_adresse_1;
	private TextView adresse;
	private View input_lieu;
	private TextView lieu;
	private View bg_menu;
	private ImageView menu_icon;
	private ImageView search;
	private ImageView logo;
	private TextView description_du_lieu_shopping;
	
		@Override
		public void onCreate(Bundle savedInstanceState) {
	
			super.onCreate(savedInstanceState);
			setContentView(R.layout.ajout_lieu);
	
			
		ou_annuler = (TextView) findViewById(R.id.ou_annuler);
		button_ajout = (View) findViewById(R.id.button_ajout);
		ajouter = (TextView) findViewById(R.id.ajouter);
		input_adresse = (View) findViewById(R.id.input_adresse);
		description = (TextView) findViewById(R.id.description);
		input_adresse_1 = (View) findViewById(R.id.input_adresse_1);
		adresse = (TextView) findViewById(R.id.adresse);
		input_lieu = (View) findViewById(R.id.input_lieu);
		lieu = (TextView) findViewById(R.id.lieu);
		bg_menu = (View) findViewById(R.id.bg_menu);
		menu_icon = (ImageView) findViewById(R.id.menu_icon);
		search = (ImageView) findViewById(R.id.search);
		logo = (ImageView) findViewById(R.id.logo);
		description_du_lieu_shopping = (TextView) findViewById(R.id.description_du_lieu_shopping);
		
			
			//custom code goes here
		
		}
	}
	
	
