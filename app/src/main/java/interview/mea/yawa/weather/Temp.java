
package interview.mea.yawa.weather;

import java.util.List;

public class Temp{
   	private Number day;
   	private Number eve;
   	private Number max;
   	private Number min;
   	private Number morn;
   	private Number night;

 	public Number getDay(){
		return this.day;
	}
	public void setDay(Number day){
		this.day = day;
	}
 	public Number getEve(){
		return this.eve;
	}
	public void setEve(Number eve){
		this.eve = eve;
	}
 	public Number getMax(){
		return this.max;
	}
	public void setMax(Number max){
		this.max = max;
	}
 	public Number getMin(){
		return this.min;
	}
	public void setMin(Number min){
		this.min = min;
	}
 	public Number getMorn(){
		return this.morn;
	}
	public void setMorn(Number morn){
		this.morn = morn;
	}
 	public Number getNight(){
		return this.night;
	}
	public void setNight(Number night){
		this.night = night;
	}
}
