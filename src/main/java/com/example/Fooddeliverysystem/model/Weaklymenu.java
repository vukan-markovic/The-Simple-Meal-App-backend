package com.example.Fooddeliverysystem.model;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the weaklymenu database table.
 */
@ApiModel
@Entity
@Table(name = "WeaklyMenu")
@NamedQuery(name = "Weaklymenu.findAll", query = "SELECT w FROM Weaklymenu w")
public class Weaklymenu implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int weaklyMenuId;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateFrom;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateTo;

	private String imagePath;

	//bi-directional many-to-one association to Dailymenu
	@OneToMany(mappedBy="weaklymenu")
	private List<Dailymenu> dailymenus;

	public Weaklymenu() {
		dailymenus=new ArrayList<Dailymenu>();
	}

	public int getWeaklyMenuId() {
		return this.weaklyMenuId;
	}

	public void setWeaklyMenuId(int weaklyMenuId) {
		this.weaklyMenuId = weaklyMenuId;
	}

	public Date getDateFrom() {
		return this.dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return this.dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public List<Dailymenu> getDailymenus() {
		return this.dailymenus;
	}

	public void setDailymenus(List<Dailymenu> dailymenus) {
		this.dailymenus = dailymenus;
	}

	public boolean addDailymenus(Dailymenu newDailyMenu) {
		for (Dailymenu dailymeny: dailymenus) {
			if(dailymeny.getDailyMenuId() == newDailyMenu.getDailyMenuId()){
				return false;
			}
		}
		dailymenus.add(newDailyMenu);
		return true;
	}

	public Dailymenu removeDailymenus(Dailymenu dailymenus) {
		getDailymenus().remove(dailymenus);
		dailymenus.setWeaklymenu(null);

		return dailymenus;
	}
	public boolean haveDailyMenu(Date date){
		for (Dailymenu dailymenu: dailymenus) {
			if(dailymenu.getDate().equals(date)){
				return true;
			}
		}
		return false;
	}
	public boolean finishCreation(){
		ArrayList<Date> dates = new ArrayList<Date>();
		Date day = dateFrom;
		Calendar c = Calendar.getInstance();
		c.setTime(dateTo);
		c.add(Calendar.DATE,1);

		while(day.before(c.getTime())) {
			dates.add(day);
			Calendar c1 = Calendar.getInstance();
			c1.setTime(day);
			c1.add(Calendar.DATE,1);
			day=c1.getTime();
		}
		if(dates.size() == dailymenus.size()){
			return true;
		}
		return false;
	}
}