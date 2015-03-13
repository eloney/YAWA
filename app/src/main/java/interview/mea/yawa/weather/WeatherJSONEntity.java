package interview.mea.yawa.weather;

/**
 * Created by Ben_Hasee on 13/03/2015.
 */

import java.lang.reflect.Field;
import java.io.Serializable;

public class WeatherJSONEntity implements Serializable {

    public Double message;
    public int cnt;
    public String cod;
    public java.util.List<List> list;

    public class List implements Serializable {

        public int clouds;
        public int dt;
        public int humidity;
        public Double pressure;
        public Double speed;
        public int deg;
        public Double rain;
        public java.util.List<Weather> weather;

        public class Weather implements Serializable {

            public int id;
            public String icon;
            public String description;
            public String main;

            @Override
            public String toString() {
                String s = "";
                Field[] arr = this.getClass().getFields();
                for (Field f : getClass().getFields()) {
                    try {
                        s += f.getName() + "=" + f.get(this) + "\n,";
                    } catch (Exception e) {
                    }
                }
                return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
            }
        }

        public Temp temp;

        public class Temp implements Serializable {

            public Double min;
            public Double eve;
            public Double max;
            public Double morn;
            public Double night;
            public Double day;

            @Override
            public String toString() {
                String s = "";
                Field[] arr = this.getClass().getFields();
                for (Field f : getClass().getFields()) {
                    try {
                        s += f.getName() + "=" + f.get(this) + "\n,";
                    } catch (Exception e) {
                    }
                }
                return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
            }
        }

        @Override
        public String toString() {
            String s = "";
            Field[] arr = this.getClass().getFields();
            for (Field f : getClass().getFields()) {
                try {
                    s += f.getName() + "=" + f.get(this) + "\n,";
                } catch (Exception e) {
                }
            }
            return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
        }
    }

    public City city;

    public class City implements Serializable {

        public int id;
        public String name;
        public int population;
        public String country;
        public Coord coord;

        public class Coord implements Serializable {

            public Double lon;
            public Double lat;

            @Override
            public String toString() {
                String s = "";
                Field[] arr = this.getClass().getFields();
                for (Field f : getClass().getFields()) {
                    try {
                        s += f.getName() + "=" + f.get(this) + "\n,";
                    } catch (Exception e) {
                    }
                }
                return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
            }
        }

        public Sys sys;

        public class Sys implements Serializable {

            public int population;

            @Override
            public String toString() {
                String s = "";
                Field[] arr = this.getClass().getFields();
                for (Field f : getClass().getFields()) {
                    try {
                        s += f.getName() + "=" + f.get(this) + "\n,";
                    } catch (Exception e) {
                    }
                }
                return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
            }
        }

        @Override
        public String toString() {
            String s = "";
            Field[] arr = this.getClass().getFields();
            for (Field f : getClass().getFields()) {
                try {
                    s += f.getName() + "=" + f.get(this) + "\n,";
                } catch (Exception e) {
                }
            }
            return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
        }
    }

    @Override
    public String toString() {
        String s = "";
        Field[] arr = this.getClass().getFields();
        for (Field f : getClass().getFields()) {
            try {
                s += f.getName() + "=" + f.get(this) + "\n,";
            } catch (Exception e) {
            }
        }
        return getClass().getSimpleName() + "[" + (arr.length == 0 ? s : s.substring(0, s.length() - 1)) + "]";
    }
}