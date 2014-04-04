package ru.antonlavr.shedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.antonlavr.model.Weather;
import ru.antonlavr.repository.WeatherRepository;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@EnableScheduling
@Service
public class WeatherShedule {

    @Autowired
    WeatherRepository weatherRepository;

    private Weather weather = new Weather();

    public WeatherShedule() {}

    @Transactional
    @Scheduled(fixedRate = 1000 * 60 * 10)
    public void getWeather() {
        weatherRepository.deleteAll();
        weatherRepository.save(downloadWeather());
    }

    public Weather downloadWeather() {
        try {
            URL url = new URL("http://export.yandex.ru/weather-ng/forecasts/34560.xml");
            InputStream stream = url.openStream();

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream);

            doc.getDocumentElement().normalize();

            Element root = doc.getDocumentElement();
            NodeList fact = root.getElementsByTagName("fact");
            for (int i = 0; i < fact.getLength(); i++){
                Node node = fact.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    NodeList nodeListTemperature = element.getElementsByTagName("temperature");
                    Element elementTemperature = (Element) nodeListTemperature.item(0);
                    NodeList temperatureNm = elementTemperature.getChildNodes();
                    weather.setTemperature(((Node) temperatureNm.item(0)).getNodeValue());


                    NodeList nodeListWeatherType = element.getElementsByTagName("weather_type");
                    Element elementWeatherType = (Element) nodeListWeatherType.item(0);
                    NodeList weatherTypeNm = elementWeatherType.getChildNodes();
                    weather.setWeatherType(((Node) weatherTypeNm.item(0)).getNodeValue());

                    NodeList nodeListWindSpeed = element.getElementsByTagName("wind_speed");
                    Element elementWindSpeed = (Element) nodeListWindSpeed.item(0);
                    NodeList windSpeedNm = elementWindSpeed.getChildNodes();
                    weather.setWindSpeed(((Node) windSpeedNm.item(0)).getNodeValue());

                    NodeList nodeListHumidity = element.getElementsByTagName("humidity");
                    Element elementHumidity = (Element) nodeListHumidity.item(0);
                    NodeList humiditeNm = elementHumidity.getChildNodes();
                    weather.setHumidity(((Node) humiditeNm.item(0)).getNodeValue());

                    NodeList nodeListPressure = element.getElementsByTagName("pressure");
                    Element elementPressure = (Element) nodeListPressure.item(0);
                    NodeList pressureNm = elementPressure.getChildNodes();
                    weather.setPressure(((Node) pressureNm.item(0)).getNodeValue());

                    weather.setLastCheck(new Date());
                    weather.setCurrent(true);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return weather;
    }

}
