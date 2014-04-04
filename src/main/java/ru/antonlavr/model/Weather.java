package ru.antonlavr.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "weather")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "last_check")
    private Date lastCheck;
    @Column(name = "temperature")
    private String temperature;
    @Column(name = "weather_type")
    private String weatherType;
    @Column(name = "wind_speed")
    private String windSpeed;
    @Column(name = "humidity")
    private String humidity;
    @Column(name = "pressure")
    private String pressure;
    @Column(name = "current")
    private Boolean current;

    public Weather() {}

    public Date getLastCheck() {
        return lastCheck;
    }

    public void setLastCheck(Date lastCheck) {
        this.lastCheck = lastCheck;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(String weatherType) {
        this.weatherType = weatherType;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public Boolean getCurrent() {
        return current;
    }

    public void setCurrent(Boolean current) {
        this.current = current;
    }

    public String toString(){
        return "Влажность: " + getHumidity() + ", давление: " + getPressure() + ", температура: " + getTemperature() + ", погода:" + getWeatherType() + ", скорость ветра: " + getWindSpeed();
    }

}
