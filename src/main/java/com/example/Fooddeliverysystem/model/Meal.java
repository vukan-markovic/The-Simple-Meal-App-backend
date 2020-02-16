package com.example.Fooddeliverysystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * The persistent class for the meal database table.
 */
@ApiModel
@Entity
@NamedQuery(name = "Meal.findAll", query = "SELECT m FROM Meal m")
public class Meal implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int mealId;

	@NotEmpty(message = "Description is empty")
	@Size(max= 45)
	private String description;
	@NotNull
	private boolean earlyOrder;
	@NotNull
	private boolean isRegular;

	@NotEmpty(message = "Name is empty")
	private String name;

	//bi-directional many-to-many association to Dailymenu
	@ManyToMany
	@JoinTable(
		name="DailyMenuMeal"
		, joinColumns={
			@JoinColumn(name="Meal_mealId")
			}
		, inverseJoinColumns={
			@JoinColumn(name="DailyMenu_dailyMenuId")
			}
		)
	@JsonIgnore
	private List<Dailymenu> dailymenus;

	//bi-directional many-to-many association to Type
	@ManyToMany(mappedBy="meals")
	private List<Type> types;

	public Meal() {
		types = new ArrayList<>();
	}

	public int getMealId() {
		return this.mealId;
	}

	public void setMealId(int mealId) {
		this.mealId = mealId;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean getEarlyOrder() {
		return this.earlyOrder;
	}

	public void setEarlyOrder(boolean earlyOrder) {
		this.earlyOrder = earlyOrder;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@JsonIgnore
	public List<Dailymenu> getDailyMenus() {
		return this.dailymenus;
	}

	public void setDailyMenus(List<Dailymenu> dailymenus) {
		this.dailymenus = dailymenus;
	}

	public List<Type> getTypes() {
		return this.types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}

	public void setIsRegular (boolean isRegular){
		this.isRegular=isRegular;
	}

	public boolean isRegular() {
		return isRegular;
	}

	@JsonIgnore
	public Type getType(String name){
		for (Type type: types) {
			if(type.getName().equals(name)){
				return type;
			}
		}
		return null;
	}
	@JsonIgnore
	public Type getType(){
		if(types.size() > 1){
			return null;
		}else {
			return types.get(0);
		}
	}

	@Override
	public String toString() {
		return 	 name +  " - "  + description;

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Meal meal = (Meal) o;
		return mealId == meal.mealId;
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(mealId)  +  Objects.hashCode(description) + Objects.hashCode(name);
	}

	public void addDailyMenu(Dailymenu dailymenu){
		for (Dailymenu current: dailymenus) {
			if(current.getDailyMenuId() == dailymenu.getDailyMenuId()){
				return;
			}
		}
		dailymenus.add(dailymenu);
	}
	public void addType(Type type){
		types.add(type);
	}

	public void removeDailyMenu(Dailymenu dailymenu){
		if(dailymenus == null && dailymenus.isEmpty()){
			return;
		}
		for (Dailymenu current: dailymenus) {
			if(current.getDailyMenuId() == dailymenu.getDailyMenuId()){
				dailymenus.remove(current);
				return;
			}
		}

	}
}