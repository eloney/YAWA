
package interview.mea.yawa.weather;


import java.util.List;

public class Weather{
   	private City city;
   	private Number cnt;
   	private String cod;
   	private Number message;
   	private List<WeatherList> weatherLists;

 	public City getCity(){
		return this.city;
	}
	public void setCity(City city){
		this.city = city;
	}
 	public Number getCnt(){
		return this.cnt;
	}
	public void setCnt(Number cnt){
		this.cnt = cnt;
	}
 	public String getCod(){
		return this.cod;
	}
	public void setCod(String cod){
		this.cod = cod;
	}
 	public Number getMessage(){
		return this.message;
	}
	public void setMessage(Number message){
		this.message = message;
	}
 	public List<WeatherList> getWeatherList(){
		return this.weatherLists;
	}
	public void setWeatherList(List<WeatherList> weatherLists){
		this.weatherLists = weatherLists;
	}
}
