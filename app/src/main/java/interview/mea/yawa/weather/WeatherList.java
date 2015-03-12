
package interview.mea.yawa.weather;

import java.util.List;

public class WeatherList{
   	private Number clouds;
   	private Number deg;
   	private Number dt;
   	private Number humidity;
   	private Number pressure;
   	private Number speed;
   	private Temp temp;
   	private WeatherMore weatherMore;

 	public Number getClouds(){
		return this.clouds;
	}
	public void setClouds(Number clouds){
		this.clouds = clouds;
	}
 	public Number getDeg(){
		return this.deg;
	}
	public void setDeg(Number deg){
		this.deg = deg;
	}
 	public Number getDt(){
		return this.dt;
	}
	public void setDt(Number dt){
		this.dt = dt;
	}
 	public Number getHumidity(){
		return this.humidity;
	}
	public void setHumidity(Number humidity){
		this.humidity = humidity;
	}
 	public Number getPressure(){
		return this.pressure;
	}
	public void setPressure(Number pressure){
		this.pressure = pressure;
	}
 	public Number getSpeed(){
		return this.speed;
	}
	public void setSpeed(Number speed){
		this.speed = speed;
	}
 	public Temp getTemp(){
		return this.temp;
	}
	public void setTemp(Temp temp){
		this.temp = temp;
	}
 	public WeatherMore getWeatherMore(){
		return this.weatherMore;
	}
	public void setWeatherMore(WeatherMore weatherMore){
		this.weatherMore = weatherMore;
	}
}
