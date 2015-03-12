
package interview.mea.yawa.weather;

public class City{
   	private Coord coord;
   	private String country;
   	private Number id;
   	private String name;
   	private Number population;
   	private Sys sys;

 	public Coord getCoord(){
		return this.coord;
	}
	public void setCoord(Coord coord){
		this.coord = coord;
	}
 	public String getCountry(){
		return this.country;
	}
	public void setCountry(String country){
		this.country = country;
	}
 	public Number getId(){
		return this.id;
	}
	public void setId(Number id){
		this.id = id;
	}
 	public String getName(){
		return this.name;
	}
	public void setName(String name){
		this.name = name;
	}
 	public Number getPopulation(){
		return this.population;
	}
	public void setPopulation(Number population){
		this.population = population;
	}
 	public Sys getSys(){
		return this.sys;
	}
	public void setSys(Sys sys){
		this.sys = sys;
	}
}
